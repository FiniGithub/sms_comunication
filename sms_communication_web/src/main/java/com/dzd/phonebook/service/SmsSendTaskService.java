package com.dzd.phonebook.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.phonebook.dao.SmsSendTaskDao;
import com.dzd.phonebook.entity.SmsSendTask;
import com.dzd.phonebook.entity.SmsSendTaskPhone;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsUserMoneyRunning;

@Service("SmsSendTaskService")
public class SmsSendTaskService<T> {
	@Autowired
	private SmsSendTaskDao<T> dao;

	/**
	 * 统计今日消费
	 * 
	 * @param userId
	 * @return
	 */
	public String queryTodaySendCount(Integer userId) {
		SmsUserMoneyRunning moneyRunning = dao.queryTodaySendCount(userId);
		if (moneyRunning != null) {
			Integer consumeNumber = moneyRunning.getConsumeNumber();
			String todayNumStr = consumeNumber.toString();
			todayNumStr = todayNumStr.substring(1, todayNumStr.length());
			return todayNumStr;
		}
		return "0";

	}
	
	/**
	 * 查询用户是否有消费记录
	 * @param userId
	 * @return
	 */
	public List<com.dzd.phonebook.util.SmsSendTask> queryTaskByUid(Integer userId){
		return dao.queryTaskByUid(userId);
	}
	

	/**
	 * 统计发送量
	 * @param userId
	 * @return
	 */
	public SmsSendTask queryTaskStatistical(DzdPageParam dzdPageParam) {
		return dao.queryTaskStatistical(dzdPageParam);
	}

	
	public void addTaskStat(SmsSendTask smsSendTask){
		dao.addTaskStat(smsSendTask);
	}
	
	
 
	
}
