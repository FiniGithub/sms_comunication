package com.dzd.phonebook.dao;

import com.dzd.phonebook.entity.SmsSpecificSymbol;

import java.util.List;

/**
 *
 * 特殊符号接口
 * Created by CHENCHAO on 2017/6/9.
 */
public interface SmsSpecificSymbolDao {

    /**
     * 查询所有特殊符号
     * @return
     */
    List<SmsSpecificSymbol> querySpecificSymbolList();
}
