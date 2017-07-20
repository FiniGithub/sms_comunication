package com.dzd.phonebook.util;

public class BaseRequestParameters {
	/* web端分页 */
	/**
	 * 分页开始
	 */
	private int pagenum = 1;

	/**
	 * 分页结束
	 */
	private int pagesize = 20;

	private String sort;// 排序的字段

	private String order;// 升序 还是降序

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
