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
public class SmsAisleGroup extends BasePage {

	private List<SysMenuBtn> sysMenuBtns;// 菜单拥有的按钮

	private Integer id; // id
	private String name; // 通道组名称
	private Integer tid; // 通道组类型id
	private String typeName; // 通道组类型名称
	private Integer state; // 转态1：启用，2：停用
	private String describes; // 备注
	private Date createTime; // 创建时间
	private Date updateTime; // 修改时间
	private Float oneIntervalPrice; // 第一区间价格
	private Integer oneIntervalStart; // 第一区间开始数量
	private Integer oneIntervalEnd; // 第一区间结束数量

	private Float twoIntervalPrice; // 第二区间价格
	private Integer twoIntervalStart; // 第二区间开始数量
	private Integer twoIntervalEnd; // 第二区间结束数量

	private Float threeIntervalPrice; // 第三区间价格
	private Integer threeIntervalStart; // 第三区间开始数量
	private Integer threeIntervalEnd; // 第三区间结束数量

	private Integer succeedBilling; // 成功计费（计费模式）0：不支持，1：支持
	private Integer failureBilling; // 失败计费（计费模式）0：不支持，1：支持
	private Integer unknownBilling; // 无知计费（计费模式）0：不支持，1：支持

	private Integer type; // 通道类型0：普通通道，1：默认通道

	private Integer smsLength;// 长短信计费条数
	private String dredgeAM;// 开通时段 - 上午
	private String dredgePM;// 开通时段 - 下午
	private Integer unregTypeId;// 退订格式 0-关，1-开
	private String shieldingFieldId;// 屏蔽词外键
	private String notice;// 公告
	private String hint;// 提示
	private String signature;// 签名

	private Integer minSendNum;// 最少发送量


	private List<SmsAisleGroupHasSmsAisle> aslist; // 多个通道

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

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
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

	public List<SmsAisleGroupHasSmsAisle> getAslist() {
		return aslist;
	}

	public void setAslist(List<SmsAisleGroupHasSmsAisle> aslist) {
		this.aslist = aslist;
	}

	public Float getOneIntervalPrice() {
		return oneIntervalPrice;
	}

	public void setOneIntervalPrice(Float oneIntervalPrice) {
		this.oneIntervalPrice = oneIntervalPrice;
	}

	public Integer getOneIntervalStart() {
		return oneIntervalStart;
	}

	public void setOneIntervalStart(Integer oneIntervalStart) {
		this.oneIntervalStart = oneIntervalStart;
	}

	public Integer getOneIntervalEnd() {
		return oneIntervalEnd;
	}

	public void setOneIntervalEnd(Integer oneIntervalEnd) {
		this.oneIntervalEnd = oneIntervalEnd;
	}

	public Float getTwoIntervalPrice() {
		return twoIntervalPrice;
	}

	public void setTwoIntervalPrice(Float twoIntervalPrice) {
		this.twoIntervalPrice = twoIntervalPrice;
	}

	public Integer getTwoIntervalStart() {
		return twoIntervalStart;
	}

	public void setTwoIntervalStart(Integer twoIntervalStart) {
		this.twoIntervalStart = twoIntervalStart;
	}

	public Integer getTwoIntervalEnd() {
		return twoIntervalEnd;
	}

	public void setTwoIntervalEnd(Integer twoIntervalEnd) {
		this.twoIntervalEnd = twoIntervalEnd;
	}

	public Float getThreeIntervalPrice() {
		return threeIntervalPrice;
	}

	public void setThreeIntervalPrice(Float threeIntervalPrice) {
		this.threeIntervalPrice = threeIntervalPrice;
	}

	public Integer getThreeIntervalStart() {
		return threeIntervalStart;
	}

	public void setThreeIntervalStart(Integer threeIntervalStart) {
		this.threeIntervalStart = threeIntervalStart;
	}

	public Integer getThreeIntervalEnd() {
		return threeIntervalEnd;
	}

	public void setThreeIntervalEnd(Integer threeIntervalEnd) {
		this.threeIntervalEnd = threeIntervalEnd;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSmsLength() {
		return smsLength;
	}

	public void setSmsLength(Integer smsLength) {
		this.smsLength = smsLength;
	}

	public String getDredgeAM() {
		return dredgeAM;
	}

	public void setDredgeAM(String dredgeAM) {
		this.dredgeAM = dredgeAM;
	}

	public String getDredgePM() {
		return dredgePM;
	}

	public void setDredgePM(String dredgePM) {
		this.dredgePM = dredgePM;
	}

	public Integer getUnregTypeId() {
		return unregTypeId;
	}

	public void setUnregTypeId(Integer unregTypeId) {
		this.unregTypeId = unregTypeId;
	}

	 

	public String getShieldingFieldId() {
		return shieldingFieldId;
	}

	public void setShieldingFieldId(String shieldingFieldId) {
		this.shieldingFieldId = shieldingFieldId;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Integer getMinSendNum() {
		return minSendNum;
	}

	public void setMinSendNum(Integer minSendNum) {
		this.minSendNum = minSendNum;
	}
}
