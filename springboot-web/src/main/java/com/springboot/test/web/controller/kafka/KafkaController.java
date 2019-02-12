package com.springboot.test.web.controller.kafka;

import com.springboot.test.util.response.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;

/**
 * kafa测试
 * @author liufei
 * @date 2019/1/30 15:19
 */
@Controller
@RequestMapping("kafka")
public class KafkaController {
    public final static Logger logger = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;//kafkaTemplate相当于生产者

    @RequestMapping(value = "/{topic}/send",method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse sendMessage(
            @RequestParam(value = "message",defaultValue = "hello world") String message,
            @PathVariable final String topic) {
        try{
            logger.info("start send message to {}",topic);
            ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(topic,message);//发送消息，topic不存在将自动创建新的topic
            listenableFuture.addCallback(//添加成功发送消息的回调和失败的回调
                    result -> logger.info("send message to {} success",topic),
                    ex -> logger.info("send message to {} failure,error message:{}",topic,ex.getMessage()));
            return AjaxResponse.success();
        }catch (Exception e){
            logger.error("kafka发送失败",e);
            return AjaxResponse.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/default/send",method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse sendMessageDefault() {
        try{
            //发送消息到默认的topic
            logger.info("start send message to default topic");
            kafkaTemplate.sendDefault("你好，世界");
            return AjaxResponse.success();
        }catch (Exception e){
            logger.error("kafka发送失败",e);
            return AjaxResponse.error(e.getMessage());
        }
    }

}
