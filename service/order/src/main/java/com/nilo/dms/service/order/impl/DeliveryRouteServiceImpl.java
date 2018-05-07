package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryOrderRouteDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderRouteDO;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dto.common.UserInfo;
import com.nilo.dms.dto.order.DeliveryRoute;
import com.nilo.dms.dto.order.DeliveryRouteMessage;
import com.nilo.dms.dto.order.NotifyRequest;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.system.RedisUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Value("#{configProperties['logisticUrl']}")
    private String logisticUrl;
    @Value("#{configProperties['logisticToken']}")
    private String logisticToken;
    @Autowired
    @Qualifier("notifyDataBusProducer")
    private AbstractMQProducer notifyDataBusProducer;

    @Override
    public List<DeliveryRoute> queryRoute(String merchantId, String orderNo) {

        List<DeliveryOrderRouteDO> routeDOs = deliveryOrderRouteDao.findBy(Long.parseLong(merchantId), orderNo);
        if (routeDOs == null) return null;

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

    @Override
    public void addKiliRoute(List<String> orderNos, String statusId) {
        //写入物流轨迹
        List<String> transList = new ArrayList<String>();
        for (String orderNo : orderNos) {
            transList.add("KE" + orderNo);
        }
        String operateTime = "" + System.currentTimeMillis() / 1000L;
        String transNo = JSONObject.toJSONString(transList);
        String sign = DigestUtils.md5Hex(logisticToken + "&transtsNo=" + transNo + "&operateTime=" + operateTime);

        Map<String, String> param = new HashMap<String, String>();
        param.put("transtsNo", URLEncoder.encode(transNo));
        param.put("operateTime", operateTime);
        param.put("statusID", statusId);
        param.put("station", "Nairobi");
        param.put("remark", "");
        param.put("sign", sign);
        try {
            NotifyRequest notify = new NotifyRequest();
            notify.setUrl(logisticUrl);
            notify.setParam(param);
            notifyDataBusProducer.sendMessage(notify);
        } catch (Exception e) {
        }
    }


    private DeliveryRoute convert(DeliveryOrderRouteDO routeDO) {
        DeliveryRoute route = new DeliveryRoute();
        route.setMerchantId("" + routeDO.getMerchantId());
        route.setOrderNo(routeDO.getOrderNo());
        route.setOpt(routeDO.getOpt());
        route.setOptNetwork(routeDO.getOptNetwork());
        route.setPhone(routeDO.getPhone());
        route.setOptBy(routeDO.getOptBy());
        route.setOptTime(routeDO.getCreatedTime());
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
