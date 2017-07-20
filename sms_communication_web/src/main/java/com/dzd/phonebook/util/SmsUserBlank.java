package com.dzd.phonebook.util;

import java.util.Date;

/**
 * 
 * @Description:代理人账户
 * @author:oygy
 * @time:2016年12月31日 上午10:41:37
 */
public class SmsUserBlank {

	private Integer id;			    //id
	private Float  awardMoney;      //授信额度
	private Float  money;            //账户金额
	private Integer freeAmount;     //免费短信条数 
	private Date createTime;        //创建时间
	private Date updateTime;		//修改时间
	private Integer userId;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Float getAwardMoney() {
		return awardMoney;
	}
	public void setAwardMoney(Float awardMoney) {
		this.awardMoney = awardMoney;
	}
	public Float getMoney() {
		return money;
	}
	public void setMoney(Float money) {
		this.money = money;
	}
	public Integer getFreeAmount() {
		return freeAmount;
	}
	public void setFreeAmount(Integer freeAmount) {
		this.freeAmount = freeAmount;
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
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
 }
