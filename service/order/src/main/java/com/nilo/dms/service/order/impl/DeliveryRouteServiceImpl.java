package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.StaffStatusEnum;
import com.nilo.dms.common.utils.BeanUtils;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryOrderRouteDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderRouteDO;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.dto.common.UserInfo;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.DeliveryRoute;
import com.nilo.dms.dto.order.DeliveryRouteMessage;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.org.StaffService;
import com.nilo.dms.service.system.RedisUtil;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/15.
 */
@Service
public class DeliveryRouteServiceImpl implements DeliveryRouteService {

    @Autowired
    private UserService userService;
    @Autowired
    private DeliveryOrderRouteDao deliveryOrderRouteDao;
    @Autowired
    @Qualifier("routeProducer")
    private AbstractMQProducer routeProducer;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private HandleThirdDao handleThirdDao;

    @Autowired
    private HandleRiderDao handleRiderDao;

    @Autowired
    private HandleSignDao handleSignDao;

    @Override
    public List<DeliveryRoute> queryRoute(String merchantId, String orderNo) {

        List<DeliveryOrderRouteDO> routeDOs = deliveryOrderRouteDao.findBy(Long.parseLong(merchantId), orderNo);
        if (routeDOs == null) return null;

        SendThirdHead bigOrderDO = handleThirdDao.queryHandleBySmallNo(Long.parseLong(SessionLocal.getPrincipal().getMerchantId()), orderNo);

        UserInfoDO userInfoDO = handleRiderDao.queryUserInfoBySmallNo(orderNo);

        for(DeliveryOrderRouteDO e :routeDOs){
            if(e.getOpt().equals("send") || e.getOpt().equals("delivery")) {
                if(bigOrderDO!=null){
                    if(bigOrderDO.getNextStation()!=null){
                        e.setNextNetwork(bigOrderDO.getNextStation());
                        break;
                    }
                    if(bigOrderDO.getThirdExpressCode()!=null){
                        e.setExpressName(bigOrderDO.getThirdExpressCode());
                        break;
                    }
                    e.setOptByName(bigOrderDO.getHandleName());

                }else if(userInfoDO!=null){
                    List<StaffDO> staffInfo = staffDao.findstaffByIDs(new Long[]{userInfoDO.getId()});
                    String name = staffInfo.get(0).getNickName()==null ? staffInfo.get(0).getRealName() : staffInfo.get(0).getNickName();
                    e.setRider(name);
                    e.setOptByNamePhone(staffInfo.get(0).getPhone());
                    e.setJobId(staffInfo.get(0).getStaffId());
                }

            }
            if(e.getOpt().equals("receive")) {
                HandleSignDO handleSignDO = handleSignDao.queryByNo(e.getMerchantId(), e.getOrderNo());
                e.setSigner(handleSignDO.getSigner());
            }

        }


        //查询并赋值出当前路由信息的list的操作人名字、电话、工号 - - end

        List<DeliveryRoute> routeList = new ArrayList<>();
        for (DeliveryOrderRouteDO routeDO : routeDOs) {
            routeList.add(convert(routeDO));
        }

        return routeList;
    }

    @Override
    public void addRoute(OrderOptRequest request) {

        Principal principal = SessionLocal.getPrincipal();

        DeliveryRouteMessage message = new DeliveryRouteMessage();
        message.setOrderNo(request.getOrderNo().toArray(new String[request.getOrderNo().size()]));
        message.setOptType(request.getOptType().getCode());
        message.setRider(request.getRider());
        message.setMerchantId(principal.getMerchantId());
        message.setOptBy(principal.getUserId());
        message.setNetworkId(principal.getFirstNetwork());
        try {
            routeProducer.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DeliveryRoute convert(DeliveryOrderRouteDO routeDO) {

        DeliveryRoute route = new DeliveryRoute();
        org.springframework.beans.BeanUtils.copyProperties(routeDO, route);
        route.setMerchantId("" + routeDO.getMerchantId());
        route.setOrderNo(routeDO.getOrderNo());
        route.setOpt(routeDO.getOpt());
        route.setOptNetwork(routeDO.getOptNetwork());
        route.setPhone(routeDO.getPhone());
        route.setOptBy(routeDO.getOptBy());
        route.setOptTime(routeDO.getCreatedTime());
        route.setSigner(routeDO.getSigner());

        if (StringUtil.isNotEmpty(routeDO.getOptNetwork())) {
            DistributionNetworkDO networkDO = JSON.parseObject(RedisUtil.hget(Constant.NETWORK_INFO + routeDO.getMerchantId(), "" + routeDO.getOptNetwork()), DistributionNetworkDO.class);
            route.setNetworkDesc(networkDO.getName());
        }
        route.setOptByName(routeDO.getOptBy());
        if (StringUtil.isNotEmpty(routeDO.getOptBy()) && !StringUtil.equals(routeDO.getOptBy(), "api")) {
            UserInfo userInfo = userService.findUserInfoByUserId("" + routeDO.getMerchantId(), routeDO.getOptBy());
            if (userInfo != null) route.setOptByName(userInfo.getName());
        }
        route.setCreatedTime(routeDO.getCreatedTime());
        return route;
    }
}
