package com.zenghao.crm.workbench.web.controller;

import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.settings.service.UserService;
import com.zenghao.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/workbench/clue")
public class ClueController {

    @Autowired
    private ClueService clueService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){

        List<User> list = userService.getUserList();

        return list;
    }
}
