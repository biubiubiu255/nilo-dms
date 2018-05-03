package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.AbnormalTypeEnum;
import com.nilo.dms.common.enums.DelayStatusEnum;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.dao.HandleAllocateDao;
import com.nilo.dms.dao.HandleDelayDao;
import com.nilo.dms.dao.HandleRefuseDao;
import com.nilo.dms.dao.HandleSignDao;
import com.nilo.dms.dao.dataobject.HandleSignDO;
import com.nilo.dms.dto.common.UserInfo;
import com.nilo.dms.dto.handle.HandleAllocate;
import com.nilo.dms.dto.handle.HandleDelay;
import com.nilo.dms.dto.handle.HandleRefuse;
import com.nilo.dms.dto.order.*;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.AbnormalOrderService;
import com.nilo.dms.service.order.AbstractOrderOpt;
import com.nilo.dms.service.order.WaybillOptService;
import com.nilo.dms.service.order.WaybillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/10/30.
 */
@Service
public class WaybillOptServiceImpl extends AbstractOrderOpt implements WaybillOptService {

    @Autowired
    WaybillService waybillService;
    @Autowired
    private AbnormalOrderService abnormalOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private HandleDelayDao handleDelayDao;
    @Autowired
    private HandleAllocateDao handleAllocateDao;
    @Autowired
    private HandleRefuseDao handleRefuseDao;
    @Autowired
    private HandleSignDao handleSignDao;

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
    public void refuse(HandleRefuse handleRefuse) {
        Long merchantId = Long.parseLong(SessionLocal.getPrincipal().getMerchantId());
        if(handleRefuse.getMerchantId()==null){
            handleRefuse.setMerchantId(merchantId);
        }
        Waybill deliveryOrder = waybillService.queryByOrderNo(merchantId.toString(), handleRefuse.getOrderNo());
        if (deliveryOrder == null) {
            throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, handleRefuse.getOrderNo());
        }
        
        List<HandleRefuse> handleRefuseList = handleRefuseDao.queryByDO(handleRefuse);

        if(handleRefuseList.size()!=0){
            throw new DMSException(BizErrorCode.REFUSE_ALREADY_EXISTS, handleRefuse.getOrderNo());
        }
        handleRefuse.setStatus(DelayStatusEnum.CREATE.getCode());
        handleRefuseDao.insert(handleRefuse);

        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setOptType(OptTypeEnum.REFUSE);
        optRequest.setRemark(handleRefuse.getRemark());
        optRequest.setOrderNo(new ArrayList<String>(Arrays.asList(new String[]{handleRefuse.getOrderNo()})));
        waybillService.handleOpt(optRequest);
    }

    @Override
    @Transactional
    public void delay(DelayParam param) {

        Principal principal = SessionLocal.getPrincipal();

        Waybill waybill = waybillService.queryByOrderNo(principal.getMerchantId(), param.getOrderNo());
        WaybillHeader header = new Waybill();
        if(waybill==null || waybill.getStatus().equals(DeliveryOrderStatusEnum.SIGN)){
            throw new DMSException(BizErrorCode.NOT_BECAME_DELAY);
        }

        //开始插入
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
        //结束插入


        header.setMerchantId(principal.getMerchantId());
        header.setOrderNo(param.getOrderNo());
        header.setDelayTimes(waybill.getDelayTimes() == null ? 1 : waybill.getDelayTimes() + 1);
        if(header.getAreDelay()==null || header.getAreDelay()==false){
            header.setAreDelay(true);
        }
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

    @Override
    public void allocate(List<String> orderNos, String riderId, String remark) {

        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setOptType(OptTypeEnum.ALLOCATE);
        optRequest.setRemark(remark);
        optRequest.setOrderNo(orderNos);
        waybillService.handleOpt(optRequest);

        Principal principal = SessionLocal.getPrincipal();
        UserInfo userInfo = userService.findUserInfoByUserId(principal.getMerchantId(), riderId);
        List<HandleAllocate> list = new ArrayList<>();
        Long merchantId = Long.parseLong(principal.getMerchantId());
        for (String o : orderNos) {
            HandleAllocate h = new HandleAllocate();
            h.setOrderNo(o);
            h.setHandleBy(principal.getUserId());
            h.setHandleName(principal.getUserName());
            h.setNetworkCode(principal.getFirstNetwork());
            h.setMerchantId(merchantId);
            h.setRemark(remark);
            h.setRiderId(riderId);
            h.setRiderName(userInfo.getName());
            h.setRiderPhone(userInfo.getPhone());
            list.add(h);
        }
        handleAllocateDao.batchInsert(list);
    }
}
