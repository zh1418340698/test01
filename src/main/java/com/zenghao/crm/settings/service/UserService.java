package com.zenghao.crm.settings.service;


import com.zenghao.crm.settings.domain.User;

public interface UserService {
    User doLogin(String loginAct, String loginPwd);
}
