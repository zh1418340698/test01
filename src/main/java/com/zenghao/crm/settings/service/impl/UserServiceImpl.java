package com.zenghao.crm.settings.service.impl;

import com.zenghao.crm.exception.LoginException;
import com.zenghao.crm.settings.dao.UserDao;
import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.settings.service.UserService;
import com.zenghao.crm.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Override
    public User login(String loginAct, String loginPwd,String ip) throws LoginException {

        User user = userDao.doLogin(loginAct,loginPwd);

        if ( user == null){
            throw new LoginException("账号或密码错误");
        }

        //如果程序能执行到这里，说明账号密码正确
        //继续验证其他3项信息

        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if ( expireTime.compareTo(currentTime) < 0 ){
            throw new LoginException("账号已失效");
        }

        //判断锁定状态
        String lockState = user.getLockState();
        if ( "0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }

        //判断ip地址
        String allowIps = user.getAllowIps();
        if ( allowIps.contains(ip)){
            throw new LoginException("ip地址受限，请联系管理员");
        }
        return user;
    }
}
