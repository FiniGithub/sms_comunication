package com.dzd.phonebook.util;

import java.util.Date;
import java.util.List;

import com.dzd.base.page.BasePage;
import com.dzd.phonebook.entity.SysMenuBtn;

public class SmsSendPackageLog extends BasePage{

	private List<SysMenuBtn> sysMenuBtns;//菜单拥有的按钮
	
	private Integer id; 					//id
	private Integer smsTaskId;				//任务ID
	private Integer userId;					//用户ID
	private Date createDate;                //创建时间
	private Integer smsAisleId;				//通道ID
	private String smsAisleName;			//通道名称
	private Integer phoneNum;				//号码数量
	private Integer state;					//状态0：发送成功，1：发送失败
	private String content;					//内容
	private String describe;				//描述
	private String phoneAll;				//发送的所有号码
	
	
	
	public List<SysMenuBtn> getSysMenuBtns() {
		return sysMenuBtns;
	}
	public void setSysMenuBtns(List<SysMenuBtn> sysMenuBtns) {
		this.sysMenuBtns = sysMenuBtns;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSmsTaskId() {
		return smsTaskId;
	}
	public void setSmsTaskId(Integer smsTaskId) {
		this.smsTaskId = smsTaskId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getSmsAisleId() {
		return smsAisleId;
	}
	public void setSmsAisleId(Integer smsAisleId) {
		this.smsAisleId = smsAisleId;
	}
	public String getSmsAisleName() {
		return smsAisleName;
	}
	public void setSmsAisleName(String smsAisleName) {
		this.smsAisleName = smsAisleName;
	}
	public Integer getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(Integer phoneNum) {
		this.phoneNum = phoneNum;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getPhoneAll() {
		return phoneAll;
	}
	public void setPhoneAll(String phoneAll) {
		this.phoneAll = phoneAll;
	}
	
}
