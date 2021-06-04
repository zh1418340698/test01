package com.zenghao.crm.settings.dao;

import com.zenghao.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
