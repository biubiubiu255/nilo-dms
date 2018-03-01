package com.nilo.dms.service.order.impl;
import java.util.*;

import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.dao.*;
import com.nilo.dms.service.order.*;
import com.nilo.dms.service.order.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.service.order.model.SendReport;
import com.nilo.dms.dao.dataobject.SendReportDO;

/**
 * Created by ronny on 2017/9/15.
 */
@Service
public class SendReportServiceImpl implements SendReportService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SendReportDao SendReportDao;


    @Override
    public List<SendReport> querySendReport(SendOrderParameter parameter, Pagination pagination) {

        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", parameter.getOrderNo());
        map.put("nextNetwork", parameter.getNextNetwork());
        map.put("status", parameter.getStatus());
        map.put("carrierName", parameter.getCarrierName());

        map.put("offset", pagination.getOffset());
        map.put("limit", pagination.getLimit());

        // 查询记录
        List<SendReportDO> queryList = SendReportDao.querySendReport(map);
        Long count = SendReportDao.queryCountBy(map);
        pagination.setTotalCount(count == null ? 0 : count);
        return batchQuery(queryList, Long.parseLong(parameter.getMerchantId()));
    }

    private List<SendReport> batchQuery(List<SendReportDO> sendReportDOs, Long merchantId) {

        List<SendReport> list = new ArrayList<>();
        // 构建订单号集合
        List<String> orderNos = new ArrayList<>();
        for (SendReportDO o : sendReportDOs) {
            orderNos.add(o.getOrderNo());
        }
//        List<SendReportDO> senderDO = deliveryOrderSenderDao.queryByOrderNos2(merchantId, orderNos);
//        List<SendReportDO> receiverDO = deliveryOrderReceiverDao.queryByOrderNos2(merchantId, orderNos);
        for (SendReportDO o : sendReportDOs) {
            SendReport order = convert(o);
            list.add(order);
        }
        return list;
    }

    private SendReport convert(SendReportDO s) {
        SendReport sendReport = new SendReport();
        sendReport.setOrderNo(s.getOrderNo());
        sendReport.setAddress(s.getAddress());
        sendReport.setCarrierName(s.getCarrierName());
        sendReport.setContactNumber(s.getContactNumber());
        sendReport.setDeliveryFee(s.getDeliveryFee());
        sendReport.setName(s.getName());
        sendReport.setNetwork(s.getNetwork());
        sendReport.setNextNetwork(s.getNextNetwork());
        sendReport.setOrderCategory(s.getOrderCategory());
        sendReport.setReferenceNo(s.getReferenceNo());
        sendReport.setWeight(s.getWeight());
        sendReport.setStatus(DeliveryOrderStatusEnum.getEnum(s.getStatus()));
        sendReport.setStop(s.getStop());
        sendReport.setRemark(s.getRemark());

        return sendReport;
    }
}
