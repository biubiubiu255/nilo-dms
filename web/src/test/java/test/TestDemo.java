package test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nilo.dms.common.enums.EnumMessage;
import com.nilo.dms.service.order.model.DeliveryOrder;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.SortedMap;

/**
 * Created by ronny on 2017/8/23.
 */
public class TestDemo {
    public static void main(String[] args) throws Exception {

        String str = "{\"warehouse_id\":\"KE01\",\"carrier_name\":\"G4S\",\"channel_station\":\"自提网点名称\",\"stop\":\"G4S\",\"delivery_fee\":123123.11,\"remark\":\"hahahahahahha\",\"notes\":\"hahahahah\",\"order_time\":1234567890,\"goods_type\":\"普货\",\"client_code\":\"900\",\"client_ordersn\":\"assdf\",\"order_platform\":\"OTHER\",\"order_type\":\"GS\",\"delivery_type\":0,\"number\":2,\"sender_info\":{\"sender_contactname\":\"Wang\",\"sender_contactnumber\":\"18718679367\",\"sender_country\":\"CN\",\"sender_province\":\"广东\",\"sender_city\":\"深圳\",\"sender_address\":\"坪山新区坑梓镇人民东路31号震雄工业园B区南门\"},\"is_pickup\":1,\"receiver_info\":{\"receiver_contactname\":\"godwin ibok\",\"receiver_contactnumber\":\"08062900171 0902804\",\"receiver_country\":\"NG\",\"receiver_province\":\"Akwa Ibom\",\"receiver_city\":\"uyo\",\"receiver_address\":\"18 essien inyang street,uyo university of uyo\"},\"order_weight\":842,\"order_amount\":\"20.66\",\"is_pod\":0,\"pod_amount\":0,\"currency\":\"USD\",\"mail_cargo_type\":\"Other\",\"order_items_list\":[{\"item_title\":\"Microphone\",\"goods_other_name\":\"Microphone\",\"goods_model_name\":\"K-001\",\"goods_specification\":\"big\",\"sale_price\":\"16.07\",\"pay_price\":\"16.07\",\"weight\":712,\"goods_desc\":\"goods desc\",\"goods_height\":1,\"goods_length\":2,\"goods_width\":3,\"goods_color\":\"blue\",\"tax_type\":\"electronic\",\"tax_rate\":3,\"quantity\":\"1\",\"unit_code\":\"PCE\",\"contain_battery\":1,\"is_liquid\":1,\"is_danger\":1,\"item_skucode\":\"138696701\"}]}\n";


        String str1 = "{\"client_ordersn\":\"KE01\"}";
        DeliveryOrder order1 = JSON.parseObject(str1,DeliveryOrder.class);


        DeliveryOrder order = JSON.parseObject(str,DeliveryOrder.class);

        System.out.println(DigestUtils.md5Hex("12345678" + str + "12345678").toUpperCase());



    }
}


