package com.dzd.phonebook.entity;

import com.dzd.base.page.BasePage;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by dzd-technology01 on 2017/5/17.
 */
public class SmsFilterNumberRecord extends BasePage implements Serializable{


    /**
     * 主键Id
     */
    private Integer filterId;

    /**
     * 账号Id
     */
    private Integer uid;

    /**
     * 账号email
     */
    private String email;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 时间
     */
    private Timestamp time;

    /**
     * 电信有效号码
     */
    private int telecomNumber;

    /**
     * 重复号码
     */
    private int unicomNumber;

    /**
     * 联通有效号码
     */
    private int mobileNumber;

    /**
     * 重复号码
     */
    private int duplicateNumber;

    /**
     * 错误号码
     */
    private int wrongNumber;

    public Integer getFilterId() {
        return filterId;
    }

    public void setFilterId(Integer filterId) {
        this.filterId = filterId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getTelecomNumber() {
        return telecomNumber;
    }

    public void setTelecomNumber(int telecomNumber) {
        this.telecomNumber = telecomNumber;
    }

    public int getUnicomNumber() {
        return unicomNumber;
    }

    public void setUnicomNumber(int unicomNumber) {
        this.unicomNumber = unicomNumber;
    }

    public int getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(int mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getDuplicateNumber() {
        return duplicateNumber;
    }

    public void setDuplicateNumber(int duplicateNumber) {
        this.duplicateNumber = duplicateNumber;
    }

    public int getWrongNumber() {
        return wrongNumber;
    }

    public void setWrongNumber(int wrongNumber) {
        this.wrongNumber = wrongNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
