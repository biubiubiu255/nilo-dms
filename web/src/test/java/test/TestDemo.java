package test;

import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.service.order.model.ReportCodQuery;
import com.nilo.dms.web.controller.report.model.ReportUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;


/**
 * Created by ronny on 2017/8/23.
 */
public class TestDemo {
    public static void main(String[] args) throws Exception {


        System.out.println("本次测试 = " + NumberUtils.isNumber("0"));
        System.exit(1);
        String order_status ="0";
        if (order_status!=null && !order_status.equals("")) order_status = DeliveryOrderStatusEnum.getEnum(Integer.valueOf(order_status)).getDesc();

        System.exit(1);
        ReportCodQuery cod = new ReportCodQuery();
        cod.setCountry("china");
        cod.setSend_company("cmxii");
        cod.setArrive_time_start(1521129600);
        cod.setArrive_time_end(1523808000);


        System.out.println(ReportUtil.queryDOtoStr(cod));
        System.exit(1);






        String str = "{\"carrier\":\"Nilo\",\"consignee\":\"steve\",\"operateTime\":\"2018-03-09 10:07:48\",\"orderInfo\":[{\"orderNo\":\"10000000507156\",\"status\":\"10\"}],\"orderNo\":\"10000000507156\",\"orderPrice\":\"0\",\"remark\":\"steve\",\"rider\":\"40004\",\"transId\":\"\"}";


        //DeliveryOrder order = JSON.parseObject(str,DeliveryOrder.class);

        System.out.println(DigestUtils.md5Hex("12345678" + str + "12345678").toUpperCase());

/*        Properties prop = new Properties();

        InputStream stream = TestDemo.class.getClassLoader().getResourceAsStream("" +
                "i18n_en_US.properties");
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        System.out.println(url.toString());*/


    }
}


