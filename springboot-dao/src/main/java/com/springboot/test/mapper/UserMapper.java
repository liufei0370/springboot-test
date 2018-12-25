package com.springboot.test.mapper;

import com.springboot.test.beans.User;
import com.springboot.test.beans.UserExample;
import java.util.List;

public interface UserMapper {
    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}