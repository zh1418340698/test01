package com.zenghao.crm.workbench.web.controller;

import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.settings.service.UserService;
import com.zenghao.crm.utils.DateTimeUtil;
import com.zenghao.crm.utils.UUIDUtil;
import com.zenghao.crm.workbench.domain.Activity;
import com.zenghao.crm.workbench.domain.Clue;
import com.zenghao.crm.workbench.domain.ClueActivityRelation;
import com.zenghao.crm.workbench.domain.Tran;
import com.zenghao.crm.workbench.service.ActivityService;
import com.zenghao.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/workbench/clue")
public class ClueController {

    @Autowired
    private ClueService clueService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;


    //展示用户列表
    @RequestMapping(value = "/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){

        List<User> list = userService.getUserList();

        return list;
    }

    //保存线索
    @RequestMapping(value = "/save.do")
    @ResponseBody
    public Map<String,Object> save(Clue clue, HttpServletRequest request){
        String id = UUIDUtil.getUUID();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();

        clue.setId(id);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);

        Map<String,Object> map = new HashMap<>();

        Boolean flag = clueService.save(clue);
        map.put("success",flag);
        return map;
    }

    //删除线索
    @RequestMapping(value = "/delete2.do")
    @ResponseBody
    public Map<String,Object> delete2(String[] id){
        Map<String,Object> map = new HashMap<>();

        boolean flag = clueService.delete2(id);
        map.put("success",flag);

        return map;
    }

    //展现线索详细信息页
    @RequestMapping(value = "/detail.do")
    @ResponseBody
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Clue c = clueService.detail(id);

        mv.addObject("c",c);
        mv.setViewName("/workbench/clue/detail.jsp");

        return mv;
    }

    //展示与线索关联的市场活动列表
    @RequestMapping(value = "/getActivityListByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByClueId(String clueId){

        List<Activity> list = activityService.getActivityListByClueId(clueId);

        return list;
    }

    //解除线索与市场活动的关联
    @RequestMapping(value = "/unbund.do")
    @ResponseBody
    public Map<String,Object> unbund(String id){
        Map<String,Object> map = new HashMap<>();

        Boolean flag = clueService.unbund(id);
        map.put("success",flag);

        return map;
    }

    //根据模糊查询查询市场活动列表（排除已关联的）
    @RequestMapping(value = "/getActivityListByNameAndNoByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByNameAndNoByClueId(String aname,String clueId){

        List<Activity> list = activityService.getActivityListByNameAndNoByClueId(aname,clueId);

        return list;
    }

    //为线索绑定市场活动
    @RequestMapping(value = "/bund.do")
    @ResponseBody
    public Map<String,Object> bund(String cid,String[] aid){

        Boolean flag = clueService.bund(cid,aid);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);

        return map;
    }

    //根据市场活动名字模糊查询市场活动
    @RequestMapping(value = "/getActivityListByName.do")
    @ResponseBody
    public List<Activity> getActivityListByName(String aname){
        List<Activity> list = activityService.getActivityListByName(aname);

        return list;
    }

    //获取线索列表
    @RequestMapping(value = "/pageList.do")
    @ResponseBody
    public Map<String,Object> pageList(Integer pageNo,Integer pageSize){
        int skipCount = (pageNo-1) * pageSize;

        Map<String,Object> map = clueService.pageList(skipCount,pageSize);

        return map;
    }

    //把线索转换成客户和联系人
    @RequestMapping(value = "/convert.do")
    @ResponseBody
    public ModelAndView convert(String clueId, String flag,Tran tran,HttpServletRequest request){
        ModelAndView mv = new ModelAndView();

        String createBy= ((User)request.getSession().getAttribute("user")).getName();

        if ("a".equals(flag)){
            //需要创建交易
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();


            tran.setId(id);
            tran.setCreateBy(createBy);
            tran.setCreateTime(createTime);

        }else {
            tran = null;
        }

        /*
                为业务层传递的对象
                    1、必须传递的参数clueId，有了这个clueId我们才知道要转换哪条记录
                    2、必须传递的参数tran，在线索转换过程中，可能创建一笔交易（业务层接收的tran也可能是空）
                    3、传递创建人，进行添加操作
         */
        Boolean flag1 = clueService.convert(clueId,tran,createBy);

        if (flag1){
            //重定向转发
            mv.setViewName("redirect:/workbench/clue/index.jsp");
        }



        return mv;
    }
}
