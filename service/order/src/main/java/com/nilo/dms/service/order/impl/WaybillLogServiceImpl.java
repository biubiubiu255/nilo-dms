package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.dao.dataobject.WaybillLogArriveDO;
import com.nilo.dms.dao.dataobject.WaybillLogPackageDO;
import com.nilo.dms.dao.dataobject.WaybillLogUnPackDO;
import com.nilo.dms.dto.order.WaybillLog;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.WaybillLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */
@Service
public class WaybillLogServiceImpl implements WaybillLogService {

    @Autowired
    private WaybillLogDao waybillLogDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private WaybillLogArriveDao waybillLogArriveDao;
    @Autowired
    private WaybillLogUnPackDao waybillLogUnPackDao;
    @Autowired
    private WaybillLogPackageDao waybillLogPackageDao;

    @Override
    public List<WaybillLog> queryBy(String merchantId, String optType, String orderNo, String optBy, Long fromtTime, Long toTime, Pagination pagination) {

        List<WaybillLog> list = new ArrayList<>();

        Long count = waybillLogDao.queryCountBy(Long.parseLong(merchantId), optType, orderNo, optBy, fromtTime, toTime);
        if (count == null || count == 0) {
            return list;
        }
        pagination.setTotalCount(count);

        List<WaybillLog> queryList = waybillLogDao.queryBy(Long.parseLong(merchantId), optType, orderNo, optBy, fromtTime, toTime, pagination.getOffset(), pagination.getLimit());

        return queryList;
    }

    @Override
    public List<WaybillLog> queryByOrderNos(String merchantId, List<String> orderNos) {

        List<WaybillLog> queryList = waybillLogDao.queryByOrderNos(Long.parseLong(merchantId), orderNos);
        return queryList;
    }

    @Override
    public void addOptLog(OrderOptRequest request) {

        Principal principal = SessionLocal.getPrincipal();
        String merchantId = principal.getMerchantId();
        String userId = principal.getUserId();
        String networkId = principal.getFirstNetwork();

        OptTypeEnum optTypeEnum = request.getOptType();

        switch (optTypeEnum) {
            case PACKAGE: {
                for (String orderNo : request.getOrderNo()) {
                    WaybillDO waybill = waybillDao.queryByOrderNo(Long.parseLong(merchantId), orderNo);
                    WaybillLogPackageDO packageDO = new WaybillLogPackageDO();
                    packageDO.setOptTime(DateUtil.getSysTimeStamp());
                    packageDO.setOptBy(userId);
                    packageDO.setNetworkId(Integer.parseInt(networkId));
                    packageDO.setParentNo(waybill.getParentNo());
                    packageDO.setOrderNo(orderNo);
                    packageDO.setMerchantId(Long.parseLong(merchantId));
                    waybillLogPackageDao.insert(packageDO);
                }
                break;
            }
            case UNPACK: {
                for (String orderNo : request.getOrderNo()) {
                    WaybillDO waybill = waybillDao.queryByOrderNo(Long.parseLong(merchantId), orderNo);
                    WaybillLogUnPackDO unPackDO = new WaybillLogUnPackDO();
                    unPackDO.setOptTime(DateUtil.getSysTimeStamp());
                    unPackDO.setOptBy(userId);
                    unPackDO.setNetworkId(Integer.parseInt(networkId));
                    unPackDO.setParentNo(waybill.getParentNo());
                    unPackDO.setOrderNo(orderNo);
                    unPackDO.setMerchantId(Long.parseLong(merchantId));
                    waybillLogUnPackDao.insert(unPackDO);
                }
                break;
            }
            case ARRIVE_SCAN: {
                for (String orderNo : request.getOrderNo()) {
                    WaybillDO waybill = waybillDao.queryByOrderNo(Long.parseLong(merchantId), orderNo);
                    WaybillLogArriveDO arriveDO = new WaybillLogArriveDO();
                    arriveDO.setOptTime(DateUtil.getSysTimeStamp());
                    arriveDO.setOptBy(userId);
                    arriveDO.setNetworkId(Integer.parseInt(networkId));
                    arriveDO.setOrderNo(orderNo);
                    arriveDO.setLastNetworkId(waybill.getNetworkId());
                    arriveDO.setMerchantId(Long.parseLong(merchantId));
                    waybillLogArriveDao.insert(arriveDO);
                }
                break;
            }
            default:
                break;
        }

        for (String orderNo : request.getOrderNo()) {
            WaybillLog optLog = new WaybillLog();
            optLog.setMerchantId(merchantId);
            optLog.setOptBy(userId);
            optLog.setOptName(principal.getUserName());
            optLog.setOpt(request.getOptType().getCode());
            optLog.setRemark(request.getRemark());
            optLog.setOrderNo(orderNo);
            waybillLogDao.insert(optLog);
        }
    }

}
