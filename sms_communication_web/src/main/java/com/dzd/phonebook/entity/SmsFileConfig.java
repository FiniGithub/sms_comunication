package com.dzd.phonebook.entity;

import java.sql.Timestamp;

/**
 * 文件配置信息
 * 
 * @author CHENCHAO
 * @date 2017-03-30 10:36:00
 *
 */
public class SmsFileConfig {
	private Integer id;
	private Integer sms_uid;// 用户编号
	private String uuid;// 文件唯一标识
	private Integer type;// 导入类型,0-添加号码,1-导入号码
	private String phone;// 多个号码仅仅保存第一个
	private Integer phoneSize;// 号码数量
	
	private Integer allPhoneNum;// 号码数量

	private String fileName;// 文件名称
	private Timestamp created;// 创建时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSms_uid() {
		return sms_uid;
	}

	public void setSms_uid(Integer sms_uid) {
		this.sms_uid = sms_uid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getPhoneSize() {
		return phoneSize;
	}

	public void setPhoneSize(Integer phoneSize) {
		this.phoneSize = phoneSize;
	}

	public Integer getAllPhoneNum() {
		return allPhoneNum;
	}

	public void setAllPhoneNum(Integer allPhoneNum) {
		this.allPhoneNum = allPhoneNum;
	}

}
