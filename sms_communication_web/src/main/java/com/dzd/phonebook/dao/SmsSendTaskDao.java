package com.dzd.phonebook.dao;

import java.util.List;

import com.dzd.phonebook.entity.SmsSendTask;
import com.dzd.phonebook.entity.SmsSendTaskPhone;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsUserMoneyRunning;

public interface SmsSendTaskDao<T> {

	/**
	 * 统计今日消费
	 * 
	 * @param userId
	 * @return
	 */
	public SmsUserMoneyRunning queryTodaySendCount(Integer userId);

	/**
	 * 统计消费量
	 *
	 * @return
	 */
	public SmsSendTask queryTaskStatistical(DzdPageParam dzdPageParam);

	/**
	 * 查询用户是否有消费记录
	 * 
	 * @param userId
	 * @return
	 */
	public List<com.dzd.phonebook.util.SmsSendTask> queryTaskByUid(Integer userId);

	public void addTaskStat(SmsSendTask smsSendTask);

 
}
