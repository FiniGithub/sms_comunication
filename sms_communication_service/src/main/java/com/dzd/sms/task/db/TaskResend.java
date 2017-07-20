package com.dzd.sms.task.db;

import static com.dzd.sms.application.Define.KEY_SMS_DB_EXCEPTION_SEND_TASK_PHONE;
import static com.dzd.sms.application.Define.KEY_SMS_TASK;
import static com.dzd.sms.application.Define.KEY_SMS_TASK_CACHE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;
import com.dzd.db.mysql.MysqlOperator;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsMessage;
import com.dzd.sms.service.data.SmsSendLog;
import com.dzd.sms.service.data.SmsTaskData;
import com.dzd.sms.task.base.BaseTask;
import com.dzd.utils.DateUtil;
import com.dzd.utils.StringUtil;

/**
 * @Description:短信重发
 * @author:oygy
 * @time:2017年5月5日 下午5:03:43
 */
public class TaskResend   extends BaseTask{
	
    private  static Logger logger = Logger.getLogger(SaveSendTaskPending.class );
    String mkey = Define.KEY_SMS_TASK_RESEND;
    RedisClient redisClient = RedisManager.I.getRedisClient();

    /**
     * 判断是否有任务可以执行
     */
    public boolean existTask() {
        return RedisManager.I.getRedisClient().llen(mkey)>0;
    }
	

    /**
     * 重发
     * @param taskId
     */
	
    @Override
    public void singleExecutor() {
    	 SmsTaskData smsTaskData = null;
         try {
             Object objectValue = redisClient.lpop(mkey);
                 //任务ID
             if( objectValue !=null ){
                 SmsTaskData smsTaskData2 =  (SmsTaskData)objectValue;
                 if( smsTaskData2 !=null && smsTaskData2.isSave() == false){
                     smsTaskData =smsTaskData2;
                 }
             }
         } catch (Exception e) {
             excep = true;
             e.printStackTrace();
         }
         if(smsTaskData!=null){
        	 resendSmsTask(smsTaskData);
         }
    	
    }
    
    /**
     * @Description:重发功能，查询出需要重发的任务和号码放入队列中（队列会自动处理）
     * @author:oygy
     * @time:2017年5月9日 下午2:19:42
     */
    private void resendSmsTask(SmsTaskData resendData){
	   //1.缓存中查询出发送任务
       SmsTaskData smsTaskData = SmsServerManager.I.getSmsTaskDataFromCache(resendData.getTaskId().toString());
       smsTaskData.setAisleGroupId(resendData.getAisleGroupId());
       //2.得到需要发送的号码
       List<SmsMessage> smsMessageList =new ArrayList<SmsMessage>();
       try {
	       if(resendData.getResendType()==0){    	  //状态重发
	    	   //得到重发的转态类型（0:待发送,1:发送失败）
	    	   Integer resendState = resendData.getResendState();
	    	   if(resendState!=null){
	    		   smsMessageList= getStateSmsMessage(resendData);
	    	   }
	       }else if(resendData.getResendType()==1){   //号码重发
	    	   if( resendData.getPhongText() != null ) {
	    		 //获取需要发送的号码
	    		   smsMessageList =getSmsMessage(resendData);  
               }
	       }
	       
	       //3.发送
	       String key = SmsServerManager.I.getSmsMessageQueueKey(resendData.getTaskId());// 构建缓存队列key值 
	        long len = redisClient.llen(key);
	        if( len>0 ){
	    	    redisClient.del(key);
	        }
	        if( smsMessageList.size()>0 ) {
	    	    SmsServerManager.I.putSmsTask(smsTaskData);
	    	    int sizes=10000;  //分包发送（10000条发一次）
	    	    int from =0;      //分包起始下标
	    	    int to =sizes;
	    	    if(smsMessageList.size()<=sizes){
	    	    	SmsServerManager.I.putSmsTaskMessageList(key.getBytes(), smsMessageList);
	    	    }else{
	    	    	//得到分包次数
	    	    	 int num = smsMessageList.size()%sizes>0? smsMessageList.size()/sizes+1:smsMessageList.size()/sizes;
	    	    	 for (int i = 0; i < num; i++) {
	    	    		 List<SmsMessage> pakHpong = smsMessageList.subList(from,to);
	    	    		 SmsServerManager.I.putSmsTaskMessageList(key.getBytes(), pakHpong);
	    				 from = to;
	    				 to= i==num-2?smsMessageList.size():to+sizes;
					}
	    	    }  
	    	    
	        } 
        }catch (Exception e){
            excep = true;
            e.printStackTrace();
        }
    }
    
    
    

