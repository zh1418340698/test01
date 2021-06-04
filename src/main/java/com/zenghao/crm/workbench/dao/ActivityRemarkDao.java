package com.zenghao.crm.workbench.dao;

import com.zenghao.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByAids(String[] id);

    int deleteByAids(String[] id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark ar);

    ActivityRemark getRemarkById(String id);

    int updateRemark(ActivityRemark ar);
}
