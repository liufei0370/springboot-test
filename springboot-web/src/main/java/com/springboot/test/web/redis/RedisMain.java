package com.springboot.test.web.redis;

/**
 * redis各项功能测试主类
 * @author liufei
 * @date 2019/1/10 11:20
 */
public class RedisMain {
    public static void main(String[] args) {
        try {
            new RedisClient().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
