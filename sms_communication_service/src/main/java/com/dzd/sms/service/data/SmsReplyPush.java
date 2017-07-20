package com.dzd.sms.service.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-1-14.
 */
public class SmsReplyPush   implements Serializable {
    Long id=0l;
    Long aisleId;
    List<String> aisleIds = new ArrayList<String>();
    String phone;
    String content;
    Long userId=0l;
    Long taskId=0l;
    Date updateTime;
    Date createTime;
    int state;
    String region;

    String pushUrl;
    int pushWay=0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAisleId() {
        return aisleId;
    }

    public void setAisleId(Long aisleId) {
        this.aisleId = aisleId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }


    public List<String> getAisleIds() {
        return aisleIds;
    }

    public void setAisleIds(List<String> aisleIds) {
        this.aisleIds = aisleIds;
    }
    public void addAisleId(String id) {
        this.aisleIds.add( id );
    }

    public int getPushWay() {
        return pushWay;
    }

    public void setPushWay(int pushWay) {
        this.pushWay = pushWay;
    }
}
