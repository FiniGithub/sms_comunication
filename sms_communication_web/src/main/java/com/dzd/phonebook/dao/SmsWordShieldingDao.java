package com.dzd.phonebook.dao;

import java.util.List;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SmsPhoneShielding;
import com.dzd.phonebook.entity.SmsWordShielding;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsShieldWord;
import com.github.pagehelper.Page;

/**
 * SmsWordShielding Mapper
 *
 * @author 接口
 */
public interface SmsWordShieldingDao<T> extends BaseDao<T> {
	public List<T> queryByMenuid(Integer menuid);

	public List<T> queryByMenuUrl(String url);

	public void deleteByMenuid(Integer menuid);

	public List<T> getMenuBtnByUser(Integer userid);

	public int update(SmsWordShielding sms);

	public int insert(SmsWordShielding sms);

	public SmsWordShielding queryByAll();
	
	public Page<SmsShieldWord> querShieldWordListPage(DzdPageParam dzdPageParam);
	
	public void saveShieldWord(SmsShieldWord smsShieldWord);
	
	public void updateShieldWord(SmsShieldWord smsShieldWord);
	
	public SmsShieldWord queryShieldWordById(Integer id);
	
	public void deleteShieldWord(Integer id);

	/**
	 * 根据类型查询敏感词
	 * @param type
	 * @return
	 */
	public List<SmsShieldWord> querySmsShieldWordList(Integer type);

}
