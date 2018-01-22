package test;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

/**
 * Created by admin on 2017/10/19.
 */
public class Producer {
    public static void main(String[] args){
        DefaultMQProducer producer = new DefaultMQProducer("notify_cg111");
        producer.setNamesrvAddr("127.0.0.1:9876");
        try {
            producer.start();
            Message msg = new Message("merchantNotify111",
                    "post",
                    "1",
                    "Just for test.".getBytes());

            SendResult result = producer.send(msg);
            System.out.println("id:" + result.getMsgId() +
                    " result:" + result.getSendStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            producer.shutdown();
        }
    }
}
