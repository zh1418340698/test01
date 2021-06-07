package com.zenghao.crm.workbench.dao;


import com.zenghao.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    int unbund(String id);

    int bund(ClueActivityRelation clueActivityRelation);

    List<ClueActivityRelation> getActivityIdListByClueId(String clueId);

    int delete(ClueActivityRelation clueActivityRelation);
}
