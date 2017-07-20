package com.dzd.phonebook.util;

/**
 * @author chenchao
 * @Description:jsp数据响应
 * @date 2016-5-29
 */
public class JspResponseBean {
	/* 列表总数 */
	private long total;
	/* 列表数据 */
	private Object dataList;
	/* 单数据 */
	private Object data;
	
	/* 列表数据2 */
	private Object dataListTow;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public Object getDataList() {
		return dataList;
	}

	public void setDataList(Object dataList) {
		this.dataList = dataList;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getDataListTow() {
		return dataListTow;
	}

	public void setDataListTow(Object dataListTow) {
		this.dataListTow = dataListTow;
	}


	
}
