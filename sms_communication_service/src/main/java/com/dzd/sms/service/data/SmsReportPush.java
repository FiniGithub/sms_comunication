package com.dzd.sms.service.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017-1-16.
 */
public class SmsReportPush  implements Serializable {
    Long id;
    Long userId;
    Long taskId;
    String mid;
    String mobile;
    int pushState;
    int pushWay=0;//0为HTTP，1为CMPP
    String state;
    Date submitDate = new Date();
    String receiveDate;
    String pushUrl;
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

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getPushState() {
        return pushState;
    }

    public void setPushState(int pushState) {
        this.pushState = pushState;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPushWay() {
        return pushWay;
    }

    public void setPushWay(int pushWay) {
        this.pushWay = pushWay;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }
}
