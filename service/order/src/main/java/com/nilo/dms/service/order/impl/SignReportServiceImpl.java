package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.SignReportDao;
import com.nilo.dms.dao.dataobject.SignReportDO;
import com.nilo.dms.service.order.SignReportService;
import com.nilo.dms.dto.order.SignOrderParameter;
import com.nilo.dms.dto.order.SignReport;
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
public class SignReportServiceImpl implements SignReportService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SignReportDao signReportDao;

    @Override
    public List<SignReport> querySignReport(SignOrderParameter parameter, Pagination pagination) {

        Map<String, Object> map = new HashMap<>();

        map.put("orderNo", parameter.getOrderNo());
        map.put("nextNetwork", parameter.getNextNetwork());
        map.put("status", parameter.getStatus());
        map.put("carrierName", parameter.getCarrierName());
        map.put("fromHandledTime", parameter.getFromHandledTime());
        map.put("toHandledTime", parameter.getToHandledTime());
        map.put("merchantId", parameter.getMerchantId());

        if (StringUtil.isEmpty(parameter.getFromHandledTime()) || StringUtil.isEmpty(parameter.getToHandledTime())) {
            if (StringUtil.isEmpty(parameter.getFromHandledTime())) {
                parameter.setFromHandledTime(parameter.getToHandledTime());
            } else {
                parameter.setToHandledTime(parameter.getFromHandledTime());
            }
        }

        map.put("offset", pagination.getOffset());
        map.put("limit", pagination.getLimit());

        // 查询记录
        List<SignReportDO> queryList = signReportDao.querySignReport(map);
        Long count = signReportDao.queryCountBy(map);
        pagination.setTotalCount(count == null ? 0 : count);
        return batchQuery(queryList, Long.parseLong(parameter.getMerchantId()));
    }

    private List<SignReport> batchQuery(List<SignReportDO> SignReportDOs, Long merchantId) {

        List<SignReport> list = new ArrayList<>();
        // 构建订单号集合
        List<String> orderNos = new ArrayList<>();
        for (SignReportDO o : SignReportDOs) {
            orderNos.add(o.getOrderNo());
        }
//        List<SendReportDO> senderDO = deliveryOrderSenderDao.queryByOrderNos2(merchantId, orderNos);
//        List<SendReportDO> receiverDO = deliveryOrderReceiverDao.queryByOrderNos2(merchantId, orderNos);
        for (SignReportDO o : SignReportDOs) {
            SignReport order = convert(o);
            list.add(order);
        }
        return list;
    }

    private SignReport convert(SignReportDO s) {
        SignReport signReport = new SignReport();
        signReport.setMerchantId(s.getMerchantId());
        signReport.setOrderNo(s.getOrderNo());
        signReport.setAddress(s.getAddress());
        signReport.setContactNumber(s.getContactNumber());
        signReport.setReferenceNo(s.getReferenceNo());
        signReport.setWeight(s.getWeight());
        signReport.setRemark(s.getRemark());
        signReport.setsName(s.getsName());
        signReport.setrName(s.getrName());
        signReport.setHandleTime(s.getHandleTime());
        signReport.setNeedPayAmount(s.getNeedPayAmount());
        signReport.setAlreadyPaid(s.getAlreadyPaid());
        signReport.setHandleBy(s.getHandleBy());
        signReport.setSigner(s.getSigner());
        signReport.setStatus(s.getStatus());

        return signReport;
    }
}
