package com.zenghao.crm.settings.web.controller;

import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login.do")
    @ResponseBody
    public ModelAndView doLogin(String a,String loginPwd){
        ModelAndView mv = new ModelAndView();
        User id = userService.doLogin(a,loginPwd);
        if ( true){
            mv.setViewName("/loginError.jsp");
        }else {
            mv.setViewName("workbench/index.html");
        }


        return mv;
    }
}
