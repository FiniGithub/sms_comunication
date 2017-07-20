package com.dzd.phonebook.entity;


/**
 * @Description:屏蔽号段
 * @author:liuquan
 * @time:2016年12月30日 下午2:28:03
 */
public class SmsPhoneShielding {

	private Integer id;//id
	private String phone; //屏蔽号码段
	private String createTime; //创建时间
	private String updateTime;//最后修改时间
	
	public SmsPhoneShielding() {
		super();
	}
	public SmsPhoneShielding(Integer id, String phone, String createTime, String updateTime) {
		super();
		this.id = id;
		this.phone = phone;
		this.createTime = createTime;
		this.updateTime = updateTime;
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