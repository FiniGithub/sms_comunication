package com.dzd.sms.api.controller;

import com.dzd.sms.addons.aisle.BaseAisle;
import com.dzd.sms.application.SmsServerManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IDEA Author: WHL Date: 2017/1/3 Time: 19:19
 */
@Controller
@RequestMapping("/v2/sms")
public class SmsReport
{
	/**
	 * 状态报告信息分析
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/report")
	@ResponseBody
	public String report(HttpServletRequest request)
	{
		String status = "";

		String aisleKey = request.getParameter("key");
		String aisleIdString = request.getParameter("id");
		long aisleId = Integer.valueOf(aisleIdString);
		BaseAisle baseAisle = SmsServerManager.I.getAisle(aisleId);
		if ( baseAisle == null )
		{
			return "没有找到通道配置信息.";
		}

		Map<String, String> params = new HashMap<String, String>();

		Enumeration enumeration = request.getParameterNames();
		while ( enumeration.hasMoreElements() )
		{
			String paramName = (String) enumeration.nextElement();
			String paramValue = request.getParameter(paramName);
			params.put(paramName, paramValue);
		}
		// String aisleName = "smsWayComCnAisle";
		// BaseAisle aisleObject = (BaseAisle)
		// SpringBeanFactoryUtils.getBean(aisleName);

		status = baseAisle.pushReport(params);
		return "ok";
	}

	/**
	 * 回复（上行）信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/reply")
	@ResponseBody
	public String reply(HttpServletRequest request)
	{
		String status = "";
		System.out.println();
		String aisleKey = request.getParameter("key");
		String aisleIdString = request.getParameter("id");
		System.out.println(" aisleKey=" + aisleKey + " aisleIdString=" + aisleIdString);
		long aisleId = Integer.valueOf(aisleIdString);
		BaseAisle baseAisle = SmsServerManager.I.getAisle(aisleId);
		if ( baseAisle == null )
		{
			return "没有找到通道配置信息.";
		}

		Map<String, String> params = new HashMap<String, String>();

		Enumeration enumeration = request.getParameterNames();
		while ( enumeration.hasMoreElements() )
		{
			String paramName = (String) enumeration.nextElement();
			String paramValue = request.getParameter(paramName);
			params.put(paramName, paramValue);
		}
		// String aisleName = "smsWayComCnAisle";
		// BaseAisle aisleObject = (BaseAisle)
		// SpringBeanFactoryUtils.getBean(aisleName);
		status = baseAisle.pushReply(params);
		return "ok";
	}
}
