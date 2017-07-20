package com.dzd.sms.service.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by IDEA </br>
 * Author: hz-liang </br>
 * Date: 2017/3/28 </br>
 * Time: 15:32 </br>
 */
public class SmsRequestParameter implements Serializable {

    String uid; // 用户唯一标识
    String sysId;
    String timestamp;
    String sign; // 用户唯一标识
    List<String> mobile; // 接收的手机号
    String text; // 短信内容
    String iid;
    String page_size;
    String start_time; // 接收的手机号;发送多个手机号请以逗号分隔
    String end_time; // 接收的手机号;发送多个手机号请以逗号分隔
    String page_num; // 接收的手机号;发送多个手机号请以逗号分隔
    Date timing; // 定时发送时间
    int errorPhoneNumber; // 重复号码数
    Long groupTypeId;// 通道组id
    boolean isCheck = false;
    boolean signatureCheck;
    String smsUserSign;
    Integer smsUserState;// 用户状态 0：启用；1：禁用
    private Integer verifyType;// 是否为首次登陆，超过50条需要验证码 0:需要,1：不需要
    private Map<String, Object> validPhoneMap;// 有效号码map


    private String dredgeAM;// 开通时间段 开始时间
    private String dredgePM;// 开通时间段 截止时间
    private Integer unregTypeId; // 退订格式 0-关，1-开
    private Integer sendType;// 发送类型
    private Integer aisleGroupState;// 通道组状态 1：启用 2：停用
    private Integer aisleLongNum;// 通道组长短信计费
    private Integer startCount;// 起发量
    private String shieldingFieldId;// 敏感词id数组


    public Integer getStartCount() {
        return startCount;
    }

    public void setStartCount(Integer startCount) {
        this.startCount = startCount;
    }

    public String getSmsUserSign() {
        return smsUserSign;
    }

    public void setSmsUserSign(String smsUserSign) {
        this.smsUserSign = smsUserSign;
    }

    public boolean isSignatureCheck() {
        return signatureCheck;
    }

    public void setSignatureCheck(boolean signatureCheck) {
        this.signatureCheck = signatureCheck;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public Long getGroupTypeId() {
        return groupTypeId;
    }

    public void setGroupTypeId(Long groupTypeId) {
        this.groupTypeId = groupTypeId;
    }

    public int getErrorPhoneNumber() {
        return errorPhoneNumber;
    }

    public void setErrorPhoneNumber(int errorPhoneNumber) {
        this.errorPhoneNumber = errorPhoneNumber;
    }

    public Date getTiming() {
        return timing;
    }

    public void setTiming(Date timing) {
        this.timing = timing;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public List<String> getMobile() {
        return mobile;
    }

    public void setMobile(List<String> mobile) {
        this.mobile = mobile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPage_num() {
        return page_num;
    }

    public void setPage_num(String page_num) {
        this.page_num = page_num;
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

    public Integer getSmsUserState() {
        return smsUserState;
    }

    public void setSmsUserState(Integer smsUserState) {
        this.smsUserState = smsUserState;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getAisleGroupState() {
        return aisleGroupState;
    }

    public void setAisleGroupState(Integer aisleGroupState) {
        this.aisleGroupState = aisleGroupState;
    }

    public Integer getAisleLongNum() {
        return aisleLongNum;
    }

    public void setAisleLongNum(Integer aisleLongNum) {
        this.aisleLongNum = aisleLongNum;
    }

    public String getShieldingFieldId() {
        return shieldingFieldId;
    }

    public void setShieldingFieldId(String shieldingFieldId) {
        this.shieldingFieldId = shieldingFieldId;
    }

    public Integer getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    public Map<String, Object> getValidPhoneMap() {
        return validPhoneMap;
    }

    public void setValidPhoneMap(Map<String, Object> validPhoneMap) {
        this.validPhoneMap = validPhoneMap;
    }
}
