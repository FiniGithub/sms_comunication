package com.dzd.phonebook.service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SysLogDao;
import com.dzd.phonebook.entity.SysLog;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dzd-technology01 on 2017/6/02.
 */
@Service("sysLogService")
public class SysLogService<T> extends BaseService<T> {

    @Autowired
    private SysLogDao<T> mapper;


    public SysLogDao<T> getDao(){
        return mapper;
    }

    /**
     * @Description:查询操作日志列表
     * @author:wangran
     * @time:2017年06月02日 上午10:00:34
     * @param dzdPageParam
     * @return
     */
    public Page<SysLog> querySysLogPage(DzdPageParam dzdPageParam){
        return mapper.querySysLogPage(dzdPageParam);
    }
}
