package com.dzd.phonebook.entity;

import java.util.List;

import com.dzd.base.page.BasePage;
import com.dzd.phonebook.util.BaseRequestParameters;

/**
 * @Description:黑名单
 * @author:lq
 * @time:2016年1月4日 
 */
public class SmsBlacklist extends BasePage {

	private Integer id;//ID
	private String phone;//手机号码
	private Integer aisleId;//通道Id
	private String aisleName;//归属通道
	private String createTime;//加入时间
	
	private List<SysMenuBtn> sysMenuBtns;//菜单拥有的按钮
	
	public SmsBlacklist() {
		super();
	}
	public SmsBlacklist(Integer id, String phone, Integer aisleId, String aisleName, String createTime) {
		super();
		this.id = id;
		this.phone = phone;
		this.aisleId = aisleId;
		this.aisleName = aisleName;
		this.createTime = createTime;
	}
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
	public Integer getAisleId() {
		return aisleId;
	}
	public void setAisleId(Integer aisleId) {
		this.aisleId = aisleId;
	}
	public String getAisleName() {
		return aisleName;
	}
	public void setAisleName(String aisleName) {
		this.aisleName = aisleName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public List<SysMenuBtn> getSysMenuBtns() {
		return sysMenuBtns;
	}
	public void setSysMenuBtns(List<SysMenuBtn> sysMenuBtns) {
		this.sysMenuBtns = sysMenuBtns;
	}
	
}
