package com.dzd.phonebook.entity;

import java.sql.Timestamp;


import com.dzd.base.page.BasePage;

/**
 * @Description:屏蔽词管理
 * @author:lq
 * @time:2016年12月30日 下午2:28:03
 */
public class SmsWordShielding extends BasePage {
	private Integer id;//id
	private String  word;//屏蔽词
	private String createTime;//创建时间
	private String  updateTime;//最后修改时间
	
	public SmsWordShielding() {
		super();
	}
	public SmsWordShielding(Integer id, String word, String createTime, String updateTime) {
		super();
		this.id = id;
		this.word = word;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}
