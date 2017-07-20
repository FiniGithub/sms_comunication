package com.dzd.phonebook.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.UserMessageDao;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsAisleGroupHasSmsUser;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.sms.service.data.SmsPullReport;
import com.dzd.sms.service.data.SmsReciveReply;

/**
 * @Description:用户信息Service
 * @author:oygy
 * @time:2017年1月13日 上午9:28:39
 */
@Service("userMessageService")
public class UserMessageService<T> extends BaseService<T> {
	private final static Logger log = Logger.getLogger(UserMessageService.class);

	@Autowired
	private UserMessageDao<T> mapper;

	public UserMessageDao<T> getDao() {
		return mapper;
	}

	/**
	 * 根据账号ID查询代理用户信息
	 * 
	 * @Description:
	 * @author:oygy
	 * @time:2017年1月13日 上午9:46:33
	 */
	public SmsUser querySmsUserById(Integer sysId) {
		return getDao().querySmsUserById(sysId);
	}

	/**
	 * 根据用户账号查询用户信息
	 * @param account
	 * @return
     */
	public SmsUser querySmsUserBySmsUserAccount(String account) {
		return getDao().querySmsUserBySmsUserAccount(account);
	}

	/**
	 * 查询充值账号
	 * @param smsId
	 * @param superAdmin
     * @return
     */
	public List<SmsUser> queryRechargeSmsUserList(Integer smsId,Integer superAdmin){
		return getDao().queryRechargeSmsUserList(smsId,superAdmin);
	}



	public List<SmsUser> querySmsUserByBId(Integer id){
		return getDao().querySmsUserByBId(id);
	}
	
	public List<SmsPullReport> getSmsPullReport(Map<String,Object> param){
		return getDao().getSmsPullReport(param);
	}
	
	public List<SmsReciveReply> getSmsReply(Map<String,Object> param){
		return getDao().getSmsReply(param);
	}
	
	public List<String> querySmsUserSignList(Map<String,Object> param)
	{
		return getDao().querySmsUserSign(param);
	}
	
	public String querySmsUserSign(Map<String,Object> param)
	{
		List<String> smsUserSign = getDao().querySmsUserSign(param);
		StringBuffer stbf = new StringBuffer();
		boolean flag=false;
		for (String sign : smsUserSign) {
            if (flag) {
            	stbf.append(",");
            }else {
                flag=true;
            }
            stbf.append(sign);
        }
		return stbf.toString();
	}

	/**
	 * 根据代理用户ID查询出代理的所有通道价格表
	 * 
	 * @Description:
	 * @author:liuyc
	 * @time:2017年1月18日 上午10:31:50
	 */
	public List<SmsAisleGroupHasSmsUser> querySmsUserPrice(Integer id) {
		return getDao().querySmsUserPrice(id);
	}

	public void updateSmsUserById(SmsUser smsUser) {
		getDao().updateSmsUserById(smsUser);
	}

	/**
	 * 
	 * @Description:根据通道用户关系ID修改关联通道组ID
	 * @author:oygy
	 * @time:2017年3月2日 上午11:14:56
	 */
	public void updateUserAg(SmsAisleGroupHasSmsUser smsAisleGroupHasSmsUser) {
		getDao().updateUserAg(smsAisleGroupHasSmsUser);
	}

	/**
	 * 
	 * @Description:代理商添加通道格子
	 * @author:oygy
	 * @time:2017年3月2日 上午11:14:56
	 */
	public void addUserAg(SmsAisleGroupHasSmsUser smsAisleGroupHasSmsUser) {
		getDao().addUserAg(smsAisleGroupHasSmsUser);
	}

	/**
	 * @Description:根据代理商ID修改KEY
	 * @author:oygy
	 * @time:2017年3月13日 上午11:49:40
	 */
	public void updateSmsUserKey(SmsUser smsUser) {
		getDao().updateSmsUserKey(smsUser);
	}

	/**
	 * @Description:根据用户id查询key
	 * @author:oygy
	 * @time:2017年3月13日 下午3:30:15
	 */
	public String querySmsUserKeyById(Integer uid) {
		return getDao().querySmsUserKeyById(uid);
	}

	/**
	 * @Description:查询服务器文件地址
	 * @author:oygy
	 * @time:2017年3月13日 下午4:28:02
	 */
	public String querySysConfig(String key) {
		return getDao().querySysConfig(key);
	}

	 /**
	  * 根据类型查找通道组
	  * @param tid
	  * @return
	  */
	public SmsAisleGroup querySmsAisleGroupByTid(Integer tid){
		return getDao().querySmsAisleGroupByTid(tid);
	}
}
