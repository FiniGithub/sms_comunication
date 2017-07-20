package com.dzd.phonebook.entity;

import java.sql.Timestamp;

import com.dzd.base.page.BasePage;

/**
 * 签名实体类
 * @author CHENCHAO
 * @date 2017-03-28 17:44:00
 *
 */
public class SmsUserSignature extends BasePage {
	
	private Integer id;
	private String signature;// 签名名称
	private String content;// 备注
	private Integer freeTrialState;// 模板状态：0：待审核，1：使用中，2：停用，3：审核不通过
	private Integer smsUserId;// 用户编号
	private Timestamp auditTime;// 审核时间
	private Integer auditId;// 审核人id
	private Timestamp createTime;// 创建时间
	private Timestamp updateTime;// 最后修改时间
	private Integer deleted;// 标记是否删除 0-未删除，1-已删除
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public Timestamp getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}
	public Integer getAuditId() {
		return auditId;
	}
	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

}
