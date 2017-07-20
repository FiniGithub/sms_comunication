package com.dzd.phonebook.entity;

import java.util.Date;

/**
 * @Description:用户登录日志
 * @author:wangran
 * @time:2017年6月02日 上午10:52:03
 */
public class SysLoginLog {

	private Integer id;
	private Integer sysUserId;    //登录用户ID
	private String email; 	      //登录账号
	private String loginState;       //操作状态，成功 失败
	private String ip;		  //登录用户ip
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLoginState() {
		return loginState;
	}

	public void setLoginState(String loginState) {
		this.loginState = loginState;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
