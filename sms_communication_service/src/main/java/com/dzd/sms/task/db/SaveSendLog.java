package com.dzd.sms.task.db;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;

import com.dzd.db.mysql.MysqlOperator;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsSendResult;
import com.dzd.sms.service.data.SmsTaskData;
import com.dzd.sms.task.base.BaseTask;
import com.dzd.utils.DateUtil;


import com.dzd.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/6
 * Time: 12:06
 */

public class SaveSendLog  extends BaseTask {

    private  static Logger logger = Logger.getLogger(SaveSendLog.class );
    String mkey = Define.KEY_SMS_AISLE_SEND_RESULT_DB;
    RedisClient redisClient = RedisManager.I.getRedisClient();

    static int noInit = 0;//debug

    public SaveSendLog(){
        this.singlePackageNumber = 1;
        this.threadNumber = 2;
    }


    /**
     * 判断是否有任务可以执行
     */
    public boolean existTask(){  return redisClient.llen(mkey)>0;  }

    @Override
    public void singleExecutor() {

        int no = ++noInit;//debug


        List<SmsSendResult> smsSendResultList = new ArrayList<SmsSendResult>();
        Map<String,Integer> smsSendResultNumForTask = new HashMap<String, Integer>();

        Map<String,List<String>> updateTaskPhoneMap = new HashMap<String, List<String>>();
        final  String sendDateTime = DateUtil.formatDateTime();
        try {
            long len = redisClient.llen( mkey );

            logger.info(no+".SaveSendLog-singleExecutor-start.len=" +len );


            if( len>singlePackageNumber) len = singlePackageNumber;
            for( int i=0; i<len; i++ ){
            	
                Object objectValue=redisClient.lpopString( mkey );// smid出现乱码,原因在于SmsServerManager object、string类型不一致
                
                if( objectValue != null ) {
                    String smsSendResultStr = (String)objectValue;
                    
//                    System.out.println("savesendlog - smsSendResultStr====================================================》》" + smsSendResultStr);
                    
                    String[] smsSendResultStrS = StringUtil.split(smsSendResultStr,"☆");
                    for(int x=0;x<smsSendResultStrS.length; x++) {
//                    	System.out.println("savesendlog - smsSendResultStrS[x]====================================================》》" + smsSendResultStrS[x]);
                    	
                        String[] cols = StringUtil.split(smsSendResultStrS[x],",");
                        // b.append("-").append(smsSendResult1.getMid()).append(",").append(smsSendResult1.getMobile()).append(",").append(smsSendResult1.getAisleId()).append(",").append(smsSendResult1.getTaskId()).append(",").append(smsSendResult1.getState());
                       
//                        System.out.println("savesendlog - cols[0]====================================================》》" + cols[0]);
                        
                        
                        
                        SmsSendResult smsSendResult = new SmsSendResult(cols[0]);
                        smsSendResult.setMobile(cols[1]);
                        smsSendResult.setAisleId(Long.valueOf(cols[2]));
                        smsSendResult.setTaskId(cols[3]);
                        smsSendResult.setState(Integer.valueOf(cols[4]));
                        smsSendResultList.add(smsSendResult);

                        String key = smsSendResult.getTaskId();
                        //logger.info("--------------savesendlog-singleExecutor-key="+key+"---------------");
                        if (smsSendResultNumForTask.containsKey(key)) {
                            smsSendResultNumForTask.put(key, smsSendResultNumForTask.get(key) + 1);
                        } else {
                            smsSendResultNumForTask.put(key, 1);
                        }

                        String phoneMapKey = new StringBuilder().append(smsSendResult.getTaskId()).append(",").append(smsSendResult.getAisleId()).append(",").append(smsSendResult.getState()).toString();
                        if( updateTaskPhoneMap.containsKey(phoneMapKey)){
                            updateTaskPhoneMap.get(phoneMapKey).add( "'"+smsSendResult.getMobile()+"'");
                        }else{
                            List<String> p = new ArrayList<String>();
                            p.add(  "'"+smsSendResult.getMobile()+"'" );
                            updateTaskPhoneMap.put(phoneMapKey, p);
                        }


                    }
                }
           }
        }catch (Exception e){
            excep = true;
            e.printStackTrace();
        }




        try {
            if( smsSendResultList.size()>0) {
                final List<SmsSendResult> tmpSmsSendResultList = smsSendResultList;
                String sql = "insert into sms_send_log(aid,receive_phone, mid, sms_send_task_id, create_time, send_time, state) values(?,?,?,?,?,?,?)";
                MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, tmpSmsSendResultList.get(i).getAisleId());
                        ps.setString(2, tmpSmsSendResultList.get(i).getMobile());
                        ps.setString(3, tmpSmsSendResultList.get(i).getMid());
                        ps.setString(4, tmpSmsSendResultList.get(i).getTaskId());
                        ps.setString(5, sendDateTime);
                        ps.setString(6, sendDateTime);
                        ps.setInt(7, tmpSmsSendResultList.get(i).getState());
                    }
                    public int getBatchSize() {
                        return tmpSmsSendResultList.size();
                    }
                });

            }
        }catch (Exception e){
            excep = true;
            e.printStackTrace();
            //发生异常需要把信息先缓存起来
            for( SmsSendResult smsSendResult:smsSendResultList){
                redisClient.rpush(Define.KEY_SMS_DB_EXCEPTION_SEND_LOG,  smsSendResult );
            }
        }

        try {
            for(String key: smsSendResultNumForTask.keySet()){
                //SmsTaskData  smsTaskData = (SmsTaskData) redisClient.hgetObject( Define.KEY_SMS_TASK_CACHE , key );
                //smsTaskData.setUpdateSendLogDatabase(false);
                //logger.info("smsSendResultNumForTask.taskid="+ key + " sendnum=" +smsSendResultNumForTask.get(key)+" smsSendResultList.size="+smsSendResultList.size() );
                //必需添加
                SmsServerManager.I.addSmsTaskSaveSendPhoneNumber( key,smsSendResultNumForTask.get(key));
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error("SmsServerManager.I.addSmsTaskSaveSendPhoneNumber."+smsSendResultNumForTask.size());
        }


        String updateSql = "";
        try {
            if( smsSendResultList.size()>0) {
                logger.info(no+".SaveSendLog-insert.start.len=" +smsSendResultList.size() );
                for(String key : updateTaskPhoneMap.keySet()){
                    String[] parts = StringUtil.split( key, ",");
                    updateSql = "UPDATE sms_send_task_phone  AS p SET p.state ="+parts[2]+", p.aid="+parts[1]+", p.send_time='"+sendDateTime+"' WHERE p.sms_send_task_id="+parts[0]+" AND p.phone IN ("+ StringUtil.arrayToString(updateTaskPhoneMap.get(key),",")+")";
                    //logger.info(updateSql);
                    try{
                        MysqlOperator.I.execute( updateSql );
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.info(no+".SaveSendLog-Exception-updateSql=" +updateSql );
                        //发生异常需要把信息先缓存起来
                        redisClient.rpushString(Define.KEY_SMS_DB_EXCEPTION_SEND_LOG_UPADATE,  updateSql );

                    }
                }
                    //
                //UPDATE sms_send_task_phone  AS p SET p.state = 3, p.aid=1,p. WHERE p.sms_send_task_id=3803 AND p.phone IN ('')
//
//                final List<SmsSendResult> tmpSmsSendResultList = smsSendResultList;
//                    String sql = "update   sms_send_task_phone set state=?, update_time=? where sms_send_task_id=? and phone=? ";
//                    MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter() {
//                        public void setValues(PreparedStatement ps, int i) throws SQLException {
//                            String dateTime = DateUtil.formatDateTime();
//                            ps.setInt(1, tmpSmsSendResultList.get(i).getState());
//                            ps.setString(2, dateTime);
//                            ps.setString(3, tmpSmsSendResultList.get(i).getTaskId());
//                            ps.setString(4, tmpSmsSendResultList.get(i).getMobile());
//                            System.out.println(" taskid=" + tmpSmsSendResultList.get(i).getTaskId() + ",mobile=" + tmpSmsSendResultList.get(i).getMobile() + ", updateTime=" + dateTime + ",state=" + tmpSmsSendResultList.get(i).getState());
//                        }
//                        public int getBatchSize() {
//                            return tmpSmsSendResultList.size();
//                        }
//                    });
//
//                //}




            }
        }catch (Exception e){
            excep = true;
            e.printStackTrace();

            //发生异常需要把信息先缓存起来
            for( SmsSendResult smsSendResult:smsSendResultList){
                redisClient.rpush( Define.KEY_SMS_DB_EXCEPTION_SEND_LOG,  smsSendResult );
            }
            logger.error( "Exception:" + e.getCause().getClass() + "," + e.getCause().getMessage() );

        }

    }
}