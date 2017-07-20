package com.dzd.phonebook.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;
import com.dzd.sms.api.controller.SmsHttpInterfaceV4;

/** * 
@author  作者 
E-mail: * 
@date 创建时间：2017年5月4日 上午10:07:32 * 
@version 1.0 * 
@parameter  * 
@since  * 
@return  */
public class AccessTimeControlUtil
{
	static Logger logger = Logger.getLogger(AccessTimeControlUtil.class);
	
	private static RedisClient redisClient = RedisManager.I.getRedisClient();

	public static String ACCESS_TIME = "ACCESS_TIME";
	public static String PULL_STATUS_TIME = "PULL_STATUS_TIME";
	public static String GET_REPLY_TIME = "GET_REPLY_TIME";
	public static String GET_BALANCE_TIME = "GET_BALANCE_TIME";

	@SuppressWarnings("unchecked")
	public static boolean fitlerAccessTime(String key, String uid, boolean isLogin)
	{/*
		logger.info("开始进行访问控制限制：fitlerAccessTime··· ");
		List<Long> accessTimes = new ArrayList<Long>();
		long currentTimeMillis = System.currentTimeMillis();// 当前时间
		boolean isTimeOut = true;
		
		if ( redisClient.hexists(key, uid) )
		{
			accessTimes = (List<Long>) redisClient.hgetObject(key, uid);// 获取当前用户登录时间集合
			long accessAcrossTime = currentTimeMillis - accessTimes.get(0);
			if ( accessAcrossTime > 1000 )// 控制时间长度为一秒钟以内
			{
				accessTimes = new ArrayList<Long>();
				isTimeOut = false;
			}
			if ( accessTimes.size() > 10 )// 控制一秒钟以内访问进程数10个
			{
				return true;
			}
		}
		
		if ( isLogin )
		{
			accessTimes.add(currentTimeMillis);
		} else if ( isTimeOut )
		{
			accessTimes.remove(0);// 一秒钟之内访问，接口访问结束移除最初进程，保证只有五个以内进程运行
		}
		redisClient.hsetObject(key, uid, accessTimes);

		return false;
	*/
		return false;
		}
}
