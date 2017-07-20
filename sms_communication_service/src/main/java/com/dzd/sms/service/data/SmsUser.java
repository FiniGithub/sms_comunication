package com.dzd.sms.service.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-1-9.
 */
public class SmsUser implements Serializable {
    Long id = 0L;
    Long sysId = 0L;
    String name;
    String account;
    String password;
    String phone;
    Double balance = 0.0;
    String key;
    String signature="";
    int state; //状态0：启用；1：禁用
    boolean signatureCheck = true;
    String reportUrl = null;
    String replyUrl = null;

    boolean httpProtocol=false;
    boolean cmppProtocol=false;
    String joinupCoding="";
    String firmName="";
    String firmPwd="";
    int joinuoMax=0;
    Long defaultGid=0l;
    
    private Integer aisleGroupId;      //通道组ID
   
    public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public Integer getAisleGroupId()
	{
		return aisleGroupId;
	}

	public void setAisleGroupId(Integer aisleGroupId)
	{
		this.aisleGroupId = aisleGroupId;
	}

	public Long getSysId()
	{
		return sysId;
	}

	public void setSysId(Long sysId)
	{
		this.sysId = sysId;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        if( signature !=null ) {
            this.signature = signature;
        }
    }

    public boolean isSignatureCheck() {
        return signatureCheck;
    }

    public void setSignatureCheck(boolean signatureCheck) {
        this.signatureCheck = signatureCheck;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getReplyUrl() {
        return replyUrl;
    }

    public void setReplyUrl(String replyUrl) {
        this.replyUrl = replyUrl;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isHttpProtocol() {
        return httpProtocol;
    }

    public void setHttpProtocol(boolean httpProtocol) {
        this.httpProtocol = httpProtocol;
    }

    public boolean isCmppProtocol() {
        return cmppProtocol;
    }

    public void setCmppProtocol(boolean cmppProtocol) {
        this.cmppProtocol = cmppProtocol;
    }

    public String getJoinupCoding() {
        return joinupCoding;
    }

    public void setJoinupCoding(String joinupCoding) {
        this.joinupCoding = joinupCoding;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getFirmPwd() {
        return firmPwd;
    }

    public void setFirmPwd(String firmPwd) {
        this.firmPwd = firmPwd;
    }

    public int getJoinuoMax() {
        return joinuoMax;
    }

    public void setJoinuoMax(int joinuoMax) {
        this.joinuoMax = joinuoMax;
    }

    public Long getDefaultGid() {
        return defaultGid;
    }

    public void setDefaultGid(Long defaultGid) {
        this.defaultGid = defaultGid;
    }
}
