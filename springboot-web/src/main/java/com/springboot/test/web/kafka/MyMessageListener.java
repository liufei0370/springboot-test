package com.springboot.test.web.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

/**
 * @author liufei
 * @date 2019/1/30 15:14
 */
public class MyMessageListener implements MessageListener<String, String> {
    public final static Logger logger = LoggerFactory.getLogger(MyMessageListener.class);

    @Override
    public void onMessage(ConsumerRecord<String, String> stringStringConsumerRecord) {
        String topic = stringStringConsumerRecord.topic();//消费的topic
        logger.info("-------------recieve message from {} topic-------------", topic);
        logger.info("partition:{}", String.valueOf(stringStringConsumerRecord.partition()));//消费的topic的分区
        logger.info("offset:{}", String.valueOf(stringStringConsumerRecord.offset()));//消费者的位置
        logger.info("get message from {} topic : {}", topic, stringStringConsumerRecord.value());//接收到的消息
    }
}
