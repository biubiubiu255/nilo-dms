package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.enums.DelayStatusEnum;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.enums.TaskStatusEnum;
import com.nilo.dms.common.enums.TaskTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.dao.DeliveryOrderDelayDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderDelayDO;
import com.nilo.dms.service.order.*;
import com.nilo.dms.service.order.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/30.
 */
@Service
public class RiderOptServiceImpl extends AbstractOrderOpt implements RiderOptService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final Integer MAX_DELAY_TIMES = 2;

    @Autowired
    TaskService taskService;
    @Autowired
    OrderService orderService;
    @Autowired
    private AbnormalOrderService abnormalOrderService;

    @Autowired
    private DeliveryOrderDelayDao deliveryOrderDelayDao;

    @Override
    @Transactional
    public void goToPickup(String merchantId, String orderNo, String optBy, String taskId) {

        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setMerchantId(merchantId);
        optRequest.setOptBy(optBy);
        optRequest.setOptType(OptTypeEnum.GO_TO_PICK_UP);
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add(orderNo);
        optRequest.setOrderNo(orderNoList);
        orderService.handleOpt(optRequest);

        Task task = new Task();
        task.setTaskId(taskId);
        task.setStatus(TaskStatusEnum.PROCESS);
        task.setHandledTime(DateUtil.getSysTimeStamp());
        taskService.updateTask(task);


    }

    @Override
    @Transactional
    public void pickup(String merchantId, String orderNo, String optBy, String taskId) {

        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setMerchantId(merchantId);
        optRequest.setOptBy(optBy);
        optRequest.setOptType(OptTypeEnum.PICK_UP);
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add(orderNo);
        optRequest.setOrderNo(orderNoList);
        orderService.handleOpt(optRequest);

        Task task = new Task();
        task.setTaskId(taskId);
        task.setStatus(TaskStatusEnum.COMPLETE);
        task.setHandledTime(DateUtil.getSysTimeStamp());
        taskService.updateTask(task);
    }

    @Override
    @Transactional
    public void pickupFailed(String merchantId, String orderNo, String reason, String optBy, String taskId) {

        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setMerchantId(merchantId);
        optRequest.setOptBy(optBy);
        optRequest.setOptType(OptTypeEnum.PICK_UP_FAILED);
        optRequest.setRemark(reason);
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add(orderNo);
        optRequest.setOrderNo(orderNoList);
        orderService.handleOpt(optRequest);

        Task task = new Task();
        task.setTaskId(taskId);
        task.setStatus(TaskStatusEnum.COMPLETE);
        task.setHandledTime(DateUtil.getSysTimeStamp());

        taskService.updateTask(task);

    }

    @Override
    @Transactional
    public void signForOrder(SignForOrderParam param) {

        //根据订单号查询任务
        Task t = taskService.queryTaskByTypeAndOrderNo(param.getMerchantId(), TaskTypeEnum.DELIVERY.getCode(), param.getOrderNo());
        /*if (t == null || t.getStatus() == TaskStatusEnum.COMPLETE) {
            throw new DMSException(BizErrorCode.ORDER_STATUS_LIMITED, param.getOrderNo());
        }*/
        if (t == null) {
            t = taskService.queryTaskByTypeAndOrderNo(param.getMerchantId(), TaskTypeEnum.SELF_DELIVERY.getCode(), param.getOrderNo());
        }
        if (t != null && t.getStatus() == TaskStatusEnum.COMPLETE) {
            throw new DMSException(BizErrorCode.ORDER_STATUS_LIMITED, param.getOrderNo());
        }

        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setMerchantId(param.getMerchantId());
        optRequest.setOptBy(param.getOptBy());
        optRequest.setOptType(OptTypeEnum.RECEIVE);
        optRequest.setRemark(param.getRemark());
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add(param.getOrderNo());
        optRequest.setOrderNo(orderNoList);
        orderService.handleOpt(optRequest);

        if (t != null) {
            Task task = new Task();
            task.setTaskId(t.getTaskId());
            task.setStatus(TaskStatusEnum.COMPLETE);
            task.setHandledTime(DateUtil.getSysTimeStamp());
            taskService.updateTask(task);
        }
    }

    @Override
    @Transactional
    public void abnormal(AbnormalParam param) {
        //根据订单号查询任务
        Task t = taskService.queryTaskByTypeAndOrderNo(param.getAbnormalOrder().getMerchantId(), null, param.getAbnormalOrder().getOrderNo());
        if (t == null || t.getStatus() == TaskStatusEnum.COMPLETE) {
            throw new DMSException(BizErrorCode.ORDER_STATUS_LIMITED, param.getAbnormalOrder().getOrderNo());
        }

        //新增异常件
        AbnormalOrder abnormalOrder = param.getAbnormalOrder();
        abnormalOrderService.addAbnormalOrder(abnormalOrder);
        //修改任务状态
        Task task = new Task();
        task.setTaskId(t.getTaskId());
        task.setStatus(TaskStatusEnum.COMPLETE);
        task.setHandledTime(DateUtil.getSysTimeStamp());
        taskService.updateTask(task);


    }

    @Override
    @Transactional
    public void delay(DelayParam param) {
        //判断是否允许delay
        DeliveryOrderDelayDO delayDO = deliveryOrderDelayDao.findByOrderNo(Long.parseLong(param.getMerchantId()), param.getOrderNo());

        //修改delay次数
        if (delayDO == null) {
            DeliveryOrderDelayDO insert = new DeliveryOrderDelayDO();
            insert.setOrderNo(param.getOrderNo());
            insert.setMerchantId(Long.parseLong(param.getMerchantId()));
            insert.setAllowTimes(MAX_DELAY_TIMES);
            insert.setDelayTimes(1);
            insert.setStatus(DelayStatusEnum.CREATE.getCode());
            insert.setDelayReason(param.getReason());
            insert.setRemark(param.getRemark());
            deliveryOrderDelayDao.insert(insert);
        } else {
            DeliveryOrderDelayDO update = new DeliveryOrderDelayDO();
            update.setOrderNo(param.getOrderNo());
            update.setMerchantId(Long.parseLong(param.getMerchantId()));
            update.setDelayTimes(delayDO.getDelayTimes() + 1);
            deliveryOrderDelayDao.update(update);
            if (update.getDelayTimes() == MAX_DELAY_TIMES) {
            }
        }

        //修改任务状态
        Task task = new Task();
        task.setTaskId(param.getTaskId());
        task.setStatus(TaskStatusEnum.COMPLETE);
        task.setHandledTime(DateUtil.getSysTimeStamp());
        taskService.updateTask(task);

    }

    @Override
    @Transactional
    public void detain(DelayParam param) {

        //查询运单信息
        DeliveryOrder deliveryOrder = orderService.queryByOrderNo(param.getMerchantId(), param.getOrderNo());

        DeliveryOrderDelayDO update = new DeliveryOrderDelayDO();
        update.setOrderNo(param.getOrderNo());
        update.setMerchantId(Long.parseLong(param.getMerchantId()));
        update.setStatus(DelayStatusEnum.COMPLETE.getCode());
        deliveryOrderDelayDao.update(update);

        AbnormalOrder abnormalOrder = new AbnormalOrder();
        abnormalOrder.setOrderNo(param.getOrderNo());
        abnormalOrder.setAbnormalType(param.getAbnormalType());
        abnormalOrder.setCreatedBy(param.getOptBy());
        abnormalOrder.setMerchantId(param.getMerchantId());
        abnormalOrder.setReferenceNo(deliveryOrder.getReferenceNo());
        abnormalOrder.setRemark(param.getRemark());
        //新增异常件
        abnormalOrderService.addAbnormalOrder(abnormalOrder);

    }


}
