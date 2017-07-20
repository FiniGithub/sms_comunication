package com.dzd.phonebook.util;

import java.util.Date;

public class SmsAisleGroupHasSmsUser {

	private Integer id;     				//id
	private Integer smsAisleGroupId;		//通道组ID
	private Integer aisleTypeId;			//通道类型ID
	private String typeName;				//通道类型名称
	private Integer smsUserId;				//用户ID
	private Integer addType;				//通道组添加类型（1：后台添加（默认通道组），2：vip用户自选添加）
	private Float uprice;               	//普通通道组移动价格
	private Float mprice;					//普通通道联通价格
	private Float tprice;					//普通通道电信价格
	private Date createTime;             	//创建时间
	private Date updateTime;             	//创建时间
	private Integer userLevel;				//通道组开放级别
	
	
	private Float guprice;               	//非普通通道组移动价格
	private Float gmprice;					//非普通通道组联通价格
	private Float gtprice;					//非普通通道组电信价格
	
	private String sagName;					//通道组名称
	private Integer lv;						//星级
	private String lvName;                  //星级符号展示
	private String describes;				//通道组备注
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSmsAisleGroupId() {
		return smsAisleGroupId;
	}
	public void setSmsAisleGroupId(Integer smsAisleGroupId) {
		this.smsAisleGroupId = smsAisleGroupId;
	}
	public Integer getSmsUserId() {
		return smsUserId;
	}
	public void setSmsUserId(Integer smsUserId) {
		this.smsUserId = smsUserId;
	}
	public Float getUprice() {
		return uprice;
	}
	public void setUprice(Float uprice) {
		this.uprice = uprice;
	}
	public Float getMprice() {
		return mprice;
	}
	public void setMprice(Float mprice) {
		this.mprice = mprice;
	}
	public Float getTprice() {
		return tprice;
	}
	public void setTprice(Float tprice) {
		this.tprice = tprice;
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
	public Integer getAisleTypeId() {
		return aisleTypeId;
	}
	public void setAisleTypeId(Integer aisleTypeId) {
		this.aisleTypeId = aisleTypeId;
	}
	public Integer getAddType() {
		return addType;
	}
	public void setAddType(Integer addType) {
		this.addType = addType;
	}
	public Integer getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	public Float getGuprice() {
		return guprice;
	}
	public void setGuprice(Float guprice) {
		this.guprice = guprice;
	}
	public Float getGmprice() {
		return gmprice;
	}
	public void setGmprice(Float gmprice) {
		this.gmprice = gmprice;
	}
	public Float getGtprice() {
		return gtprice;
	}
	public void setGtprice(Float gtprice) {
		this.gtprice = gtprice;
	}
	public String getSagName() {
		return sagName;
	}
	public void setSagName(String sagName) {
		this.sagName = sagName;
	}
	public Integer getLv() {
		return lv;
	}
	public void setLv(Integer lv) {
		this.lv = lv;
		if(lv!=null || lv>=0){
			String name ="";
			for (int i = 0; i < lv; i++) {
				name+="★";
			}
			this.lvName = name;
		}
	}
	public String getLvName() {
		return lvName;
	}
	public String getDescribes() {
		return describes;
	}
	public void setDescribes(String describes) {
		this.describes = describes;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
