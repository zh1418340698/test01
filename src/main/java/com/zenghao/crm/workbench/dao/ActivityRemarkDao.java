package com.zenghao.crm.workbench.dao;

public interface ActivityRemarkDao {
    int getCountByAids(String[] id);

    int deleteByAids(String[] id);
}
