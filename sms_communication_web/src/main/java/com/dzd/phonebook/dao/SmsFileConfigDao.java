package com.dzd.phonebook.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dzd.base.dao.BaseDao;

/**
 * 文件配置信息的接口
 * 
 * @author CHENCHAO
 * @date 2017-03-30 10:45:00
 *
 * @param <T>
 */
public interface SmsFileConfigDao<T> extends BaseDao<T> {

	/**
	 * 根据uuid查询上传的文件信息
	 * 
	 * @param uuid
	 * @return
	 */
	public List<T> querySmsFileConfigList(@Param("uuid") String uuid, @Param("fileName") String fileName);

	/**
	 * 根据uuid删除文件
	 * 
	 * @param uuid
	 */
	public void deleteByUUID(@Param("uuid") String uuid, @Param("fileName") String fileName);
}
