package com.dzd.phonebook.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.phonebook.dao.SmsApiConfigDao;
import com.dzd.phonebook.entity.SmsApiConfig;

/**
 * API接口配置信息服务类
 * 
 * @author CHENCHAO
 * @date 2017-04-10
 *
 * @param <T>
 */
@Service("SmsApiConfigService")
public class SmsApiConfigService {
	@Autowired
	private SmsApiConfigDao smsApiConfigDao;


	/**
	 * 查询api配置文档信息
	 * 
	 * @return
	 */
	public Map<String, Object> querySmsApiConfigList() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			List<SmsApiConfig> smsApiConfigs = smsApiConfigDao.querySmsApiConfigList();
			for(SmsApiConfig config:smsApiConfigs){
				String title   = config.getTitle();
				String content = config.getContent();
				String htmlContent = pegDown(content);
				map.put(title, htmlContent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	

	/**
	 * String 转 HTML
	 */
	public static String pegDown(String content) {
		PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
		content = pdp.markdownToHtml(content);
		return content;
	}
	
 
}
