package com.dzd.phonebook.util;

import java.util.Date;
import java.util.List;

import com.dzd.base.page.BasePage;
import com.dzd.phonebook.entity.SysMenuBtn;

/**
 *
 * @Description: 代理人信息
 * @author:oygy
 * @time:2016年12月31日 上午10:33:46
 */
public class SmsUser extends BasePage{

    private List<SysMenuBtn> sysMenuBtns;//菜单拥有的按钮

    private Integer id;
    private String name;          //姓名
    private String key;			  //（标识）
    private Integer smsUserBlankId;//账户ID
    private String phone;		   //电话
    private String email;		   //账户（电话）
    private Integer smsUserId;      //账号ID
    private Integer sysUserId;      //系统账号ID
    private Date createTime;        //创建时间
    private Date updateTime;		//修改时间
    private Integer state;          //状态（0：启用，1:禁用）
    private String contact;			//联系人   【删除】
    private Integer bid;			//业务员ID
    private String  nickName;       //业务员名称
    private Integer teamId;			//团队ID
    private String teamName;        //团队名称
    private String describes;        //描述
    private String signature;       //签名
    private Integer signatureCheck;  //签名是否校验 (0：校验，1：不校验）
    private String reportUrl;		//报告推送地址
    private String replyUrl;		//上行推送地址
    private Integer userLevel;      //用户级别如（1：普通用户，2：VIP1,2：VIP2）   【删除】

    private Integer statistical;	 //统计查询出来的所有条数

    private Integer aisleGroupId;      //通道组ID
    private String aisleGroup;     		 //通道组
    private Integer groupTypeId;      //通道组类型（账户类型）ID
    private String aisleGroupType;      //通道组类型（账户类型）
    private String smsAisleName;// 通道名
    private String smsAisleTypeId;// 通道类型

    private Integer blankNum;			 //模板数量
    private Integer signatureNum;		 //签名数量

    private Float  awardMoney;      //授信额度  【删除】
    private Float money;            //账户金额	   【删除】
    private Integer freeAmount;     //免费短信条数  【删除】
    private Integer surplusNum;     //剩余短信条数

    private String auditTime;        //审核时间

    private int succeedNum;  //发送成功数量
    private int failureNum;  //发送失败数量
    private int unknownSucceedNum;  //未知成功数量
    private int unknownFailureNum;  //未知失败数量

    private Integer succeedNumUs;  //移动发送成功数量
    private Integer failureNumUs;  //移动发送失败数量
    private Integer unknownNumUs;  //移动未知数量

    private Integer succeedNumMs;  //联通发送成功数量
    private Integer failureNumMs;  //联通发送失败数量
    private Integer unknownNumMs;  //联通未知数量

    private Integer succeedNumTs;  //电信发送成功数量
    private Integer failureNumTs;  //电信发送失败数量
    private Integer unknownNumTs;  //电信未知数量

    private int billingNum;// 计费量
    private int actualNum;// 实际计费量
    private Integer sendNum;     //发送量
    private Float expectMoney;   //预扣金额【删除】
    private Float  consumeMoney; //消费金额【删除】


    private Long sumSucceedNumUs;  //移动代理总发送成功数量
    private Long sumFailureNumUs;  //移动代理总发送失败数量
    private Long sumUnknownNumUs;  //移动代理总未知数量

    private Long sumSucceedNumMs;  //联通代理总发送成功数量
    private Long sumFailureNumMs;  //联通代理总发送失败数量
    private Long sumUnknownNumMs;  //联通代理总未知数量

    private Long sumSucceedNumTs;  //电信代理总发送成功数量
    private Long sumFailureNumTs;  //电信代理总发送失败数量
    private Long sumUnknownNumTs;  //电信代理总未知数量

    private Long sumSendNum;       //代理总发送量
    private Long sumActualNum;     //代理实际总计费量
    private Long sumBillingNum;    //代理总计费量
    private Float sumExpectMoney;   //代理总预扣金额
    private Float   sumConsumeMoney; //代理总消费金额

    private Integer httpProtocol;       //http协议支持状态（0：不支持，1：支持）
    private Integer cmppProtocol;		//cmpp协议支持状态（0：不支持，1：支持）
    private String joinupCoding;		//通道接入码
    private String firmName;	        //企业用户名
    private String firmPwd;				//企业密码
    private Integer joinuoMax;			//最大接入数
    private String firmIp;				//企业IP(多个逗号隔开)
    private Integer defaultAgid;        //默认通道组ID

