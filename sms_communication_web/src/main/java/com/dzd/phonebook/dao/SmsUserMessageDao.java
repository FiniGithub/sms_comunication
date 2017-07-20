package com.dzd.phonebook.dao;

import org.apache.ibatis.annotations.Param;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SmsUserMessage;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;

/**
 * 代理商用戶 - 接收推送消息接口
 * 
 * @author CHENCHAO
 * @date 2017-04-12 10:14:00
 *
 */
public interface SmsUserMessageDao<T> extends BaseDao<T> {

	/**
	 * 查询代理商消息数量
	 * 
	 * @param smsUserId
	 * @return
	 */
	public Integer queryMessageCount(@Param("smsUserEmail") String smsUserEmail,
			@Param("smsUserTypeId") Integer smsUserTypeId);

	/**
	 * 查询首页消息
	 * 
	 * @param smsUserEmail
	 * @param smsUserTypeId
	 * @return
	 */
	public SmsUserMessage queryMsgForIndex(@Param("smsUserEmail") String smsUserEmail,
			@Param("smsUserTypeId") Integer smsUserTypeId);

	/**
	 * 查询代理商的消息
	 * 
	 * @param smsUserId
	 * @return
	 */
	public Page<SmsUserMessage> queryMessagePage(DzdPageParam dzdPageParam);

}
