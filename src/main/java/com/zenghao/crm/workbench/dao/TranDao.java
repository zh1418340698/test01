package com.zenghao.crm.workbench.dao;

import com.zenghao.crm.workbench.domain.Tran;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran tran);

    int getTotal();

    List<Tran> getTranList(@Param("skipCount") Integer skipCount,@Param("pageSize") Integer pageSize);

    Tran getTranById(String id);

    int changeStage(Tran tran);

    List<Map<String, String>> getCharts();
}
