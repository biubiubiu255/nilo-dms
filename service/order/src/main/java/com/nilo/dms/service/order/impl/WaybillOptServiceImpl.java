package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.*;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.dao.DeliveryOrderDelayDao;
import com.nilo.dms.dao.HandleSignDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderDelayDO;
import com.nilo.dms.dao.dataobject.HandleSignDO;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.dto.order.*;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.*;
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
public class WaybillOptServiceImpl extends AbstractOrderOpt implements WaybillOptService {

    private static final Integer MAX_DELAY_TIMES = 2;
    @Autowired
    WaybillService waybillService;
    @Autowired
    private AbnormalOrderService abnormalOrderService;
    @Autowired
    private DeliveryOrderDelayDao deliveryOrderDelayDao;
    @Autowired
    private HandleSignDao handleSignDao;


    @Override
    @Transactional
    public void goToPickup(String merchantId, String orderNo, String optBy, String taskId) {
        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setOptType(OptTypeEnum.GO_TO_PICK_UP);
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add(orderNo);
        optRequest.setOrderNo(orderNoList);
        waybillService.handleOpt(optRequest);
    }

    @Override
    @Transactional
    public void pickup(String merchantId, String orderNo, String optBy, String taskId) {
        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setOptType(OptTypeEnum.PICK_UP);
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add(orderNo);
        optRequest.setOrderNo(orderNoList);
        waybillService.handleOpt(optRequest);
    }

    @Override
    @Transactional
    public void pickupFailed(String merchantId, String orderNo, String reason, String optBy, String taskId) {

        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setOptType(OptTypeEnum.PICK_UP_FAILED);
        optRequest.setRemark(reason);
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add(orderNo);
        optRequest.setOrderNo(orderNoList);
        waybillService.handleOpt(optRequest);

    }

    @Override
    public void sign(String orderNo, String remark) {

        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setOptType(OptTypeEnum.SIGN);
        optRequest.setRemark(remark);
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add(orderNo);
        optRequest.setOrderNo(orderNoList);
        waybillService.handleOpt(optRequest);

        //写入 t_handler_sign
        Principal principal = SessionLocal.getPrincipal();

        HandleSignDO signDO = new HandleSignDO();
        signDO.setMerchantId(Long.parseLong(principal.getMerchantId()));
        signDO.setOrderNo(orderNo);
        signDO.setRemark(remark);
        signDO.setHandledBy(principal.getUserId());
        signDO.setHandledTime(DateUtil.getSysTimeStamp());
        signDO.setNetworkCode(principal.getFirstNetwork());
        signDO.setSigner("Self");
        handleSignDao.insert(signDO);
    }

    @Override
    @Transactional
    public void refuse(AbnormalParam param) {

        Waybill deliveryOrder = waybillService.queryByOrderNo(param.getMerchantId(), param.getOrderNo());
        if (deliveryOrder == null) {
            throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, param.getOrderNo());
        }
        //新增异常件
        AbnormalOrder abnormalOrder = new AbnormalOrder();
        abnormalOrder.setMerchantId(param.getMerchantId());
        abnormalOrder.setAbnormalType(AbnormalTypeEnum.REFUSE);
        abnormalOrder.setOrderNo(param.getOrderNo());
        abnormalOrder.setRemark(param.getRemark());
        abnormalOrder.setReason(param.getReason());
        abnormalOrder.setCreatedBy(param.getOptBy());
        abnormalOrderService.addAbnormalOrder(abnormalOrder);
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

    }

    @Override
    @Transactional
    public void resend(DelayParam param) {

        DeliveryOrderDelayDO update = new DeliveryOrderDelayDO();
        update.setOrderNo(param.getOrderNo());
        update.setMerchantId(Long.parseLong(param.getMerchantId()));
        update.setStatus(DelayStatusEnum.COMPLETE.getCode());
        deliveryOrderDelayDao.update(update);
    }
}
