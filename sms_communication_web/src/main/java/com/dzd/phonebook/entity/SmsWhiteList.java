package com.dzd.phonebook.entity;

/**
 * @Description:短信白名单
 * @author:lq
 * @time:2016年1月3日 
 */
public class SmsWhiteList {

	private Integer id;
	private String phone;//白名单手机号
	private Integer aisleId;//通道id
	private String aisleName;//通道名称
	private String createTime;//创建时间
	
	public SmsWhiteList() {
		super();
	}
	public SmsWhiteList(Integer id, String phone, Integer aisleId, String aisleName, String createTime) {
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
	
	
}
