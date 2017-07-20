package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsUser;
import com.github.pagehelper.Page;

import java.util.List;

//账户管理
public interface SmsManageMentAccountDao<T> extends BaseDao<T>  {

    /**
     * 账户管理列表
     * @param dzdPageParam
     * @return
     */
    Page<SmsUser> querySmsUserManageMentListPage(DzdPageParam dzdPageParam);

    /**
     * 添加账户信息
     * @param smsUser
     */
    public void saveSmsUserManagement(SmsUser smsUser);




    /**
     * 查询账户信息
     * @param object
     * @return
     */
    public SmsUser querySmsManageMentUser(Object object);

    /**
     * 批量删除账户信息
     * @param list
     */
    public void deleteByIds(List list);

	
}