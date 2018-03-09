package test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nilo.dms.common.enums.EnumMessage;
import com.nilo.dms.service.order.model.DeliveryOrder;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.Properties;


/**
 * Created by ronny on 2017/8/23.
 */
public class TestDemo {
    public static void main(String[] args) throws Exception {

        //String str = "{\"orderNo\":\"NILO18020826619\",\"opt_by\":\"ronny.zeng\",\"status\":\"180\",\"network\":\"\"}";


        //DeliveryOrder order = JSON.parseObject(str,DeliveryOrder.class);

        //System.out.println(DigestUtils.md5Hex("12345678" + str + "12345678").toUpperCase());

/*        Properties prop = new Properties();

        InputStream stream = TestDemo.class.getClassLoader().getResourceAsStream("" +
                "i18n_en_US.properties");
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        System.out.println(url.toString());*/


        System.out.println(1);

    }
}


