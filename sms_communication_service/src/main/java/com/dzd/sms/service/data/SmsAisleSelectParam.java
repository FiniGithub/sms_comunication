package com.dzd.sms.service.data;

import java.util.List;

/**
 * Created by Administrator on 2017-1-10.
 */
public class SmsAisleSelectParam {
    List<SmsAisleGroupAisleRelation> smsAisleGroupAisleRelationList;
    Integer groupSort; //优先级（1：级别优先，2价格优先）

    public List<SmsAisleGroupAisleRelation> getSmsAisleGroupAisleRelationList() {
        return smsAisleGroupAisleRelationList;
    }

    public void setSmsAisleGroupAisleRelationList(List<SmsAisleGroupAisleRelation> smsAisleGroupAisleRelationList) {
        this.smsAisleGroupAisleRelationList = smsAisleGroupAisleRelationList;
    }

    public Integer getGroupSort() {
        return groupSort;
    }

    public void setGroupSort(Integer groupSort) {
        this.groupSort = groupSort;
    }
}
