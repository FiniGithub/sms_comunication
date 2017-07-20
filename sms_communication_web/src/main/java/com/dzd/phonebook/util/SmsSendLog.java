package com.dzd.phonebook.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dzd.base.page.BasePage;
import com.dzd.phonebook.entity.SysMenuBtn;

/**
 * @Description:短信发送日志
 * @author:oygy
 * @time:2017年1月9日 下午2:27:51
 */
public class SmsSendLog extends BasePage implements Serializable {

    private List<SysMenuBtn> sysMenuBtns;//菜单拥有的按钮

    private Integer id;                        //id
    private String smsUserName;                //用户名称
    private String smsUserId;                //用户id
    private Integer sysUserId;                //系统用户ID
    private String smsUserEmail;            //用户账号
    private Integer groupNameId;            //通道组ID
    private Integer aisleId;                //通道ID
    private String aisleName;                //通道名称
    private String receivePhone;            //接收号码
    private String region;                    //归宿地
    private Float aisleDeduction;            //通道扣费金额【删除】
    private String sendPhone;                //下发号码
    private Integer state;                    //转态（等待发送，1：正在发送，2：终止发送，3发送完成）
    private Map<Integer, String> stateMap;
    private Integer stateBs;
    private Float agencyDeduction;            //代理扣费金额【删除】
    private int billingNnum;                // 计费条数
    private int sendNum;                    // 发送条数
    private Timestamp sendTime;                //发送时间
    private Timestamp timing;                //定时发送时间
    private int sendType;                //定时发送类型
    private Timestamp feedbackTime;            //反馈时间
    private String content;                    //发送内容
    private String signature;                // 签名
    private Timestamp createTime;            //消息创建时间
    private Integer supplier;                //运营商：0：联通，1：移动，2：电信
    private Integer bgztSelect;                //状态报告是否放回 0：已返回，1：未返回
    private Integer smsBid;

    private Integer receiveState;            //推送状态:1：未推送，2：已推送,3:推送成功,4:回执URL空
    private String fkState;            //反馈转态
    private String receiveCode;
    private Integer pushNum;                //推送次数

    private String temaName;                //团队名称
    private String nickName;                // 归属

    private String smsUserVal;                //sql 条件


    private String startInput; //开始时间
    private String endInput;   //结束时间
    private String logTime;        // 日志时间

    private Integer grossNum;     //总号码数
    private Integer succeedNum;  //发送成功数量
    private Integer failureNum;  //发送失败数量
    private Integer unknownNum;  //未知数量

    private Integer sendingNum;// 发送中数量
    private Integer sendFinishNum;// 发送完成数量
    private Integer type;// 0-发送失败,1-发送成功

    private String needPage;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public Timestamp getTiming() {
        return timing;
    }

    public void setTiming(Timestamp timing) {
        this.timing = timing;
    }

    public String getSmsUserId() {
        return smsUserId;
    }

    public void setSmsUserId(String smsUserId) {
        this.smsUserId = smsUserId;
    }

    public String getNeedPage() {
        return needPage;
    }

    public void setNeedPage(String needPage) {
        this.needPage = needPage;
    }

    public Integer getSendingNum() {
        return sendingNum;
    }

    public void setSendingNum(Integer sendingNum) {
        this.sendingNum = sendingNum;
    }

    public Integer getSendFinishNum() {
        return sendFinishNum;
    }

