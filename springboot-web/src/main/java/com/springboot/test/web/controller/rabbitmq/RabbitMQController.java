package com.springboot.test.web.controller.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liufei
 * @date 2019/3/6 11:29
 */
@Controller
@RequestMapping(value = "rabbitmq")
public class RabbitMQController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @GetMapping(value = "send")
    public void send(String queueName,String message){
        amqpTemplate.convertAndSend(queueName,message);
    }
}
