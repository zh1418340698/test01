package com.zenghao.crm.workbench.service;


import com.zenghao.crm.vo.PaginationVo;
import com.zenghao.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity);

    PaginationVo<Activity> pageList(Map<String,Object> map);

    Boolean delete(String[] id);

    Activity edit(String id);

    Boolean updateActivity(Activity activity);
}
