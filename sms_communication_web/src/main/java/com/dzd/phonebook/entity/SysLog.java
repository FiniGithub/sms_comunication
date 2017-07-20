package com.dzd.phonebook.entity;

import java.util.Date;
/**
 * @Description:系统日志
 * @author:oygy
 * @time:2016年12月30日 下午2:28:03
 */
public class SysLog {

	private Integer id;
	private Integer sysUserId;    //系统用户ID
	private String userName; 	      //登录类型
	private String content;       //操作类型
	private String remark;		  //备注
	private Date createTime;      //操作时间
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
