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

import com.dzd.sms.application.Define;

/**
 * *
 * 
 * @author 作者 E-mail: *
 * @date 创建时间：2017年4月11日 下午4:44:25 *
 * @version 1.0 *
 * @parameter *
 * @since *
 * @return
 */
public class ExcelUtil {
	/**
	 * 多行表头 dataList：导出的数据；sheetName：表头名称； head0：表头第一行列名；headnum0：第一行合并单元格的参数
	 * head1：表头第二行列名；headnum1：第二行合并单元格的参数；detail：导出的表体字段
	 *
	 */
	public static void reportMergeXls(HttpServletRequest request, HttpServletResponse response,
			List<Map<String, Object>> dataList, String sheetName, String[] head0, String[] headnum0, String[] head1,
			String[] headnum1, String[] detail) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);// 创建一个表
		// 表头标题样式
		HSSFFont headfont = setHeadFont(workbook);
		HSSFCellStyle headstyle = setHeadStyle(workbook, headfont);
		// 表头时间样式
		HSSFCellStyle style = setStyle(workbook);
		HSSFCellStyle style2 = setStyle2(workbook);
		// 设置列宽 （第几列，宽度）
		boolean customer_order = Boolean.parseBoolean(request.getParameter(Define.FILENAME.CUSTOMER_ORDER));
		if ( customer_order )
		{
			setCustomSheetWidthAndHigh(sheet);
		} else
		{
			setSheetWidthAndHigh(sheet);
		}

		// 第一行表头标题
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, detail.length));
		HSSFRow row = sheet.createRow(0);
		row.setHeight((short) 0x349);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(headstyle);
		cell.setCellValue(sheetName);

		// 动态合并单元格
		for (int i = 0; i < headnum0.length; i++) {
			String[] temp = headnum0[i].split(",");
			Integer startrow = Integer.parseInt(temp[0]);
			Integer overrow = Integer.parseInt(temp[1]);
			Integer startcol = Integer.parseInt(temp[2]);
			Integer overcol = Integer.parseInt(temp[3]);
			sheet.addMergedRegion(new CellRangeAddress(startrow, overrow, startcol, overcol));
		}

		// 动态合并单元格
		for (int i = 0; i < headnum1.length; i++) {
			String[] temp = headnum1[i].split(",");
			Integer startrow = Integer.parseInt(temp[0]);
			Integer overrow = Integer.parseInt(temp[1]);
			Integer startcol = Integer.parseInt(temp[2]);
			Integer overcol = Integer.parseInt(temp[3]);
			// firstRow 开始行 lastRow 结束行 firstCol 开始列 lastCol 结束列
			sheet.addMergedRegion(new CellRangeAddress(startrow, overrow, startcol, overcol));
		}

		// 第二行表头列名
		if ( customer_order )
		{
			setCustomCell(head0, head1, sheet, style);
		} else
		{
			setCell(head0, head1, sheet, style);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 设置列值-内容
		for (int i = 0; i < dataList.size(); i++) {
			row = sheet.createRow(i + 3);
			for (int j = 0; j < detail.length; j++) {
				Map<String, Object> tempmap = dataList.get(i);
				Object data = tempmap.get(detail[j]);
				cell = row.createCell(j);
				cell.setCellStyle(style2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				if (data instanceof String) {
					cell.setCellValue(data.toString());
				} else if (data instanceof Integer) {
					cell.setCellValue(Integer.valueOf(data.toString()));
				} else if (data instanceof Date) {
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
		response.setHeader( "Content-Disposition", "attachment;filename=\""+ new String( fileName.getBytes( "gb2312" ), "ISO8859-1" )+ ".xls" + "\"");
		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}

	}

	private static void setCustomCell(String[] head0, String[] head1, HSSFSheet sheet,
	        HSSFCellStyle style)
	{
		HSSFRow row;
		HSSFCell cell;
		row = sheet.createRow(1);
		int jj = 0;
		for (int i = 0; i < 6; i++) {
			if (i > 3) {					// 第八列开始进行单元格合并
				jj = jj > 6 ? jj : i + 2;	// 递增两格，共三格进行单元格合并
				cell = row.createCell(jj);	// 合并单元格
				jj += 3;
			} else {
				cell = row.createCell(i);	// 不需要合并单元格
			}
			cell.setCellValue(head0[i]);	// 列名
			cell.setCellStyle(style);
		}

		// 设置合并单元格的参数并初始化带边框的表头（这样做可以避免因为合并单元格后有的单元格的边框显示不出来）
		row = sheet.createRow(2);// 因为下标从0开始，所以这里表示的是excel中的第四行
		for (int i = 0; i < head0.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(style);
			if (i > 2 && i < 12) {
				for (int j = 0; j < head1.length; j++) {
					cell = row.createCell(j + 3);
					cell.setCellValue(head1[j]);// 给excel中第四行的8、9、10等列赋值（"成功", "失败", "未知)
					cell.setCellStyle(style);// 设置excel中第四行的8、9、10列的边框
				}
			}
		}
	}
	
	private static void setCell(String[] head0, String[] head1, HSSFSheet sheet,
	        HSSFCellStyle style)
	{
		HSSFRow row;
		HSSFCell cell;
		row = sheet.createRow(1);
		int jj = 0;
		for (int i = 0; i < 11; i++) {
			if (i > 8) {					// 第三列开始进行单元格合并
				jj = jj > 11 ? jj : i + 2;	// 递增两格，共三格进行单元格合并
				cell = row.createCell(jj);	// 合并单元格
				jj += 3;
			} else {
				cell = row.createCell(i);	// 不需要合并单元格
			}
			cell.setCellValue(head0[i]);	// 列名
			cell.setCellStyle(style);
		}

		// 设置合并单元格的参数并初始化带边框的表头（这样做可以避免因为合并单元格后有的单元格的边框显示不出来）
		row = sheet.createRow(2);// 因为下标从0开始，所以这里表示的是excel中的第四行
		for (int i = 0; i < head0.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(style);
			if (i > 7 && i < 17) {
				for (int j = 0; j < head1.length; j++) {
					cell = row.createCell(j + 8);
					cell.setCellValue(head1[j]);// 给excel中第四行的3、4、5等列赋值（"成功", "失败", "未知)
					cell.setCellStyle(style);// 设置excel中第四行的3、4、5列的边框
				}
			}
		}
	}

	private static void setSheetWidthAndHigh(HSSFSheet sheet) {
		sheet.setColumnWidth(0, 1600);
		sheet.setColumnWidth(1, 3600);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 4500);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 4500);
		sheet.setColumnWidth(6, 2800);
		sheet.setColumnWidth(7, 2800);
		sheet.setColumnWidth(8, 2800);
		sheet.setColumnWidth(9, 2800);
		sheet.setColumnWidth(10, 2800);
		sheet.setColumnWidth(11, 2800);
		sheet.setColumnWidth(12, 2800);
		sheet.setColumnWidth(13, 2800);
		sheet.setColumnWidth(14, 2800);
		sheet.setColumnWidth(15, 2800);
		sheet.setColumnWidth(16, 2800);
		sheet.setDefaultRowHeight((short) 360);// 设置行高
	}
	
	private static void setCustomSheetWidthAndHigh(HSSFSheet sheet) {
		sheet.setColumnWidth(0, 1600);
		sheet.setColumnWidth(1, 3600);
		sheet.setColumnWidth(2, 2800);
		sheet.setColumnWidth(3, 2800);
		sheet.setColumnWidth(4, 2800);
		sheet.setColumnWidth(5, 2800);
		sheet.setColumnWidth(6, 2800);
		sheet.setColumnWidth(7, 2800);
		sheet.setColumnWidth(8, 2800);
		sheet.setColumnWidth(9, 2800);
		sheet.setColumnWidth(10, 2800);
		sheet.setColumnWidth(11, 2800);
		sheet.setDefaultRowHeight((short) 360);// 设置行高
	}

	private static HSSFCellStyle setStyle2(HSSFWorkbook workbook) {
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

	private static HSSFFont setDateFont(HSSFWorkbook workbook) {
		HSSFFont datefont = workbook.createFont();
		datefont.setFontName("宋体");
		datefont.setFontHeightInPoints((short) 12);// 字体大小
		return datefont;
	}

	private static HSSFCellStyle setHeadStyle(HSSFWorkbook workbook, HSSFFont headfont) {
		HSSFCellStyle headstyle = workbook.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		headstyle.setLocked(true);
		return headstyle;
	}

	private static HSSFFont setHeadFont(HSSFWorkbook workbook) {
		HSSFFont headfont = workbook.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 22);// 字体大小
		return headfont;
	}

}
