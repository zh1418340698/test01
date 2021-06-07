package com.zenghao.crm.workbench.dao;

import com.zenghao.crm.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);
    List<Activity> getActivityListByCondition(Map<String,Object> map);
    int getTotalByCondition(Map<String,Object> map);

    int delete(String[] id);

    Activity edit(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNoByClueId(@Param("aname") String aname,
                                                      @Param("clueId") String clueId);

    List<Activity> getActivityListByName(String aname);
}
