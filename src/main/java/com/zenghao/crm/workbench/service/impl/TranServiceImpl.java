package com.zenghao.crm.workbench.service.impl;

import com.zenghao.crm.utils.DateTimeUtil;
import com.zenghao.crm.utils.UUIDUtil;
import com.zenghao.crm.workbench.dao.CustomerDao;
import com.zenghao.crm.workbench.dao.TranDao;
import com.zenghao.crm.workbench.dao.TranHistoryDao;
import com.zenghao.crm.workbench.domain.Customer;
import com.zenghao.crm.workbench.domain.Tran;
import com.zenghao.crm.workbench.domain.TranHistory;
import com.zenghao.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private TranDao tranDao;
    @Autowired
    private TranHistoryDao tranHistoryDao;

    @Autowired
    private CustomerDao customerDao;

    @Override
    public Boolean save(Tran tran, String customerName,String createBy) {
        Boolean flag = true;

        //根据客户名字判断是否有该客户
        Customer customer = customerDao.getCustomerByName(customerName);
        if (customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateBy(createBy);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setOwner(tran.getOwner());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setName(customerName);
            customer.setDescription(tran.getDescription());
            customer.setContactSummary(tran.getContactSummary());
            //添加客户
            int count = customerDao.save(customer);
            if (count != 1){
                flag  = false;
            }
        }
        String customerId = customer.getId();
        tran.setId(UUIDUtil.getUUID());
        tran.setCreateBy(createBy);
        tran.setCreateTime(DateTimeUtil.getSysTime());
        tran.setCustomerId(customerId);
        //添加交易
        int count = tranDao.save(tran);
        if ( count != 1){
            flag = false;
        }

        TranHistory tranHistory = new TranHistory();
        tranHistory.setCreateBy(createBy);
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());
        tranHistory.setId(UUIDUtil.getUUID());

        //添加交易历史
        int count1 = tranHistoryDao.save(tranHistory);
        if (count1 != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> pageList(Integer skipCount, Integer pageSize) {
        //获取总记录条数
        int total = tranDao.getTotal();
        //获取交易列表
        List<Tran> tranList = tranDao.getTranList(skipCount,pageSize);

        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("tranList",tranList);

        return map;
    }

    @Override
    public Tran getTranById(String id) {
        Tran tran = tranDao.getTranById(id);

        return tran;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(tranId);
        return thList;
    }

    @Override
    public Boolean changeStage(Tran tran) {
        Boolean flag = true;

        int count = tranDao.changeStage(tran);
        if (count != 1){
            flag = false;
        }

        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(tran.getEditBy());
        tranHistory.setPossibility(tran.getPossibility());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setCreateTime(tran.getEditTime());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setMoney(tran.getMoney());
        int count2 = tranHistoryDao.save(tranHistory);
        if (count2 != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String,Object> getCharts() {
        Map<String,Object> map = new HashMap<>();

        int total = tranDao.getTotal();
        map.put("total",total);

        List<Map<String,String>> dataList = tranDao.getCharts();
        map.put("dataList",dataList);

        return map;
    }
}
