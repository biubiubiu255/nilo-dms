package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryOrderOptDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderOptDO;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.User;
import com.nilo.dms.service.order.OrderOptLogService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.service.order.model.DeliveryOrderOpt;
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

    private DeliveryOrderOpt convert(DeliveryOrderOptDO d) {
        DeliveryOrderOpt opt = new DeliveryOrderOpt();
        opt.setOptBy(d.getOptBy());
        opt.setOpt(d.getOpt());
        opt.setToStatus(d.getToStatus());
        opt.setFromStatus(d.getFromStatus());
        opt.setMerchantId("" + d.getMerchantId());
        opt.setId("" + d.getId());
        opt.setOrderNo(d.getOrderNo());
        opt.setCreatedTime(d.getCreatedTime());
        opt.setRemark(d.getRemark());
        return opt;
    }
}
