package com.dzd.sms.api.service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dzd.phonebook.util.NoMergeExcelUtil;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.sms.application.Define;
import com.github.pagehelper.Page;

/**
 * Created by IDEA Author: WHL Date: 2016/12/31 Time: 10:18
 */
public class SmsChannlOrderExportBusiness
{
	private  static Logger logger = Logger.getLogger(SmsChannlOrderExportBusiness.class );
	
	public void orderExport(HttpServletRequest request, HttpServletResponse response,
	        Page<SmsUser> dataList)
	{
		try
		{
			// 构造导出数据
			List<Map<String, Object>> resultList = constructeResultList(dataList);

			String sheetName = "通道统计信息";

			String[] head0 = new String[]
			{ "序号", "账号", "通道名称", "发送数", "成功数", "失败数", "未知数", "成功率", "未知率", "日期" };// 在excel中

			String[] headnum0 = new String[]
			{ "1,2,0,0", "1,2,1,1", "1,2,2,2", "1,2,3,3", "1,2,4,4", "1,2,5,5", "1,2,6,6",
			        "1,2,7,7", "1,2,8,8", "1,2,9,9" };// 对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}

			String[] colName = new String[]
			{ Define.STATICAL.NUMBER, Define.STATICAL.EMAIL, Define.STATICAL.SMSAISLENAME,
			        Define.STATICAL.SENDNUM, Define.STATICAL.SUCCEEDNUM, Define.STATICAL.FAILURENUM,
			        Define.STATICAL.UNKNOWNFAILURENUM, Define.STATICAL.SUCCESSRATE,
			        Define.STATICAL.FAILURERATE, Define.STATICAL.SENDTIME };// 需要显示在excel中的参数对应的值，因为是用map存的，放的都是对应的key

			NoMergeExcelUtil.reportMergeXls(request, response, resultList, sheetName, head0,
			        headnum0, colName, true);// utils类需要用到的参数
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
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);// 控制保留小数点后几位，2：表示保留2位小数点
		for ( SmsUser smsUser : dataList )
		{
			number = number++ + 1;
			tmpMap = new HashMap<String, Object>();
			tmpMap.put(Define.STATICAL.NUMBER, number);
			tmpMap.put(Define.STATICAL.EMAIL, smsUser.getEmail());
			tmpMap.put(Define.STATICAL.SMSAISLENAME,
			        number == 1 ? "合计" : smsUser.getSmsAisleName());
			tmpMap.put(Define.STATICAL.SENDNUM, smsUser.getSucceedNum() + smsUser.getFailureNum()
			        + smsUser.getUnknownFailureNum());
			tmpMap.put(Define.STATICAL.SUCCEEDNUM, smsUser.getSucceedNum());
			tmpMap.put(Define.STATICAL.FAILURENUM, smsUser.getFailureNum());
			tmpMap.put(Define.STATICAL.UNKNOWNFAILURENUM, smsUser.getUnknownFailureNum());
			tmpMap.put(Define.STATICAL.SUCCESSRATE,
			        Integer.valueOf(tmpMap.get(Define.STATICAL.SENDNUM).toString()) == 0 ? "0%"
			                : nf.format(Double.valueOf(smsUser.getSucceedNum() * 1.0 / Integer
			                        .valueOf(tmpMap.get(Define.STATICAL.SENDNUM).toString()))));
			tmpMap.put(Define.STATICAL.FAILURERATE,
			        Integer.valueOf(tmpMap.get(Define.STATICAL.SENDNUM).toString()) == 0 ? "0%"
			                : nf.format(Double
			                        .valueOf(smsUser.getUnknownFailureNum() * 1.0 / Integer.valueOf(
			                                tmpMap.get(Define.STATICAL.SENDNUM).toString()))));
			tmpMap.put(Define.STATICAL.SENDTIME, smsUser.getAuditTime());

			resultList.add(tmpMap);
		}
		return resultList;
	}
}
