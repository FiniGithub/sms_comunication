package com.dzd.phonebook.service;


import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SmsApplyAccountDao;
import com.dzd.phonebook.util.*;
import com.github.pagehelper.Page;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Description:申请账户列表
 * @author:wangran
 * @time:2017年5月22日 上午13:37:06
 */
@Service("smsApplyAccountService")
public class SmsApplyAccountService<T> extends BaseService<T> {
    private final static Logger log = Logger.getLogger(SmsApplyAccountService.class);


    @Autowired
    private SmsApplyAccountDao<T> mapper;

    public SmsApplyAccountDao<T> getDao() {
        return mapper;
    }
    
    /**
     * 
     * @Description:申请账户列表
     * @author:wangran
     * @time:2017年5月22日 下午13:45:48
     */
    public Page<SmsUser> queryApplyAccountlistPage(DzdPageParam dzdPageParam) {
        return getDao().queryApplyAccountlistPage(dzdPageParam);
    }

    /**
     * 查询是否存在相同的用户名称
     * @param smsUser
     * @author:wangran
     * @time:2017年5月22日 下午13:45:48
     */
    public Integer querySmsUserName(SmsUser smsUser){
        return getDao().querySmsUserName(smsUser);
    }

    /**
     * @Description:修改申请账户信息
     * @author:wangran
     * @time:2017年5月22日 下午13:45:48
     */
    public void updateApplyAccount(SmsUser smsUser){
    	getDao().updateApplyAccount(smsUser);
    }

    /**
     * @Description:根据id删除申请账户信息
     * @author:wangran
     * @time:2017年5月22日 下午13:45:48
     */
    public void deleteApplyAccount(List ids){
    	getDao().deleteApplyAccount(ids);
    }

    /**
     * 新增申请账户信息
     * @param smsUser
     * @author:wangran
     * @time:2017年5月22日 下午13:45:48
     */
    public void saveSmsUserApplyAccount(SmsUser smsUser){
        getDao().saveSmsUserApplyAccount(smsUser);
    }

    /**
     * 根据id查询申请账户信息
     * @param object
     * @author:wangran
     * @time:2017年5月23日 下午19:18:48
     */
    public SmsUser queryApplyAccountById(Object object){
      return getDao().queryApplyAccountById(object);
    }

}
