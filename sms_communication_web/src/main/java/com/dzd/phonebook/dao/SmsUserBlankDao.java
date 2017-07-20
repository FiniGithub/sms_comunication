package com.dzd.phonebook.dao;

import com.dzd.phonebook.entity.SmsUserBlank;

import java.util.List;

public interface SmsUserBlankDao {
	
	/**
	 * 查询用户剩余短信条数
	 * @param uid
	 * @return
	 */
	public Integer querySurplusNumByUid(Integer uid);
	public SmsUserBlank queryUserBlank(Integer uid);
	public void updateUserBlank(SmsUserBlank smsUserBlank);

	/**
	 * 新增用户短信条数
	 */
	public void addUserBlank(SmsUserBlank blank);

	/**
	 * 删除
	 * @param sms_user_id
	 */
	public void deleteSmsUserBlank(List list);
}
