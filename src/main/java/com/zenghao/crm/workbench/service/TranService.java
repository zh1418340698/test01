package com.zenghao.crm.workbench.service;

import com.zenghao.crm.workbench.domain.Tran;
import com.zenghao.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    Boolean save(Tran tran, String customerName,String createBy);

    Map<String, Object> pageList(Integer skipCount, Integer pageSize);

    Tran getTranById(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    Boolean changeStage(Tran tran);

    Map<String,Object> getCharts();
}
