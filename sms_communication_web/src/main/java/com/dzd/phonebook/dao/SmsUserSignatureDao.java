package com.dzd.phonebook.dao;

import java.util.List;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SmsUserSignature;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;

/**
 * 签名接口
 * @author CHENCHAO
 * @date 2017-03-28 17:46:00
 *
 * @param <T>
 */
public interface SmsUserSignatureDao<T> extends BaseDao<T> {
	
	/**
	 * 用户群发 - 查询签名
	 * @return
	 */
	List<T> querySignatureList(Integer uid);
	
 
	
	/**
	 * 根据id查询签名
	 * @param id
	 * @return
	 */
	public SmsUserSignature querySignById(Integer id);
	
	public  Page<SmsUserSignature>  querySignByListPage(DzdPageParam dzdPageParam);

}
