package com.dzd.phonebook.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dzd.sms.api.service.ManagementUserBusiness;
import com.dzd.sms.api.service.SmsChannlOrderExportBusiness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dzd.phonebook.util.SmsSendLog;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.sms.api.service.SmsCustomOrderExportBusiness;
import com.dzd.sms.api.service.SmsLogOrderBusiness;
import com.dzd.sms.api.service.SmsOrderExportBusiness;
import com.dzd.sms.api.service.SmsTaskOrderBusiness;
import com.github.pagehelper.Page;

/**
 * 发送短信服务类
 * 
 * @author CHENCHAO
 * @date 2017-03-28 09:33:00
 *
 */
public class SmsOrderExportService
{
	public static final Logger logger = LoggerFactory.getLogger(SmsOrderExportService.class);

	public void orderExport(HttpServletRequest request, HttpServletResponse response,
	        Page<SmsUser> dataList)
	{
		SmsOrderExportBusiness orderExportBusiness = new SmsOrderExportBusiness();
		orderExportBusiness.orderExport(request, response, dataList);
	}
	
	public void channlOrderExport(HttpServletRequest request, HttpServletResponse response,
	        Page<SmsUser> dataList)
	{
		SmsChannlOrderExportBusiness channlorderExportBusiness = new SmsChannlOrderExportBusiness();
		channlorderExportBusiness.orderExport(request, response, dataList);
	}
	
	public void customerOrderExport(HttpServletRequest request, HttpServletResponse response,
	        Page<SmsUser> dataList)
	{
		SmsCustomOrderExportBusiness smsCustomOrderExportBusiness = new SmsCustomOrderExportBusiness();
		smsCustomOrderExportBusiness.orderExport(request, response, dataList);
	}

	//账单管理信息导出
	public void managementUserExport(HttpServletRequest request, HttpServletResponse response,
									Page<SmsUser> dataList)
	{
		ManagementUserBusiness managementUserBusiness = new ManagementUserBusiness();
		managementUserBusiness.export(request, response, dataList);
	}
	
	public void logrOrderExport(HttpServletRequest request, HttpServletResponse response,
	        List<SmsSendLog> dataList, String sheetName)
	{
		SmsLogOrderBusiness smsLogOrderBusiness = new SmsLogOrderBusiness();
		smsLogOrderBusiness.orderExport(request, response, dataList, sheetName);
	}
	
	public void taskOrderExport(HttpServletRequest request, HttpServletResponse response,
			List<List<String>> dataList)
	{
		SmsTaskOrderBusiness smsTaskOrderBusiness = new SmsTaskOrderBusiness();
		smsTaskOrderBusiness.orderExport(request, response, dataList);
	}

}
