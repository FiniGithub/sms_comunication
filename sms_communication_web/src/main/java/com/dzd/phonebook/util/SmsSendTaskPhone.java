package com.dzd.phonebook.util;

import java.util.Date;

/**
 * 发送号码表
 * 
 * @author Fini
 *
 */
public class SmsSendTaskPhone {
	private Integer id; 			//id
	private String phone;	 		//电话号码
	private Float fee; 				//价格【删除】
	private Integer state; 			//状态:-1：待发送,0：已提交，21; //网络错误，22; //空号错误，23;
									//发送上限，24; //黑名单错误，25：审核失败，26; //发送失败，27;
									//通道拒绝接收，2：发送失败，3未知，31:通道返回失败，99; //状态报告为未知，100：成功.
	private Integer smsSendTaskId;	//消息ID
	private Integer smsSendLogId;	//发送ID
	private Date createTime;		//创建时间
	private Date updateTime;		//最后修改时间
	private String region;			//归属地
	private Integer iId;			//归属地ID
	private Integer supplier;		//供应商类型：0：联通，1：移动，2：电信
	private Integer billingNum;		//计费条数（如：发送信息过长收两条或者三条短信的价格）
	private Date sendTime;			//发送时间
	private Integer pushState;		//推送状态（1：未推送，2：已推送,3:推送成功,4:回执URL空）注：推送给下家
	private Integer pushNum;		//状态推送次数
	private String receiveState;	//状态 DELIVRD成功，UNDELIV失败.
	private Integer receiveode;		//状态码
	private Date receiveTime;		//反馈时间
	private Integer aid;			//通道ID
	
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
	public Float getFee() {
		return fee;
	}
	public void setFee(Float fee) {
		this.fee = fee;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getSmsSendTaskId() {
		return smsSendTaskId;
	}
	public void setSmsSendTaskId(Integer smsSendTaskId) {
		this.smsSendTaskId = smsSendTaskId;
	}
	public Integer getSmsSendLogId() {
		return smsSendLogId;
	}
	public void setSmsSendLogId(Integer smsSendLogId) {
		this.smsSendLogId = smsSendLogId;
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
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public Integer getiId() {
		return iId;
	}
	public void setiId(Integer iId) {
		this.iId = iId;
	}
	public Integer getSupplier() {
		return supplier;
	}
	public void setSupplier(Integer supplier) {
		this.supplier = supplier;
	}
	public Integer getBillingNum() {
		return billingNum;
	}
	public void setBillingNum(Integer billingNum) {
		this.billingNum = billingNum;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getPushState() {
		return pushState;
	}
	public void setPushState(Integer pushState) {
		this.pushState = pushState;
	}
	public Integer getPushNum() {
		return pushNum;
	}
	public void setPushNum(Integer pushNum) {
		this.pushNum = pushNum;
	}
	public String getReceiveState() {
		return receiveState;
	}
	public void setReceiveState(String receiveState) {
		this.receiveState = receiveState;
	}
	public Integer getReceiveode() {
		return receiveode;
	}
	public void setReceiveode(Integer receiveode) {
		this.receiveode = receiveode;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	public Integer getAid() {
		return aid;
	}
	public void setAid(Integer aid) {
		this.aid = aid;
	}
}
