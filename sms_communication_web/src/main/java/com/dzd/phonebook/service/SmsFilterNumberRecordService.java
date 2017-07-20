package com.dzd.phonebook.service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SmsFilterNumberRecordDao;
import com.dzd.phonebook.entity.OperatorSectionNo;
import com.dzd.phonebook.entity.SmsFilterNumberRecord;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dzd-technology01 on 2017/5/17.
 */
@Service("smsFilterNumberRecordService")
public class SmsFilterNumberRecordService<T> extends BaseService<T> {

    @Autowired
    private SmsFilterNumberRecordDao<T> mapper;


    public SmsFilterNumberRecordDao<T> getDao(){
        return mapper;
    }

    /**
     * @Description:查询过滤记录列表
     * @author:wangran
     * @time:2017年05月17日 下午2:01:34
     * @param dzdPageParam
     * @return
     */
    public Page<SmsFilterNumberRecord> querySmsFilterList(DzdPageParam dzdPageParam){
        return mapper.querySmsFilterNumberRecordPage(dzdPageParam);
    }

    /**
     * @Description:添加手机过滤记录
     * @author:wangran
     * @time:2017年05月17日 下午2:01:34
     * @param smsFilterNumberRecord
     * @return
     */
    public void saveFilterNumberRecord(SmsFilterNumberRecord smsFilterNumberRecord){
        getDao().saveFilterNumberRecord(smsFilterNumberRecord);
    }

	public void updateOperatorSectionNo(String type, String sectionNo)
	{
		getDao().updateOperatorSectionNo(type, sectionNo);
	}

	public List<OperatorSectionNo> queryOperatorSectionNo()
	{
		return getDao().queryOperatorSectionNo();
	}
}
