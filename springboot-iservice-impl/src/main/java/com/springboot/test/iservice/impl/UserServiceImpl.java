package com.springboot.test.iservice.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.test.beans.User;
import com.springboot.test.beans.UserExample;
import com.springboot.test.iservice.IUserService;
import com.springboot.test.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author liufei
 * @date 2018/12/21 17:06
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public int insert(User record) {
        return userMapper.insert(record);
    }

    @Override
    public int insertSelective(User record) {
        return userMapper.insertSelective(record);
    }

    @Override
    @Cacheable(value = "myCache",key = "'user_list_'+#pageNum+'_'+#pageSize")
    public PageInfo<User> selectByExample(UserExample example,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo(userMapper.selectByExample(example));
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        return userMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(User record) {
        return userMapper.updateByPrimaryKey(record);
    }
}
