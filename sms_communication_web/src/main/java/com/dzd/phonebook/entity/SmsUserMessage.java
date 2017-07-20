package com.dzd.phonebook.entity;

import java.util.Date;
import java.util.List;

import com.dzd.base.page.BasePage;

/**
 * 代理商-推送消息实体类
 * 
 * @author CHENCHAO
 * @date 2017-04-12 10:15:00
 *
 */
public class SmsUserMessage extends BasePage {
	
	private List<SysMenuBtn> sysMenuBtns;//菜单拥有的按钮
	
	private Integer id;
	private Integer smsUserId;// 代理商id
	private String title;// 消息标题
	private Integer state;// 状态
	private String content;// 消息内容
	private Date created;// 创建时间
	private Date updated;// 修改时间
	
	private Integer type;  //类型：0：全部发推送，1：分类推送，2：单独推送
	private String smsUserEmail;  //类型：0：全部发推送，1：分类推送，2：单独推送
	private Integer pushIndex;    //是否推送至首页，0：不推送，1：推送
	private String smsUserTypeId; //分类推送ID，多种类型逗号隔开如：1,2,3
	private Integer sysUserId;  //操作人ID
	private String sysName;  //操作人
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSmsUserId() {
		return smsUserId;
	}

	public void setSmsUserId(Integer smsUserId) {
		this.smsUserId = smsUserId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSmsUserEmail() {
		return smsUserEmail;
	}

	public void setSmsUserEmail(String smsUserEmail) {
		this.smsUserEmail = smsUserEmail;
	}

	public Integer getPushIndex() {
		return pushIndex;
	}

	public void setPushIndex(Integer pushIndex) {
		this.pushIndex = pushIndex;
	}

	public String getSmsUserTypeId() {
		return smsUserTypeId;
	}

	public void setSmsUserTypeId(String smsUserTypeId) {
		this.smsUserTypeId = smsUserTypeId;
	}

	public List<SysMenuBtn> getSysMenuBtns() {
		return sysMenuBtns;
	}

	public void setSysMenuBtns(List<SysMenuBtn> sysMenuBtns) {
		this.sysMenuBtns = sysMenuBtns;
	}

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

}
