package com.zenghao.crm.workbench.dao;

import com.zenghao.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);
}
