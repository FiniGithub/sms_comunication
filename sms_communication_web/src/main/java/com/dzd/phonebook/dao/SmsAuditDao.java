package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsSendTask;
import com.github.pagehelper.Page;

public interface SmsAuditDao<T> extends BaseDao<T>  {

	public Page<SmsSendTask> querySmsAuditListPage(DzdPageParam dzdPageParam);
	
	public void updateSmsAudit(SmsSendTask smsSendTask);
	
	public SmsSendTask querySmsSeng(Integer sstid);

}
