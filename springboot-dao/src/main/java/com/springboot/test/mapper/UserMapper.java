package com.springboot.test.mapper;

import com.springboot.test.beans.User;
import com.springboot.test.beans.UserExample;

import java.util.List;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}