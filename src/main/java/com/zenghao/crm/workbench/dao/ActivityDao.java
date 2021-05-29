package com.zenghao.crm.workbench.dao;

import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.workbench.domain.Activity;

import java.util.List;

public interface ActivityDao {
    int save(Activity activity);
}
