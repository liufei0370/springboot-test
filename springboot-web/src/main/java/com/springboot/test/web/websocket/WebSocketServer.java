package com.springboot.test.web.websocket;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.springboot.test.util.websocket.Command;
import com.springboot.test.util.websocket.Message;
import com.springboot.test.util.websocket.WebSocketCode;
import org.apache.kafka.common.utils.CopyOnWriteMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liufei
 * @date 2019/1/21 9:38
 */
@Component
@ServerEndpoint("/websocket/{sid}")
public class WebSocketServer {
    private static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    //concurrent包线程安全的Map，用来存放每个客户端对应的websocket对象
    private static CopyOnWriteMap<String,WebSocketServer> webSocketServerMap = new CopyOnWriteMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private Lock sendLock=new ReentrantLock();

    //接收sid
    private String sid = "";

    /**
     * 连接建立成功调用的方法
     * @param session
     * @param sid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid")String sid){
        this.session = session;
        this.sid = sid;
        if(webSocketServerMap.keySet().contains(sid)){
            logger.info("sid：{}账号已连接，强制踢下线",sid);
            WebSocketServer server = webSocketServerMap.get(sid);
            server.sendCommand(Command.error("您的账号在其他地方登陆，连接断开"));
            server.close();
        }
        webSocketServerMap.put(this.sid,this);
        logger.info("有新窗口开始监听："+sid+"，当前在线人数为"+webSocketServerMap.size());
        sendCommand(Command.success("连接成功"));
    }

    /**
     * 收到客户端消息后调用的方法
     * @param session
     * @param message
     */
    @OnMessage
    public void onMessage(Session session,String message){
        if(!"000000".equals(message)){
            logger.info("收到来自窗口："+this.sid+"的信息："+message);
            Message data;
            try{
                data = JSONObject.parseObject(message,Message.class);
            }catch (Exception e){
                data = new Message();
                data.setCategory("string");
                data.setContent(message);
            }
            data.setSendTime(new Date());
            //群发消息--消息发送给除当前用户的其他所有用户
            for(WebSocketServer server : webSocketServerMap.values()){
                if(!server.sid.equals(this.sid)){
                    Command command = Command.success("收到一条来自sid："+this.sid+"的消息");
                    command.setData(data);
                    server.sendCommand(command);
                }
            }
        }
    }

    /**
     * 实现服务券主动推送
     * @param data
     */
    public void sendCommand(Object data) {
        if(data instanceof Command){
            sendMessage(JSONObject.toJSONString(data));
        }else if (data instanceof String){
            sendMessage(data.toString());
        }else {
            logger.error("推送内容{}类别错误",data.getClass());
        }
    }

    public void sendMessage(String message){
        logger.info("发送消息：{}",message);
        sendLock.lock();
        try {
            if(!this.isClose()){
                this.session.getBasicRemote().sendText(message);
            }else {
                logger.error("客户端已关闭");
                onClose();
            }
        } catch (IOException e) {
            logger.error("服务器发送消息异常",e);
        } finally {
            sendLock.unlock();
        }
    }

    public static void sendInfo(String message,@PathParam("sid")String sid){
        logger.info("推送消息到窗口："+sid+"，推送内容："+message);
        //sid为null，群发消息
        if(sid==null){
            for(WebSocketServer server : webSocketServerMap.values()){
                server.sendMessage(message);
            }
        }else if(webSocketServerMap.keySet().contains(sid)){
            webSocketServerMap.get(sid).sendMessage(message);
        }
    }

    /**
     * 主动关闭连接
     */
    public void close() {
        try {
            if(!isClose()){
                this.session.close();
            }
        } catch (Exception e) {
            logger.error("sid："+this.sid+"close",e);
        }finally {
            this.session = null;
        }
    }

    /**
     * 该连接是否已关闭
     * @return
     */
    public boolean isClose(){
        return this.session==null || !this.session.isOpen();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketServerMap.remove(this.sid);
        logger.info("连接窗口："+this.sid+"关闭，当前在线人数为"+webSocketServerMap.size());
    }

    @OnError
    public void onError(Session session,Throwable error){
        logger.error("发生错误",error);
        onClose();
    }

}
