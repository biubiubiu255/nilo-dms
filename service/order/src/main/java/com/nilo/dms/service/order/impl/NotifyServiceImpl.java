package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.enums.MethodEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.AbnormalOrderDao;
import com.nilo.dms.dao.WaybillLogDao;
import com.nilo.dms.dao.HandleRiderDao;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.dataobject.AbnormalOrderDO;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.UserInfoDO;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.dto.order.NotifyRequest;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.dto.order.WaybillLog;
import com.nilo.dms.dto.system.InterfaceConfig;
import com.nilo.dms.dto.system.MerchantConfig;
import com.nilo.dms.dto.system.OrderHandleConfig;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.NotifyService;
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
    private WaybillDao waybillDao;
    @Autowired
    private AbnormalOrderDao abnormalOrderDao;
    @Autowired
    private WaybillLogDao waybillLogDao;
    @Autowired
    private HandleRiderDao handleRiderDao;

    @Value("#{configProperties['kili_dispatch']}")
    private String kili_dispatch;
    @Value("#{configProperties['kili_refuse']}")
    private String kili_refuse;
    @Value("#{configProperties['kili_sign']}")
    private String kili_sign;

    @Override
    public void updateStatus(OrderOptRequest request) {

        String merchantId = SessionLocal.getPrincipal().getMerchantId();

        OrderHandleConfig handleConfig = SystemConfig.getOrderHandleConfig(merchantId,
                request.getOptType().getCode());
        String convertResult = StatusConvert.convert(DeliveryOrderStatusEnum.getEnum(handleConfig.getUpdateStatus()));
        if (convertResult == null) return;
        try {
            MerchantConfig merchantConfig = JSON.parseObject(RedisUtil.get(Constant.MERCHANT_CONF + merchantId),
                    MerchantConfig.class);
            InterfaceConfig interfaceConfig = JSON.parseObject(
                    RedisUtil.hget(Constant.INTERFACE_CONF + merchantId, "update_status"), InterfaceConfig.class);
            if (interfaceConfig == null) {
                return;
            }

            for (String orderNo : request.getOrderNo()) {

                //请求参数
                WaybillDO deliveryOrder = waybillDao.queryByOrderNo(Long.parseLong(merchantId), orderNo);
                //kilimall 临时方案
                if (StringUtil.equals(deliveryOrder.getOrderPlatform(), "kilimall")) {
                    String orderType = StringUtil.equals(deliveryOrder.getOrderType(), "FBK") ? "SELL" : "YK";
                    Map<String, String> param = new HashMap<>();
                    switch (request.getOptType()) {

                        case DELIVERY:
                        case SEND: {

                            logger.info("Notify Kilimall Dispatch,orderNo:" + deliveryOrder.getOrderNo());

                            String data = "{\"data\":{\"orderinfo\":[{\"DeliveryNo\":\"" + deliveryOrder.getOrderNo() + "\",\"CustomerID\":\"KILIMALL\",\"WarehouseID\":\"KE01\",\"OrderNo\":\"" + deliveryOrder.getReferenceNo() + "\",\"OrderType\":\"" + orderType +
                                    "\"}]}}";
                            param.put("data", URLEncoder.encode(data, "utf-8"));
                            param.put("sign", createSign(merchantConfig.getKey(), data));
                            param.put("op", "confirmSOData");
                            NotifyRequest notify = new NotifyRequest();
                            notify.setUrl(kili_dispatch);
                            notify.setParam(param);
                            notifyDataBusProducer.sendMessage(notify);
                            break;
                        }
                        case REFUSE: {
                            String data = "{\"orderInfo\":[{\"orderNo\":\"" + deliveryOrder.getReferenceNo() + "\",\"status\":\"20\"}]}";
                            param.put("data", URLEncoder.encode(data, "utf-8"));
                            param.put("sign", createSign(merchantConfig.getKey(), data));
                            param.put("op", "confirmorder");
                            NotifyRequest notify = new NotifyRequest();
                            notify.setUrl(kili_refuse);
                            notify.setParam(param);
                            notifyDataBusProducer.sendMessage(notify);
                            break;
                        }
                        case SIGN: {
                            String data = "{\"orderInfo\":[{\"orderNo\":\"" + deliveryOrder.getReferenceNo() + "\",\"status\":\"10\"}]}";
                            param.put("data", URLEncoder.encode(data, "utf-8"));
                            param.put("sign", createSign(merchantConfig.getKey(), data));
                            param.put("op", "confirmorder");
                            NotifyRequest notify = new NotifyRequest();
                            notify.setUrl(kili_sign);
                            notify.setParam(param);
                            notifyDataBusProducer.sendMessage(notify);
                            break;
                        }
                        default:
                            break;
                    }
                }

            }
        } catch (Exception e) {
            logger.error("Notify Failed.", e);
        }
    }

    private static class StatusConvert {
        private static Map<DeliveryOrderStatusEnum, String> convertRelation = new HashMap<>();

        static {
            convertRelation.put(DeliveryOrderStatusEnum.ARRIVED, "170");
            convertRelation.put(DeliveryOrderStatusEnum.DELIVERY, "180");
            convertRelation.put(DeliveryOrderStatusEnum.SIGN, "190");
            convertRelation.put(DeliveryOrderStatusEnum.PROBLEM, "197");
            convertRelation.put(DeliveryOrderStatusEnum.REFUSE, "195");

        }

        public static String convert(DeliveryOrderStatusEnum status) {
            if (convertRelation.containsKey(status)) {
                return convertRelation.get(status);
            } else {
                return null;
            }
        }
    }

    private String createSign(String key, String data) {
        return new String(DigestUtils.md5Hex(key + data + key).toUpperCase());
    }

}
