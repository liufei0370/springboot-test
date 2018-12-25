package com.springboot.test.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.springboot.test.beans.User;
import com.springboot.test.beans.UserExample;
import com.springboot.test.iservice.IUserService;
import com.springboot.test.util.response.AjaxResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author liufei
 * @Date 2018/12/21 15:00
 */
@Api("用户管理")
@RestController
@RequestMapping(value = "user")
public class UserController {

    @Reference
    IUserService iUserService;

    @ApiOperation("获取所有用户信息")
    @PostMapping(value = "findAll")
    public AjaxResponse findAll(){
        List<User> list = iUserService.selectByExample(new UserExample());
        return AjaxResponse.success(list);
    }
}
