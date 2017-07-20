package com.dzd.phonebook.entity;

import java.util.Date;

/**
 * 首页样式实体类
 * Created by CHENCHAO on 2017/5/31.
 */
public class SmsHomePage {
    private Integer id;
    private String content;// 样式内容
    private Date created;// 创建时间
    private Date updated;// 修改时间


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
