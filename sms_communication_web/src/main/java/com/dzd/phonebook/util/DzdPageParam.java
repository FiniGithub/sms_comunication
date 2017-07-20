package com.dzd.phonebook.util;

/**
 * 分页参数
 * @author chenchao
 * @date 2016-6-27 10:07:00
 *
 */
public class DzdPageParam {
	/**
	 * 查询开始页面
	 */
	private int start;
	/**
	 * 查询限制
	 */
	private int limit;
	/**
	 * sql语句中的条 可为bean 可为map
	 */
	private Object condition;

	public DzdPageParam() {

	}

	public DzdPageParam(int start, int limit) {
		this.start = start;
		this.limit = limit;
	}

	public DzdPageParam(int start, int limit, Object condition) {
		this(start, limit);
		this.condition = condition;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setCondition(Object condition) {
		this.condition = condition;
	}

	public Object getCondition() {
		return condition;
	}

}
