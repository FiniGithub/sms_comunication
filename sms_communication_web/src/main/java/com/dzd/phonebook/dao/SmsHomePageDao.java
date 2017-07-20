package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SmsHomePage;

import java.util.List;

/**
 * 首页样式接口
 * Created by CHENCHAO on 2017/5/31.
 */
public interface SmsHomePageDao<T> extends BaseDao<T> {
    public SmsHomePage querySmsHome();

}
