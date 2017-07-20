package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.OperatorSectionNo;
import com.dzd.phonebook.entity.SmsFilterNumberRecord;
import com.dzd.phonebook.entity.SysLog;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wangran on 2017/6/02.
 */

/**
 * 操作日志
 * @param <T>
 */
public interface SysLogDao<T> extends BaseDao<T> {

    /**
     * 操作日志列表
     * @param dzdPageParam
     * @return
     */
    Page<SysLog> querySysLogPage(DzdPageParam dzdPageParam);

}
