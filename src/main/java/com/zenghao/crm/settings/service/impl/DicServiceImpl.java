package com.zenghao.crm.settings.service.impl;

import com.zenghao.crm.settings.dao.DicTypeDao;
import com.zenghao.crm.settings.dao.DicValueDao;
import com.zenghao.crm.settings.domain.DicType;
import com.zenghao.crm.settings.domain.DicValue;
import com.zenghao.crm.settings.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {

    @Autowired
    private DicTypeDao dicTypeDao;

    @Autowired
    private DicValueDao dicValueDao;

    public void setDicTypeDao(DicTypeDao dicTypeDao) {
        this.dicTypeDao = dicTypeDao;
    }

    public void setDicValueDao(DicValueDao dicValueDao) {
        this.dicValueDao = dicValueDao;
    }

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String,List<DicValue>> map = new HashMap<>();

        //将字典类型表取出
        List<DicType> dtList = dicTypeDao.getTypeList();

        //遍历字典类型
        for ( DicType dt:dtList){
            String code = dt.getCode();

            //根据字典类型取得字典值列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);

            map.put(code,dvList);
        }
        return map;
    }
}
