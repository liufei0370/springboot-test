package com.springboot.test.web.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author liufei
 * @date 2019/1/21 11:11
 */
@Controller
@Api("页面配置")
public class MainController {

    @GetMapping("/templates/{page}.html")
    public String html(@PathVariable String page){
        return page;
    }
}
