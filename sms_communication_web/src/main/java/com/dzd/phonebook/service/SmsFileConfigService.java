package com.dzd.phonebook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.base.dao.BaseDao;
import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SmsFileConfigDao;

/**
 * 文件配置信息的服務類
 * 
 * @author CHENCHAO
 * @date 2017-03-30 10:45:00
 *
 * @param <T>
 */
@Service("SmsFileConfigService")
public class SmsFileConfigService<T> extends BaseService<T> {
	@Autowired
	private SmsFileConfigDao<T> smsFileConfigDao;

	@Override
	public BaseDao<T> getDao() {
		return smsFileConfigDao;
	}

	/**
	 * 根据uuid查询上传的文件信息
	 * 
	 * @param uuid
	 * @return
	 */
	public List<T> querySmsFileConfigList(String uuid, String fileName) {
		return smsFileConfigDao.querySmsFileConfigList(uuid,fileName);
	}

	/**
	 * 根据uuid删除文件
	 * 
	 * @param uuid
	 */
	public void deleteByUUID(String uuid, String fileName) {
		smsFileConfigDao.deleteByUUID(uuid,fileName);
	}
}