    public void setSendFinishNum(Integer sendFinishNum) {
        this.sendFinishNum = sendFinishNum;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public int getSendNum() {
        return sendNum;
    }

    public void setSendNum(int sendNum) {
        this.sendNum = sendNum;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSmsUserName() {
        return smsUserName;
    }

    public void setSmsUserName(String smsUserName) {
        this.smsUserName = smsUserName;
    }

    public String getSmsUserEmail() {
        return smsUserEmail;
    }

    public void setSmsUserEmail(String smsUserEmail) {
        this.smsUserEmail = smsUserEmail;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Float getAisleDeduction() {
        return aisleDeduction;
    }

    public void setAisleDeduction(Float aisleDeduction) {
        this.aisleDeduction = aisleDeduction;
    }

    public String getSendPhone() {
        return sendPhone;
    }

    public void setSendPhone(String sendPhone) {
        this.sendPhone = sendPhone;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Float getAgencyDeduction() {
        return agencyDeduction;
    }

    public void setAgencyDeduction(Float agencyDeduction) {
        this.agencyDeduction = agencyDeduction;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public Timestamp getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Timestamp feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getGroupNameId() {
        return groupNameId;
    }

    public void setGroupNameId(Integer groupNameId) {
        this.groupNameId = groupNameId;
    }

    public Integer getAisleId() {
        return aisleId;
    }

    public void setAisleId(Integer aisleId) {
        this.aisleId = aisleId;
    }

    public String getAisleName() {
        return aisleName;
    }

    public void setAisleName(String aisleName) {
        this.aisleName = aisleName;
    }

    public Integer getSupplier() {
        return supplier;
    }

    public void setSupplier(Integer supplier) {
        this.supplier = supplier;
    }

    public String getStartInput() {
        return startInput;
    }

    public void setStartInput(String startInput) {
        this.startInput = startInput;
    }

    public String getEndInput() {
        return endInput;
    }

    public void setEndInput(String endInput) {
        this.endInput = endInput;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Integer getStateBs() {
        return stateBs;
    }

    public void setStateBs(Integer stateBs) {
        this.stateBs = stateBs;
    }

    public Integer getSucceedNum() {
        return succeedNum;
    }

    public void setSucceedNum(Integer succeedNum) {
        this.succeedNum = succeedNum;
    }

    public Integer getFailureNum() {
        return failureNum;
    }

    public void setFailureNum(Integer failureNum) {
        this.failureNum = failureNum;
    }

    public Integer getUnknownNum() {
        return unknownNum;
    }

    public void setUnknownNum(Integer unknownNum) {
        this.unknownNum = unknownNum;
    }

    public Integer getReceiveState() {
        return receiveState;
    }

    public void setReceiveState(Integer receiveState) {
        this.receiveState = receiveState;
    }

    public Integer getPushNum() {
        return pushNum;
    }

    public void setPushNum(Integer pushNum) {
        this.pushNum = pushNum;
    }

    public int getBillingNnum() {
        return billingNnum;
    }

    public void setBillingNnum(int billingNnum) {
        this.billingNnum = billingNnum;
    }

    public String getTemaName() {
        return temaName;
    }

    public void setTemaName(String temaName) {
        this.temaName = temaName;
    }

    public String getSmsUserVal() {
        return smsUserVal;
    }

    public void setSmsUserVal(String smsUserVal) {
        this.smsUserVal = smsUserVal;
    }

    public Integer getGrossNum() {
        return grossNum;
    }

    public void setGrossNum(Integer grossNum) {
        this.grossNum = grossNum;
    }

    public String getFkState() {
        return fkState;
    }

    public void setFkState(String fkState) {
        this.fkState = fkState;
    }

    public Integer getBgztSelect() {
        return bgztSelect;
    }

    public void setBgztSelect(Integer bgztSelect) {
        this.bgztSelect = bgztSelect;
    }

    public String getReceiveCode() {
        return receiveCode;
    }

    public void setReceiveCode(String receiveCode) {
        this.receiveCode = receiveCode;
    }

    public Integer getSmsBid() {
        return smsBid;
    }

    public void setSmsBid(Integer smsBid) {
        this.smsBid = smsBid;
    }

    public Map<Integer, String> getStateMap() {
        stateMap = new HashMap<Integer, String>();
        stateMap.put(-1, "待发送");
        stateMap.put(0, "已发送");
        stateMap.put(2, "发送失败");
        stateMap.put(26, "发送失败");
        stateMap.put(3, "发送失败");
        stateMap.put(21, "网络错误");
        stateMap.put(22, "空号错误");
        stateMap.put(23, "发送上限");
        stateMap.put(24, "黑名单错误");
        stateMap.put(25, "审核失败");
        stateMap.put(27, "通道拒绝接收");
        stateMap.put(31, "通道返回失败");
        stateMap.put(99, "状态报告为未知");
        stateMap.put(100, "发送成功");
        return stateMap;
    }

    public void setStateMap(Map<Integer, String> stateMap) {
        this.stateMap = stateMap;
    }

}
