package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryOrderOptDao;
import com.nilo.dms.dao.WaybillLogPackageDao;
import com.nilo.dms.dao.WaybillLogUnPackDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderOptDO;
import com.nilo.dms.dao.dataobject.WaybillLogPackageDO;
import com.nilo.dms.dao.dataobject.WaybillLogUnPackDO;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.User;
import com.nilo.dms.service.order.OrderOptLogService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.service.order.model.DeliveryOrderOpt;
import com.nilo.dms.service.order.model.DeliveryRoute;
import com.nilo.dms.service.order.model.OrderOptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2017/11/14.
 */
@Service
public class OrderOptLogServiceImpl implements OrderOptLogService {

    @Autowired
    private DeliveryOrderOptDao deliveryOrderOptDao;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private WaybillLogUnPackDao waybillLogUnPackDao;
    @Autowired
    private WaybillLogPackageDao waybillLogPackageDao;

    @Override
    public List<DeliveryOrderOpt> queryBy(String merchantId, String optType, String orderNo, String optBy, Long fromtTime, Long toTime, Pagination pagination) {

        List<DeliveryOrderOpt> list = new ArrayList<>();

        Long count = deliveryOrderOptDao.queryCountBy(Long.parseLong(merchantId), optType, orderNo, optBy, fromtTime, toTime);
        if (count == null || count == 0) {
            return list;
        }
        pagination.setTotalCount(count);

        List<DeliveryOrderOptDO> queryList = deliveryOrderOptDao.queryBy(Long.parseLong(merchantId), optType, orderNo, optBy, fromtTime, toTime, pagination.getOffset(), pagination.getLimit());

        return convert(queryList, merchantId);
    }

    private List<DeliveryOrderOpt> convert(List<DeliveryOrderOptDO> list, String merchantId) {

        List<DeliveryOrderOpt> routeList = new ArrayList<>();

        Set<String> userIds = new HashSet<>();
        Set<String> orderNos = new HashSet<>();
        for (DeliveryOrderOptDO d : list) {
            userIds.add(d.getOptBy());
            orderNos.add(d.getOrderNo());
        }

        List<String> userIdList = new ArrayList<>();
        userIdList.addAll(userIds);
        List<User> userList = userService.findByUserIds(merchantId, userIdList);

        List<String> orderNoList = new ArrayList<>();
        orderNoList.addAll(orderNos);
        List<DeliveryOrder> orderList = orderService.queryByOrderNos(merchantId, orderNoList);


        for (DeliveryOrderOptDO d : list) {
            DeliveryOrderOpt opt = convert(d);
            for (User u : userList) {
                if (StringUtil.equals(u.getUserId(), d.getOptBy())) {
                    opt.setOptName(u.getUserInfo().getName());
                    break;
                }
            }

            for (DeliveryOrder o : orderList) {
                if (StringUtil.equals(o.getOrderNo(), d.getOrderNo())) {
                    opt.setDeliveryOrder(o);
                    break;
                }
            }
            routeList.add(opt);
        }

        return routeList;

    }

    @Override
    public List<DeliveryOrderOpt> queryByOrderNos(String merchantId, List<String> orderNos) {

        List<DeliveryOrderOptDO> queryList = deliveryOrderOptDao.queryByOrderNos(Long.parseLong(merchantId), orderNos);

        return convert(queryList, merchantId);
    }

    @Override
    public void addOptLog(OrderOptRequest request) {

        OptTypeEnum optTypeEnum = request.getOptType();

        switch (optTypeEnum) {
            /*case PACKAGE: {
                for(String orderNo : request.getOrderNo()) {
                    DeliveryOrder deliveryOrder = orderService.queryByOrderNo(request.getMerchantId(), orderNo);
                    WaybillLogPackageDO packageDO = new WaybillLogPackageDO();
                    packageDO.setOptTime(DateUtil.getSysTimeStamp());
                    packageDO.setOptBy(request.getOptBy());
                    packageDO.setNetworkId(Integer.parseInt(request.getNetworkId()));
                    packageDO.setParentNo(deliveryOrder.getParentNo());
                    packageDO.setOrderNo(orderNo);
                    packageDO.setMerchantId(Long.parseLong(request.getMerchantId()));
                    waybillLogPackageDao.insert(packageDO);
                }
                break;
            }
            case UNPACK: {
                for(String orderNo : request.getOrderNo()) {
                    DeliveryOrder deliveryOrder = orderService.queryByOrderNo(request.getMerchantId(), orderNo);
                    WaybillLogUnPackDO unPackDO = new WaybillLogUnPackDO();
                    unPackDO.setOptTime(DateUtil.getSysTimeStamp());
                    unPackDO.setOptBy(request.getOptBy());
                    unPackDO.setNetworkId(Integer.parseInt(request.getNetworkId()));
                    unPackDO.setParentNo(deliveryOrder.getParentNo());
                    unPackDO.setOrderNo(orderNo);
                    unPackDO.setMerchantId(Long.parseLong(request.getMerchantId()));
                    waybillLogUnPackDao.insert(unPackDO);
                }
                break;
            }*/
            default:
                break;
        }

        for(String orderNo: request.getOrderNo()) {
            DeliveryOrderOptDO optLog = new DeliveryOrderOptDO();
            optLog.setMerchantId(Long.parseLong(request.getMerchantId()));
            optLog.setOptBy(request.getOptBy());
            optLog.setOpt(request.getOptType().getCode());
            optLog.setRemark(request.getRemark());
            optLog.setOrderNo(orderNo);
            deliveryOrderOptDao.insert(optLog);
        }
    }

    private DeliveryOrderOpt convert(DeliveryOrderOptDO d) {
        DeliveryOrderOpt opt = new DeliveryOrderOpt();
        opt.setOptBy(d.getOptBy());
        opt.setOpt(d.getOpt());
        opt.setMerchantId("" + d.getMerchantId());
        opt.setId("" + d.getId());
        opt.setOrderNo(d.getOrderNo());
        opt.setCreatedTime(d.getCreatedTime());
        opt.setRemark(d.getRemark());
        return opt;
    }
}
