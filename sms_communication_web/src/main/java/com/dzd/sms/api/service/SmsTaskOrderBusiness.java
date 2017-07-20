package com.dzd.sms.api.service;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dzd.phonebook.util.BatchCompressExportUtil;
import com.dzd.sms.application.Define;

/** * 
@author  作者 
E-mail: * 
@date 创建时间：2017年5月22日 上午9:50:39 * 
@version 1.0 * 
@parameter  * 
@since  * 
@return  */
public class SmsTaskOrderBusiness
{
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public void orderExport(HttpServletRequest request, HttpServletResponse response,
	        List<List<String>> dataList)
	{
		BatchCompressExportUtil excelUtil = new BatchCompressExportUtil();
		OutputStream out = null;
		try
		{
			out = response.getOutputStream();
			excelUtil.setResponseHeader(response, request.getParameter(Define.STATICAL.EMAIL)
			        + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
			String[] headers = { "序号", "号码" };
			String[] columns = { Define.STATICAL.NUMBER, Define.STATICAL.MOBILE };

			excelUtil.exportExcel(headers, columns, dataList, out, request);
		} catch (Exception e1)
		{
			e1.printStackTrace();
		} finally
		{
			try
			{
				out.flush();
				out.close();
				// 导出完成后删除文件夹中文件
				excelUtil.delFolder(request.getSession().getServletContext().getRealPath("/exportZipFiles"));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
