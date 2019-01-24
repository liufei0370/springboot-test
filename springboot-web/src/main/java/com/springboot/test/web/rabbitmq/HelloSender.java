package com.springboot.test.web.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 向队里发送消息
 * @author liufei
 * @date 2019/1/16 15:55
 */
@Component
public class HelloSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(){
        amqpTemplate.convertAndSend("queue","hello,rabbit");
    }
}
