package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SysConsultationFeedback;
import com.dzd.phonebook.entity.SysLog;
import com.dzd.phonebook.entity.SysLoginLog;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * Created by wangran on 2017/6/02.
 */

/**
 * 登录日志
 * @param <T>
 */
public interface SysConsultationFeedbackDao<T> extends BaseDao<T> {
    /**
     * 咨询与反馈信息列表
     * @param dzdPageParam
     * @return
     */
    Page<SysConsultationFeedback> querySysListPage(DzdPageParam dzdPageParam);

    /**
     * 新增咨询与反馈信息
     * @param sysConsultationFeedback
     */
    public void saveData(SysConsultationFeedback sysConsultationFeedback);

    /**
     * 删除
     * @param list
     */
    public void delete(List list);

}
