package com.springboot.test.web.easemob;

import com.alibaba.fastjson.JSONObject;
import com.springboot.test.web.easemob.impl.EasemobIMUsers;
import com.springboot.test.web.easemob.impl.EasemobSendMessage;
import io.swagger.client.model.*;

/**
 * @author liufei
 * @date 2019/1/14 10:25
 */
public class EasemobTest {
    public static void main(String[] args) {
        EasemobTest easemobTest = new EasemobTest();
        //新增用户
        //easemobTest.createNewIMUserSingle();
        //查询用户在线状态
        easemobTest.getIMUserStatus("liufei");
        //向用户推送消息
        EasemobSendMessage sendMessage = new EasemobSendMessage();
        Msg msg = new Msg();
        MsgContent msgContent = new MsgContent();
        msgContent.type(MsgContent.TypeEnum.TXT).msg("环信测试");
        /**
         * target_type :users：用户，chatgroups：群组，
         */
        UserName userName = new UserName();
        msg.setTargetType("users");
        userName.add("liufei");
//        msg.setTargetType("chatgroups");
//        userName = new UserName();
//        userName.add("71360786268161");

        msg.target(userName).msg(msgContent);
        Object object = sendMessage.sendMessage(msg);
        System.out.println(object.toString());
    }

    public void createNewIMUserSingle(){
        EasemobIMUsers imUsers = new EasemobIMUsers();
        RegisterUsers registerUsers = new RegisterUsers();
        User user = new User();
        user.setUsername("liufei");
        user.setPassword("123456");
        registerUsers.add(user);
        Object result = imUsers.createNewIMUserSingle(registerUsers);
        System.out.println(result.toString());
    }

    public void getIMUserStatus(String username){
        //查询用户在线状态
        EasemobIMUsers imUsers = new EasemobIMUsers();
        Object result = imUsers.getIMUserStatus(username);
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        System.out.println(jsonObject.getJSONObject("data").getString(username));
    }
}
