package com.dzd.phonebook.service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SysLogDao;
import com.dzd.phonebook.dao.SysLoginLogDao;
import com.dzd.phonebook.entity.SysLog;
import com.dzd.phonebook.entity.SysLoginLog;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wangran on 2017/6/02
 */
@Service("sysLoginLogService")
public class SysLoginLogService<T> extends BaseService<T> {

    @Autowired
    private SysLoginLogDao<T> mapper;


    public SysLoginLogDao<T> getDao(){
        return mapper;
    }

    /**
     * @Description:查询登录日志列表
     * @author:wangran
     * @time:2017年06月02日 上午11:00:00
     * @param dzdPageParam
     * @return
     */
    public Page<SysLog> querySysLoginLogPage(DzdPageParam dzdPageParam){
        return mapper.querySysLoginLogPage(dzdPageParam);
    }

    /**
     * 新增登录日志
     * @param sysLoginLog
     */
    @Transactional
    public void saveLoginLog(SysLoginLog sysLoginLog){
        mapper.saveLoginLog(sysLoginLog);
    }
}
