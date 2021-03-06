package com.zenghao.crm.workbench.service.impl;

import com.zenghao.crm.vo.PaginationVo;
import com.zenghao.crm.workbench.dao.ActivityDao;
import com.zenghao.crm.workbench.dao.ActivityRemarkDao;
import com.zenghao.crm.workbench.domain.Activity;
import com.zenghao.crm.workbench.domain.ActivityRemark;
import com.zenghao.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private ActivityRemarkDao activityRemarkDao;


    @Override
    public boolean save(Activity activity) {

        int nums = activityDao.save(activity);
        if ( nums == 1){
            return true;
        }
        return false;
    }

    @Override
    public PaginationVo<Activity> pageList(Map<String,Object> map) {

        //取得total
        int total = activityDao.getTotalByCondition(map);

        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        //将total和dataList封装到vo中返回
        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //返回vo
        return vo;
    }

    @Override
    public Boolean delete(String[] id) {

        Boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(id);

        //删除备注，返回受影响的行数（实际删除数量）
        int count2 = activityRemarkDao.deleteByAids(id);

        if ( count1 != count2){
            flag = false;
        }
        //输出市场活动
        int count3 = activityDao.delete(id);
        if ( count3 != id.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public Activity edit(String id) {

        return activityDao.edit(id);
    }

    @Override
    public Boolean updateActivity(Activity activity) {
        Boolean flag = true;

        int count = activityDao.update(activity);
        if ( count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Activity detail(String id) {

        Activity a = activityDao.detail(id);

        return a;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> list = activityRemarkDao.getRemarkListByAid(activityId);
        return list;
    }

    @Override
    public Boolean deleteRemark(String id) {
        Boolean flag = true;

        int count = activityRemarkDao.deleteRemark(id);
        if ( count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Boolean saveRemark(ActivityRemark ar) {
        Boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);
        if ( count != 1 ){
            flag = false;
        }
        return flag;
    }

    @Override
    public ActivityRemark getRemarkById(String id) {
        ActivityRemark ar = activityRemarkDao.getRemarkById(id);
        return ar;
    }

    @Override
    public Boolean updateRemark(ActivityRemark ar) {
        Boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);
        if ( count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> list = activityDao.getActivityListByClueId(clueId);
        return list;
    }

    @Override
    public List<Activity> getActivityListByNameAndNoByClueId(String aname, String clueId) {

        List<Activity> list = activityDao.getActivityListByNameAndNoByClueId(aname,clueId);
        return list;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {

        List<Activity> list = activityDao.getActivityListByName(aname);

        return list;
    }
}
