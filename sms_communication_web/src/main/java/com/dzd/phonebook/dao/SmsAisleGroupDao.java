package com.dzd.phonebook.dao;


import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.util.SmsAisleGroup;

import java.util.List;

/**
 * 通道组接口
 *
 * @param <T>
 * @author CHENCHAO
 * @date 2017-04-28 13:54:00
 */
public interface SmsAisleGroupDao<T> extends BaseDao<T> {

    /**
     * 查询通道组信息
     *
     * @param aisleGroupId
     * @return
     */
    SmsAisleGroup querySmsAisleGroupById(Integer aisleGroupId);





}
