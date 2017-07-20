package com.dzd.sms.service.data;

import java.io.Serializable;

/**
 * 短信通道信息
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/3
 * Time: 14:45
 */
public class SmsAisle  implements Serializable {
    Long id;
    Double price;
    Integer type;
    Integer regionId;
    String name; //通道名称
    String className;
    String sendUrl; //发送URL信息
    String sendType; //是否为HTTP协
    Integer state;  //状态 1：正常启用，2：停用，3:自动停用
    int singleNum=100;
    int maxNum = 1000000;
    boolean isMobile = false;
    boolean isUnicom = false;
    boolean istelecom = false;

    String paramValue;
    String extra="";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSendUrl() {
        return sendUrl;
    }

    public void setSendUrl(String sendUrl) {
        this.sendUrl = sendUrl;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }

    public boolean isUnicom() {
        return isUnicom;
    }

    public void setUnicom(boolean unicom) {
        isUnicom = unicom;
    }

    public boolean istelecom() {
        return istelecom;
    }

    public void setIstelecom(boolean istelecom) {
        this.istelecom = istelecom;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public int getSingleNum() {
        return singleNum;
    }

    public void setSingleNum(int singleNum) {
        this.singleNum = singleNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
