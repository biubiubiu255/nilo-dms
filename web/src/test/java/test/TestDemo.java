package test;

import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dto.order.ReportCodQuery;
import com.nilo.dms.web.controller.report.model.ReportUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;


/**
 * Created by ronny on 2017/8/23.
 */
public class TestDemo {
    public static void main(String[] args) throws Exception {

        String str = "123123";


        //Waybill order = JSON.parseObject(str,Waybill.class);

        System.out.println(DigestUtils.md5Hex("12345678" + str + "12345678").toUpperCase());

/*        Properties prop = new Properties();

        InputStream stream = TestDemo.class.getClassLoader().getResourceAsStream("" +
                "i18n_en_US.properties");
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        System.out.println(url.toString());*/


    }
}


