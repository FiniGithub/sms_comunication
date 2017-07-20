package com.dzd.phonebook.util;

import java.util.Date;

/**
 * @Description: 通道类型
 * @author:oygy
 * @time:2017年1月3日 下午4:34:43
 */
public class SmsAisleGroupType {

	private Integer id;   		    //id
	private String name;   		    //通道类型名称
	private Date createTime;        //创建时间
	private Date updateTime;		//修改时间
	private String describe;        //描述
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
}
