package com.dzd.phonebook.util;

import java.util.Date;
import java.util.List;

import com.dzd.base.page.BasePage;
import com.dzd.phonebook.entity.SysMenuBtn;

/**
 * @Description:通道
 * @author:oygy
 * @time:2017年1月3日 下午4:54:18
 */
public class SmsAisle extends BasePage{
	
	private List<SysMenuBtn> sysMenuBtns;//菜单拥有的按钮
	
	private Integer id; 				 //id
	private String name;				 //通道名称
	private Integer smsAisleTypeId;      //通道类型id
	private String typeName;			 //通道类型名称
	private Integer smsRegionId;		 //区域Id
	private String regionName;			 //区域中文名称
	private Float remainingMoney;        //通道所剩金额
	private String numOrMoney;           //金额或者条数
	private Integer remainingNum;        //通道所剩可以发送的短信条数
	private Integer shieldingFieldId;    //屏蔽词id
	private String shieldingField;       //通道的屏蔽词
	private Integer state;			     //转态1：启用，2：停用，3：自动停用	   
	private String comment;				 //备注
	private Integer maxNum;				 //每日上限数量
	private Integer singleNum; 			 //单包发送的量（每次条用该通道最多发送的短信数量）
	private Integer signatureState;	     //签名转态 1：签名前置 2：签名后置
	private Integer mobileSate;			 //移动支持状态（0：不支持，1：支持）
	private Float mobileMoney;				 //金额（每条短信的费用）
	private Integer unicomSate;			 //联通支持状态（0：不支持，1：支持）
	private Float unicomMoney;				 //金额（每条短信的费用）
	private Integer telecomState;		 //电信支持状态（0：不支持，1：支持）
	private Float telecomMoney;				 //金额（每条短信的费用）
	private Date createTime;        	//创建时间
	private Date updateTime;			//修改时间
	private String className;			//通道源
	private String optionValue;			//通道源json参数
	private Integer succeedBilling;     //成功计费（计费模式）0：不支持，1：支持
	private Integer failureBilling;     //失败计费（计费模式）0：不支持，1：支持
	private Integer unknownBilling;     //无知计费（计费模式）0：不支持，1：支持
	private String extra;				//json参数拼接（Md5加密）
	private Integer startCount;// 起发量
	private Float money;// 通道成本


	
	
	
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
	public Integer getSmsAisleTypeId() {
		return smsAisleTypeId;
	}
	public void setSmsAisleTypeId(Integer smsAisleTypeId) {
		this.smsAisleTypeId = smsAisleTypeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getSmsRegionId() {
		return smsRegionId;
	}
	public void setSmsRegionId(Integer smsRegionId) {
		this.smsRegionId = smsRegionId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public Float getRemainingMoney() {
		return remainingMoney;
	}
	public void setRemainingMoney(Float remainingMoney) {
		this.remainingMoney = remainingMoney;
	}
	public Integer getRemainingNum() {
		return remainingNum;
	}
	public void setRemainingNum(Integer remainingNum) {
		this.remainingNum = remainingNum;
	}
	public String getShieldingField() {
		return shieldingField;
	}
	public void setShieldingField(String shieldingField) {
		this.shieldingField = shieldingField;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}
	public Integer getSingleNum() {
		return singleNum;
	}
	public void setSingleNum(Integer singleNum) {
		this.singleNum = singleNum;
	}
	public Integer getSignatureState() {
		return signatureState;
	}
	public void setSignatureState(Integer signatureState) {
		this.signatureState = signatureState;
	}
	public Integer getMobileSate() {
		return mobileSate;
	}
	public void setMobileSate(Integer mobileSate) {
		this.mobileSate = mobileSate;
	}
	public Integer getUnicomSate() {
		return unicomSate;
	}
	public void setUnicomSate(Integer unicomSate) {
		this.unicomSate = unicomSate;
	}
	public Integer getTelecomState() {
		return telecomState;
	}
	public void setTelecomState(Integer telecomState) {
		this.telecomState = telecomState;
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
	public String getNumOrMoney() {
		return numOrMoney;
	}
	public void setNumOrMoney(String numOrMoney) {
		this.numOrMoney = numOrMoney;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	public Integer getShieldingFieldId() {
		return shieldingFieldId;
	}
	public void setShieldingFieldId(Integer shieldingFieldId) {
		this.shieldingFieldId = shieldingFieldId;
	}
	public Float getMobileMoney() {
		return mobileMoney;
	}
	public void setMobileMoney(Float mobileMoney) {
		this.mobileMoney = mobileMoney;
	}
	public Float getUnicomMoney() {
		return unicomMoney;
	}
	public void setUnicomMoney(Float unicomMoney) {
		this.unicomMoney = unicomMoney;
	}
	public Float getTelecomMoney() {
		return telecomMoney;
	}
	public void setTelecomMoney(Float telecomMoney) {
		this.telecomMoney = telecomMoney;
	}
	public Integer getSucceedBilling() {
		return succeedBilling;
	}
	public void setSucceedBilling(Integer succeedBilling) {
		this.succeedBilling = succeedBilling;
	}
	public Integer getFailureBilling() {
		return failureBilling;
	}
	public void setFailureBilling(Integer failureBilling) {
		this.failureBilling = failureBilling;
	}
	public Integer getUnknownBilling() {
		return unknownBilling;
	}
	public void setUnknownBilling(Integer unknownBilling) {
		this.unknownBilling = unknownBilling;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}

	public Integer getStartCount() {
		return startCount;
	}

	public void setStartCount(Integer startCount) {
		this.startCount = startCount;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}
}