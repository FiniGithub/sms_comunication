package com.dzd.phonebook.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.base.dao.BaseDao;
import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.MsmSendDao;
import com.dzd.phonebook.dao.SmsAuditDao;
import com.dzd.phonebook.dao.SmsUserDao;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.InstructState;
import com.dzd.phonebook.util.RedisUtil;
import com.dzd.phonebook.util.SmsSendTask;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.phonebook.util.SmsUserMoneyRunning;
import com.github.pagehelper.Page;

/**
 * @Description:消息审核服务类
 * @author:oygy
 * @time:2017年1月10日 上午11:08:45
 */
@Service("smsAuditService")
public class SmsAuditService<T> extends BaseService<T>  {
	private final static Logger log = Logger.getLogger(SmsAuditService.class);

	 @Autowired
	    private SmsAuditDao<T> mapper;

	    public SmsAuditDao<T> getDao() {
	        return mapper;
	    }
	    
	    
	    @Autowired
		private SmsUserDao<T> smsUserDao;
	    
		
	    /**
	     * 
	     * @Description:查询审核消息列表
	     * @author:oygy
	     * @time:2016年12月31日 下午2:18:48
	     */
	    public Page<SmsSendTask> querySmsAuditList(DzdPageParam dzdPageParam) {
	        return getDao().querySmsAuditListPage(dzdPageParam);
	    }

	    /**
	     * @Description:审核修改发送消息
	     * @author:oygy
	     * @time:2017年1月10日 上午11:53:06
	     */
	    public void updateSmsAudit(SmsSendTask smsSendTask){
	    	getDao().updateSmsAudit(smsSendTask);
	    }
	    
	    /**
	     * @Description:
	     * @author:oygy
	     * @time:2017年4月21日 下午4:55:30
	     */
	    public SmsSendTask querySmsSeng(Integer sstid){
	    	return getDao().querySmsSeng(sstid);
	    }
	    
	    
		 public Integer rollbackNum(SmsSendTask smsSend,Integer sysUid){
			 Integer num =1;
			 try {
					Integer saList = smsUserDao.querySurplusNum(smsSend.getSmsUserId());  //得到最操作前的条数
					Integer czmoneyValue = smsSend.getBillingNum();  //操作条数
					Date createTime = new Date();
					SmsUser smsUser = new SmsUser();
					smsUser.setId(smsSend.getSmsUserId());
					smsUser.setSurplusNum(czmoneyValue);
					smsUser.setCreateTime(createTime);
					smsUserDao.updateSmsUserBlankMoney(smsUser);
					
					log.info("-----------------》用户添加短信条数");
					
					SmsUserMoneyRunning smr = new SmsUserMoneyRunning();
					smr.setSmsUserId(smsSend.getSmsUserId());
					smr.setUid(sysUid);
					smr.setBeforeNum(saList);   //操作前条数
					smr.setType(2);
					smr.setAfterNum(saList+czmoneyValue);   //操作后条数
					smr.setOperateNum(czmoneyValue);				//操作条数
					smr.setCreateTime(createTime);          //操作时间
					smr.setComment("任务ID:"+smsSend.getId());
					
					smsUserDao.saveSmsUserMoneyRunning(smr);
					
					
					String keys =smsUserDao.querySmsUserKey(smsUser.getId());
					instructSend(InstructState.USERTOPUP_SUCESS,keys,smsUser.getId());   //发送动作指令到redis
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					num=0;
				}
			 return num;
		 }
		 
		 
		    /**
		     * @Description:代理数据处理动作发送
		     * @author:oygy
		     * @time:2017年1月11日 下午2:45:22
		     */
		    private void instructSend(String keys,String smsUserKey,Integer smsUserId){
		    	Instruct instruct = new Instruct();
		    	instruct.setKey(keys);
		    	instruct.setSmsUserKey(smsUserKey);
		    	instruct.setSmsUserId(smsUserId+"");
	            ObjectMapper mapper = new ObjectMapper(); 
				try {
					 String jsonStr = mapper.writeValueAsString(instruct);
					 RedisUtil.publish(InstructState.AB, jsonStr);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	            
		    }
	
}
