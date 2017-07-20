package com.dzd.phonebook.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.OperatorSectionNo;
import com.dzd.phonebook.entity.SmsFilterNumberRecord;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;

/**
 * Created by wangran on 2017/5/17.
 */

/**
 * 电话号码过滤接口
 * @param <T>
 */
public interface SmsFilterNumberRecordDao<T> extends BaseDao<T> {

    /**
     * 过滤记录列表
     * @param dzdPageParam
     * @return
     */
    Page<SmsFilterNumberRecord> querySmsFilterNumberRecordPage(DzdPageParam dzdPageParam);


    /**
     * 添加手机过滤记录
     * @param smsFilterNumberRecord
     */
    public void saveFilterNumberRecord(SmsFilterNumberRecord smsFilterNumberRecord);


	void updateOperatorSectionNo(@Param("type") String type, @Param("sectionNo") String sectionNo);


	List<OperatorSectionNo> queryOperatorSectionNo();

}
