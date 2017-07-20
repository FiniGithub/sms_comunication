package com.dzd.phonebook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.dzd.base.dao.BaseDao;
import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SmsReceiveReplyPushDao;
import com.dzd.phonebook.entity.SmsReceiveReplyPush;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;

/**
 * @Description:短信回复
 * @author:liuquan
 * @time:2016年12月30日 下午2:28:03
 */
@Service
public class SmsReceiveReplyPushService<T> extends BaseService<T> {
	@Autowired
	private SmsReceiveReplyPushDao<T> mapper;

	@Override
	public SmsReceiveReplyPushDao<T> getDao() {
		// TODO Auto-generated method stub
		return mapper;
	}

	/*
	 * public List<T> queryByAll(){ return mapper.queryByAll();
	 * 
	 * 
	 * }
	 */

	public Page<SmsReceiveReplyPush> queryUserList(DzdPageParam dzdPageParam) {

		return getDao().queryUserPage(dzdPageParam);

	}

	/**
	 * 保存用户
	 *
	 * @param
	 * @param
	 * @return
	 */
	/*
	 * public int saveUser(SmsReceiveReplyLog smsReceiveReplyLog) { return
	 * mapper.addlist(smsReceiveReplyLog);
	 * 
	 * 
	 * }
	 */

	/**
	 * 删除
	 *
	 * @param
	 * @param
	 * @return
	 */
	
	  
	  public void deleteSmsReceiveReplyPush(Integer smsReceiveReplyPushId){
	  mapper.deleteSmsReceive(smsReceiveReplyPushId);
	  
	  }
	 

}
