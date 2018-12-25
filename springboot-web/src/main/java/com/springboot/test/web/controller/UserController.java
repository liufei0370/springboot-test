package com.springboot.test.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.springboot.test.beans.User;
import com.springboot.test.beans.UserExample;
import com.springboot.test.iservice.IUserService;
import com.springboot.test.util.response.AjaxResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Author liufei
 * @Date 2018/12/21 15:00
 */
@Api("用户管理")
@RestController
@RequestMapping(value = "user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Reference
    IUserService iUserService;

    @ApiOperation("获取所有用户信息")
    @PostMapping(value = "findAll")
    public AjaxResponse findAll(){
        List<User> list = iUserService.selectByExample(new UserExample());
        return AjaxResponse.success(list);
    }

    @ApiOperation("注册")
    @PostMapping(value = "register")
    public AjaxResponse register(@RequestParam String username,@RequestParam String nickname,
                                 @RequestParam String password,@RequestParam String mobile){
        try{
            logger.info("注册：username:{},nickname:{},password:{},mobile:{}", username,nickname,password,mobile);
            User user = new User();
            user.setUsername(username);
            user.setNickname(nickname);
            user.setMobile(mobile);
            user.setPassword(password);
            user.setCreateDate(new Date());
            iUserService.insertSelective(user);
            return AjaxResponse.success();
        }catch (Exception e){
            logger.error("注册失败",e);
            return AjaxResponse.error("注册失败");
        }
    }
}
