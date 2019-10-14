package com.springboot.test.web.controller.generator;

import com.springboot.test.beans.User;
import com.springboot.test.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author liufei
 * @date 2019/5/17 17:39
 */
@RequestMapping(value = "/generator/")
@Controller
public class GeneratorController extends BaseController {

    @ResponseBody
    @RequestMapping(value = "test",method = {RequestMethod.POST,RequestMethod.DELETE})
    public void test(String name,long phone){
        System.out.println("test的POST方法测试"+name+",phone:"+phone);
    }

    @ResponseBody
    @RequestMapping(value = "test1",method = RequestMethod.POST)
    public void test1(@RequestParam String name){
        System.out.println("test1的POST方法测试"+name);
    }

    @ResponseBody
    @PostMapping(value = {"test2"})
    public void test2(@RequestBody User user){
        System.out.println("test2POST方法测试"+user.getNickname());
    }


    @ResponseBody
    @GetMapping(value = "test3/{name}")
    public void test3(@PathVariable String name){
        System.out.println("test3的POST方法测试"+name);
    }

    public void test4(String name){
        System.out.println("test4的POST方法测试"+name);
    }

    @ResponseBody
    @PostMapping(value = {"test5"})
    public void test5(User user){
        System.out.println("test5POST方法测试"+user.getNickname());
    }

    @ResponseBody
    @PostMapping(value = {"test6"})
    public String test6(@RequestBody User user){
        System.out.println("test6POST方法测试"+user.getNickname());
        return "test6";
    }
}
