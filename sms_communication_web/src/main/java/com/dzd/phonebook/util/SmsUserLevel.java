package com.dzd.phonebook.util;

import java.util.Date;
/**
 * 
 * @Description:用户级别
 * @author:oygy
 * @time:2017年2月23日 下午2:28:39
 */
public class SmsUserLevel {

	private Integer id;             //id
	private String name;			//名称
	private String comment;			//备注
	private Date createTime;        //创建时间
	
	
	
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
