package com.dzd.phonebook.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

/** * 
@author  作者 
E-mail: * 
@date 创建时间：2017年6月8日 下午5:40:47 * 
@version 1.0 * 
@parameter  * 
@since  * 
@return  */
public class BatchCompressExportUtil<T>
{
	// 每次设置导出数量
	public static int NUM = 5000;
	public static String title = "";

	/**
	 * 
	 * @Title: exportExcel
	 * @Description: 多Excel表格压缩打包导出
	 * @author:    hz-liang
	 * @param headers
	 * @param columns
	 * @param result
	 * @param out
	 * @param request
	 * @param pattern
	 * @throws Exception  
	 * @return: void   
	 * @throws
	 */
	public void exportExcel(String[] headers, String[] columns, List<List<String>> result,
	        OutputStream out, HttpServletRequest request) throws Exception
	{
		// 创建文件夹存放压缩文件
		String Path = request.getSession().getServletContext().getRealPath("/exportZipFiles");
		File zipFile = new File(Path);
		if ( !zipFile.exists() )
		{
			zipFile.mkdirs();
		}

		File zip = new File(request.getSession().getServletContext().getRealPath("/exportZipFiles")
		        + "/" + getFileName() + ".zip");// 压缩文件

		int n = result.size();
		List<String> fileNames = new ArrayList<String>();// 用于存放生成的文件名称s
		// 文件流用于转存文件

		FileInputStream inStream = null;
		FileOutputStream o = null;

		for ( int j = 0; j < n; j++ )
		{
			List<String> result1 = result.get(j);
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = (HSSFSheet) workbook.createSheet(title);
			// 设置表格默认列宽度为18个字节
			sheet.setColumnWidth(0, 3600);
			sheet.setColumnWidth(1, 8000);

			String file = request.getSession().getServletContext().getRealPath("/exportZipFiles")
			        + "/" + getFileName() + "-" + j + ".xls";

			fileNames.add(file);

			try
			{
				o = new FileOutputStream(file);
				// 生成一个样式
				HSSFCellStyle style = setStyle(workbook);
				HSSFCellStyle style2 = setStyle2(workbook);
				// 产生表格标题行
				// 表头的样式
				HSSFCellStyle titleStyle = (HSSFCellStyle) workbook.createCellStyle();// 创建样式对象
				titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
				titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
				// 设置字体
				HSSFFont titleFont = (HSSFFont) workbook.createFont(); // 创建字体对象
				titleFont.setFontHeightInPoints((short) 12); // 设置字体大小
				titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
				// titleFont.setFontName("黑体"); // 设置为黑体字
				titleStyle.setFont(titleFont);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (headers.length - 1)));// 指定合并区域
				HSSFRow rowHeader = sheet.createRow(0);
				HSSFCell cellHeader = rowHeader.createCell(0); // 只能往第一格子写数据，然后应用样式，就可以水平垂直居中
				HSSFRichTextString textHeader = new HSSFRichTextString(title);
				cellHeader.setCellStyle(titleStyle);
				cellHeader.setCellValue(textHeader); // 表头名称

				HSSFRow row = sheet.createRow(1);
				for ( int i = 0; i < headers.length; i++ )
				{
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(headers[i]);
					cell.setCellValue(text);
				}
				// 遍历集合数据，产生数据行
				if ( result1 != null )
				{
					int index = 2;
					for ( String t : result1 )
					{
						row = sheet.createRow(index);
						index++;
						for ( int i = 0; i < columns.length; i++ )
						{
							HSSFCell cell = row.createCell(i);
							if ( i == 0 )
							{
								cell.setCellValue(index - 2);
								cell.setCellStyle(style2);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							} else
							{
								cell.setCellValue(t);
								cell.setCellStyle(style2);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							}

						}
					}
				}
				workbook.write(o);
				File srcfile[] = new File[fileNames.size()];
				for ( int i = 0, n1 = fileNames.size(); i < n1; i++ )
				{
					srcfile[i] = new File(fileNames.get(i));
				}
				ZipFiles(srcfile, zip);
				inStream = new FileInputStream(zip);
				byte[] buf = new byte[4096];
				int readLength;
				while ( ((readLength = inStream.read(buf)) != -1) )
				{
					out.write(buf, 0, readLength);
				}
			} finally
			{
				inStream.close();
				o.flush();
				o.close();
			}
		}
	}

	// 获取文件名字
	public static String getFileName()
	{
		// 文件名获取
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String f = title + format.format(date);
		return f;
	}

	// 压缩文件
	public static void ZipFiles(java.io.File[] srcfile, java.io.File zipfile)
	{
		byte[] buf = new byte[1024];
		ZipOutputStream out = null;
		FileInputStream in = null;
		try
		{
			out = new ZipOutputStream(new FileOutputStream(zipfile));
			for ( int i = 0; i < srcfile.length; i++ )
			{
				try
				{
					in = new FileInputStream(srcfile[i]);
					out.putNextEntry(new ZipEntry(srcfile[i].getName()));
					int len;
					while ( (len = in.read(buf)) > 0 )
					{
						out.write(buf, 0, len);
					}
				} finally
				{
					out.closeEntry();
					in.close();
				}

			}

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				out.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/** 设置响应头 */
	public void setResponseHeader(HttpServletResponse response, String fileName)
	{
		try
		{
			String titleName = "定时任务";
			title = fileName;
			response.reset();// 清空输出流
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename="
			        + new String(titleName.getBytes("GB2312"), "8859_1") + ".zip");
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private static HSSFCellStyle setStyle2(HSSFWorkbook workbook)
	{
		HSSFFont font2 = setDateFont(workbook);
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style2.setFont(font2);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style2.setWrapText(true); // 换行
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		return style2;
	}

	private static HSSFCellStyle setStyle(HSSFWorkbook workbook)
	{
		HSSFFont font = setDateFont(workbook);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		style.setLocked(true);
		return style;
	}

	private static HSSFFont setDateFont(HSSFWorkbook workbook)
	{
		HSSFFont datefont = workbook.createFont();
		datefont.setFontName("宋体");
		datefont.setFontHeightInPoints((short) 12);// 字体大小
		return datefont;
	}

	/**
	 * 
	 * @Title: delFolder
	 * @Description: 导出完成后删除文件夹中文件
	 * @author:    hz-liang
	 * @param folderPath  
	 * @return: void   
	 * @throws
	 */
	public static void delFolder(String folderPath)
	{
		try
		{
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path)
	{
		boolean flag = false;
		File file = new File(path);
		if ( !file.exists() )
		{
			return flag;
		}
		if ( !file.isDirectory() )
		{
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for ( int i = 0; i < tempList.length; i++ )
		{
			if ( path.endsWith(File.separator) )
			{
				temp = new File(path + tempList[i]);
			} else
			{
				temp = new File(path + File.separator + tempList[i]);
			}
			if ( temp.isFile() )
			{
				temp.delete();
			}
			if ( temp.isDirectory() )
			{
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
}
