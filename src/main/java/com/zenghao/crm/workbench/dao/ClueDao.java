package com.zenghao.crm.workbench.dao;


import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueDao {


    int save(Clue clue);

    Clue detail(String id);

    Clue getClueById(String id);

    int delete(String clueId);

    List<Clue> getClueList();

    int getCount();
}
