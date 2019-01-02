package com.springboot.test.web.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.springboot.test.beans.User;
import com.springboot.test.beans.UserExample;
import com.springboot.test.iservice.IUserService;
import com.springboot.test.util.response.AjaxResponse;
import com.springboot.test.util.validate.ValidateRex;
import com.springboot.test.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
public class UserController extends BaseController {

    @Reference
    IUserService iUserService;

    @ApiOperation("获取所有用户信息")
    @GetMapping(value = "findAll")
    public AjaxResponse findAll(){
        PageInfo<User> pageInfo = iUserService.selectByExample(new UserExample(),1,2);
        return AjaxResponse.success(pageInfo);
    }

    @ApiOperation("注册")
    @PostMapping(value = "register")
    public AjaxResponse register(@RequestParam String password,@RequestParam String mobile){
        try{
            logger.info("注册：mobile:{},password:{}", mobile,password);
            if(!ValidateRex.isMobile(mobile)){
                return AjaxResponse.error("手机号码不正确");
            }
            User user = new User();
            user.setUsername(mobile);
            user.setNickname(mobile);
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

    @ApiOperation("验证手机号码是否已注册")
    @GetMapping(value = "checkMobile")
    public AjaxResponse checkMobile(@RequestParam String mobile){
        logger.info("验证手机号是否正确：mobile:{}", mobile);
        if(!ValidateRex.isMobile(mobile)){
            return AjaxResponse.error("手机号码不正确");
        }
        UserExample example = new UserExample();
        example.createCriteria().andMobileEqualTo(mobile);
        List<User> list = iUserService.selectByExample(example);
        return AjaxResponse.success(list.isEmpty());
    }
}