package com.dzd.phonebook.service;

import com.dzd.phonebook.dao.SmsSpecificSymbolDao;
import com.dzd.phonebook.entity.SmsSpecificSymbol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 特殊符号服务类
 * Created by CHENCHAO on 2017/6/9.
 */
@Service("SmsSpecificSymbolService")
public class SmsSpecificSymbolService {
    @Autowired
    private SmsSpecificSymbolDao smsSpecificSymbolDao;

    /**
     * 查询所有特殊符号
     *
     * @return
     */
    public List<SmsSpecificSymbol> querySpecificSymbolList() {
        return smsSpecificSymbolDao.querySpecificSymbolList();
    }
}
