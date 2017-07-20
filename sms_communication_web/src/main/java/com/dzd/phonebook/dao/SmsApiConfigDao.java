package com.dzd.phonebook.dao;

import java.util.List;

import com.dzd.phonebook.entity.SmsApiConfig;

/**
 * API配置文档接口
 * 
 * @author CHENCHAO
 * @date 2017-04-10 10:24:00
 *
 */
public interface SmsApiConfigDao {

	/**
	 * 查询api配置文档信息
	 * 
	 * @return
	 */
	public List<SmsApiConfig> querySmsApiConfigList();

}
