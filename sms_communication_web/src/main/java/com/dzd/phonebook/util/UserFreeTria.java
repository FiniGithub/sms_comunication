package com.dzd.phonebook.util;

import java.util.Date;
import java.util.List;

import com.dzd.base.page.BasePage;
import com.dzd.phonebook.entity.SysMenuBtn;

/**
 * 用户免审模板实体
 * @Description:
 * @author:oygy
 * @time:2017年1月19日 上午10:44:04
 */
public class UserFreeTria extends BasePage {
	
	private List<SysMenuBtn> sysMenuBtns;//菜单拥有的按钮

	private Integer id;               //id
	private String name;			  //模板名称
	private String content;			  //模板内容
	private Integer freeTrialType;    //面审模板类型（通道类型ID）
	private String typeName;		  //通道类型名称
	private Date createTime;		  //创建时间
	private Date updateTime;		  //修改时间
	private Integer freeTrialState;	  //模板状态：0：待审核，1：使用中，2：停用，3：审核不通过
	private Integer smsUserId;		  //用户代理ID
	private String smsUserName;		  //代理商账户名称
	private String email;			  //代理商账户
	private Date auditTime;			  //审核时间
	private Integer auditId;		  //审核人ID
	private Integer temaId;			  //团队ID
	private String temaName;		  //团队名称
	
	
	public List<SysMenuBtn> getSysMenuBtns() {
		return sysMenuBtns;
	}
	public void setSysMenuBtns(List<SysMenuBtn> sysMenuBtns) {
		this.sysMenuBtns = sysMenuBtns;
	}
	
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
	public Integer getFreeTrialState() {
		return freeTrialState;
	}
	public void setFreeTrialState(Integer freeTrialState) {
		this.freeTrialState = freeTrialState;
	}
	public Integer getSmsUserId() {
		return smsUserId;
	}
	public void setSmsUserId(Integer smsUserId) {
		this.smsUserId = smsUserId;
	}
	public String getSmsUserName() {
		return smsUserName;
	}
	public void setSmsUserName(String smsUserName) {
		this.smsUserName = smsUserName;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public Integer getAuditId() {
		return auditId;
	}
	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getTemaId() {
		return temaId;
	}
	public void setTemaId(Integer temaId) {
		this.temaId = temaId;
	}
	public String getTemaName() {
		return temaName;
	}
	public void setTemaName(String temaName) {
		this.temaName = temaName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
