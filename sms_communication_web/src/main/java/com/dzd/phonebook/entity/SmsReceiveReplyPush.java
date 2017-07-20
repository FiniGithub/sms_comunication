package com.dzd.phonebook.entity;

import java.util.List;

/**
 * @Description:短信回复
 * @author:liuquan
 * @time:2017年1月5日
 */
public class SmsReceiveReplyPush {

	
	
	private Integer id;
	private String phone;//用户手机号码
	private String content;//回复内容
	private String createTime;//创建时间
	private String contents;//下发内容
	private String email;//账号
	private String region;//归属地
	private String name;//客户名字
	private Integer state;//推送状态（1：未推送，2：已推送）注：推送给下家
	private String aname;	//通道名称
	
	private String ywName;   //业务员名称
	private String teamName; //团队名称

	private List<SysMenuBtn> sysMenuBtns;//菜单拥有的按钮
	
	public List<SysMenuBtn> getSysMenuBtns() {
		return sysMenuBtns;
	}
	public void setSysMenuBtns(List<SysMenuBtn> sysMenuBtns) {
		this.sysMenuBtns = sysMenuBtns;
	}
	
	public SmsReceiveReplyPush() {
		super();
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getAname() {
		return aname;
	}
	public void setAname(String aname) {
		this.aname = aname;
	}
	public String getYwName() {
		return ywName;
	}
	public void setYwName(String ywName) {
		this.ywName = ywName;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

}
