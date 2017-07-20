package com.dzd.sms.service.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-1-10.
 */
public class SmsAisleGroupAisleRelation implements Serializable {
    Long id;
    Long aisleId;
    Long groupId;
    String keyword;
    Integer sort;  //排序值 GROUP SORT 为排序方式
    Integer operatorId;
    SmsAisle smsAisle;

    byte[] cacheKey;
    String cacheKey2;

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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public SmsAisle getSmsAisle() {
        return smsAisle;
    }

    public void setSmsAisle(SmsAisle smsAisle) {
        this.smsAisle = smsAisle;
    }

    public void calCacheKey(Long taskId){


        this.cacheKey2 = (taskId.toString()+"-"+ aisleId.toString());
        this.cacheKey = this.cacheKey2.getBytes();
    }
    public byte[] getCacheKey(){ return cacheKey;}
    public String getCacheKey2(){ return cacheKey2;}

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }
}
