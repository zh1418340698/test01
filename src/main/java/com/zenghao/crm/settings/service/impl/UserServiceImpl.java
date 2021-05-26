package com.zenghao.crm.settings.service.impl;

import com.zenghao.crm.settings.dao.UserDao;
import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Override
    public User doLogin(String loginAct, String loginPwd) {
        User id = userDao.doLogin(loginAct,loginPwd);
        return id;
    }
}
