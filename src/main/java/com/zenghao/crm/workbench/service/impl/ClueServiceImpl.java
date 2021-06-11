package com.zenghao.crm.workbench.service.impl;

import com.zenghao.crm.settings.domain.User;
import com.zenghao.crm.utils.DateTimeUtil;
import com.zenghao.crm.utils.UUIDUtil;
import com.zenghao.crm.workbench.dao.*;
import com.zenghao.crm.workbench.domain.*;
import com.zenghao.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {

    //线索相关表
    @Autowired
    private ClueDao clueDao;
    @Autowired
    private ClueActivityRelationDao clueActivityRelationDao;
    @Autowired
    private ClueRemarkDao clueRemarkDao;

    //客户相关表
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CustomerRemarkDao customerRemarkDao;

    //联系人相关表
    @Autowired
    private ContactsDao contactsDao;
    @Autowired
    private ContactsRemarkDao contactsRemarkDao;
    @Autowired
    private ContactsActivityRelationDao contactsActivityRelationDao;

    //交易相关表
    @Autowired
    private TranDao tranDao;
    @Autowired
    private TranHistoryDao tranHistoryDao;

    @Override
    public Boolean save(Clue clue) {

        Boolean flag = true;

        int count = clueDao.save(clue);
        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public Boolean unbund(String id) {
        return clueActivityRelationDao.unbund(id)==1?true:false;
    }

    @Override
    public Boolean bund(String cid,String[] aid) {

        Boolean flag = true;

        for (int i = 0;i<aid.length;i++){
            ClueActivityRelation car = new ClueActivityRelation();
            String id = UUIDUtil.getUUID();
            car.setId(id);
            car.setClueId(cid);
            car.setActivityId(aid[i]);
            int count = clueActivityRelationDao.bund(car);

            if ( count != 1){
                flag = false;
            }
        }

        return flag;
    }

    @Override
    public Boolean convert(String clueId, Tran tran, String createBy) {

        String createTime = DateTimeUtil.getSysTime();

        boolean flag = true;

        //1、通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getClueById(clueId);

        //2、通过线索对象提取信息，当客户不存在时，新建客户（根据公司的名称精确匹配，判断客户是否存在）
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
        //如果为null，说明以前没有这个客户,需要新建一个
        if (customer == null){

            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setContactSummary(clue.getContactSummary());
            //添加客户
            int count = customerDao.save(customer);
            if ( count != 1){
                flag = false;
            }

        }

        //3、通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());

        int count = contactsDao.save(contacts);
        if ( count != 1){
            flag = false;
        }

        //4、将线索的备注转换成客户备注、联系人备注
        //查询出与该线索关联的备注信息列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);

        //取出每一条线索的备注
        for (ClueRemark clueRemark: clueRemarkList) {

            //取出备注信息（主要转换到客户备注和联系人备注的就是这个备注信息）
            String noteContent = clueRemark.getNoteContent();

            //创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setCreateTime(createTime);
            customerRemark.setCreateBy(createBy);
            customerRemark.setEditFlag("0");

            int count1 = customerRemarkDao.save(customerRemark);
            if ( count1 != 1){
                flag = false;
            }

            //创建联系人备注对象，添加联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setEditFlag("0");

            int count2 = contactsRemarkDao.save(contactsRemark);
            if ( count2 != 1){
                flag = false;
            }
        }

        //5、“线索和市场活动”的关系转换到“联系人和市场活动”的关系
        //查询出与该条线索关联的市场活动，查询与市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getActivityIdListByClueId(clueId);

        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList) {

            //创建联系人和市场活动关系的对象
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
            //添加联系人与市场活动的关联关系
            int count3 = contactsActivityRelationDao.save(contactsActivityRelation);
            if ( count3 != 1){
                flag = false;
            }
        }

        //6、如果有创建交易的需求，创建一条交易
        if ( tran != null){

            /*
                tran对象在controller中封装好的信息如下
                    id,money,name,expectedDate,stage,activityId,createBy,createTime
                 接下来可以通过第一步生成的clue对象，取出一些信息，继续完善tran对象的封装

             */
            tran.setSource(clue.getSource());
            tran.setOwner(clue.getOwner());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setContactSummary(clue.getContactSummary());
            tran.setContactsId(contacts.getId());
            tran.setCustomerId(customer.getId());
            //添加交易
            int count4 = tranDao.save(tran);
            if ( count4 != 1){
                flag = false;
            }

            //7、如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setTranId(tran.getId());
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setCreateTime(createTime);
            tranHistory.setCreateBy(createBy);
            //添加交易历史对象
            int count5 = tranHistoryDao.save(tranHistory);
            if ( count5 != 1){
                flag = false;
            }
        }

        //8、删除线索备注
        for( ClueRemark clueRemark:clueRemarkList){

            int count6 = clueRemarkDao.delete(clueRemark);
            if ( count6 != 1){
                flag = false;
            }
        }

        //9、删除线索和市场活动关联的关系
        for ( ClueActivityRelation clueActivityRelation:clueActivityRelationList){
            int count7 = clueActivityRelationDao.delete(clueActivityRelation);
            if ( count7 != 1){
                flag = false;
            }
        }

        //10、删除线索
        int count8 = clueDao.delete(clueId);
        if ( count8 != 1){
            flag = false;
        }
        





        return flag;
    }

    @Override
    public Map<String, Object> pageList(Integer skipCount, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        int total = clueDao.getCount();
        List<Clue> clueList = clueDao.getClueList(skipCount,pageSize);

        map.put("total",total);
        map.put("clueList",clueList);

        return map;
    }

    @Override
    public boolean delete2(String[] id) {
        boolean flag = true;

        int count = clueDao.delete2(id);
        if ( count != id.length){
            flag = false;
        }

        return flag;
    }


}
