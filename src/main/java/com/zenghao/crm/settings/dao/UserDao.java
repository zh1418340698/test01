package com.zenghao.crm.settings.dao;


import com.zenghao.crm.settings.domain.User;

public interface UserDao {
    User doLogin(String loginAct, String loginPwd);
}
