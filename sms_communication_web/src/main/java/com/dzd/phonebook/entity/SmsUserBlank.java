package com.dzd.phonebook.entity;

import java.util.Date;

public class SmsUserBlank {
	private Integer id;
	private Integer userId;// 用户id
	private Integer surplusNum;// 剩余条数
	private Date createTime;// 创建时间
	private Date updateTime;// 修改时间
	private Integer sumNuum;  //总条数
	private Integer usedNum;   //已用条数

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getSurplusNum() {
		return surplusNum;
	}
	public void setSurplusNum(Integer surplusNum) {
		this.surplusNum = surplusNum;
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

	public Integer getSumNuum() {
		return sumNuum;
	}

	public void setSumNuum(Integer sumNuum) {
		this.sumNuum = sumNuum;
	}

	public Integer getUsedNum() {
		return usedNum;
	}

	public void setUsedNum(Integer usedNum) {
		this.usedNum = usedNum;
	}
}
