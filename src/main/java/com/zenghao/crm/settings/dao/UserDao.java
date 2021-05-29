package com.zenghao.crm.settings.dao;


import com.zenghao.crm.settings.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    User doLogin(@Param("loginAct") String loginAct,
                 @Param("loginPwd") String loginPwd);

    List<User> getUserList();
}
