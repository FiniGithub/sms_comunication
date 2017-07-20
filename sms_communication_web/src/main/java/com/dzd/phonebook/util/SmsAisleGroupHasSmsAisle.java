package com.dzd.phonebook.util;

import java.util.Date;

public class SmsAisleGroupHasSmsAisle {

    private Integer id;                //id
    private Integer smsAisleGroupId;    //通道组ID
    private Integer smsAisleId;            //通道ID
    /*	private Integer sorts;				//排序字段（如：1.2.3.4）
        private String  importName;         //导流关键字
    */    private Date createTime;             //创建时间
    private Integer minSendNum;            //最少发送量（条）
    private Integer operatorId; // 运营商id；0-移动，1-联通，2-电信,3-未知

    private String smsAisleName;// 通道名称
    private Integer smsAisleState;// 通道状态
    private Integer startCount;// 通道起发量


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSmsAisleGroupId() {
        return smsAisleGroupId;
    }

    public void setSmsAisleGroupId(Integer smsAisleGroupId) {
        this.smsAisleGroupId = smsAisleGroupId;
    }

    public Integer getSmsAisleId() {
        return smsAisleId;
    }

    public void setSmsAisleId(Integer smsAisleId) {
        this.smsAisleId = smsAisleId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getMinSendNum() {
        return minSendNum;
    }

    public void setMinSendNum(Integer minSendNum) {
        this.minSendNum = minSendNum;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getSmsAisleName() {
        return smsAisleName;
    }

    public void setSmsAisleName(String smsAisleName) {
        this.smsAisleName = smsAisleName;
    }

    public Integer getSmsAisleState() {
        return smsAisleState;
    }

    public void setSmsAisleState(Integer smsAisleState) {
        this.smsAisleState = smsAisleState;
    }

    public Integer getStartCount() {
        return startCount;
    }

    public void setStartCount(Integer startCount) {
        this.startCount = startCount;
    }
}
