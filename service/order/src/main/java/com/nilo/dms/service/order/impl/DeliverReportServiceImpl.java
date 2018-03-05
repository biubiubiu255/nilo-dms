package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.TaskStatusEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliverReportDao;
import com.nilo.dms.dao.dataobject.DeliverReportDO;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.service.order.DeliverReportService;
import com.nilo.dms.service.order.model.DeliverOrderParameter;
import com.nilo.dms.service.order.model.DeliverReport;
import com.nilo.dms.service.system.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
@Service
public class DeliverReportServiceImpl implements DeliverReportService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DeliverReportDao seliverReportDao;

    @Override
    public List<DeliverReport> queryDeliverReport(DeliverOrderParameter parameter, Pagination pagination) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", parameter.getOrderNo());
        map.put("status", parameter.getStatus());
        map.put("merchantId", parameter.getMerchantId());
        map.put("carrierName", parameter.getCarrierName());
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

        // 查询记录
        List<DeliverReportDO> queryList = seliverReportDao.queryDeliverReport(map);
//        for(DeliverReportDO d:queryList){
//            System.out.println(d.getHandledBy());
//        }
        Long count = seliverReportDao.queryCountBy(map);
        pagination.setTotalCount(count == null ? 0 : count);
        return batchQuery(queryList, Long.parseLong(parameter.getMerchantId()));
    }

    private List<DeliverReport> batchQuery(List<DeliverReportDO> deliverReportDOs, Long merchantId) {

        List<DeliverReport> list = new ArrayList<>();
        // 构建订单号集合
        List<String> orderNos = new ArrayList<>();
        for (DeliverReportDO o : deliverReportDOs) {
            orderNos.add(o.getOrderNo());
        }
//        List<SendReportDO> senderDO = deliveryOrderSenderDao.queryByOrderNos2(merchantId, orderNos);
//        List<SendReportDO> receiverDO = deliveryOrderReceiverDao.queryByOrderNos2(merchantId, orderNos);
        for (DeliverReportDO o : deliverReportDOs) {
            DeliverReport order = convert(o);
            list.add(order);
        }
        return list;
    }

    private DeliverReport convert(DeliverReportDO s) {
        DeliverReport deliverReport = new DeliverReport();
        deliverReport.setMerchantId("" + s.getMerchantId());
        deliverReport.setOrderNo(s.getOrderNo());
        deliverReport.setAddress(s.getAddress());
        deliverReport.setContactNumber(s.getContactNumber());
        deliverReport.setDeliveryFee(s.getDeliveryFee());
        deliverReport.setName(s.getName());
        deliverReport.setNetworkId(s.getNetworkId());
        if (s.getNetworkId() != null) {
            DistributionNetworkDO networkDO = JSON.parseObject(RedisUtil.hget(Constant.NETWORK_INFO + s.getMerchantId(), "" + s.getNetworkId()), DistributionNetworkDO.class);
            deliverReport.setNetworkDesc(networkDO.getName());
        }
        deliverReport.setHandledBy(s.getHandledBy());
        deliverReport.setReferenceNo(s.getReferenceNo());
        deliverReport.setStatus(TaskStatusEnum.getEnum(s.getStatus()));
        deliverReport.setRemark(s.getRemark());
        deliverReport.setCreatedTime(s.getCreatedTime());
        deliverReport.setHandledTime(s.getHandledTime());
        deliverReport.setNeedPayAmount(s.getNeedPayAmount());

        return deliverReport;
    }
}
