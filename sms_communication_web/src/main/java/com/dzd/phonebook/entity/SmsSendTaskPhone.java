package com.dzd.phonebook.entity;

import java.util.Date;

/**
 * (测试)发送任务
 * 
 * @author Administrator
 *
 */
public class SmsSendTaskPhone {
	private Integer id;
	private String phone;
	private Integer state;
	private Integer smsSendTaskId;
	private Date createTime;
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getSmsSendTaskId() {
		return smsSendTaskId;
	}

	public void setSmsSendTaskId(Integer smsSendTaskId) {
		this.smsSendTaskId = smsSendTaskId;
	}

	 

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
