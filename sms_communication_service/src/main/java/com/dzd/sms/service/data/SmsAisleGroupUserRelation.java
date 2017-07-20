package com.dzd.sms.service.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-1-10.
 */
public class SmsAisleGroupUserRelation implements Serializable {
    Long id;
    Long groupId;
    Long userId;
    int smsType;
    Double mobilePrice;
    Double unicomPrice;
    Double telecPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getMobilePrice() {
        return mobilePrice;
    }

    public void setMobilePrice(Double mobilePrice) {
        this.mobilePrice = mobilePrice;
    }

    public Double getUnicomPrice() {
        return unicomPrice;
    }

    public void setUnicomPrice(Double unicomPrice) {
        this.unicomPrice = unicomPrice;
    }

    public Double getTelecPrice() {
        return telecPrice;
    }

    public void setTelecPrice(Double telecPrice) {
        this.telecPrice = telecPrice;
    }

    public int getSmsType() {
        return smsType;
    }

    public void setSmsType(int smsType) {
        this.smsType = smsType;
    }
}
