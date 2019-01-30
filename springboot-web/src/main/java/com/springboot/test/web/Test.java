package com.springboot.test.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liufei
 * @date 2019/1/28 15:19
 */
public class Test {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(new Date().getTime());
        System.out.println(sdf.parse("2000-01-01 00:00:00").getTime());
    }
}
