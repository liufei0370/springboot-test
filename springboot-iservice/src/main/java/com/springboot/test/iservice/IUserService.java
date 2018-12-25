package com.springboot.test.iservice;

import com.springboot.test.beans.User;
import com.springboot.test.beans.UserExample;

import java.util.List;

/**
 * @author liufei
 * @date 2018/12/21 16:54
 */
public interface IUserService {
    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}
