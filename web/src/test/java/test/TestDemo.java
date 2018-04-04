package test;

import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.dao.dataobject.DeliveryOrderDO;
import com.nilo.dms.dao.dataobject.RiderDeliverySmallDO;
import com.nilo.dms.service.order.model.ReportCodQuery;
import com.nilo.dms.web.controller.report.model.ReportUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;


/**
 * Created by ronny on 2017/8/23.
 */
public class TestDemo {
    public static void main(String[] args) throws Exception {


        DeliveryOrderDO deliveryOrderDO = new DeliveryOrderDO();
        deliveryOrderDO.setWeight(1.2);
        deliveryOrderDO.setOrderNo("dsfdsfds");

        RiderDeliverySmallDO riderDeliverySmallDO = new RiderDeliverySmallDO();

        BeanUtils.copyProperties(deliveryOrderDO, riderDeliverySmallDO);

        System.out.println("本次测试 = " + riderDeliverySmallDO);
        System.exit(1);

        String abc =  "{\"sales\": {\"item\": ["
                    + "{\"-id\": \"1\",\"equipment\": \"测探仪\",\"project\": \"维修\",\"count\": \"1\",\"price\": \"2500\",\"total\": \"2500\",\"ps\": \"1）贵司雷达申请单是两台S波段雷达，应该是不准确的，因此我们报价一个X波段磁控管，一个S波段磁控管；2）罗经球寿命是3年，其中有一台罗经球使用2年半，建议更换该罗经球，从而两台罗经球使用时间不同，确保两台罗经球不至于由于使用时间临近而同时出现故障；3）以上工程，正常我们会在3天内完成，如果延长时间，我们会适当增加一些交通费\", \"refernceNo\": \"1000254852\", \"quotationNo\": \"15TMKQS090701\",\"quotationDate\": \"2016-11-21 13:48\",\"to\": \"天津中散船舶管理有限公司\",\"pic\": \"Kang Baoyi（康宝义）\",\"attn\": \"马洪元主管\",\"email\": \"kangbaoyi@cosbulk.com\",\"mv\": \"China Energy\",\"imo\": \"9495064\",\"tel\": \"（86）22 65705801\",\"mobile\": \"（86） 13602170331\",\"serviceCharge\": \"25600\",\"materialCharge\": \"91590\",\"allCharge\": \"117190\"},"
                    + "{ \"-id\": \"1\", \"equipment\": \"测探仪\",\"project\": \"探头\",\"count\": \"1\",\"price\": \"12500\",\"total\": \"12500\"},"
                    + " {\"-id\": \"1\",\"equipment\": \"电罗经\",\"project\": \"保养\",\"count\": \"2\",\"price\": \"2200\",\"total\": \"4400\"},"
                    + "{\"-id\": \"1\",\"equipment\": \"交通费\",\"project\": \" \",\"count\": \"1\",\"price\": \"1500\",\"total\": \"1500\",\"ps\": \"1）贵司雷达申请单是两台S波段雷达，应该是不准确的，因此我们报价一个X波段磁控管，一个S波段磁控管；2）罗经球寿命是3年，其中有一台罗经球使用2年半，建议更换该罗经球，从而两台罗经球使用时间不同，确保两台罗经球不至于由于使用时间临近而同时出现故障；3）以上工程，正常我们会在3天内完成，如果延长时间，我们会适当增加一些交通费\",\"refernceNo\": \"1000254852\",\"quotationNo\": \"15TMKQS090701\",\"quotationDate\": \"2016-11-21 13:48\",\"to\": \"天津中散船舶管理有限公司\",\"pic\": \"Kang Baoyi（康宝义）\",\"attn\": \"马洪元主管\",\"email\": \"kangbaoyi@cosbulk.com\",\"mv\": \"China Energy\",\"imo\": \"9495064\",\"tel\": \"（86）22 65705801\",\"mobile\": \"（86） 13602170331\",\"serviceCharge\": \"25600\",\"materialCharge\": \"91590\",\"allCharge\": \"117190\"}"
                    + "]}}";
        System.out.println("本次测试 = " + abc);

        System.exit(1);

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


