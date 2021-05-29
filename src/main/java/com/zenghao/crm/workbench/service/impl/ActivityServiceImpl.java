package com.zenghao.crm.workbench.service.impl;

import com.zenghao.crm.workbench.dao.ActivityDao;
import com.zenghao.crm.workbench.domain.Activity;
import com.zenghao.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityDao activityDao;

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    @Override
    public boolean save(Activity activity) {

        int nums = activityDao.save(activity);
        if ( nums == 1){
            return true;
        }
        return false;
    }
}
