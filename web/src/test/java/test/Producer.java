package test;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.nilo.dms.common.utils.DateUtil;

/**
 * Created by admin on 2017/10/19.
 */
public class Producer {
    public static void main(String[] args){
        DefaultMQProducer producer = new DefaultMQProducer("notify_cg111");
        producer.setNamesrvAddr("127.0.0.1:9876");
        try {
            producer.start();
            for(int i = 0;i<20;i++) {
                String body = DateUtil.getSysTimeStamp() + "_"+i;
                Message msg = new Message("merchantNotify1122",
                        "post",
                        "1",
                        body.getBytes());

                SendResult result = producer.send(msg);
                System.out.println(body+result.getSendStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            producer.shutdown();
        }
    }
}
