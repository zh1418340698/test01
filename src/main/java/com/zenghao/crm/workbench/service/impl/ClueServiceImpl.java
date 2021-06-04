package com.zenghao.crm.workbench.service.impl;

import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.workbench.dao.ClueDao;
import com.zenghao.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueDao clueDao;



}
