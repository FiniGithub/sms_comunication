package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SysLog;
import com.dzd.phonebook.entity.SysLoginLog;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;

/**
 * Created by wangran on 2017/6/02.
 */

/**
 * 登录日志
 * @param <T>
 */
public interface SysLoginLogDao<T> extends BaseDao<T> {

    /**
     * 登录日志列表
     * @param dzdPageParam
     * @return
     */
    Page<SysLog> querySysLoginLogPage(DzdPageParam dzdPageParam);

    /**
     * 新增登录日志
     * @param sysLoginLog
     */
    public void saveLoginLog(SysLoginLog sysLoginLog);

}
