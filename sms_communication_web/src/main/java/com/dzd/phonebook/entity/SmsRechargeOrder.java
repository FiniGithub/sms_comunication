package com.dzd.phonebook.entity;

import java.util.Date;

/**
 * Created by Administrator on 2017/5/22.
 */
public class SmsRechargeOrder {
    private Integer id;
    private Integer userId;// 操作人id
    private Integer smsUserId;// 用户id
    private String orderNo;// 订单编号
    private Float money;// 充值金额
    private Integer smsNumber;// 充值短信条数
    private Date created;// 创建时间
    private Date updated;// 修改时间
    private Integer status;// 状态 0:未支付,1：已支付

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSmsUserId() {
        return smsUserId;
    }

    public void setSmsUserId(Integer smsUserId) {
        this.smsUserId = smsUserId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Integer getSmsNumber() {
        return smsNumber;
    }

    public void setSmsNumber(Integer smsNumber) {
        this.smsNumber = smsNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


}
