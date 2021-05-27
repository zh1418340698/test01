package com.zenghao.crm.settings.service;


import com.zenghao.crm.exception.LoginException;
import com.zenghao.crm.settings.domain.User;

public interface UserService {
    User login(String loginAct, String loginPwd,String ip) throws LoginException;
}
