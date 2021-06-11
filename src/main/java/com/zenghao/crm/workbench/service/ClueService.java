package com.zenghao.crm.workbench.service;

import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.workbench.domain.Activity;
import com.zenghao.crm.workbench.domain.Clue;
import com.zenghao.crm.workbench.domain.ClueActivityRelation;
import com.zenghao.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {

    Boolean save(Clue clue);

    Clue detail(String id);

    Boolean unbund(String id);

    Boolean bund(String cid,String[] aids);

    Boolean convert(String clueId, Tran tran, String createBy);

    Map<String, Object> pageList(Integer skipCount, Integer pageSize);

    boolean delete2(String[] id);
}
