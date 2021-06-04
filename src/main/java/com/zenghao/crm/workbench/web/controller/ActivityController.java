package com.zenghao.crm.workbench.web.controller;

import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.settings.service.UserService;
import com.zenghao.crm.utils.DateTimeUtil;
import com.zenghao.crm.utils.UUIDUtil;
import com.zenghao.crm.vo.PaginationVo;
import com.zenghao.crm.workbench.dao.ActivityRemarkDao;
import com.zenghao.crm.workbench.domain.Activity;
import com.zenghao.crm.workbench.domain.ActivityRemark;
import com.zenghao.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    //修改市场活动
    @RequestMapping(value = "/update.do")
    @ResponseBody
    public Map updateActivity(Activity activity,HttpServletRequest request){

        Map map = new HashMap();

        //设置修改时间为当前时间，
        String editTime = DateTimeUtil.getSysTime();
        activity.setEditTime(editTime);

        User user = (User) request.getSession().getAttribute("user");
        activity.setEditBy(user.getName());

        Boolean flag = activityService.updateActivity(activity);

        map.put("success",flag);

        return map;

    }

    //跳转到详细信息页的操作
    @ResponseBody
    @RequestMapping(value = "/detail.do")
    public ModelAndView detail(String id, HttpServletRequest request, HttpServletResponse response){

        ModelAndView mv = new ModelAndView();

        Activity a = activityService.detail(id);

        mv.addObject("a",a);
        mv.setViewName("/workbench/activity/detail.jsp");

        return mv;
    }


    //把市场活动的备注列表显示出来
    @RequestMapping(value = "/getRemarkListByAid.do")
    @ResponseBody
    public List<ActivityRemark> getRemarkListByAid(String activityId){

        List<ActivityRemark> activityRemarkList = activityService.getRemarkListByAid(activityId);

        return activityRemarkList;

    }

    //删除市场活动的备注
    @RequestMapping(value = "/deleteRemark.do")
    @ResponseBody
    public Map deleteRemark(String id){

        Map map = new HashMap();

        Boolean flag = activityService.deleteRemark(id);
        map.put("success",flag);

        return map;
    }

    //保存市场活动的备注
    @RequestMapping(value = "/saveRemark.do")
    @ResponseBody
    public Map saveRemark(String noteContent,String activityId,HttpServletRequest request){

        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);
        ar.setActivityId(activityId);

        Map map = new HashMap();
        Boolean flag = activityService.saveRemark(ar);
        map.put("success",flag);
        return map;
    }

    //打开修改市场活动备注的模态窗口
    @RequestMapping(value = "/editRemark.do")
    @ResponseBody
    public Map editRemark(String id){

        ActivityRemark ar = activityService.getRemarkById(id);
        Map map = new HashMap();
        map.put("id",ar.getId());
        map.put("noteContent",ar.getNoteContent());

        return map;

    }

    //修改备注
    @RequestMapping(value = "/updateRemark.do")
    @ResponseBody
    public Map updateRemark(String id,String noteContent,HttpServletRequest request){
        Map map = new HashMap();

        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";
        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        Boolean flag = activityService.updateRemark(ar);
        map.put("success",flag);

        return map;
    }
}
