package com.zenghao.crm.settings.web.controller;

import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.settings.service.UserService;
import com.zenghao.crm.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login.do")
    @ResponseBody
    public Map doLogin(String loginAct,String loginPwd,HttpServletRequest request){

        /*String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");*/
        System.out.println("进入到登录验证操作");
        //将密码的明文转化为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收浏览器的ip地址
        String ip = request.getRemoteAddr();

        System.out.println(ip);

        try{
            User user = userService.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);

            //如果程序执行到此处，业务层没有抛出异常
            //表示登录成功
            //{"success":"true"}
            Map map = new HashMap();
            map.put("success",true);
            return map;

        }catch (Exception exception){
            exception.printStackTrace();

            //一旦程序执行了catch块的信息，说明业务层为我们验证失败，抛出了异常
            //表示登录失败
            //{"success":"false","msg":""}

            //获取错误信息
            String msg = exception.getMessage();

            /*
                有两种方法返回ajax，一种是map，一种是vo类
                对于展现的信息以后会大量使用，创建vo类
                展现的信息只有在这个需求中使用，我们用map
             */
            Map map = new HashMap();
            map.put("success",false);
            map.put("msg",msg);


            return map;
        }



    }
}