    private Integer sumNum;  //短信总量
    private Integer usedNum; //已用短信量
    private Integer networkChargingState; //网上充值状态0：开通  1：关闭
    private String roleName;   //用户类型，角色名称
    private String qq;  //qq号
    private String userEmail;  //邮件
    private String telphone;  //电话
    private String address;  //地址
    private Integer signatureType;  //签名类型0：绑定签名  1：自定义签名
    private Integer smsReplyState;  //短信回复状态 0：开通  1：关闭
    private Integer checkState;     //申请账户审核状态0：带审核  1：已经注册
    private String pwd;
    private Integer proposerId;   //申请人id
    private Integer roleId;  //用户角色id

    private Integer verifyType;// 发送短信是否需要短信验证，0：需要，1：不需要

    public Integer getSmsUserId()
	{
		return smsUserId;
	}

	public void setSmsUserId(Integer smsUserId)
	{
		this.smsUserId = smsUserId;
	}

	public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getProposerId() {
        return proposerId;
    }
    public void setProposerId(Integer proposerId) {
        this.proposerId = proposerId;
    }

    public void setSucceedNum(int succeedNum) {
        this.succeedNum = succeedNum;
    }

    public void setFailureNum(int failureNum) {
        this.failureNum = failureNum;
    }

    public void setUnknownSucceedNum(int unknownSucceedNum) {
        this.unknownSucceedNum = unknownSucceedNum;
    }

    public void setUnknownFailureNum(int unknownFailureNum) {
        this.unknownFailureNum = unknownFailureNum;
    }

    public Integer getNetworkChargingState() {
        return networkChargingState;
    }

