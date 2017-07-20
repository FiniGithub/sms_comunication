package com.dzd.sms.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dzd.phonebook.util.ExcelUtil;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.sms.application.Define;
import com.github.pagehelper.Page;

/**
 * Created by IDEA Author: WHL Date: 2016/12/31 Time: 10:18
 */
public class SmsOrderExportBusiness
{
	private  static Logger logger = Logger.getLogger(SmsOrderExportBusiness.class );
	
	public void orderExport(HttpServletRequest request, HttpServletResponse response,
	        Page<SmsUser> dataList)
	{
		try
		{
			// 构造导出数据
			List<Map<String, Object>> resultList = constructeResultList(dataList);

			String sheetName = "统计信息";
			String[] head0 = new String[]
			{ "序号", "日期", "业务员", "客户名称", "账户", "通道", "通道类型", "计费量", "移动", "联通", "电信" };// 在excel中的第3行每列的参数
			String[] head1 = new String[]
			{ "成功", "失败", "未知", "成功", "失败", "未知", "成功", "失败", "未知" };// 在excel中的第4行每列（合并列）的参数
			String[] headnum0 = new String[]
			{ "1,2,0,0", "1,2,1,1", "1,2,2,2", "1,2,3,3", "1,2,4,4", "1,2,5,5", "1,2,6,6",
			        "1,2,7,7", "1,1,8,10", "1,1,11,13", "1,1,14,16" };// 对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
			String[] headnum1 = new String[]
			{ "2,2,8,8", "2,2,9,9", "2,2,10,10", "2,2,11,11", "2,2,12,12", "2,2,13,13", "2,2,14,14",
			        "2,2,15,15", "2,2,16,16" };
			String[] colName = new String[]
			{ Define.STATICAL.NUMBER, Define.STATICAL.DATE, Define.STATICAL.NICKNAME,
			        Define.STATICAL.NAME, Define.STATICAL.EMAIL, Define.STATICAL.SMSAISLENAME,
			        Define.STATICAL.SMSAISLETYPEID, Define.STATICAL.SENDNUM,
			        Define.STATICAL.SUMSUCCEEDNUMUS, Define.STATICAL.SUMFAILURENUMUS,
			        Define.STATICAL.SUMUNKNOWNNUMUS, Define.STATICAL.SUMSUCCEEDNUMMS,
			        Define.STATICAL.SUMFAILURENUMMS, Define.STATICAL.SUMUNKNOWNNUMMS,
			        Define.STATICAL.SUMSUCCEEDNUMTS, Define.STATICAL.SUMFAILURENUMTS,
			        Define.STATICAL.SUMUNKNOWNNUMTS };// 需要显示在excel中的参数对应的值，因为是用map存的，放的都是对应的key
			ExcelUtil.reportMergeXls(request, response, resultList, sheetName, head0, headnum0,
			        head1, headnum1, colName);// utils类需要用到的参数
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("导出失败");
			throw new RuntimeException("导出失败");
		}
		
	}

