package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.enums.MethodEnum;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.NotifyRequest;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.dto.order.WaybillLog;
import com.nilo.dms.dto.system.InterfaceConfig;
import com.nilo.dms.dto.system.MerchantConfig;
import com.nilo.dms.dto.system.OrderHandleConfig;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.NotifyService;
import com.nilo.dms.service.order.RiderDeliveryService;
import com.nilo.dms.service.order.SendThirdService;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.SystemCodeUtil;
import com.nilo.dms.service.system.SystemConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.*;

/**
 * 通知上游系统服务
 * Created by admin on 2018/3/1.
 */
@Service
public class NotifyServiceImpl implements NotifyService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("notifyDataBusProducer")
    private AbstractMQProducer notifyDataBusProducer;
    @Autowired
    private AbnormalOrderDao abnormalOrderDao;
    @Autowired
    private SendThirdService sendThirdService;
    @Autowired
    private HandleRiderDao handleRiderDao;
    @Autowired
    private DeliveryOrderReceiverDao deliveryOrderReceiverDao;

    @Override
    public void updateStatus(OrderOptRequest request) {

        Principal p = SessionLocal.getPrincipal();
        String merchantId = p.getMerchantId();
        String network = p.getFirstNetwork();

        String convertResult = StatusConvert.convert(request.getOptType());
        if (convertResult == null) return;

        MerchantConfig merchantConfig = JSON.parseObject(RedisUtil.get(Constant.MERCHANT_CONF + merchantId),
                MerchantConfig.class);
        InterfaceConfig interfaceConfig = JSON.parseObject(
                RedisUtil.hget(Constant.INTERFACE_CONF + merchantId, "update_status"), InterfaceConfig.class);
        if (interfaceConfig == null) {
            return;
        }

        for (String orderNo : request.getOrderNo()) {
            NotifyRequest notify = new NotifyRequest();
            Map<String, Object> routeData = new HashMap<>();

            switch (request.getOptType()) {
                case ARRIVE_SCAN: {
                    break;
                }
                case DELIVERY: {
                    UserInfoDO userInfoDO = handleRiderDao.queryUserInfoBySmallNo(orderNo);
                    routeData.put("rider_name", userInfoDO.getName());
                    routeData.put("rider_phone", userInfoDO.getPhone());
                    break;
                }
                case SEND: {
                    SendThirdHead sendThirdHead = sendThirdService.queryLoadingBySmallNo(merchantId, orderNo);
                    routeData.put("third_express", sendThirdHead.getThirdExpressCode());
                    break;
                }
                case PROBLEM: {
                    String reason = SystemCodeUtil.getCodeVal(merchantId, Constant.PROBLEM_REASON, request.getRemark());
                    routeData.put("type", reason);
                    routeData.put("type_code", request.getRemark());
                    break;
                }
                case REFUSE: {
                    String reason = SystemCodeUtil.getCodeVal(merchantId, Constant.REFUSE_REASON, request.getRemark());
                    routeData.put("type", reason);
                    routeData.put("type_code", request.getRemark());
                    break;
                }
                case SIGN: {
                    DeliveryOrderReceiverDO receiverDO = deliveryOrderReceiverDao.queryByOrderNo(Long.parseLong(merchantId), orderNo);
                    routeData.put("sign_name", receiverDO.getName());
                    break;
                }
                default:
                    break;
            }

            DistributionNetworkDO networkDO = JSON.parseObject(RedisUtil.hget(Constant.NETWORK_INFO + merchantId, network), DistributionNetworkDO.class);
            routeData.put("network_code", networkDO == null ? "" : networkDO.getCode());
            routeData.put("network", networkDO == null ? "" : networkDO.getName());
            routeData.put("waybill_number", orderNo);
            routeData.put("status", convertResult);
            routeData.put("track_time", DateUtil.getSysTimeStamp());
            routeData.put("remark", request.getRemark());
            String data = JSON.toJSONString(routeData);
            Map<String, String> param = new HashMap<>();
            try {
                param.put("method", interfaceConfig.getOp());
                param.put("app_key", "dms");
                param.put("country_code", "ke");
                param.put("data", data);
                param.put("request_id", UUID.randomUUID().toString());
                param.put("timestamp", "" + DateUtil.getSysTimeStamp());
                param.put("data", URLEncoder.encode(data, "utf-8"));
                param.put("sign", createSign(merchantConfig.getKey(), data));
                notify.setUrl(interfaceConfig.getUrl());
                notify.setParam(param);
                notifyDataBusProducer.sendMessage(notify);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class StatusConvert {
        private static Map<OptTypeEnum, String> convertRelation = new HashMap<>();

        static {
            convertRelation.put(OptTypeEnum.ARRIVE_SCAN, "162");
            convertRelation.put(OptTypeEnum.DELIVERY, "180");
            convertRelation.put(OptTypeEnum.SEND, "185");
            convertRelation.put(OptTypeEnum.SIGN, "190");
            convertRelation.put(OptTypeEnum.PROBLEM, "197");
            convertRelation.put(OptTypeEnum.REFUSE, "195");

        }

        public static String convert(OptTypeEnum opt) {
            if (convertRelation.containsKey(opt)) {
                return convertRelation.get(opt);
            } else {
                return null;
            }
        }
    }

    private String createSign(String key, String data) {
        return new String(DigestUtils.md5Hex(key + data + key).toUpperCase());
    }

}
