package com.dzd.phonebook.service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SysConsultationFeedbackDao;
import com.dzd.phonebook.dao.SysLoginLogDao;
import com.dzd.phonebook.entity.SysConsultationFeedback;
import com.dzd.phonebook.entity.SysLog;
import com.dzd.phonebook.entity.SysLoginLog;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wangran on 2017/6/02
 */
@Service("sysConsultationFeedbackService")
public class SysConsultationFeedbackService<T> extends BaseService<T> {

    @Autowired
    private SysConsultationFeedbackDao<T> mapper;


    public SysConsultationFeedbackDao<T> getDao(){
        return mapper;
    }

    /**
     * 咨询与反馈信息列表
     * @param dzdPageParam
     * @return
     */
     public Page<SysConsultationFeedback> querySysListPage(DzdPageParam dzdPageParam){
       return mapper.querySysListPage(dzdPageParam);
    }

    /**
     * 新增咨询与反馈信息
     * @param sysConsultationFeedback
     */
    @Transactional
    public void saveData(SysConsultationFeedback sysConsultationFeedback){
        mapper.saveData(sysConsultationFeedback);
    }

    /**
     * 删除
     * @param list
     */
    @Transactional
    public void delete(List list){
        mapper.delete(list);
    }
}
