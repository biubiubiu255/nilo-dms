package com.nilo.dms.service.order.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.enums.CreateDeliveryRequestStatusEnum;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.enums.ThirdPushOrderStatusEnum;
import com.nilo.dms.common.utils.*;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.dto.handle.SendThirdDetail;
import com.nilo.dms.dto.order.*;
import com.nilo.dms.dto.system.InterfaceConfig;
import com.nilo.dms.dto.system.MerchantConfig;
import com.nilo.dms.service.mq.consumer.AbstractMQConsumer;
import com.nilo.dms.service.mq.model.ConsumerDesc;
import com.nilo.dms.service.system.RedisUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/10/18.
 */
@ConsumerDesc(topic = "send_third", group = "send_third_group", filterExpression = "notify")
public class SendThirdPushConsumer extends AbstractMQConsumer {

    private static Logger logger = LoggerFactory.getLogger(SendThirdPushConsumer.class);
    @Autowired
    private DeliveryOrderGoodsDao deliveryOrderGoodsDao;

    @Autowired
    private WaybillDao waybillDao;

    @Autowired
    private DeliveryOrderReceiverDao deliveryOrderReceiverDao;

    @Autowired
    private ThirdPushDao thirdPushDao;

    @Override
    public void handleMessage(MessageExt messageExt, Object obj) throws Exception {

        //Map{orders: , expressCode:, merchantId: }
        //解析传入数据
        Map<String, Object> map = (Map<String, Object>) obj;

        Long merchantId = Long.parseLong(map.get("merchantId").toString());

        List<String> orders = (List<String>) map.get("orders");

        String expressCode = map.get("expressCode").toString();


        //获取POST 接口地址信息
        MerchantConfig merchantConfig = JSON.parseObject(RedisUtil.get(Constant.MERCHANT_CONF + merchantId), MerchantConfig.class);
        InterfaceConfig interfaceConfig = JSON.parseObject(RedisUtil.hget(Constant.INTERFACE_CONF + merchantId, "push_third_waybill"), InterfaceConfig.class);
        if (interfaceConfig == null) {
            return;
        }

        //开始逐条推送
        for (String order : orders) {

            //构建POST body
            WaybillDO waybillDO = waybillDao.queryByOrderNo(merchantId, order);
            List<DeliveryOrderGoodsDO> goods = deliveryOrderGoodsDao.queryByOrderNo(merchantId, order);
            DeliveryOrderReceiverDO receiver = deliveryOrderReceiverDao.queryByOrderNo(merchantId, order);

            Integer goodsQty = 0;
            StringBuilder goodsDesc = new StringBuilder();
            for (DeliveryOrderGoodsDO good : goods){
                goodsDesc.append(good.getGoodsDesc() + "|");
                goodsQty  += good.getQty();
            }

            //HttpUtil.post();
            Map<String, String> postMap = new HashMap<String, String>();
            postMap.put("code", expressCode);
            postMap.put("city",  StringUtil.isEmpty(receiver.getCity()) ? "Nairobi" : receiver.getCity());
            postMap.put("contact_name", receiver.getName());
            postMap.put("contact_number", receiver.getContactNumber());
            postMap.put("goods_desc", goodsDesc.toString());
            postMap.put("goods_qty", goodsQty.toString());
            postMap.put("receiver_address", receiver.getAddress());
            postMap.put("weight", waybillDO.getWeight().toString());
            postMap.put("order_no", waybillDO.getOrderNo());
            postMap.put("reference_no", waybillDO.getReferenceNo());
            postMap.put("country",PickUtil.coalesce(receiver.getCountry(), "ke").toString());
            postMap.put("request_id", DateUtil.getSysTimeStamp().toString());
            //postMap.put("third_waybill_num", (String) map.get("expressNo"));
            Map<String, String> postMapOut = new HashMap<String, String>();
            postMapOut.put("data", JSON.toJSONString(postMap));
            postMapOut.put("method", interfaceConfig.getOp());

            //第三方推送
            ThirdPushDo thirdPushDo = new ThirdPushDo();
            try {
                String result = HttpUtil.post(interfaceConfig.getUrl(), postMapOut);
                JSONObject jsonObject = JSON.parseObject(result);
                String status = jsonObject.getString("status");
                if(status.equals("succ")){
                    thirdPushDo.setStatus(ThirdPushOrderStatusEnum.PUSH_SUCC.getCode());
                    thirdPushDo.setOrderId(jsonObject.getJSONObject("response").getString("order_no"));
                }else {
                    thirdPushDo.setStatus(ThirdPushOrderStatusEnum.PUSH_FAIL.getCode());
                    thirdPushDo.setMsg(jsonObject.getString("msg"));
                }
            } catch (Exception e) {
                logger.error("send third interface fail, order：{},result:{}", order,e);
                thirdPushDo.setStatus(ThirdPushOrderStatusEnum.PUSH_FAIL.getCode());
            }
            thirdPushDo.setOrderNo(order);
            thirdPushDo.setExpressCode(expressCode);
            thirdPushDao.insert(thirdPushDo);
        }

        return;
    }




}
