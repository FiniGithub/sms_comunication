package com.dzd.phonebook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.base.dao.BaseDao;
import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SmsWordShieldingDao;
import com.dzd.phonebook.entity.SmsWordShielding;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsShieldWord;
import com.github.pagehelper.Page;

/**
 * @Description:黑名单
 * @author:lq
 * @time:2016年1月3日
 */
@Service
public class SmsWordShieldingService<T> extends BaseService<T> {
    @Autowired
    private SmsWordShieldingDao<T> mapper;

    @Override
    public BaseDao<T> getDao() {
        // TODO Auto-generated method stub
        return mapper;
    }

    public Page<SmsShieldWord> querShieldWordList(DzdPageParam dzdPageParam) {
        return mapper.querShieldWordListPage(dzdPageParam);
    }


    public void saveShieldWord(SmsShieldWord smsShieldWord) {
        mapper.saveShieldWord(smsShieldWord);
    }

    public void updateShieldWord(SmsShieldWord smsShieldWord) {
        mapper.updateShieldWord(smsShieldWord);
    }


    public SmsShieldWord queryShieldWordById(Integer id) {
        return mapper.queryShieldWordById(id);
    }

    /**
     * 根据类型查询敏感词
     *
     * @param type
     * @return
     */
    public List<SmsShieldWord> querySmsShieldWordList(Integer type) {
        return mapper.querySmsShieldWordList(type);
    }

    public void deleteShieldWord(Integer id) {
        mapper.deleteShieldWord(id);
    }

    /**
     * 批量删除
     * @param idList
     */
    public void deleteShieldWordList(List<Integer> idList){
        for(Integer id :idList){
            mapper.deleteShieldWord(id);
        }
    }

}
