package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.*;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.AbnormalOrderDao;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.dataobject.AbnormalOrderDO;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.dto.order.AbnormalOptRequest;
import com.nilo.dms.dto.order.AbnormalOrder;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.dto.order.QueryAbnormalOrderParameter;
import com.nilo.dms.service.order.AbnormalOrderService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.service.system.SystemCodeUtil;
import com.nilo.dms.service.system.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nilo.dms.common.enums.AbnormalTypeEnum.PROBLEM;

/**
 * Created by admin on 2017/11/9.
 */
@Service
public class AbnormalOrderServiceImpl implements AbnormalOrderService {

    @Autowired
    private AbnormalOrderDao abnormalOrderDao;
    @Autowired
    private WaybillService waybillService;
    @Autowired
    private WaybillDao waybillDao;

    @Override
    @Transactional
    public void addAbnormalOrder(AbnormalOrder abnormalOrder) {

        WaybillDO orderDO = waybillDao.queryByOrderNo(Long.parseLong(abnormalOrder.getMerchantId()), abnormalOrder.getOrderNo());
        if (orderDO == null) throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, abnormalOrder.getOrderNo());

        String abnormalNo = SystemConfig.getNextSerialNo(abnormalOrder.getMerchantId(), SerialTypeEnum.ABNORMAL_DELIVERY_ORDER_NO.getCode());
        abnormalOrder.setAbnormalNo(abnormalNo);
        abnormalOrder.setStatus(AbnormalOrderStatusEnum.CREATE);
        AbnormalOrderDO abnormalOrderDO = convertTo(abnormalOrder);
        abnormalOrderDao.insert(abnormalOrderDO);

        //操作运单状态为问题件
        OrderOptRequest optRequest = new OrderOptRequest();

        switch (abnormalOrder.getAbnormalType()) {
            case PROBLEM: {
                optRequest.setOptType(OptTypeEnum.PROBLEM);
                break;
            }
            case REFUSE: {
                optRequest.setOptType(OptTypeEnum.REFUSE);
                break;
            }
        }
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add(abnormalOrder.getOrderNo());
        optRequest.setOrderNo(orderNoList);
        optRequest.setRemark(abnormalOrder.getRemark());
        waybillService.handleOpt(optRequest);

    }

    @Override
    public void delete(String abnormalNo, String merchantId) {

        AbnormalOrderDO abnormalOrder = new AbnormalOrderDO();
        abnormalOrder.setMerchantId(Long.parseLong(merchantId));
        abnormalOrder.setAbnormalNo(abnormalNo);
        abnormalOrder.setStatus(AbnormalOrderStatusEnum.DELETE.getCode());
        abnormalOrderDao.update(abnormalOrder);
    }

    @Override
    public List<AbnormalOrder> queryAbnormalBy(QueryAbnormalOrderParameter parameter, Pagination pagination) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", parameter.getOrderNo());
        map.put("merchantId", parameter.getMerchantId());
        map.put("abnormalType", parameter.getAbnormalType());
        map.put("status", parameter.getStatus());
        map.put("reason", parameter.getReason());

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
                break;
            }
            case RETURN: {
                break;
            }
        }

    }


    private AbnormalOrderDO convertTo(AbnormalOrder abnormalOrder) {
        if (abnormalOrder == null) {
            return null;
        }
        AbnormalOrderDO abnormalOrderDO = new AbnormalOrderDO();
        if (abnormalOrder.getStatus() != null) {
            abnormalOrderDO.setStatus(abnormalOrder.getStatus().getCode());
        }
        abnormalOrderDO.setCreatedBy(abnormalOrder.getCreatedBy());
        abnormalOrderDO.setMerchantId(Long.parseLong(abnormalOrder.getMerchantId()));
        abnormalOrderDO.setAbnormalNo(abnormalOrder.getAbnormalNo());
        if (abnormalOrder.getAbnormalType() != null) {
            abnormalOrderDO.setAbnormalType(abnormalOrder.getAbnormalType().getCode());
        }
        abnormalOrderDO.setRemark(abnormalOrder.getRemark());
        abnormalOrderDO.setOrderNo(abnormalOrder.getOrderNo());
        abnormalOrderDO.setReason(abnormalOrder.getReason());
        return abnormalOrderDO;
    }

    private AbnormalOrder convertTo(AbnormalOrderDO abnormalOrderDO) {
        if (abnormalOrderDO == null) {
            return null;
        }
        AbnormalOrder abnormalOrder = new AbnormalOrder();
        abnormalOrder.setStatus(AbnormalOrderStatusEnum.getEnum(abnormalOrderDO.getStatus()));
        abnormalOrder.setCreatedBy(abnormalOrderDO.getCreatedBy());
        abnormalOrder.setMerchantId("" + abnormalOrderDO.getMerchantId());
        abnormalOrder.setAbnormalNo(abnormalOrderDO.getAbnormalNo());

        AbnormalTypeEnum type = AbnormalTypeEnum.getEnum(abnormalOrderDO.getAbnormalType());
        abnormalOrder.setAbnormalType(type);
        String reasonDesc = "";
        if (type == PROBLEM) {
            reasonDesc = SystemCodeUtil.getCodeVal("" + abnormalOrderDO.getMerchantId(), Constant.PRBOLEM_REASON, abnormalOrderDO.getReason());
        } else {
            reasonDesc = SystemCodeUtil.getCodeVal("" + abnormalOrderDO.getMerchantId(), Constant.REFUSE_REASON, abnormalOrderDO.getReason());
        }
        abnormalOrder.setReasonDesc(reasonDesc);
        abnormalOrder.setRemark(abnormalOrderDO.getRemark());
        abnormalOrder.setOrderNo(abnormalOrderDO.getOrderNo());
        abnormalOrder.setCreatedTime(abnormalOrderDO.getCreatedTime());
        abnormalOrder.setUpdatedTime(abnormalOrderDO.getUpdatedTime());
        abnormalOrder.setReason(abnormalOrderDO.getReason());


        return abnormalOrder;
    }

}
