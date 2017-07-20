package com.dzd.sms.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dzd.phonebook.util.NoMergeExcelUtil;
import com.dzd.phonebook.util.SmsSendLog;
import com.dzd.sms.application.Define;

/** * 
@author  作者 
E-mail: * 
@date 创建时间：2017年5月22日 上午9:50:39 * 
@version 1.0 * 
@parameter  * 
@since  * 
@return  */
public class SmsLogOrderBusiness
{
	private  static Logger logger = Logger.getLogger(SmsOrderExportBusiness.class );
	
	public void orderExport(HttpServletRequest request, HttpServletResponse response,
			List<SmsSendLog> dataList, String sheetName)
	{
		try
		{
			// 构造导出数据
			List<Map<String, Object>> resultList = constructeResultList(dataList);

			String[] head0 = new String[]
			{ "序号", "账号", "手机号码", "发送时间", "短信内容", "字数", "发送状态"};// 在excel中
			String[] headnum0 = new String[]
			{ "1,2,0,0", "1,2,1,1", "1,2,2,2", "1,2,3,3", "1,2,4,4", "1,2,5,5", "1,2,6,6"};// 对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
			String[] colName = new String[]
			{ Define.STATICAL.NUMBER, Define.STATICAL.EMAIL, Define.STATICAL.MOBILE,
			        Define.STATICAL.SENDTIME, Define.STATICAL.CONTENT, Define.STATICAL.LENGTH,
			        Define.STATICAL.STATE };// 需要显示在excel中的参数对应的值，因为是用map存的，放的都是对应的key
			NoMergeExcelUtil.reportMergeXls(request, response, resultList, sheetName, head0,
			        headnum0, colName, false);// utils类需要用到的参数
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("导出失败");
			throw new RuntimeException("导出失败");
		}
		
	}

	private List<Map<String, Object>> constructeResultList(List<SmsSendLog> dataList)
	{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> tmpMap = null;
		int number = 0;
		for ( SmsSendLog smsSendLog : dataList )
		{
			number = number++ + 1;
			tmpMap = new HashMap<String, Object>();
			tmpMap.put(Define.STATICAL.NUMBER, number);
			tmpMap.put(Define.STATICAL.EMAIL, smsSendLog.getSmsUserEmail());
			tmpMap.put(Define.STATICAL.MOBILE, smsSendLog.getReceivePhone());
			tmpMap.put(Define.STATICAL.SENDTIME, smsSendLog.getSendTime());
			tmpMap.put(Define.STATICAL.CONTENT, smsSendLog.getContent());
			tmpMap.put(Define.STATICAL.LENGTH, smsSendLog.getContent().length());
			tmpMap.put(Define.STATICAL.STATE, smsSendLog.getStateMap().get(smsSendLog.getState()));

			resultList.add(tmpMap);
		}
		return resultList;
	}


}
