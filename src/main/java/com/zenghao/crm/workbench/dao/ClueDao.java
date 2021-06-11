package com.zenghao.crm.workbench.dao;


import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.workbench.domain.Clue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueDao {


    int save(Clue clue);

    Clue detail(String id);

    Clue getClueById(String id);

    int delete(String clueId);

    List<Clue> getClueList(@Param("skipCount") Integer skipCount,@Param("pageSize") Integer pageSize);

    int getCount();

    int delete2(String[] id);
}
