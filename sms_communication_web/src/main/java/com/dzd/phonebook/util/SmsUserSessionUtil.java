package com.dzd.phonebook.util;

import javax.servlet.http.HttpServletRequest;

import com.dzd.base.util.SessionUtils;

/**
 * 保存用户SESSION
 * 
 * @author CHENCHAO
 * @date 2017-04-14 14:08:00
 *
 */
public class SmsUserSessionUtil {

	/**
	 * 保存短信条数session
	 * 
	 * @param request
	 * @param surplusNum
	 */
	public static void saveBlankSession(HttpServletRequest request, Integer surplusNum) {
		SessionUtils.setBlank(request, surplusNum);
	}

	/**
	 * 保存消息条数session
	 * 
	 * @param request
	 * @param surplusNum
	 */
	public static void saveMsgCountSession(HttpServletRequest request, Integer messageCount) {
		SessionUtils.setMessageCount(request, messageCount);
	}
}
