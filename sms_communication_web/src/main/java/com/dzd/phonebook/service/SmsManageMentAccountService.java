package com.dzd.phonebook.service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SmsManageMentAccountDao;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsUser;
import com.github.pagehelper.Page;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:账户管理列表
 * @author:wangran
 * @time:2017年5月22日 上午13:37:06
 */
@Service("smsManageMentAccountService")
public class SmsManageMentAccountService<T> extends BaseService<T> {
    private final static Logger log = Logger.getLogger(SmsManageMentAccountService.class);


    @Autowired
    private SmsManageMentAccountDao<T> mapper;

    public SmsManageMentAccountDao<T> getDao() {
        return mapper;
    }

    /**
     * @Description:账户管理列表
     * @author:ougy
     * @time:2016年12月31日 下午2:18:48
     */
    public Page<SmsUser> querySmsUserManageMentListPage(DzdPageParam dzdPageParam) {
        return getDao().querySmsUserManageMentListPage(dzdPageParam);
    }

    /**
     * 查询账户信息
     *
     * @param id
     * @return
     */
    public SmsUser querySmsManageMentUser(Object id) {
        return getDao().querySmsManageMentUser(id);
    }

    /**
     * 批量删除账户信息
     *
     * @param list
     */
    public void deleteByIds(List list) {
        getDao().deleteByIds(list);
    }

    /**
     * @Description:添加账户信息
     * @author:oygy
     * @time:2017年05月19日 下午16:11:16
     */
    public void saveSmsUserManagement(SmsUser smsUser) {
        getDao().saveSmsUserManagement(smsUser);
    }


}
