package com.zenghao.crm.workbench.service;


import com.zenghao.crm.vo.PaginationVo;
import com.zenghao.crm.workbench.domain.Activity;
import com.zenghao.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity);

    PaginationVo<Activity> pageList(Map<String,Object> map);

    Boolean delete(String[] id);

    Activity edit(String id);

    Boolean updateActivity(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    Boolean deleteRemark(String id);

    Boolean saveRemark(ActivityRemark ar);

    ActivityRemark getRemarkById(String id);

    Boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNoByClueId(String aname, String clueId);

    List<Activity> getActivityListByName(String aname);
}
