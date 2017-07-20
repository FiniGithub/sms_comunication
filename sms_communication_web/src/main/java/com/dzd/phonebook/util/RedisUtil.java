package com.dzd.phonebook.util;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;

/** * 
@author  hz-liang
E-mail: * 
@date 创建时间：2017年3月30日 上午9:35:33 * 
@version 1.0 * 
@parameter  * 
@since  * 
@return  */
public class RedisUtil
{
	// REDIS操作对象
	public static RedisClient redisClient = RedisManager.I.getRedisClient();

	public static void publish(String channel, String jsonStr)
	{
		redisClient.publish(channel, jsonStr);
	}

}
