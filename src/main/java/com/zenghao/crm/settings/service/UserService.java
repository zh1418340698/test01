package com.zenghao.crm.settings.service;


import com.zenghao.crm.exception.LoginException;
import com.zenghao.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd,String ip) throws LoginException;
    List<User> getUserList();
}
