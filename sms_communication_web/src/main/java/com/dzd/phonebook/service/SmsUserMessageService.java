package com.dzd.phonebook.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SmsUserMessageDao;
import com.dzd.phonebook.entity.SmsUserMessage;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;

/**
 * 代理商 - 接收推送消息服务类
 * 
 * @author CHENCHAO
 * @date 2017-04-12 10:19:00
 *
 * @param <T>
 */
@Service("SmsUserMessageService")
public class SmsUserMessageService<T> extends BaseService<T> {
	@Autowired
	private SmsUserMessageDao<T> smsUserMessageDao;

	@Override
	public SmsUserMessageDao<T> getDao() {
		return smsUserMessageDao;
	}

	/**
	 * 查询代理商消息数量
	 * 
	 * @param smsUserId
	 * @return
	 */
	public Integer queryMessageCount(String smsUserEmail,Integer smsUserTypeId) {
		if(smsUserTypeId == null){
			return 0;
		}
		Integer count = smsUserMessageDao.queryMessageCount(smsUserEmail,smsUserTypeId);
		if (count == null) {
			count = 0;
		}
		return count;
	}

	/**
	 * 查询首页消息
	 * 
	 * @param smsUserEmail
	 * @param smsUserTypeId
	 * @return
	 */
	public SmsUserMessage queryMsgForIndex(String smsUserEmail,Integer smsUserTypeId){
		return smsUserMessageDao.queryMsgForIndex(smsUserEmail, smsUserTypeId);
	}
	
	/**
	 * 查询代理商的消息
	 * 
	 * @param smsUserId
	 * @return
	 */
	public Page<SmsUserMessage> queryMessagePage(DzdPageParam dzdPageParam) {
		return smsUserMessageDao.queryMessagePage(dzdPageParam);
	}
}
