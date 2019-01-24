package com.springboot.test.util.websocket;

import com.springboot.test.util.common.IDUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liufei
 * @date 2019/1/24 17:54
 */
public class Message implements Serializable {
    public static final long serialVersionUID=1L;

    private String id = IDUtil.RANDOM32();

    //消息类别
    private String category;

    //消息内容
    private String content;

    //消息发送时间
    private Date sendTime = new Date();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
