package com.dzd.sms.service.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017-1-17.
 */
public class SmsSendPackage   implements Serializable {
    Long taskId;
    Long userId;
    Long aisleId; //通道ID
    int phoneNum; //号码数量
    int state; //状态，0：发送成功，1：发送失败
    String content; //发送内容
    String describe; //描述
    String phoneAll; //号码
    String params;
    String result;
    Date createDate = new Date();

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getAisleId() {
        return aisleId;
    }

    public void setAisleId(Long aisleId) {
        this.aisleId = aisleId;
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPhoneAll() {
        return phoneAll;
    }

    public void setPhoneAll(String phoneAll) {
        this.phoneAll = phoneAll;
    }
}
