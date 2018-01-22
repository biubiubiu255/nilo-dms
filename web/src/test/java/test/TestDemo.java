package test;

import com.nilo.dms.common.enums.EnumMessage;

import java.util.HashMap;
import java.util.SortedMap;

/**
 * Created by ronny on 2017/8/23.
 */
public class TestDemo {
    public static void main(String[] args) throws Exception {
/*
        int i = 1 << 30;
        System.out.println(i);

        String str = "{\"carrier\":\"KiliExpress\",\"consignee\":\"pickupSet\",\"operateTime\":\"2017-12-01 15:49:01\",\"orderInfo\":[{\"orderNo\":\"90000000481458\",\"status\":\"10\"}],\"orderNo\":\"90000000481458\",\"orderPrice\":\"0\",\"remark\":\"pickupSet\",\"rider\":\"dx1\",\"transId\":\"\"}";

        System.out.println(DigestUtils.md5Hex("12345678" + str + "12345678").toUpperCase());

        Map map = new ConcurrentHashMap();*/

        EnumMessage enumMessage = (EnumMessage) Class.forName("com.nilo.dms.common.enums.CustomerTypeEnum").newInstance();

    }
}


