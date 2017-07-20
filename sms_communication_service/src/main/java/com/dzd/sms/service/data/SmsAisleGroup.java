package com.dzd.sms.service.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-1-10.
 */
public class SmsAisleGroup implements Serializable {
    Long id;
    Integer tid; //通道组类型ID
    Integer sort; //优先级（1：级别优先，2价格优先）
    Double mobilePrice;
    Double unicomPrice;
    Double telecPrice;
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
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
}
