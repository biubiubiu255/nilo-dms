package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.enums.MethodEnum;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.enums.TaskTypeEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.AbnormalOrderDao;
import com.nilo.dms.dao.DeliveryOrderOptDao;
import com.nilo.dms.dao.NotifyDao;
import com.nilo.dms.dao.dataobject.AbnormalOrderDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderOptDO;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.NotifyDO;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.UserInfo;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.NotifyService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.TaskService;
import com.nilo.dms.service.order.model.*;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.SystemCodeUtil;
import com.nilo.dms.service.system.SystemConfig;
import com.nilo.dms.service.system.model.InterfaceConfig;
import com.nilo.dms.service.system.model.MerchantConfig;
import com.nilo.dms.service.system.model.OrderHandleConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知上游系统服务
 * Created by admin on 2018/3/1.
 */
@Service
public class NotifyServiceImpl implements NotifyService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NotifyDao notifyDao;
    @Autowired
    @Qualifier("notifyDataBusProducer")
    private AbstractMQProducer notifyDataBusProducer;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AbnormalOrderDao abnormalOrderDao;
    @Autowired
    private DeliveryOrderOptDao deliveryOrderOptDao;

    @Override
    public void updateStatus(OrderOptRequest request) {
        OrderHandleConfig handleConfig = SystemConfig.getOrderHandleConfig(request.getMerchantId(),
                request.getOptType().getCode());
        String convertResult = StatusConvert.convert(DeliveryOrderStatusEnum.getEnum(handleConfig.getUpdateStatus()));
        if (convertResult == null) return;
        try {
            MerchantConfig merchantConfig = JSON.parseObject(RedisUtil.get(Constant.MERCHANT_CONF + request.getMerchantId()),
                    MerchantConfig.class);
            InterfaceConfig interfaceConfig = JSON.parseObject(
                    RedisUtil.hget(Constant.INTERFACE_CONF + request.getMerchantId(), "update_status"), InterfaceConfig.class);
            if (interfaceConfig == null) {
                return;
            }

            for (String orderNo : request.getOrderNo()) {

                NotifyRequest notify = new NotifyRequest();
                Map<String, Object> dataMap = new HashMap<>();

                switch (request.getOptType()) {
                    case ARRIVE_SCAN: {
                        //到件只通知一次
                        List<DeliveryOrderOptDO> list = deliveryOrderOptDao.queryByOrderNos(Long.parseLong(request.getMerchantId()), Arrays.asList(orderNo));
                        if (list == null || list.size() > 0) {
                            return;
                        }
                        DistributionNetworkDO networkDO = JSON.parseObject(RedisUtil.hget(Constant.NETWORK_INFO + request.getMerchantId(), "" + request.getNetworkId()), DistributionNetworkDO.class);
                        dataMap.put("location", networkDO.getName());
                        break;
                    }
                    case DELIVERY: {
                        Task task = taskService.queryTaskByTypeAndOrderNo(request.getMerchantId(), TaskTypeEnum.DELIVERY.getCode(), orderNo);
                        UserInfo userInfo = userService.findUserInfoByUserId(request.getMerchantId(), task.getHandledBy());

                        DistributionNetworkDO networkDO = JSON.parseObject(RedisUtil.hget(Constant.NETWORK_INFO + request.getMerchantId(), "" + request.getNetworkId()), DistributionNetworkDO.class);
                        dataMap.put("location", networkDO.getName());

                        dataMap.put("rider_name", userInfo.getName());
                        dataMap.put("rider_phone", userInfo.getPhone());
                        break;
                    }
                    case SEND: {
                        Task task = taskService.queryTaskByTypeAndOrderNo(request.getMerchantId(), TaskTypeEnum.DELIVERY.getCode(), orderNo);
                        break;
                    }
                    case PROBLEM: {
                        AbnormalOrderDO abnormalOrderDO = abnormalOrderDao.queryByOrderNo(Long.parseLong(request.getMerchantId()), orderNo);
                        String reason = SystemCodeUtil.getCodeVal("" + abnormalOrderDO.getMerchantId(), Constant.PRBOLEM_REASON, abnormalOrderDO.getReason());
                        dataMap.put("type", reason);
                        break;
                    }
                    case REFUSE: {
                        AbnormalOrderDO abnormalOrderDO = abnormalOrderDao.queryByOrderNo(Long.parseLong(request.getMerchantId()), orderNo);
                        String reason = SystemCodeUtil.getCodeVal("" + abnormalOrderDO.getMerchantId(), Constant.REFUSE_REASON, abnormalOrderDO.getReason());
                        dataMap.put("type", reason);
                        break;
                    }
                    case RECEIVE: {
                        break;
                    }
                    default:
                        break;
                }

                DeliveryOrder deliveryOrder = orderService.queryByOrderNo(request.getMerchantId(), orderNo);

                notify.setOrderNo(orderNo);
                notify.setReferenceNo(deliveryOrder.getReferenceNo());
                notify.setMerchantId(request.getMerchantId());
                notify.setBizType(request.getOptType().getCode());
                notify.setMethod(MethodEnum.STATUS_UPDATE.getCode());
                notify.setUrl(interfaceConfig.getUrl());

                dataMap.put("waybill_number", orderNo);
                dataMap.put("status", convertResult);
                dataMap.put("track_time", DateUtil.getSysTimeStamp());
                dataMap.put("remark", request.getRemark());
                String data = JSON.toJSONString(dataMap);
                notify.setData(data);
                notify.setSign(createSign(merchantConfig.getKey(), data));
                notifyDataBusProducer.sendMessage(notify);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class StatusConvert {
        private static Map<DeliveryOrderStatusEnum, String> convertRelation = new HashMap<>();

        static {
            convertRelation.put(DeliveryOrderStatusEnum.ARRIVED, "170");
            convertRelation.put(DeliveryOrderStatusEnum.DELIVERY, "180");
            convertRelation.put(DeliveryOrderStatusEnum.RECEIVED, "190");
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
