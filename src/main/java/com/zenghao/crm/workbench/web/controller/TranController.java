package com.zenghao.crm.workbench.web.controller;

import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.settings.service.UserService;
import com.zenghao.crm.utils.DateTimeUtil;
import com.zenghao.crm.workbench.domain.Tran;
import com.zenghao.crm.workbench.domain.TranHistory;
import com.zenghao.crm.workbench.service.CustomerService;
import com.zenghao.crm.workbench.service.TranService;
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
@RequestMapping(value = "/workbench/transaction")
public class TranController {

    @Autowired
    private TranService tranService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;

    //打开创建交易页面，同时传递用户列表
    @RequestMapping(value = "/add.do")
    @ResponseBody
    public ModelAndView add(){
        ModelAndView mv = new ModelAndView();

        List<User> userList = userService.getUserList();
        mv.addObject("uList",userList);

        mv.setViewName("/workbench/transaction/save.jsp");

        return mv;
    }

    //根据用户输入的值模糊查询客户的名字
    @RequestMapping(value = "/getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName(String name){
        List<String> nameList = customerService.getCustomerName(name);

        return nameList;
    }

    //保存交易
    @RequestMapping(value = "/save.do")
    @ResponseBody
    public ModelAndView save(Tran tran, String customerName, HttpServletRequest request){
        ModelAndView mv = new ModelAndView();

        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        boolean flag = tranService.save(tran,customerName,createBy);

        if (flag){
            mv.setViewName("redirect:/workbench/transaction/index.jsp");
        }

        return mv;
    }

    //获得交易列表
    @RequestMapping(value = "/pageList.do")
    @ResponseBody
    public Map<String,Object> pageList(Integer pageNo,Integer pageSize){

        //计算略过的条数
        int skipCount = (pageNo-1) * pageSize;

        Map<String,Object> map = tranService.pageList(skipCount,pageSize);

        return map;
    }

    //打开详细信息页
    @RequestMapping(value = "/detail.do")
    @ResponseBody
    public ModelAndView detail(String id,HttpServletRequest request){
        ModelAndView mv = new ModelAndView();

        Tran tran = tranService.getTranById(id);
        String stage = tran.getStage();
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);

        mv.addObject("tran",tran);
        mv.addObject("possibility",possibility);
        mv.setViewName("/workbench/transaction/detail.jsp");

        return mv;
    }

    //获取历史交易列表
    @RequestMapping(value = "/getHistoryListByTranId.do")
    @ResponseBody
    public List<TranHistory> getHistoryListByTranId(String tranId,HttpServletRequest request){


        List<TranHistory> thList = tranService.getHistoryListByTranId(tranId);

        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        for ( TranHistory tranHistory:thList){
            //获取阶段对应的可能性
            String possibility = pMap.get(tranHistory.getStage());
            tranHistory.setPossibility(possibility);
        }

        return thList;
    }

    //更改交易阶段
    @RequestMapping(value = "/changeStage.do")
    @ResponseBody
    public Map<String,Object> changeStage(Tran tran,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();

        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);

        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        tran.setEditBy(editBy);
        tran.setEditTime(DateTimeUtil.getSysTime());

        Boolean flag = tranService.changeStage(tran);
        map.put("success",flag);
        map.put("t",tran);

        return map;
    }

    //取得交易统计图表的数据
    @RequestMapping(value = "/getCharts.do")
    @ResponseBody
    public Map<String,Object> getCharts(){

        Map<String,Object> map = tranService.getCharts();

        return map;
    }
}
