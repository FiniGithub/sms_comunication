package com.dzd.phonebook.service;

import com.dzd.base.dao.BaseDao;
import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SmsHomePageDao;
import com.dzd.phonebook.entity.SmsHomePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页样式服务类
 * Created by CHENCHAO on 2017/5/31.
 */
@Service("SmsHomePageService")
public class SmsHomePageService<T> extends BaseService<T> {

    @Autowired
    private SmsHomePageDao<T> smsHomePageDao;

    public SmsHomePageDao<T> getDao() {
        return smsHomePageDao;
    }

    public SmsHomePage querySmsHome() {
        return getDao().querySmsHome();
    }

}
