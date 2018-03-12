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

        String str = "{\"carrier\":\"Exdous\",\"consignee\":\"Sudi\",\"operateTime\":\"2018-01-12 10:16:54\",\"orderInfo\":[{\"orderNo\":\"10000000507156\",\"status\":\"10\"}],\"orderNo\":\"10000000507156\",\"orderPrice\":\"0\",\"remark\":\"Sudi\",\"rider\":\"200012\",\"transId\":\"\"}";


        //DeliveryOrder order = JSON.parseObject(str,DeliveryOrder.class);

        System.out.println(DigestUtils.md5Hex("12345678" + str + "12345678").toUpperCase());

/*        Properties prop = new Properties();

        InputStream stream = TestDemo.class.getClassLoader().getResourceAsStream("" +
                "i18n_en_US.properties");
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        System.out.println(url.toString());*/


    }
}


