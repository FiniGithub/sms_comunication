package com.dzd.sms.service.data;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/23.
 */
public class ShieldWord {
    private String wordName;        //屏蔽词多个“，”隔开
    private int level;// 屏蔽词等级

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
