package com.dzd.phonebook.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dzd.base.page.BasePage;
import com.dzd.phonebook.entity.SysMenuBtn;

public class SmsSendTask extends BasePage{
	/**
	 * 发送任务
	 * @author Fini
	 */

	private List<SysMenuBtn> sysMenuBtns;//菜单拥有的按钮
	private List<SmsSendTaskPhone> smsSendTaskPhones = new ArrayList<SmsSendTaskPhone>(); //任务发送的号码表
	
	private Integer id;						//id
	private Date sendTime;					//发送时间
	private String content;					//发送内容
	private Integer smsUserId;				//用户ID
	private String smsUserName;				//用户名称
	private String smsUserEmail;			//用户账号
	private Integer state;					//状态（消息的状态：-1：等待发送， 0：正在发送，9：终止发送，100:发送完成）
											//	  （每条号码的状态：-1：未知,0：等待发送，1：发送成功，2：发送失败）
	private Date timingTime;				//定时发送的时间
	private Float expectDeduction;			//预计扣费【删除】
	private Float actualDeduction;			//实际扣费【删除】
	private Integer sendNum;				//发送数量
	private Integer billingNum;				//计费数量
	private Integer actualNum;				//实际计费数量
	
	
	private Integer sendNumCount;				//发送数量统计
	private Integer billingNumCount;			//计费数量统计
	private Integer actualNumCount;				//实际计费数量统计
	
	private Integer errorPhoneNum;			//错误号码数量
	private Integer blacklistPhoneNum;		//黑名单数量
	private Integer sendType;				//发送类型（0：即时发送，1：定时发送）
	private Date createTime;        		//创建时间
	private Date updateTime;				//修改时间
	private Integer groupId;				//通道组ID
	private Integer tid;					//通道组类型ID
	private String groupName;				//通道组名称
	private Integer auditState;				//审核状态0:等待审核，1：自动通过，2：人工通过，3：人工拒绝
	private Date auditTime;					//审核时间
	private Integer audiId;					//审核人
	private String temaName;				//团队名称
	private Integer sendResendState;        //重发状态-  0：未重发,1：已重发
	
	
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
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getSmsUserId() {
		return smsUserId;
	}
	public void setSmsUserId(Integer smsUserId) {
		this.smsUserId = smsUserId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getTimingTime() {
		return timingTime;
	}
	public void setTimingTime(Date timingTime) {
		this.timingTime = timingTime;
	}
	public Float getExpectDeduction() {
		return expectDeduction;
	}
	public void setExpectDeduction(Float expectDeduction) {
		this.expectDeduction = expectDeduction;
	}
	public Float getActualDeduction() {
		return actualDeduction;
	}
	public void setActualDeduction(Float actualDeduction) {
		this.actualDeduction = actualDeduction;
	}
	public Integer getSendNum() {
		return sendNum;
	}
	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
	}
	public Integer getBillingNum() {
		return billingNum;
	}
	public void setBillingNum(Integer billingNum) {
		this.billingNum = billingNum;
	}
	public Integer getErrorPhoneNum() {
		return errorPhoneNum;
	}
	public void setErrorPhoneNum(Integer errorPhoneNum) {
		this.errorPhoneNum = errorPhoneNum;
	}
	public Integer getBlacklistPhoneNum() {
		return blacklistPhoneNum;
	}
	public void setBlacklistPhoneNum(Integer blacklistPhoneNum) {
		this.blacklistPhoneNum = blacklistPhoneNum;
	}
	public Integer getSendType() {
		return sendType;
	}
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
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
	public String getSmsUserName() {
		return smsUserName;
	}
	public void setSmsUserName(String smsUserName) {
		this.smsUserName = smsUserName;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getSmsUserEmail() {
		return smsUserEmail;
	}
	public void setSmsUserEmail(String smsUserEmail) {
		this.smsUserEmail = smsUserEmail;
	}
	public Integer getAuditState() {
		return auditState;
	}
	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public Integer getAudiId() {
		return audiId;
	}
	public void setAudiId(Integer audiId) {
		this.audiId = audiId;
	}
	public Integer getActualNum() {
		return actualNum;
	}
	public void setActualNum(Integer actualNum) {
		this.actualNum = actualNum;
	}
	public String getTemaName() {
		return temaName;
	}
	public void setTemaName(String temaName) {
		this.temaName = temaName;
	}
	public Integer getSendNumCount() {
		return sendNumCount;
	}
	public void setSendNumCount(Integer sendNumCount) {
		this.sendNumCount = sendNumCount;
	}
	public Integer getBillingNumCount() {
		return billingNumCount;
	}
	public void setBillingNumCount(Integer billingNumCount) {
		this.billingNumCount = billingNumCount;
	}
	public Integer getActualNumCount() {
		return actualNumCount;
	}
	public void setActualNumCount(Integer actualNumCount) {
		this.actualNumCount = actualNumCount;
	}
	public Integer getTid() {
		return tid;
	}
	public void setTid(Integer tid) {
		this.tid = tid;
	}
	public Integer getSendResendState() {
		return sendResendState;
	}
	public void setSendResendState(Integer sendResendState) {
		this.sendResendState = sendResendState;
	}
	public List<SmsSendTaskPhone> getSmsSendTaskPhones() {
		return smsSendTaskPhones;
	}
	public void setSmsSendTaskPhones(List<SmsSendTaskPhone> smsSendTaskPhones) {
		this.smsSendTaskPhones = smsSendTaskPhones;
	}
	
}
