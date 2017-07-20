package com.dzd.sms.service.data;

import java.io.Serializable;

/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/4
 * Time: 19:27
 */
public class SmsReply  implements Serializable {
    Long id;
    String mid;
    String mobile;
    String content;
    String aisleCode;
    String createDate;
    String aisleClassName;

    Long aisleId;

    public SmsReply( String mid ){
        this.mid = mid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAisleCode() {
        return aisleCode;
    }

    public void setAisleCode(String aisleCode) {
        this.aisleCode = aisleCode;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Long getAisleId() {
        return aisleId;
    }

    public void setAisleId(Long aisleId) {
        this.aisleId = aisleId;
    }

    public String getAisleClassName() {
        return aisleClassName;
    }

    public void setAisleClassName(String aisleClassName) {
        this.aisleClassName = aisleClassName;
    }
}
