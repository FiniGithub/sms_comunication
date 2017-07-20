package com.dzd.sms.api.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dzd.phonebook.util.FilterPhoneOrderExport;
import com.dzd.sms.application.Define;

/** * 
@author  作者 
E-mail: * 
@date 创建时间：2017年5月22日 上午9:50:39 * 
@version 1.0 * 
@parameter  * 
@since  * 
@return  */
public class SmsFilterOrderBusiness
{
	private  static Logger logger = Logger.getLogger(SmsOrderExportBusiness.class );
	
	public void orderExport(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> dateMap, List<Map<String, Object>> resultList)
	{
		try
		{
			String sheetName = "运营商号区分";
			String[] head0 = new String[]
			{ "序号", "移动号码：" + dateMap.get(Define.DISTINGUISHOPERATOR.MOBILELENGTH) + "个",
			        "联通号码：" + dateMap.get(Define.DISTINGUISHOPERATOR.UNICOMLENGTH) + "个",
			        "电信号码：" + dateMap.get(Define.DISTINGUISHOPERATOR.TELECOMLENGTH) + "个",
			        "未知号码：" + dateMap.get(Define.DISTINGUISHOPERATOR.INVALIDLENGTH) + "个" };// 在excel中
			String[] headnum0 = new String[]
			{ "1,2,0,0", "1,2,1,1", "1,2,2,2", "1,2,3,3", "1,2,4,4"};// 对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
			String[] colName = new String[]
			{ Define.STATICAL.NUMBER, Define.DISTINGUISHOPERATOR.MOBILEOPERATOR,
			        Define.DISTINGUISHOPERATOR.UNICOMOPERATOR,
			        Define.DISTINGUISHOPERATOR.TELECOMOPERATOR,
			        Define.DISTINGUISHOPERATOR.INVALIDOPERATOR };// 需要显示在excel中的参数对应的值，因为是用map存的，放的都是对应的key
			
			FilterPhoneOrderExport.reportMergeXls(request, response, resultList, sheetName, head0,
			        headnum0, colName);// utils类需要用到的参数
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("导出失败");
			throw new RuntimeException("导出失败");
		}
		
	}

}
