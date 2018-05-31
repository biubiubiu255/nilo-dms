package test;

import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dto.order.ReportCodQuery;
import com.nilo.dms.web.controller.report.model.ReportUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by ronny on 2017/8/23.
 */
public class TestDemo {
    public static void main(String[] args) throws Exception {

        System.out.println( -1L ^ (-1L << 6L));

        LocalDateTime time = LocalDateTime.now();

        List<String> list = new ArrayList<String>();

        list.add("sfd");
        list.add("sfddfd");

        String[] larsr = new String[5];
        System.out.println("本次测试 = " + list.toArray(larsr));
        //System.out.println("本次测试 = " + Arrays.toString(list.toArray(larsr)));
        System.out.println("本次测试 = " + Arrays.toString(larsr));

        //String[] lars = (String[]) list.toArray();
        System.exit(1);


        time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println("本次测试 = " + time.withHour(3).toInstant(ZoneOffset.of("+8")).toEpochMilli());
        System.out.println("本次测试 = " + LocalTime.now().toString());
        System.exit(1);

        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        long millSeconds = ChronoUnit.MILLIS.between(LocalDateTime.now(),midnight);
        long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), midnight);
        System.out.println("当天剩余毫秒3：" + millSeconds);
        System.out.println("当天剩余秒3：" + seconds);

        System.out.println("本次测试 = " + time.getSecond());

        System.out.println("本次测试 = " + new SimpleDateFormat("yyyy").format(new Date()).toString());
        String str = "123123";
        String str1 = "213";
        Integer sd = 1251;
        Integer dfsd = 1251;
//CD13E19766309743EABFAA9968B548C1
        System.out.println("本次测试 = " + str.hashCode());
        System.out.println("本次测试 = " + dfsd.hashCode());


        //Waybill order = JSON.parseObject(str,Waybill.class);

        //System.out.println(DigestUtils.md5Hex("12345678" + str + "12345678").toUpperCase());

/*        Properties prop = new Properties();

        InputStream stream = TestDemo.class.getClassLoader().getResourceAsStream("" +
                "i18n_en_US.properties");
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        System.out.println(url.toString());*/


    }
}


