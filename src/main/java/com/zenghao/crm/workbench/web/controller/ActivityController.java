package com.zenghao.crm.workbench.web.controller;

import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.settings.service.UserService;
import com.zenghao.crm.utils.DateTimeUtil;
import com.zenghao.crm.utils.UUIDUtil;
import com.zenghao.crm.vo.PaginationVo;
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

    //查询市场活动信息列表的操作
    @RequestMapping(value = "/pageList.do")
    @ResponseBody
    public PaginationVo pageList(Activity activity,HttpServletRequest request){

        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);

        //每页展现的记录
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);

        //计算略过的条数
        int skipCount = (pageNo-1) * pageSize;

        Map<String,Object> map = new HashMap();
        map.put("name",activity.getName());
        map.put("owner",activity.getOwner());
        map.put("startDate",activity.getStartDate());
        map.put("endDate",activity.getEndDate());
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        PaginationVo<Activity> vo = activityService.pageList(map);

        return vo;
    }

    //执行市场活动的删除操作
    @RequestMapping(value = "/delete.do")
    @ResponseBody
    public Map delete(String[] id){

        Boolean flag = activityService.delete(id);

        Map map = new HashMap();
        map.put("success",flag);

        return map;

    }

    //用户打开修改界面时，把要修改的市场活动的信息展现出来
    @RequestMapping(value = "/edit.do")
    @ResponseBody
    public Map edit(String id){

        Map map = new HashMap();

        Activity activity = activityService.edit(id);
        map.put("activity",activity);

        List<User> list = userService.getUserList();
        map.put("list",list);

        return map;
    }

    @RequestMapping(value = "/update.do")
    @ResponseBody
    public Map updateActivity(Activity activity){

        Map map = new HashMap();

        Boolean flag = activityService.updateActivity(activity);

        map.put("success",flag);

        return map;

    }
}
