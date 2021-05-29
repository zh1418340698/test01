package com.zenghao.crm.workbench.web.controller;

import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.settings.service.UserService;
import com.zenghao.crm.utils.DateTimeUtil;
import com.zenghao.crm.utils.UUIDUtil;
import com.zenghao.crm.workbench.domain.Activity;
import com.zenghao.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/workbench/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/add.do")
    @ResponseBody
    public List addUserList(){

        List<User> list = userService.getUserList();

        return list;
    }


    @RequestMapping(value = "/save.do")
    @ResponseBody
    public Map save(Activity activity, HttpServletRequest request){
        String id = UUIDUtil.getUUID();
        activity.setId(id);
        //系统当前时间
        activity.setCreateTime(DateTimeUtil.getSysTime());
        //创建人，当前登录用户
        activity.setCreateBy( ((User)request.getSession().getAttribute("user")).getName());

        boolean flag = activityService.save(activity);

        Map map = new HashMap();
        map.put("success",flag);

        return map;
    }
}
