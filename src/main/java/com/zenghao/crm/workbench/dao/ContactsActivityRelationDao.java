package com.zenghao.crm.workbench.dao;

import com.zenghao.crm.workbench.domain.ContactsActivityRelation;
import org.apache.ibatis.annotations.Param;

public interface ContactsActivityRelationDao {

    int save(ContactsActivityRelation contactsActivityRelation);
}
