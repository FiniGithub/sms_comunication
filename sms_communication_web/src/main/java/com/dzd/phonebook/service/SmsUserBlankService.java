package com.dzd.phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.phonebook.dao.SmsUserBlankDao;
import com.dzd.phonebook.entity.SmsUserBlank;

import java.util.List;

@Service("SmsUserBlankService")
public class SmsUserBlankService {
	@Autowired
	private SmsUserBlankDao dao;

	/**
	 * 查询用户剩余短信条数
	 * 
	 * @param uid
	 * @return
	 */
	public Integer querySurplusNumByUid(Integer uid) {
		Integer surplusNum = dao.querySurplusNumByUid(uid);
		if (surplusNum == null) {
			surplusNum = 0;
		}
		return surplusNum;
	}
	
	public SmsUserBlank queryUserBlank(Integer uid){
		return dao.queryUserBlank(uid);
	}
	public void updateUserBlank(SmsUserBlank smsUserBlank){
		dao.updateUserBlank(smsUserBlank);
	}

	/**
	 * 新增用户短信条数
	 */
	public void addUserBlank(SmsUserBlank blank) {
		dao.addUserBlank(blank);
	}


	/**
	 * 删除
	 */
	public void deleteSmsUserBlank(List list){
		dao.deleteSmsUserBlank(list);
	}
}
