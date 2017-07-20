package com.dzd.sms.service.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-1-24.
 */
public class SmsSendLog implements Serializable {
    Long userId=0l;
    Long taskId=0l;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}

