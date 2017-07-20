package com.dzd.phonebook.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

/** * 
@author  作者 
E-mail: * 
@date 创建时间：2017年5月22日 上午11:34:34 * 
@version 1.0 * 
@parameter  * 
@since  * 
@return  */
public class NoMergeExcelUtil
{
	/**
	 * 多行表头 dataList：导出的数据；sheetName：表头名称； head0：表头第一行列名；headnum0：第一行合并单元格的参数
	 * head1：表头第二行列名；headnum1：第二行合并单元格的参数；detail：导出的表体字段
	 *
	 */
	public static void reportMergeXls(HttpServletRequest request, HttpServletResponse response,
	        List<Map<String, Object>> dataList, String sheetName, String[] head0, String[] headnum0,
	        String[] detail, boolean ischannl) throws Exception
	{
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);// 创建一个表
		// 表头标题样式
		HSSFFont headfont = setHeadFont(workbook);
		HSSFCellStyle headstyle = setHeadStyle(workbook, headfont);
		// 表头时间样式
		HSSFCellStyle style = setStyle(workbook);
		HSSFCellStyle style2 = setStyle2(workbook);
		// 设置列宽 （第几列，宽度）
		if(ischannl){
			setChannlSheetWidthAndHigh(sheet);
		}else{
			setSheetWidthAndHigh(sheet);
		}

		// 第一行表头标题
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, detail.length-1));
		HSSFRow row = sheet.createRow(0);
		row.setHeight((short) 0x349);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(headstyle);
		cell.setCellValue(sheetName);

		// 动态合并单元格
		for ( int i = 0; i < headnum0.length; i++ )
		{
			String[] temp = headnum0[i].split(",");
			Integer startrow = Integer.parseInt(temp[0]);
			Integer overrow = Integer.parseInt(temp[1]);
			Integer startcol = Integer.parseInt(temp[2]);
			Integer overcol = Integer.parseInt(temp[3]);
			sheet.addMergedRegion(new CellRangeAddress(startrow, overrow, startcol, overcol));
		}
		
		if(ischannl){
			setChannlCell(head0, sheet, style);
		}else{
			setCell(head0, sheet, style);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 设置列值-内容
		for ( int i = 0; i < dataList.size(); i++ )
		{
			row = sheet.createRow(i + 3);
			for ( int j = 0; j < detail.length; j++ )
			{
				Map<String, Object> tempmap = dataList.get(i);
				Object data = tempmap.get(detail[j]);
				cell = row.createCell(j);
				cell.setCellStyle(style2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				if ( data instanceof String )
				{
					cell.setCellValue(data.toString());
				} else if ( data instanceof Integer )
				{
					cell.setCellValue(Integer.valueOf(data.toString()));
				} else if ( data instanceof Date )
				{
					String startTime = sdf.format(data);
					cell.setCellValue(startTime);
				}
			}
		}
		String fileName = new String(sheetName);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		workbook.write(os);
		byte[] content = os.toByteArray();

		InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("applicationnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=\""
		        + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls" + "\"");
		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try
		{
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while ( -1 != (bytesRead = bis.read(buff, 0, buff.length)) )
			{
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e)
		{
			throw e;
		} finally
		{
			if ( bis != null )
				bis.close();
			if ( bos != null )
				bos.close();
		}

	}

	private static void setSheetWidthAndHigh(HSSFSheet sheet)
	{
		sheet.setColumnWidth(0, 1600);
		sheet.setColumnWidth(1, 6300);
		sheet.setColumnWidth(2, 3600);
		sheet.setColumnWidth(3, 3600);
		sheet.setColumnWidth(4, 30000);
		sheet.setColumnWidth(5, 2800);
		sheet.setColumnWidth(6, 3600);
		sheet.setDefaultRowHeight((short) 360);// 设置行高
	}
	
	private static void setChannlSheetWidthAndHigh(HSSFSheet sheet)
	{
		sheet.setColumnWidth(0, 1600);
		sheet.setColumnWidth(1, 6300);
		sheet.setColumnWidth(2, 6300);
		sheet.setColumnWidth(3, 3600);
		sheet.setColumnWidth(4, 3600);
		sheet.setColumnWidth(5, 3600);
		sheet.setColumnWidth(6, 3600);
		sheet.setColumnWidth(7, 3600);
		sheet.setColumnWidth(8, 3600);
		sheet.setColumnWidth(9, 3600);
		sheet.setDefaultRowHeight((short) 360);// 设置行高
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

	private static HSSFFont setDateFont(HSSFWorkbook workbook)
	{
		HSSFFont datefont = workbook.createFont();
		datefont.setFontName("宋体");
		datefont.setFontHeightInPoints((short) 12);// 字体大小
		return datefont;
	}

	private static HSSFCellStyle setHeadStyle(HSSFWorkbook workbook, HSSFFont headfont)
	{
		HSSFCellStyle headstyle = workbook.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		headstyle.setLocked(true);
		return headstyle;
	}

	private static HSSFFont setHeadFont(HSSFWorkbook workbook)
	{
		HSSFFont headfont = workbook.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 22);// 字体大小
		return headfont;
	}
	
	private static void setCell(String[] head0, HSSFSheet sheet,
	        HSSFCellStyle style)
	{
		HSSFRow row;
		HSSFCell cell;
		row = sheet.createRow(1);
		for ( int i = 0; i < 7; i++ )
		{
			cell = row.createCell(i); // 不需要合并单元格
			cell.setCellValue(head0[i]); // 列名
			cell.setCellStyle(style);
		}

		// 设置合并单元格的参数并初始化带边框的表头（这样做可以避免因为合并单元格后有的单元格的边框显示不出来）
		row = sheet.createRow(2);// 因为下标从0开始，所以这里表示的是excel中的第四行
		for ( int i = 0; i < head0.length; i++ )
		{
			cell = row.createCell(i);
			cell.setCellStyle(style);
		}
	}
	
	private static void setChannlCell(String[] head0, HSSFSheet sheet,
			HSSFCellStyle style)
	{
		HSSFRow row;
		HSSFCell cell;
		row = sheet.createRow(1);
		for ( int i = 0; i < 10; i++ )
		{
			cell = row.createCell(i); // 不需要合并单元格
			cell.setCellValue(head0[i]); // 列名
			cell.setCellStyle(style);
		}
		
		// 设置合并单元格的参数并初始化带边框的表头（这样做可以避免因为合并单元格后有的单元格的边框显示不出来）
		row = sheet.createRow(2);// 因为下标从0开始，所以这里表示的是excel中的第四行
		for ( int i = 0; i < head0.length; i++ )
		{
			cell = row.createCell(i);
			cell.setCellStyle(style);
		}
	}
	
	private static HSSFCellStyle setStyle(HSSFWorkbook workbook) {
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

}
