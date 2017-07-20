package com.dzd.phonebook.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.MsmSendDao;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsAisle;
import com.dzd.phonebook.util.SmsSendLog;
import com.dzd.phonebook.util.SmsSendPackageLog;
import com.dzd.phonebook.util.SmsSendTask;
import com.github.pagehelper.Page;


/**
 * 发送消息服务类
 *
 * @author ougy
 * @date 2016-6-24
 */
@Service("msmSendService")
public class MsmSendService<T> extends BaseService<T> {

    @Autowired
    private MsmSendDao<T> mapper;

    public MsmSendDao<T> getDao() {
        return mapper;
    }
    
    
   /**
    * 
    * @Description:查询消息列表
    * @author:ougy
    * @time:2016年12月31日 下午2:18:48
    */
   public Page<SmsSendTask> queryMsmSendList(DzdPageParam dzdPageParam) {
       return getDao().queryMsmSendListPage(dzdPageParam);
   }
   
   /**
    * 
    * @Description:查询子任务消息列表
    * @author:ougy
    * @time:2016年12月31日 下午2:18:48
    */
   public Page<SmsSendPackageLog> queryMsmSendPackageList(DzdPageParam dzdPageParam) {
       return getDao().queryMsmSendPackagePage(dzdPageParam);
   }
   
   
   /**
    * @Description:发送消息统计数量
    * @author:oygy
    * @time:2017年4月1日 上午10:56:47
    */
   public SmsSendTask queryMsmSendCount(DzdPageParam dzdPageParam) {
       return getDao().queryMsmSendCount(dzdPageParam);
   }
   
   /**
    * @Description:根据消息ID查询出本批次发送的所有手机号码
    * @author:oygy
    * @time:2017年1月7日 下午4:46:17
    */
   public List<SmsSendLog> queryMsmSendPhoneByid(Integer msmSendId){
	   return getDao().queryMsmSendPhoneByid(msmSendId);
   }
 
   /**
    * @Description:查询消息详情列表
    * @author:oygy
    * @time:2017年1月9日 上午11:42:42
    */
   public Page<SmsSendLog> queryMsmSendDetailsList(DzdPageParam dzdPageParam) {
       return getDao().queryMsmSendDetailsListPage(dzdPageParam);
   }
   
   
   public SmsSendLog querySedNum(SmsSendLog smsSendLog) {
		return getDao().querySedNum(smsSendLog);
	}
   
   /**
    * @Description:查询当前登录用户下的待审核信息数量
    * @author:oygy
    * @time:2017年4月17日 上午9:59:44
    */
   public Integer querySmsToAudit(Map<String, Object> sortMap){
	   return getDao().querySmsToAudit(sortMap);
   }
   
   /**
    * 根据通道类型ID查询通道
    * @Description:
    * @author:oygy
    * @time:2017年4月18日 下午5:28:01
    */
   public List<SmsAisle> querySmsAisle(Integer tid){
	   return getDao().querySmsAisle(tid);
   }
   
   /**
    * @Description:修改任务重发转态为已重发
    * @author:oygy
    * @time:2017年4月21日 上午10:16:24
    */
   public void updateSendResendState(Integer sstid){
	   getDao().updateSendResendState(sstid);
   }
   
   /**
    * @Description:根据任务ID查询重发状态
    * @author:oygy
    * @time:2017年4月21日 上午10:22:10
    */
   public Integer querySendResendState(Integer sstid){
	   return getDao().querySendResendState(sstid);
   }
   
   /**
    * @Description:导出子任务的发送号码
    * @author:oygy
    * @time:2017年4月27日 下午2:05:04
    */
   public String querySmsSendPackageLogByid(Integer id){
	   return getDao().querySmsSendPackageLogByid(id);
   }
   
   /**
    * 根据通道类型ID查询通道组
    * @Description:
    * @author:oygy
    * @time:2017年4月18日 下午5:28:01
    */
   public List<SmsAisle> querySmsAisleGroup(Integer tid){
	   return getDao().querySmsAisleGroup(tid);
   }


	public Page<SmsSendLog> queryStatisticalUserInfo(DzdPageParam dzdPageParam)
	{
		return getDao().queryStatisticalUserInfoPage(dzdPageParam);
	}
	
	public Map<String, SmsSendLog> queryStatisticalSucceedNum(Map<String, Object> param)
	{
		Map<String, SmsSendLog> succeedNumMap = new HashMap<String, SmsSendLog>();
		List<SmsSendLog> succeedNum = getDao().queryStatisticalSucceedNum(param);
		for ( SmsSendLog smsSendLog : succeedNum )
		{
			succeedNumMap.put(smsSendLog.getSmsUserId(), smsSendLog);
		}
		return succeedNumMap;
	}
	public Map<String, SmsSendLog> queryStatisticalFailureNum(Map<String, Object> param)
	{
		Map<String, SmsSendLog> failureNumMap = new HashMap<String, SmsSendLog>();
		List<SmsSendLog> failureNum = getDao().queryStatisticalFailureNum(param);
		for ( SmsSendLog smsSendLog : failureNum )
		{
			failureNumMap.put(smsSendLog.getSmsUserId(), smsSendLog);
		}
		return failureNumMap;
	}

	public List<String> queryAisleNames()
	{
		return getDao().queryAisleNames();
	}

	public List<String> querynickNames()
	{
		return getDao().querynickNames();
	}


	public Page<SmsSendLog> querySmsTimedTask(DzdPageParam dzdPageParam)
	{
		return getDao().querySmsTimedTaskPage(dzdPageParam);
	}


	public void updateTimedTask(SmsSendTask smsSendTask)
	{
		getDao().updateTimedTask(smsSendTask);
	}


	public SmsSendLog queryTimedTaskForModify(Map<String, Object> param)
	{
		return getDao().queryTimedTaskForModify(param);
	}


	public void deleteTimedTask(Map<String, Object> param)
	{
		getDao().deleteTimedTask(param);
	}


	public List<String> querySmsTaskPhone(String taskId)
	{
		return getDao().querySmsTaskPhone(taskId);
	}


	public int queryStatisticalSendNum(Map<String, Object> param)
	{
		return getDao().queryStatisticalSendNum(param);
	}
}
