package com.springboot.test.web.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Date;
import java.util.Properties;

/**
 * kafka生产者测试类
 * @author liufei
 * @date 2019/1/8 10:39
 */
public class SimpleProducer {
    public static void main(String[] args) {

        String topicName = "test";
        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        //acks：配置完成请求的条件
        props.put("acks","all");
        //retries：发送失败的重试次数
        props.put("retries",0);
        //batch.size：producer维护了每个分区未发送记录的缓存，该缓存的大小由batch.size设定。
        props.put("batch.size",16384);
        //linger.ms：一般情况下，记录会被立即发送出去，而不会等待缓存的填充。用户可以通过配置linger.ms来让producer等待一段时间再发送消息。
        props.put("linger.ms",1);
        //buffer.memory： 缓存的大小。消息填满缓存后，后续的消息就会阻塞。阻塞超过max.block.ms设定的时间，就会抛出TimeoutException。
        props.put("buffer.memory",33554432);
        //key.serializer and value.serializer： 如何将key和value组合成对象，可以自定义类。使用 StringSerializer默认组合成字符串。
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        /**
         * transactional.id: 开启transactional。transactional.id被设置后，idempotent也会自动开启。一旦被设置，发送代码就需要使用事务
         * 在分区应用中，每个producer的transactional.id须唯一。
         * consumer端需要配置为 只消费committed的消息。即：props.put("enable.auto.commit", "true");
         */
        props.put("transactional.id", "my-transactional-id");
        Date time = new Date();
        Producer<String, String> producer = new KafkaProducer<>(props);
        System.out.println("耗时================================"+(new Date().getTime()-time.getTime()));
        time = new Date();
        for(int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<>(topicName, Integer.toString(i), Integer.toString(i)));
        }
        System.out.println("耗时---------------------"+(new Date().getTime()-time.getTime()));
        producer.close();
    }
}
