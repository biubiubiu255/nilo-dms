package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.*;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.AbnormalOrderDao;
import com.nilo.dms.dao.dataobject.AbnormalOrderDO;
import com.nilo.dms.service.system.MerchantService;
import com.nilo.dms.service.order.AbnormalOrderService;
import com.nilo.dms.service.order.DeliveryFeeDetailsService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.*;
import com.nilo.dms.service.system.SystemCodeUtil;
import com.nilo.dms.service.system.SystemConfig;
import com.nilo.dms.service.system.model.MerchantInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/11/9.
 */
@Service
public class AbnormalOrderServiceImpl implements AbnormalOrderService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AbnormalOrderDao abnormalOrderDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MerchantService merchantService;

    @Override
    @Transactional
    public void addAbnormalOrder(AbnormalOrder abnormalOrder) {

        String abnormalNo = SystemConfig.getNextSerialNo(abnormalOrder.getMerchantId(), SerialTypeEnum.ABNORMAL_DELIVERY_ORDER_NO.getCode());
        abnormalOrder.setAbnormalNo(abnormalNo);
        abnormalOrder.setStatus(AbnormalOrderStatusEnum.CREATE);
        AbnormalOrderDO abnormalOrderDO = convertTo(abnormalOrder);
        abnormalOrderDao.insert(abnormalOrderDO);

        //操作运单状态为问题件
        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setMerchantId(abnormalOrder.getMerchantId());
        optRequest.setOptBy(abnormalOrder.getCreatedBy());
        optRequest.setOptType(OptTypeEnum.PROBLEM);
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add(abnormalOrder.getOrderNo());
        optRequest.setOrderNo(orderNoList);
        optRequest.setRemark(abnormalOrder.getRemark());
        orderService.handleOpt(optRequest);

    }

    @Override
    public List<AbnormalOrder> queryAbnormalBy(QueryAbnormalOrderParameter parameter, Pagination pagination) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", parameter.getOrderNo());
        map.put("merchantId", parameter.getMerchantId());
        map.put("abnormalType", parameter.getAbnormalType());
        map.put("status", parameter.getStatus());
        map.put("referenceNo", parameter.getReferenceNo());

        if (StringUtil.isEmpty(parameter.getFromCreatedTime()) || StringUtil.isEmpty(parameter.getToCreatedTime())) {
            if (StringUtil.isEmpty(parameter.getFromCreatedTime())) {
                parameter.setFromCreatedTime(parameter.getToCreatedTime());
            } else {
                parameter.setToCreatedTime(parameter.getFromCreatedTime());
            }
        }
        if (StringUtil.isNotEmpty(parameter.getFromCreatedTime())) {
            Long fromTime = DateUtil.parse(parameter.getFromCreatedTime(), "yyyy-MM-dd");
            map.put("fromCreatedTime", fromTime);
        }
        if (StringUtil.isNotEmpty(parameter.getToCreatedTime())) {
            Long toTime = DateUtil.parse(parameter.getToCreatedTime(), "yyyy-MM-dd") + 24 * 60 * 60 - 1;
            map.put("toCreatedTime", toTime);
        }
        map.put("offset", pagination.getOffset());
        map.put("limit", pagination.getLimit());

        //查询记录
        List<AbnormalOrderDO> queryList = abnormalOrderDao.queryAbnormalListBy(map);
        Long count = abnormalOrderDao.queryCountBy(map);
        pagination.setTotalCount(count == null ? 0 : count);

        List<AbnormalOrder> list = new ArrayList<>();
        if (queryList != null) {
            for (AbnormalOrderDO o : queryList) {
                AbnormalOrder order = convertTo(o);
                list.add(order);
            }
        }
        return list;
    }

    @Override
    public AbnormalOrder queryByOrderNo(String merchantId, String orderNo) {
        return null;
    }

    @Override
    @Transactional
    public void handleAbnormal(AbnormalOptRequest request) {

        String abnormalOrder = request.getAbnormalNo();
        AbnormalOrderDO abnormalOrderDO = abnormalOrderDao.queryByAbnormalNo(Long.parseLong(request.getMerchantId()), abnormalOrder);
        if (abnormalOrderDO == null) {
            throw new DMSException(BizErrorCode.ABNORMAL_ORDER_NOT_EXIST, abnormalOrder);
        }
        if (abnormalOrderDO.getStatus() != AbnormalOrderStatusEnum.CREATE.getCode()) {
            throw new DMSException(BizErrorCode.ABNORMAL_ORDER_STATUS_LIMITED, abnormalOrder);
        }
        String orderNo = abnormalOrderDO.getOrderNo();


        switch (request.getHandleType()) {
            case RESEND: {
                handlerAbnormalOpt(request, orderNo);
                break;
            }
            case RETURN: {
                handlerAbnormalOpt(request, orderNo);
                break;
            }
        }

    }


    private void handlerAbnormalOpt(AbnormalOptRequest request, String orderNo) {

        if (request.getHandleType() == AbnormalHandleTypeEnum.RETURN) {
            //处理订单状态
            OrderOptRequest optRequest = new OrderOptRequest();
            optRequest.setOptType(OptTypeEnum.PROBLEM);
            optRequest.setOptBy(request.getOptBy());
            optRequest.setMerchantId(request.getMerchantId());
            List<String> orderList = new ArrayList<>();
            orderList.add(orderNo);
            optRequest.setOrderNo(orderList);
            optRequest.setRemark(request.getRemark());
            orderService.handleOpt(optRequest);
        }

        //修改异常件状态及处理结果
        AbnormalOrderDO abnormalOrderDO = new AbnormalOrderDO();
        abnormalOrderDO.setMerchantId(Long.parseLong(request.getMerchantId()));
        abnormalOrderDO.setAbnormalNo(request.getAbnormalNo());
        abnormalOrderDO.setStatus(AbnormalOrderStatusEnum.COMPLETE.getCode());
        abnormalOrderDO.setHandleBy(request.getOptBy());
        abnormalOrderDO.setHandleRemark(request.getRemark());
        abnormalOrderDO.setHandleType(request.getHandleType().getCode());
        abnormalOrderDO.setHandleTime(DateUtil.getSysTimeStamp());
        abnormalOrderDao.update(abnormalOrderDO);
    }


    private void returnToMerchant(String merchantId, String orderNo) {

        DeliveryOrder deliveryOrder = orderService.queryByOrderNo(merchantId, orderNo);
        deliveryOrder.setServiceType(ServiceTypeEnum.NORMAL);
        deliveryOrder.setStatus(DeliveryOrderStatusEnum.CREATE);
        deliveryOrder.setReferenceNo(deliveryOrder.getOrderNo());
        deliveryOrder.setOrderType("RM");

        SenderInfo senderInfo = new SenderInfo();
        senderInfo.setSenderName("System");


        MerchantInfo merchantInfo = merchantService.findById(merchantId);
        ReceiverInfo receiverInfo = new ReceiverInfo();
        receiverInfo.setReceiverName(merchantInfo.getContactName());
        receiverInfo.setReceiverCountry(merchantInfo.getCountry());
        receiverInfo.setReceiverProvince("2");
        receiverInfo.setReceiverCity("3");
        receiverInfo.setReceiverArea("4");
        receiverInfo.setReceiverAddress(merchantInfo.getAddress());
        receiverInfo.setReceiverPhone(merchantInfo.getContactPhone());

        deliveryOrder.setSenderInfo(senderInfo);
        deliveryOrder.setReceiverInfo(receiverInfo);
        orderService.createDeliveryOrder(deliveryOrder);

    }

    private AbnormalOrderDO convertTo(AbnormalOrder abnormalOrder) {
        if (abnormalOrder == null) {
            return null;
        }
        AbnormalOrderDO abnormalOrderDO = new AbnormalOrderDO();
        abnormalOrderDO.setReferenceNo(abnormalOrder.getReferenceNo());
        if (abnormalOrder.getStatus() != null) {
            abnormalOrderDO.setStatus(abnormalOrder.getStatus().getCode());
        }
        abnormalOrderDO.setCreatedBy(abnormalOrder.getCreatedBy());
        abnormalOrderDO.setMerchantId(Long.parseLong(abnormalOrder.getMerchantId()));
        abnormalOrderDO.setAbnormalNo(abnormalOrder.getAbnormalNo());
        abnormalOrderDO.setAbnormalType(abnormalOrder.getAbnormalType());
        if (abnormalOrder.getHandleType() != null) {
            abnormalOrderDO.setHandleType(abnormalOrder.getHandleType().getCode());
        }
        abnormalOrderDO.setRemark(abnormalOrder.getRemark());
        abnormalOrderDO.setOrderNo(abnormalOrder.getOrderNo());

        return abnormalOrderDO;
    }

    private AbnormalOrder convertTo(AbnormalOrderDO abnormalOrderDO) {
        if (abnormalOrderDO == null) {
            return null;
        }
        AbnormalOrder abnormalOrder = new AbnormalOrder();
        abnormalOrder.setReferenceNo(abnormalOrderDO.getReferenceNo());
        abnormalOrder.setStatus(AbnormalOrderStatusEnum.getEnum(abnormalOrderDO.getStatus()));
        abnormalOrder.setCreatedBy(abnormalOrderDO.getCreatedBy());
        abnormalOrder.setMerchantId("" + abnormalOrderDO.getMerchantId());
        abnormalOrder.setAbnormalNo(abnormalOrderDO.getAbnormalNo());
        abnormalOrder.setAbnormalType(abnormalOrderDO.getAbnormalType());
        String abnormalTypeDesc = SystemCodeUtil.getCodeVal("" + abnormalOrderDO.getMerchantId(), Constant.ABNORMAL_ORDER_TYPE, abnormalOrderDO.getAbnormalType());
        abnormalOrder.setAbnormalTypeDesc(abnormalTypeDesc == "" ? abnormalOrderDO.getAbnormalType() : abnormalTypeDesc);
        abnormalOrder.setRemark(abnormalOrderDO.getRemark());
        abnormalOrder.setOrderNo(abnormalOrderDO.getOrderNo());

        abnormalOrder.setCreatedTime(abnormalOrderDO.getCreatedTime());
        abnormalOrder.setUpdatedTime(abnormalOrderDO.getUpdatedTime());

        if (StringUtil.isNotEmpty(abnormalOrderDO.getHandleType())) {
            abnormalOrder.setHandleType(AbnormalHandleTypeEnum.getEnum(abnormalOrderDO.getHandleType()));
        }
        abnormalOrder.setHandleBy(abnormalOrderDO.getHandleBy());
        abnormalOrder.setHandleRemark(abnormalOrderDO.getHandleRemark());
        abnormalOrder.setHandleTime(abnormalOrderDO.getHandleTime());

        return abnormalOrder;
    }

}