    /**
     * @Description:根据重发转态查询出需要重发的号码！
     * @author:oygy
     * @time:2017年5月9日 下午2:31:35
     */
    public List<SmsMessage> getStateSmsMessage(SmsTaskData resendData){
    	 List<SmsMessage> smsMessageList =new ArrayList<SmsMessage>();
        try{
            String sql = " SELECT phone,sms_send_task_id,iid,supplier  FROM sms_send_task_phone WHERE sms_send_task_id ="+resendData.getTaskId()+"";
            if(resendData.getResendState()==0){
            	sql+=" and  state =-1";
            }else if(resendData.getResendState()==1){
            	sql+=" and state>=2 and state<99";
            }
            logger.info(" getSmsSendHpong:"+sql);
            smsMessageList = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                	SmsMessage smsMessage = new SmsMessage(rs.getString("phone"),rs.getString("sms_send_task_id"),0.0);
                	smsMessage.setSupplier(rs.getInt("supplier"));
                	smsMessage.setRegionId(rs.getLong("iid"));
                	return smsMessage;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return smsMessageList;
    }
    
    
    

    /**
     * @Description:根据重发的号码，查询当前任务中是否存在该号码，存在的进行重发。得到最终发送的号码列表！
     * @author:oygy
     * @time:2017年5月9日 下午2:31:35
     */
    public List<SmsMessage> getSmsMessage(SmsTaskData resendData){
    	List<SmsMessage> smsMessageList = new ArrayList<SmsMessage>();
    	String hpongs = resendData.getPhongText();
    	if(hpongs==null){
    		return smsMessageList;
    	}
    	int sizes = 1000;
    	if(hpongs.lastIndexOf(",")>0){
    		String [] hpongAr = hpongs.split(",");
    		List<String> hpongList = new ArrayList<String>();
    		Collections.addAll(hpongList, hpongAr);
     		if(hpongList.size()<=sizes){
    			smsMessageList = quertSmsSendHpong(hpongList,resendData.getTaskId());
    		}else{
    			int num = hpongList.size()%sizes>0? hpongList.size()/sizes+1:hpongList.size()/sizes;
    			int from =0;
        		int to =sizes;
    			for (int i = 0; i < num; i++) {
    				 List<String> pakHpong = hpongList.subList(from,to);
    				 List<SmsMessage> list = quertSmsSendHpong(pakHpong,resendData.getTaskId());
    				 if(list.size()>0){
    					 smsMessageList.addAll(list);
    				 }
    				 from = to;
    				 to= i==num-2?hpongAr.length:to+sizes;
				}
    		}
    		
    	}else{
    		 SmsMessage smsMessage = new SmsMessage(resendData.getPhongText(),resendData.getTaskId().toString(),0.0 );
    		 smsMessageList.add(smsMessage);
    	}
    	return smsMessageList;
    }
    
    
    
   /**
    * @Description:查询需要发送的号码
    * @author:oygy
    * @time:2017年5月9日 下午2:17:47
    */
    public List<SmsMessage> quertSmsSendHpong(List<String> hpongs,Long tid){
        List<SmsMessage> smsMessageList = new ArrayList<SmsMessage>();
        try{
            String sql = " SELECT phone,sms_send_task_id,iid,supplier  FROM sms_send_task_phone WHERE sms_send_task_id ="+tid+" and  phone in ("+StringUtil.arrayToString( hpongs )+")";
            logger.info(" getSmsSendHpong:"+sql);
            smsMessageList = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                	SmsMessage smsMessage = new SmsMessage(rs.getString("phone"),rs.getString("sms_send_task_id"),0.0);
                	smsMessage.setSupplier(rs.getInt("supplier"));
                	smsMessage.setRegionId(rs.getLong("iid"));
                	return smsMessage;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return smsMessageList;
    }
    
    
    
}