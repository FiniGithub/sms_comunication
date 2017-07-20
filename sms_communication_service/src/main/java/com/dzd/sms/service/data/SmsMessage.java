package com.dzd.sms.service.data;

import java.io.Serializable;

/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/3
 * Time: 14:04
 */
public class SmsMessage   implements Serializable {
    String mobile; //手机号码
    String taskId;
    Integer state;
    Double fee =0.0;
    String province;//省
    Long regionId; //地区ID
    Integer supplier; //运营商类型
    Integer num=1;
    public SmsMessage( String mobile , String taskId, Double fee){
        this.mobile = mobile;
        this.taskId = taskId;
        this.fee = fee;
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Integer getSupplier() {
        return supplier;
    }

    public void setSupplier(Integer supplier) {
        this.supplier = supplier;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
