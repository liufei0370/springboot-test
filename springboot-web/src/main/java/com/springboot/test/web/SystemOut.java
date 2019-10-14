package com.springboot.test.web;

/**
 * @author liufei
 * @date 2019/9/5 10:55
 */
public class SystemOut {
    {
        System.out.println("SystemOut 1");
    }

    public static SystemOut systemOut1 = new SystemOut();

    static{
        System.out.println("static SystemOut 1");
    }

    {
        System.out.println("SystemOut 2");
    }

    public SystemOut(){
        System.out.println("Constructor SystemOut");
    }

    public static void main(String[] args) {
        new SystemOut();
    }
}
