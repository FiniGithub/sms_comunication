package com.dzd.phonebook.util;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.dzd.base.page.BasePage;
import com.dzd.phonebook.entity.SysMenuBtn;

/**
 * @Description:总屏蔽词管理
 * @author:oygy
 * @time:2017年2月24日 下午4:40:54
 */
public class SmsShieldWord extends BasePage implements Serializable {

    private List<SysMenuBtn> sysMenuBtns;//菜单拥有的按钮


    private Integer id;                //id
    private Integer type;            // 屏蔽词类型,0:通用屏蔽词,1:特殊屏蔽词
    private String wordName;        //屏蔽词多个“，”隔开
    private Date createTime;        //创建时间
    private Date updateTime;        //修改时间
    private Integer level;// 屏蔽词等级

    private String name;            // 【删除】
    private String comment;            //备注【删除】


    public List<SysMenuBtn> getSysMenuBtns() {
        return sysMenuBtns;
    }

    public void setSysMenuBtns(List<SysMenuBtn> sysMenuBtns) {
        this.sysMenuBtns = sysMenuBtns;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
