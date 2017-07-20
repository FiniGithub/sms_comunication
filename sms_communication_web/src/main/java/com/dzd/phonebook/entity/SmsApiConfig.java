package com.dzd.phonebook.entity;

import java.util.Date;

/**
 * API配置文档实体类
 * 
 * @author CHENCHAO
 * @date 2017-04-10 10:19:00
 *
 */
public class SmsApiConfig {
	private Integer id;// 自增编号
	private String  title;// 标题
	private String  content;// 内容
	private Integer sortId;// 排序编号
	private Date created;// 创建时间
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Integer getSortId() {
		return sortId;
	}
	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}
}
