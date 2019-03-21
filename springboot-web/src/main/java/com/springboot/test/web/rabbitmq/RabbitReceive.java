package com.springboot.test.web.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author liufei
 * @date 2019/3/8 16:42
 */
public class RabbitReceive {

    @RabbitListener(queues = "queue")
    public void receive(Object o){
        System.out.println("Receive:"+ JSONObject.toJSONString(o));
    }
}
