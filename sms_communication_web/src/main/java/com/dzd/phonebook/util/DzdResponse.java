package com.dzd.phonebook.util;

import java.util.List;

/**
 * 数据响应
 * @author chenchao
 * @date 2016-6-27 10:06:00
 *
 */
public class DzdResponse {
	private String retCode;
	private String retMsg;
	private Object data;

	// 表格更改后的数据格式
	@SuppressWarnings("rawtypes")
	private List rows;
	private long total;// 分页总数

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@SuppressWarnings("rawtypes")
	public List getRows() {
		return rows;
	}

	@SuppressWarnings("rawtypes")
	public void setRows(List rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
