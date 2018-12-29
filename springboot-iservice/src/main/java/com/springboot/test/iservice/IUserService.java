package com.springboot.test.iservice;

import com.github.pagehelper.PageInfo;
import com.springboot.test.beans.User;
import com.springboot.test.beans.UserExample;

import java.util.List;

/**
 * @author liufei
 * @date 2018/12/21 16:54
 */
public interface IUserService {
    int insert(User record);

    int insertSelective(User record);

    PageInfo<User> selectByExample(UserExample example, int pageNum, int pageSize);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}
