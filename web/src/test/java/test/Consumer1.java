package test;

import com.alibaba.rocketmq.client.consumer.AllocateMessageQueueStrategy;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragelyByCircle;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * Created by admin on 2017/10/19.
 */
public class Consumer1 {
    public static void main(String[] args) {
        DefaultMQPushConsumer consumer =
                new DefaultMQPushConsumer("group_1");
        consumer.setNamesrvAddr("10.0.1.114:9876");
        try {
            //订阅PushTopic下Tag为push的消息
            consumer.subscribe("topic_1", "post");
            consumer.setInstanceName("Ronny");
            AllocateMessageQueueStrategy aqs = new AllocateMessageQueueAveragelyByCircle();
            consumer.setAllocateMessageQueueStrategy(aqs);
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