    public void setNetworkChargingState(Integer networkChargingState) {
        this.networkChargingState = networkChargingState;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(Integer signatureType) {
        this.signatureType = signatureType;
    }

    public Integer getSmsReplyState() {
        return smsReplyState;
    }

    public void setSmsReplyState(Integer smsReplyState) {
        this.smsReplyState = smsReplyState;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public Integer getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getSumNum() {
        return sumNum;
    }

    public void setSumNum(Integer sumNum) {
        this.sumNum = sumNum;
    }

    public Integer getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(Integer usedNum) {
        this.usedNum = usedNum;
    }
    public Integer getSucceedNum()
    {
        return succeedNum;
    }
    public void setSucceedNum(Integer succeedNum)
    {
        this.succeedNum = succeedNum;
    }
    public Integer getFailureNum()
    {
        return failureNum;
    }
    public void setFailureNum(Integer failureNum)
    {
        this.failureNum = failureNum;
    }
    public Integer getUnknownSucceedNum()
    {
        return unknownSucceedNum;
    }
    public void setUnknownSucceedNum(Integer unknownSucceedNum)
    {
        this.unknownSucceedNum = unknownSucceedNum;
    }
    public Integer getUnknownFailureNum()
    {
        return unknownFailureNum;
    }
    public void setUnknownFailureNum(Integer unknownFailureNum)
    {
        this.unknownFailureNum = unknownFailureNum;
    }

    public Long getSumActualNum()
    {
        return sumActualNum;
    }
    public void setSumActualNum(Long sumActualNum)
    {
        this.sumActualNum = sumActualNum;
    }
    public Long getSumBillingNum()
    {
        return sumBillingNum;
    }
    public void setSumBillingNum(Long sumBillingNum)
    {
        this.sumBillingNum = sumBillingNum;
    }

    public int getBillingNum()
    {
        return billingNum;
    }
    public void setBillingNum(int billingNum)
    {
        this.billingNum = billingNum;
    }
    public int getActualNum()
    {
        return actualNum;
    }
    public void setActualNum(int actualNum)
    {
        this.actualNum = actualNum;
    }

    public String getSmsAisleName()
    {
        return smsAisleName;
    }
    public void setSmsAisleName(String smsAisleName)
    {
        this.smsAisleName = smsAisleName;
    }
    public String getSmsAisleTypeId()
    {
        return smsAisleTypeId;
    }
    public void setSmsAisleTypeId(String smsAisleTypeId)
    {
        this.smsAisleTypeId = smsAisleTypeId;
    }
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
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public Integer getSmsUserBlankId() {
        return smsUserBlankId;
    }
    public void setSmsUserBlankId(Integer smsUserBlankId) {
        this.smsUserBlankId = smsUserBlankId;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getSysUserId() {
        return sysUserId;
    }
    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
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
    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public Float getAwardMoney() {
        return awardMoney;
    }
    public void setAwardMoney(Float awardMoney) {
        this.awardMoney = awardMoney;
    }
    public Float getMoney() {
        return money;
    }
    public void setMoney(Float money) {
        this.money = money;
    }
    public Integer getFreeAmount() {
        return freeAmount;
    }
    public void setFreeAmount(Integer freeAmount) {
        this.freeAmount = freeAmount;
    }
    public String getDescribes() {
        return describes;
    }
    public void setDescribes(String describes) {
        this.describes = describes;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public void setBid(Integer bid) {
        this.bid = bid;
    }
    public Integer getBid() {
        return bid;
    }
    public String getAisleGroup() {
        return aisleGroup;
    }
    public void setAisleGroup(String aisleGroup) {
        this.aisleGroup = aisleGroup;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
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
    public Integer getSignatureCheck() {
        return signatureCheck;
    }
    public void setSignatureCheck(Integer signatureCheck) {
        this.signatureCheck = signatureCheck;
    }
    public Integer getSendNum() {
        return sendNum;
    }
    public void setSendNum(Integer sendNum) {
        this.sendNum = sendNum;
    }
    public Float getConsumeMoney() {
        return consumeMoney;
    }
    public void setConsumeMoney(Float consumeMoney) {
        this.consumeMoney = consumeMoney;
    }
    public Long getSumSendNum() {
        return sumSendNum;
    }
    public void setSumSendNum(Long sumSendNum) {
        this.sumSendNum = sumSendNum;
    }
    public Float getSumConsumeMoney() {
        return sumConsumeMoney;
    }
    public void setSumConsumeMoney(Float sumConsumeMoney) {
        this.sumConsumeMoney = sumConsumeMoney;
    }
    public Integer getSucceedNumUs() {
        return succeedNumUs;
    }
    public void setSucceedNumUs(Integer succeedNumUs) {
        this.succeedNumUs = succeedNumUs;
    }
    public Integer getFailureNumUs() {
        return failureNumUs;
    }
    public void setFailureNumUs(Integer failureNumUs) {
        this.failureNumUs = failureNumUs;
    }
    public Integer getUnknownNumUs() {
        return unknownNumUs;
    }
    public void setUnknownNumUs(Integer unknownNumUs) {
        this.unknownNumUs = unknownNumUs;
    }
    public Integer getSucceedNumMs() {
        return succeedNumMs;
    }
    public void setSucceedNumMs(Integer succeedNumMs) {
        this.succeedNumMs = succeedNumMs;
    }
    public Integer getFailureNumMs() {
        return failureNumMs;
    }
    public void setFailureNumMs(Integer failureNumMs) {
        this.failureNumMs = failureNumMs;
    }
    public Integer getUnknownNumMs() {
        return unknownNumMs;
    }
    public void setUnknownNumMs(Integer unknownNumMs) {
        this.unknownNumMs = unknownNumMs;
    }
    public Integer getSucceedNumTs() {
        return succeedNumTs;
    }
    public void setSucceedNumTs(Integer succeedNumTs) {
        this.succeedNumTs = succeedNumTs;
    }
    public Integer getFailureNumTs() {
        return failureNumTs;
    }
    public void setFailureNumTs(Integer failureNumTs) {
        this.failureNumTs = failureNumTs;
    }
    public Integer getUnknownNumTs() {
        return unknownNumTs;
    }
    public void setUnknownNumTs(Integer unknownNumTs) {
        this.unknownNumTs = unknownNumTs;
    }
    public Long getSumSucceedNumUs() {
        return sumSucceedNumUs;
    }
    public void setSumSucceedNumUs(Long sumSucceedNumUs) {
        this.sumSucceedNumUs = sumSucceedNumUs;
    }
    public Long getSumFailureNumUs() {
        return sumFailureNumUs;
    }
    public void setSumFailureNumUs(Long sumFailureNumUs) {
        this.sumFailureNumUs = sumFailureNumUs;
    }
    public Long getSumUnknownNumUs() {
        return sumUnknownNumUs;
    }
    public void setSumUnknownNumUs(Long sumUnknownNumUs) {
        this.sumUnknownNumUs = sumUnknownNumUs;
    }
    public Long getSumSucceedNumMs() {
        return sumSucceedNumMs;
    }
    public void setSumSucceedNumMs(Long sumSucceedNumMs) {
        this.sumSucceedNumMs = sumSucceedNumMs;
    }
    public Long getSumFailureNumMs() {
        return sumFailureNumMs;
    }
    public void setSumFailureNumMs(Long sumFailureNumMs) {
        this.sumFailureNumMs = sumFailureNumMs;
    }
    public Long getSumUnknownNumMs() {
        return sumUnknownNumMs;
    }
    public void setSumUnknownNumMs(Long sumUnknownNumMs) {
        this.sumUnknownNumMs = sumUnknownNumMs;
    }
    public Long getSumSucceedNumTs() {
        return sumSucceedNumTs;
    }
    public void setSumSucceedNumTs(Long sumSucceedNumTs) {
        this.sumSucceedNumTs = sumSucceedNumTs;
    }
    public Long getSumFailureNumTs() {
        return sumFailureNumTs;
    }
    public void setSumFailureNumTs(Long sumFailureNumTs) {
        this.sumFailureNumTs = sumFailureNumTs;
    }
    public Long getSumUnknownNumTs() {
        return sumUnknownNumTs;
    }
    public void setSumUnknownNumTs(Long sumUnknownNumTs) {
        this.sumUnknownNumTs = sumUnknownNumTs;
    }
    public String getAuditTime() {
        return auditTime;
    }
    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }
    public Float getExpectMoney() {
        return expectMoney;
    }
    public void setExpectMoney(Float expectMoney) {
        this.expectMoney = expectMoney;
    }
    public Float getSumExpectMoney() {
        return sumExpectMoney;
    }
    public void setSumExpectMoney(Float sumExpectMoney) {
        this.sumExpectMoney = sumExpectMoney;
    }
    public Integer getUserLevel() {
        return userLevel;
    }
    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }
    public Integer getHttpProtocol() {
        return httpProtocol;
    }
    public void setHttpProtocol(Integer httpProtocol) {
        this.httpProtocol = httpProtocol;
    }
    public Integer getCmppProtocol() {
        return cmppProtocol;
    }
    public void setCmppProtocol(Integer cmppProtocol) {
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
    public Integer getJoinuoMax() {
        return joinuoMax;
    }
    public void setJoinuoMax(Integer joinuoMax) {
        this.joinuoMax = joinuoMax;
    }
    public String getFirmIp() {
        return firmIp;
    }
    public void setFirmIp(String firmIp) {
        this.firmIp = firmIp;
    }
    public Integer getDefaultAgid() {
        return defaultAgid;
    }
    public void setDefaultAgid(Integer defaultAgid) {
        this.defaultAgid = defaultAgid;
    }
    public Integer getSurplusNum() {
        return surplusNum;
    }
    public void setSurplusNum(Integer surplusNum) {
        this.surplusNum = surplusNum;
    }
    public Integer getBlankNum() {
        return blankNum;
    }
    public void setBlankNum(Integer blankNum) {
        this.blankNum = blankNum;
    }
    public Integer getSignatureNum() {
        return signatureNum;
    }
    public void setSignatureNum(Integer signatureNum) {
        this.signatureNum = signatureNum;
    }
    public Integer getGroupTypeId() {
        return groupTypeId;
    }
    public void setGroupTypeId(Integer groupTypeId) {
        this.groupTypeId = groupTypeId;
    }
    public String getAisleGroupType() {
        return aisleGroupType;
    }
    public void setAisleGroupType(String aisleGroupType) {
        this.aisleGroupType = aisleGroupType;
    }
    public Integer getTeamId() {
        return teamId;
    }
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public Integer getStatistical() {
        return statistical;
    }
    public void setStatistical(Integer statistical) {
        this.statistical = statistical;
    }
    public Integer getAisleGroupId() {
        return aisleGroupId;
    }
    public void setAisleGroupId(Integer aisleGroupId) {
        this.aisleGroupId = aisleGroupId;
    }

}
