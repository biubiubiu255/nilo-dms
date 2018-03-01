package test;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * Created by admin on 2017/10/19.
 */
public class Consumer2 {
    public static void main(String[] args) {
        DefaultMQPushConsumer consumer =
                new DefaultMQPushConsumer("notify_cg111");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        try {
            //订阅PushTopic下Tag为push的消息
            consumer.subscribe("merchantNotify1122", "post");
            consumer.setInstanceName("Ronny");
            consumer.registerMessageListener(
                    new MessageListenerOrderly() {
                        public ConsumeOrderlyStatus consumeMessage(
                                List<MessageExt> list,
                                ConsumeOrderlyContext Context) {
                            try {
                                Message msg = list.get(0);
                                System.out.println(new String(msg.getBody()));
                            }catch (Exception e){
                                System.out.println("exception");
                                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                            }
                            System.out.println("succ");
                            return ConsumeOrderlyStatus.SUCCESS;
                        }
                    }
            );
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
