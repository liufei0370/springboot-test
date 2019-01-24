package com.springboot.test.util.websocket;

import java.io.Serializable;

/**
 * websocket通信携带的数据对象
 * @author liufei
 * @date 2019/1/24 16:51
 */
public class Command implements Serializable {
    public static final long serialVersionUID=1L;

    private String code;

    private Object data;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Command success(String message){
        Command command = new Command();
        command.setCode(WebSocketCode.SUCCESS_CODE);
        command.setMessage(message);
        return command;
    }

    public static Command error(String message){
        Command command = new Command();
        command.setCode(WebSocketCode.ERROR_CODE);
        command.setMessage(message);
        return command;
    }
}
