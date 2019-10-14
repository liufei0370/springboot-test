package com.springboot.test.web;

/**
 * @author liufei
 * @date 2019/10/12 14:53
 */
public class Test {
    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder("人员编号：${人员编号}，人员姓名：${人员姓名}");
        stringBuilder.replace(stringBuilder.indexOf("${人员编号}"),stringBuilder.indexOf("${人员编号}")+"${人员编号}".length(),"编号测试");
        System.out.println(stringBuilder);
    }
}
