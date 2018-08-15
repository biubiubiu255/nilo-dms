package com.nilo.dms.service.order.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.enums.ThirdPushOrderStatusEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.HttpUtil;
import com.nilo.dms.common.utils.PickUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryOrderGoodsDao;
import com.nilo.dms.dao.DeliveryOrderReceiverDao;
import com.nilo.dms.dao.ThirdPushDao;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderGoodsDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderReceiverDO;
import com.nilo.dms.dao.dataobject.ThirdPushDo;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.dto.order.WaybillHeader;
import com.nilo.dms.dto.system.InterfaceConfig;
import com.nilo.dms.dto.system.MerchantConfig;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.mq.consumer.AbstractMQConsumer;
import com.nilo.dms.service.mq.model.ConsumerDesc;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.service.system.RedisUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by admin on 2017/10/18.
 */
@ConsumerDesc(topic = "arrive_weight", group = "arrive_weight_group", filterExpression = "arrive_weight")
public class ArriveConsumer extends AbstractMQConsumer {

    private static Logger logger = LoggerFactory.getLogger(ArriveConsumer.class);
    @Autowired
    private DeliveryOrderGoodsDao deliveryOrderGoodsDao;

    @Autowired
    private WaybillDao waybillDao;

    @Override
    public void handleMessage(MessageExt messageExt, Object obj) throws Exception {

        if (messageExt.getReconsumeTimes() == 4) {
            logger.error("Arrive write fail, msgId{0}", messageExt.getKeys());
            return;
        }

        Map<String, Object> msgMap = (Map) obj;

        String merchantId = (String) msgMap.get("merchantId");

        List<String> waybillNos = (List) msgMap.get("orderList");

        List<WaybillDO> waybillDOS = waybillDao.queryByOrderNos(Long.parseLong(merchantId), waybillNos);
        MerchantConfig merchantConfig = JSON.parseObject(RedisUtil.get(Constant.MERCHANT_CONF + merchantId),
                MerchantConfig.class);
        InterfaceConfig interfaceConfig = JSON.parseObject(RedisUtil.hget(Constant.INTERFACE_CONF + merchantId, "get_order_info"), InterfaceConfig.class);

        for (WaybillDO e : waybillDOS){
            if (e.getWeight()==null || e.getWeight().equals(0D)){
                Map<String, String> map = new HashMap<String, String>();
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("client_ordersn", e.getReferenceNo());
                String sign = new String(DigestUtils.md5Hex(merchantConfig.getKey() + JSON.toJSONString(dataMap) + merchantConfig.getKey()).toUpperCase());
                map.put("sign", sign);
                map.put("data", JSON.toJSONString(dataMap));
                map.put("method", interfaceConfig.getOp());
                map.put("app_key", "kilimall");
                map.put("sign", sign);
                map.put("timestamp", DateUtil.getSysTimeStamp().toString());
                map.put("request_id", UUID.randomUUID().toString());

                try {
                    String result = HttpUtil.post(interfaceConfig.getUrl(), map);
                    JSONObject jsonObject = JSON.parseObject(result);
                    if (jsonObject!=null && "succ".equals(jsonObject.getString("status"))){

                        if(jsonObject.getJSONObject("response")!=null && StringUtil.isNotEmpty(jsonObject.getJSONObject("response").getString("weight"))){
                            String weightStr = jsonObject.getJSONObject("response").getString("weight");
                            WaybillDO header = new WaybillDO();
                            header.setMerchantId(Long.parseLong(merchantId));
                            header.setOrderNo(e.getOrderNo());
                            header.setWeight(Double.parseDouble(weightStr));
                            waybillDao.update(header);
                        }

                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    throw new DMSException(BizErrorCode.WMS_QUERY_FAIL, e.getOrderNo());
                }
            }
        }

        return;
    }




}
