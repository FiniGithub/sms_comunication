package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsUser;
import com.github.pagehelper.Page;

import java.util.List;

//短信账户
public interface SmsAccountDao<T> extends BaseDao<T>  {

    /**
     * 短信账户列表
     * @param dzdPageParam
     * @return
     */
    Page<SmsUser> querySmsAccountUserListPage(DzdPageParam dzdPageParam);


    /**
     * 根据用户sys_user_id 查询下面所有归属bId (bId 下面的子归属 多层)
     * @param list
     * @return
     */
    public List<Integer> queryBids(List list);
}