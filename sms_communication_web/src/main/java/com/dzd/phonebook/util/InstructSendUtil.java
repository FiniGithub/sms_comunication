package com.dzd.phonebook.util;

import org.codehaus.jackson.map.ObjectMapper;

import com.dzd.phonebook.service.Instruct;

/**
 * 指令处理工具类
 * 
 * @author CHENCHAO
 * @description 封装类
 * @date 2017-04-14 13:46:00
 *
 */
public class InstructSendUtil {
	/**
	 * @Description:注册动作发送
	 * @author:oygy
	 * @time:2017年1月11日 下午2:45:22
	 */
	public static void instructSend(String keys, Integer smsUserId) {
		Instruct instruct = new Instruct();
		instruct.setKey(keys);
		instruct.setSmsUserId(smsUserId + "");
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonStr = mapper.writeValueAsString(instruct);
			RedisUtil.publish(InstructState.AB, jsonStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
