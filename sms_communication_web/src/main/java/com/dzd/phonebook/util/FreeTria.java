package com.dzd.phonebook.util;

import java.util.Date;

/**
 * 免审模板实体
 * @Description:
 * @author:oygy
 * @time:2017年1月19日 上午10:44:04
 */
public class FreeTria {

	private Integer id;               //id
	private String name;			  //模板名称
	private String content;			  //模板内容
	private Integer freeTrialType;    //面审模板类型（通道类型ID）
	private Date createTime;		  //创建时间
	private Date updateTime;		  //修改时间
	
	
	
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getFreeTrialType() {
		return freeTrialType;
	}
	public void setFreeTrialType(Integer freeTrialType) {
		this.freeTrialType = freeTrialType;
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
