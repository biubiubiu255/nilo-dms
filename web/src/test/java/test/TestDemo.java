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

        String str = "{\"orderNo\":\"NILO18020826619\",\"opt_by\":\"ronny.zeng\",\"status\":\"180\",\"network\":\"\"}";


        DeliveryOrder order = JSON.parseObject(str,DeliveryOrder.class);

        System.out.println(DigestUtils.md5Hex("12345678" + str + "12345678").toUpperCase());



    }
}


