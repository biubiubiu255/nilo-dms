package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.TaskStatusEnum;
import com.nilo.dms.common.enums.TaskTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.TaskDao;
import com.nilo.dms.dao.TaskTransferDao;
import com.nilo.dms.dao.dataobject.TaskDO;
import com.nilo.dms.dao.dataobject.TaskTransferDO;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.UserInfo;
import com.nilo.dms.service.order.model.TaskMessage;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.TaskService;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.service.order.model.Task;
import com.nilo.dms.service.order.model.TaskParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;

/**
 * Created by admin on 2017/10/23.
 */
@Service
public class TaskServiceImpl implements TaskService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private TaskTransferDao taskTransferDao;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    @Qualifier("messageProducer")
    private AbstractMQProducer messageProducer;

    @Override
    public String addTask(Task task) {


        TaskDO taskDO = new TaskDO();
        Long taskId = IdWorker.getInstance().nextId();
        taskDO.setTaskId(taskId);
        taskDO.setMerchantId(Long.parseLong(task.getMerchantId()));
        taskDO.setStatus(TaskStatusEnum.CREATE.getCode());
        taskDO.setCreatedBy(task.getCreatedBy());
        taskDO.setOrderNo(task.getOrderNo());
        taskDO.setHandledBy(task.getHandledBy());
        taskDO.setTaskType(task.getTaskType().getCode());
        taskDao.insert(taskDO);

        return "" + taskId;
    }

    @Override
    public void transferTask(String taskId, String from, String to, String remark) {

        //根据taskId查询任务信息
        TaskDO taskDO = taskDao.findByTaskId(Long.parseLong(taskId));
        if (taskDO == null) {
            throw new DMSException(BizErrorCode.TASK_NOT_EXIST, taskId);
        }
        //校验任务发出人是否合法
        if (!taskDO.getHandledBy().equalsIgnoreCase(from)) {
            throw new DMSException(BizErrorCode.TASK_NOT_ALLOW, taskId);
        }
        //判断状态是否允许
        if (taskDO.getStatus() != TaskStatusEnum.CREATE.getCode()) {
            throw new DMSException(BizErrorCode.TASK_NOT_ALLOW, taskId);
        }
        UserInfo toUser = userService.findUserInfoByUserId("" + taskDO.getMerchantId(), to);
        if (toUser == null) {
            throw new DMSException(BizErrorCode.USER_ID_ILLEGAL);
        }

        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {
                try {

                    //修改当前任务执行人
                    taskDO.setStatus(null);
                    taskDO.setHandledBy(to);
                    taskDao.update(taskDO);
                    //增加任务转派记录
                    TaskTransferDO transferDO = new TaskTransferDO();
                    transferDO.setTaskId(taskDO.getTaskId());
                    transferDO.setMerchantId(taskDO.getMerchantId());
                    transferDO.setTaskType(taskDO.getTaskType());
                    transferDO.setFrom(from);
                    transferDO.setTo(to);
                    transferDO.setRemark(remark);
                    taskTransferDao.insert(transferDO);
                } catch (Exception e) {
                    logger.error("transferTask. TaskID:{}", taskId, e);
                    transactionStatus.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });


    }

    @Override
    public void updateTask(Task task) {
        TaskDO taskDO = convert(task);
        taskDao.update(taskDO);
    }

    @Override
    public List<Task> queryTask(TaskParameter parameter, Pagination pagination) {
        Map<String, Object> map = new HashMap<>();
        map.put("merchantId", parameter.getMerchantId());
        map.put("handledBy", parameter.getHandledBy());
        map.put("orderNo", parameter.getOrderNo());
        map.put("taskType", parameter.getTaskType());
        map.put("status", parameter.getStatus());
        map.put("fromTime", parameter.getFromTime());
        map.put("toTime", parameter.getToTime());
        map.put("offset", pagination.getOffset());
        map.put("limit", pagination.getLimit());

        List<Task> list = new ArrayList<>();
        List<TaskDO> doList = taskDao.findBy(map);
        Long count = taskDao.findCountBy(map);
        pagination.setTotalCount(count == null ? 0 : count);
        //判空
        if (doList == null || doList.size() == 0) {
            return list;
        }
        //获取运单号
        List<String> orderNos = new ArrayList<>();
        for (TaskDO d : doList) {
            orderNos.add(d.getOrderNo());
        }
        //获取用户ID
        Set<String> userIds = new HashSet<>();
        for (TaskDO d : doList) {
            userIds.add(d.getCreatedBy());
            userIds.add(d.getHandledBy());
        }
        List<String> userIdList = new ArrayList<>();
        userIdList.addAll(userIds);
        //查询所有订单信息
        List<DeliveryOrder> deliveryOrderList = orderService.queryByOrderNos(parameter.getMerchantId(), orderNos);
        //查询所有用户信息
        List<UserInfo> userInfoList = userService.findUserInfoByUserIds(parameter.getMerchantId(), userIdList);

        for (TaskDO t : doList) {
            Task task = convert(t);
            for (DeliveryOrder order : deliveryOrderList) {
                if (StringUtil.equals(order.getOrderNo(), t.getOrderNo())) {
                    task.setDeliveryOrder(order);
                    break;
                }
            }
            for (UserInfo userInfo : userInfoList) {
                if (StringUtil.equals(t.getHandledBy(), userInfo.getUserId())) {
                    task.setHandledName(userInfo.getName());
                    break;
                }
            }
            list.add(task);
        }
        return list;
    }

    @Override
    public Task queryTaskByTypeAndOrderNo(String merchantId, String taskType, String orderNo) {

        TaskDO taskDO = taskDao.queryByTypeAndOrderNo(Long.parseLong(merchantId), taskType, orderNo);
        return convert(taskDO);
    }

    private Task convert(TaskDO taskDO) {
        if (taskDO == null) return null;
        Task task = new Task();
        task.setTaskId("" + taskDO.getTaskId());
        task.setHandledBy("" + taskDO.getHandledBy());
        task.setMerchantId("" + taskDO.getMerchantId());
        task.setOrderNo(taskDO.getOrderNo());
        task.setHandledTime(taskDO.getHandledTime());
        task.setRemark(taskDO.getRemark());
        task.setTaskType(TaskTypeEnum.getEnum(taskDO.getTaskType()));
        task.setCreatedBy(taskDO.getCreatedBy());
        task.setCreatedTime(taskDO.getCreatedTime());
        task.setStatus(TaskStatusEnum.getEnum(taskDO.getStatus()));
        task.setTaskDesc(taskDO.getTaskDesc());
        task.setTaskRate(taskDO.getTaskRate());

        return task;
    }

    private TaskDO convert(Task task) {
        if (task == null) return null;
        TaskDO taskDO = new TaskDO();
        taskDO.setTaskId(Long.parseLong(task.getTaskId()));
        taskDO.setHandledBy(task.getHandledBy());
        if (task.getMerchantId() != null) {
            taskDO.setMerchantId(Long.parseLong(task.getMerchantId()));
        }
        taskDO.setOrderNo(task.getOrderNo());
        taskDO.setHandledTime(task.getHandledTime());
        taskDO.setRemark(task.getRemark());
        if (task.getTaskType() != null) {
            taskDO.setTaskType(task.getTaskType().getCode());
        }
        taskDO.setCreatedBy(taskDO.getCreatedBy());
        if (task.getStatus() != null) {
            taskDO.setStatus(task.getStatus().getCode());
        }

        taskDO.setTaskDesc(task.getTaskDesc());
        taskDO.setTaskRate(task.getTaskRate());

        return taskDO;
    }
}
