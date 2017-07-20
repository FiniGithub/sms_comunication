package com.dzd.phonebook.dao;

import java.util.List;
import java.util.Map;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsAisle;
import com.dzd.phonebook.util.SmsSendLog;
import com.dzd.phonebook.util.SmsSendPackageLog;
import com.dzd.phonebook.util.SmsSendTask;
import com.github.pagehelper.Page;


/**
 * 代理接口
 *
 * @author chenchao
 * @date 2016-6-24
 */
public interface MsmSendDao<T> extends BaseDao<T> {

	Page<SmsSendTask> queryMsmSendListPage(DzdPageParam dzdPageParam);
	
	public Page<SmsSendPackageLog> queryMsmSendPackagePage(DzdPageParam dzdPageParam);
	
	 public SmsSendTask queryMsmSendCount(DzdPageParam dzdPageParam);
	
	public List<SmsSendLog> queryMsmSendPhoneByid(Integer msmSendId);
	
	Page<SmsSendLog> queryMsmSendDetailsListPage(DzdPageParam dzdPageParam);
	
	public SmsSendLog querySedNum(SmsSendLog smsSendLog);
	
	public Integer querySmsToAudit(Map<String, Object> sortMap);
	
	public List<SmsAisle> querySmsAisle(Integer tid);
	
	public void updateSendResendState(Integer sstid);
	
	public Integer querySendResendState(Integer sstid);
	
	public String querySmsSendPackageLogByid(Integer id);
	
	public List<SmsAisle> querySmsAisleGroup(Integer tid);
	
	Page<SmsSendLog> queryStatisticalUserInfoPage(DzdPageParam dzdPageParam);
	
	List<SmsSendLog> queryStatisticalSucceedNum(Map<String, Object> param);
	List<SmsSendLog> queryStatisticalFailureNum(Map<String, Object> param);

	List<String> queryAisleNames();

	List<String> querynickNames();

	Page<SmsSendLog> querySmsTimedTaskPage(DzdPageParam dzdPageParam);

	void updateTimedTask(SmsSendTask smsSendTask);

	SmsSendLog queryTimedTaskForModify(Map<String, Object> param);

	void deleteTimedTask(Map<String, Object> param);

	List<String> querySmsTaskPhone(String taskId);

	int queryStatisticalSendNum(Map<String, Object> param);
	
}