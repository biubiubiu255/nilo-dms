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
import com.nilo.dms.service.system.model.InterfaceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

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
    private TransactionTemplate transactionTemplate;

    @Autowired
    private DeliveryOrderDelayDao deliveryOrderDelayDao;

    @Override
    public void goToPickup(String merchantId, String orderNo, String optBy, String taskId) {


        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {
                try {
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
                } catch (Exception e) {
                    logger.error("goToPickup Failed. OrderNo:{}", orderNo, e);
                    transactionStatus.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });
    }

    @Override
    public void pickup(String merchantId, String orderNo, String optBy, String taskId) {

        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {
                try {
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
                } catch (Exception e) {
                    logger.error("goToPickup Failed. OrderNo:{}", orderNo, e);
                    transactionStatus.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });

    }

    @Override
    public void pickupFailed(String merchantId, String orderNo, String reason, String optBy, String taskId) {

        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {
                try {
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
                } catch (Exception e) {
                    logger.error("goToPickup Failed. OrderNo:{}", orderNo, e);
                    transactionStatus.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });
    }

    @Override
    public void signForOrder(SignForOrderParam param) {

        //根据订单号查询任务
        Task t = taskService.queryTaskByTypeAndOrderNo(param.getMerchantId(), TaskTypeEnum.DISPATCH.getCode(),param.getOrderNo());
        if(t == null || t.getStatus() ==TaskStatusEnum.COMPLETE){
            throw new DMSException(BizErrorCode.ORDER_STATUS_LIMITED,param.getOrderNo());
        }

        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {
                try {
                    OrderOptRequest optRequest = new OrderOptRequest();
                    optRequest.setMerchantId(param.getMerchantId());
                    optRequest.setOptBy(param.getOptBy());
                    optRequest.setOptType(OptTypeEnum.RECEIVE);
                    optRequest.setRemark(param.getRemark());
                    List<String> orderNoList = new ArrayList<>();
                    orderNoList.add(param.getOrderNo());
                    optRequest.setOrderNo(orderNoList);
                    orderService.handleOpt(optRequest);

                    Task task = new Task();
                    task.setTaskId(t.getTaskId());
                    task.setStatus(TaskStatusEnum.COMPLETE);
                    task.setHandledTime(DateUtil.getSysTimeStamp());

                    taskService.updateTask(task);
                } catch (Exception e) {
                    logger.error("goToPickup Failed. OrderNo:{}", param.getOrderNo(), e);
                    transactionStatus.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });
    }

    @Override
    public void abnormal(AbnormalParam param) {
        //根据订单号查询任务
        Task t = taskService.queryTaskByTypeAndOrderNo(param.getAbnormalOrder().getMerchantId(), TaskTypeEnum.DISPATCH.getCode(),param.getAbnormalOrder().getOrderNo());
        if(t == null || t.getStatus() ==TaskStatusEnum.COMPLETE){
            throw new DMSException(BizErrorCode.ORDER_STATUS_LIMITED,param.getAbnormalOrder().getOrderNo());
        }
        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {
                try {
                    //新增异常件
                    AbnormalOrder abnormalOrder = param.getAbnormalOrder();
                    abnormalOrderService.addAbnormalOrder(abnormalOrder);
                    //修改任务状态
                    Task task = new Task();
                    task.setTaskId(t.getTaskId());
                    task.setStatus(TaskStatusEnum.COMPLETE);
                    task.setHandledTime(DateUtil.getSysTimeStamp());
                    taskService.updateTask(task);
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });


    }

    @Override
    public void delay(DelayParam param) {
        //判断是否允许delay
        DeliveryOrderDelayDO delayDO = deliveryOrderDelayDao.findByOrderNo(Long.parseLong(param.getMerchantId()), param.getOrderNo());
        if (delayDO != null && delayDO.getDelayTimes() >= delayDO.getAllowTimes()) {
            throw new DMSException(BizErrorCode.DELAY_TIMES_LIMIT);
        }
        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {
                try {
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
                    }

                    //修改任务状态
                    Task task = new Task();
                    task.setTaskId(param.getTaskId());
                    task.setStatus(TaskStatusEnum.COMPLETE);
                    task.setHandledTime(DateUtil.getSysTimeStamp());
                    taskService.updateTask(task);
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });
    }

    @Override
    public void detain(DelayParam param) {

        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {
                try {

                    DeliveryOrderDelayDO update = new DeliveryOrderDelayDO();
                    update.setOrderNo(param.getOrderNo());
                    update.setMerchantId(Long.parseLong(param.getMerchantId()));
                    update.setStatus(DelayStatusEnum.DETAIN.getCode());
                    deliveryOrderDelayDao.update(update);

                    //修改订单状态为滞留
                    OrderOptRequest optRequest = new OrderOptRequest();
                    optRequest.setMerchantId(param.getMerchantId());
                    optRequest.setOptBy(param.getOptBy());
                    optRequest.setOptType(OptTypeEnum.DETAIN);
                    optRequest.setRemark(param.getRemark());
                    List<String> orderNoList = new ArrayList<>();
                    orderNoList.add(param.getOrderNo());
                    optRequest.setOrderNo(orderNoList);
                    orderService.handleOpt(optRequest);
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });
    }


}