	/**
	 * 导出账户管理页面列表
	 * @param request
	 * @param response
	 * @param dataList
	 */
	public void export(HttpServletRequest request, HttpServletResponse response,
							Page<SmsUser> dataList)
	{
		try
		{
			// 构造导出数据
			List<Map<String, Object>> resultList = constructeResultList(dataList);

			String sheetName = "统计信息";
			String[] head0 = new String[]
					{ "序号", "日期", "业务员", "客户名称", "账户", "通道", "通道类型", "计费量", "移动", "联通", "电信" };// 在excel中的第3行每列的参数
			String[] head1 = new String[]
					{ "成功", "失败", "未知", "成功", "失败", "未知", "成功", "失败", "未知" };// 在excel中的第4行每列（合并列）的参数
			String[] headnum0 = new String[]
					{ "1,2,0,0", "1,2,1,1", "1,2,2,2", "1,2,3,3", "1,2,4,4", "1,2,5,5", "1,2,6,6",
							"1,2,7,7", "1,1,8,10", "1,1,11,13", "1,1,14,16" };// 对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
			String[] headnum1 = new String[]
					{ "2,2,8,8", "2,2,9,9", "2,2,10,10", "2,2,11,11", "2,2,12,12", "2,2,13,13", "2,2,14,14",
							"2,2,15,15", "2,2,16,16" };
			String[] colName = new String[]
					{ Define.STATICAL.NUMBER, Define.STATICAL.DATE, Define.STATICAL.NICKNAME,
							Define.STATICAL.NAME, Define.STATICAL.EMAIL, Define.STATICAL.SMSAISLENAME,
							Define.STATICAL.SMSAISLETYPEID, Define.STATICAL.SENDNUM,
							Define.STATICAL.SUMSUCCEEDNUMUS, Define.STATICAL.SUMFAILURENUMUS,
							Define.STATICAL.SUMUNKNOWNNUMUS, Define.STATICAL.SUMSUCCEEDNUMMS,
							Define.STATICAL.SUMFAILURENUMMS, Define.STATICAL.SUMUNKNOWNNUMMS,
							Define.STATICAL.SUMSUCCEEDNUMTS, Define.STATICAL.SUMFAILURENUMTS,
							Define.STATICAL.SUMUNKNOWNNUMTS };// 需要显示在excel中的参数对应的值，因为是用map存的，放的都是对应的key
			ExcelUtil.reportMergeXls(request, response, resultList, sheetName, head0, headnum0,
					head1, headnum1, colName);// utils类需要用到的参数
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("导出失败");
			throw new RuntimeException("导出失败");
		}

	}
	private List<Map<String, Object>> constructeResultList(Page<SmsUser> dataList)
	{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> tmpMap = null;
		int number = 0;
		for ( SmsUser smsUser : dataList )
		{
			number = number++ + 1;
			tmpMap = new HashMap<String, Object>();
			tmpMap.put(Define.STATICAL.NUMBER, number);
			tmpMap.put(Define.STATICAL.DATE, smsUser.getAuditTime());
			tmpMap.put(Define.STATICAL.NICKNAME, smsUser.getNickName());
			tmpMap.put(Define.STATICAL.NAME, smsUser.getName());
			tmpMap.put(Define.STATICAL.EMAIL, smsUser.getEmail());
			tmpMap.put(Define.STATICAL.SMSAISLENAME, smsUser.getSmsAisleName());
			tmpMap.put(Define.STATICAL.SMSAISLETYPEID, smsUser.getSmsAisleTypeId());
			tmpMap.put(Define.STATICAL.SENDNUM, smsUser.getSendNum());
			tmpMap.put(Define.STATICAL.SUMSUCCEEDNUMUS, smsUser.getSucceedNumUs());
			tmpMap.put(Define.STATICAL.SUMFAILURENUMUS, smsUser.getFailureNumUs());
			tmpMap.put(Define.STATICAL.SUMUNKNOWNNUMUS, smsUser.getUnknownNumUs());
			tmpMap.put(Define.STATICAL.SUMSUCCEEDNUMMS, smsUser.getSucceedNumMs());
			tmpMap.put(Define.STATICAL.SUMFAILURENUMMS, smsUser.getFailureNumMs());
			tmpMap.put(Define.STATICAL.SUMUNKNOWNNUMMS, smsUser.getUnknownNumMs());
			tmpMap.put(Define.STATICAL.SUMSUCCEEDNUMTS, smsUser.getSucceedNumTs());
			tmpMap.put(Define.STATICAL.SUMFAILURENUMTS, smsUser.getFailureNumTs());
			tmpMap.put(Define.STATICAL.SUMUNKNOWNNUMTS, smsUser.getUnknownNumTs());

			resultList.add(tmpMap);
		}
		return resultList;
	}
}
