package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.AbnormalTypeEnum;
import com.nilo.dms.common.enums.DelayStatusEnum;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.dao.HandleDelayDao;
import com.nilo.dms.dao.HandleSignDao;
import com.nilo.dms.dao.dataobject.HandleSignDO;
import com.nilo.dms.dto.handle.HandleDelay;
import com.nilo.dms.dto.order.*;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.AbnormalOrderService;
import com.nilo.dms.service.order.AbstractOrderOpt;
import com.nilo.dms.service.order.WaybillOptService;
import com.nilo.dms.service.order.WaybillService;
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
    private HandleDelayDao handleDelayDao;
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
        signDO.setHandleBy(principal.getUserId());
        signDO.setHandleTime(DateUtil.getSysTimeStamp());
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

        Principal principal = SessionLocal.getPrincipal();

        HandleDelay insert = new HandleDelay();
        insert.setOrderNo(param.getOrderNo());
        insert.setMerchantId(Long.parseLong(principal.getMerchantId()));
        insert.setHandleBy(principal.getUserId());
        insert.setHandleName(principal.getUserName());
        insert.setStatus(DelayStatusEnum.CREATE.getCode());
        insert.setRemark(param.getRemark());
        insert.setReason(param.getReason());
        insert.setReasonId(param.getReasonId());
        handleDelayDao.insert(insert);

        Waybill waybill = waybillService.queryByOrderNo(principal.getMerchantId(), param.getOrderNo());
        WaybillHeader header = new Waybill();
        header.setMerchantId(principal.getMerchantId());
        header.setOrderNo(param.getOrderNo());
        header.setDelayTimes(waybill.getDelayTimes() == null ? 1 : waybill.getDelayTimes() + 1);
        waybillService.updateWaybill(header);
    }

    @Override
    @Transactional
    public void completeDelay(String orderNo) {

        Principal principal = SessionLocal.getPrincipal();
        HandleDelay update = new HandleDelay();
        update.setOrderNo(orderNo);
        update.setMerchantId(Long.parseLong(principal.getMerchantId()));
        update.setStatus(DelayStatusEnum.COMPLETE.getCode());
        handleDelayDao.update(update);

    }
}
