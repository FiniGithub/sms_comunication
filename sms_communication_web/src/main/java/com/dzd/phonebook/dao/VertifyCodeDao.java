package com.dzd.phonebook.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.VertifyCode;

/**
 * 验证码接口
 * @author CHENCHAO
 * @date 2017-04-11 11:35:00
 *
 * @param <T>
 */
public interface VertifyCodeDao<T> extends BaseDao<T> {
	
	public Integer getCodeCountByToday(String phone);

	public List<VertifyCode> getCodeCountByPhone(String phone);
	
	
	public VertifyCode queryCodeByPhoneAndCode(@Param("phone") String phone,@Param("code") String code);

	/**
	 * 查询当天发送短信验证码的次数
	 * @param phone
	 * @param type
	 * @return
	 */
	public List<VertifyCode> getCodeCountByPhoneAndType(@Param("phone") String phone,@Param("type") Integer type);
}
