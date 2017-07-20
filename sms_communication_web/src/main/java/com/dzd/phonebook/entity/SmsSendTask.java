package com.dzd.phonebook.entity;

import java.util.Date;

import com.alibaba.druid.sql.visitor.functions.Char;

public class SmsSendTask {
	private Integer id;
	private Date sendTime;// 发送时间
	private String content;// 短信内容
	private Integer smsUserId;// 用户id
	private Integer state;// 状态 -1：等待发送， 0：正在发送，9：终止发送，100:发送完成,101:已结算
	private Date createTime;// 创建时间
	private Date updateTime;// 修改时间
	private Date timingTime;// 定时发送时间
	private Integer sendNum;// 发送数量
	private Integer billingNum;// 计费数量
	private Integer errorPhoneNum;// 错误号码数量
	private Integer blacklistPhoneNum;// 黑名单数量
	private Integer sendType;// 发送类型（0：即时发送，1：定时发送）
	private Integer smsAisleGroupId;// 通道组ID
	private Integer auditState;// 审核状态  0:等待审核，1：自动通过，2：人工通过，3：人工拒绝,4:终止审核
	private Date auditTime;// 审核时间
	private Integer auditId;// 审核人id
	private Integer returnState;// 返还状态0:未返还 ，1:已返还
	private String callbackUrl;// 状态推送URL
	private Integer actualNum;// 实际计费条数
	
	
	
	
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
	public Date getTimingTime() {
		return timingTime;
	}
	public void setTimingTime(Date timingTime) {
		this.timingTime = timingTime;
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
	public Integer getSmsAisleGroupId() {
		return smsAisleGroupId;
	}
	public void setSmsAisleGroupId(Integer smsAisleGroupId) {
		this.smsAisleGroupId = smsAisleGroupId;
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
	public Integer getAuditId() {
		return auditId;
	}
	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}
	public Integer getReturnState() {
		return returnState;
	}
	public void setReturnState(Integer returnState) {
		this.returnState = returnState;
	}
	 
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public Integer getActualNum() {
		return actualNum;
	}
	public void setActualNum(Integer actualNum) {
		this.actualNum = actualNum;
	}
	
	

}
