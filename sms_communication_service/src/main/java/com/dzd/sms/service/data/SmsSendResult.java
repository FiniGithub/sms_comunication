package com.dzd.sms.service.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/4
 * Time: 19:29
 */
public class SmsSendResult  implements Serializable {
    String mid;             //发送结果号码序列号
    String taskId;          //发送结果任务ID
    Long aisleId=0L;           //通道ID
    String mobile;
    Integer state;
    Date createDate = new Date();

    public SmsSendResult( String mid ){
        this.mid = mid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getAisleId() {
        return aisleId;
    }

    public void setAisleId(Long aisleId) {
        this.aisleId = aisleId;
    }
}
