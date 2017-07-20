package com.dzd.phonebook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SmsUserSignatureDao;
import com.dzd.phonebook.entity.SmsUserSignature;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;

/**
 * 签名服务类
 * @author CHENCHAO
 * @date 2017-3-28 18:04:00
 *
 */
@Service("SmsUserSignatureService")
public class SmsUserSignatureService<T> extends BaseService<T> {
	@Autowired
	private SmsUserSignatureDao<T> smsUserSignatureDao;
	
	public SmsUserSignatureDao<T> getDao() {
		return smsUserSignatureDao;
	}
 
	/**
	 * 用户群发 - 查询签名
	 * @return
	 */
	public List<T> querySignatureList(Integer uid){
		return smsUserSignatureDao.querySignatureList(uid);
	}
 
	
	/**
	 * 根据ID查询签名
	 * @param id
	 * @return
	 */
	public SmsUserSignature querySignById(Integer id){
		return smsUserSignatureDao.querySignById(id);
	}
	
	
	public  Page<SmsUserSignature>  querySignByListPage(DzdPageParam dzdPageParam){
		   return getDao().querySignByListPage(dzdPageParam);
	 }

	
}
