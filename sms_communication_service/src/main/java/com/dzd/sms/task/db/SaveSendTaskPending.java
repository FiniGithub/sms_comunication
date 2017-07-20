package com.dzd.sms.task.db;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;

import com.dzd.db.mysql.MysqlOperator;
import com.dzd.sms.application.Define;
import com.dzd.sms.service.data.SmsTaskData;
import com.dzd.sms.task.base.BaseTask;
import com.dzd.utils.DateUtil;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dzd.sms.application.Define.KEY_SMS_TASK_CACHE;


/**
 * Created by Administrator on 2017-1-10.
 */
public class SaveSendTaskPending extends BaseTask {


    private  static Logger logger = Logger.getLogger(SaveSendTaskPending.class );
    String mkey = Define.KEY_SMS_TASK_PENDING_IDS;
    RedisClient redisClient = RedisManager.I.getRedisClient();
    public SaveSendTaskPending() {
        this.singlePackageNumber = 100;
        this.threadNumber = 3;
    }

    /**
     * 判断是否有任务可以执行
     */
    public boolean existTask() {
        return RedisManager.I.getRedisClient().llen(mkey)>0;
    }

    @Override
    public void singleExecutor() {

        List<SmsTaskData> smsTaskDataList = new ArrayList<SmsTaskData>();
        try {
            long len = redisClient.llen(mkey);
            if (len > singlePackageNumber) len = singlePackageNumber;

            for (int i = 0; i < len; i++) {
                Object taskIdByte = redisClient.lpop(mkey);
                if (taskIdByte != null) {
                    //任务ID
                    Object objectValue = redisClient.hgetObject(KEY_SMS_TASK_CACHE, taskIdByte);
                    if( objectValue !=null ){
                        SmsTaskData smsTaskData =  (SmsTaskData)objectValue;
                        if( smsTaskData !=null && smsTaskData.isSave() == false){
                            smsTaskData.setSave(true);
                            smsTaskDataList.add( smsTaskData );
                            redisClient.hsetObject(KEY_SMS_TASK_CACHE, taskIdByte, smsTaskData);
                            System.out.println(" taskid="+taskIdByte+" smsTaskData="+smsTaskData);
                        }
                    }
                }
            }

        } catch (Exception e) {
            excep = true;
            e.printStackTrace();
        }



        try {
            if( smsTaskDataList.size()>0) {
                final List<SmsTaskData> tempSmsTaskDataList = smsTaskDataList;
                //insert into sms_send_task set content='" + smsTaskData.getText() + "', send_num='" + smsTaskData.getCount() + "',create_time='" + DateUtil.formatDateTime() + "'
                String sql = "insert into sms_send_task(id, sms_user_id,content, send_num,audit_state, create_time,update_time,audit_time," +
                        "expect_deduction,actual_deduction, billing_num,error_phone_num,blacklist_phone_num,send_type,sms_aisle_group_id,callback_url,state,timing_time,send_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        String createDate = DateUtil.formatDateTime();
                        ps.setLong( 1, Long.valueOf(tempSmsTaskDataList.get(i).getTaskId()));
                        ps.setLong( 2, tempSmsTaskDataList.get(i).getUserId() );
                        ps.setString(3,  tempSmsTaskDataList.get(i).getText());
                        ps.setInt(4, tempSmsTaskDataList.get(i).getCount());

                        if( tempSmsTaskDataList.get(i).isFree() ){
                            ps.setInt(5, Define.STATE_AUDIT_AUTO);//自动审核
                        }else {
                            ps.setInt(5, Define.STATE_AUDIT_PENDING);//待审核
                        }
                        ps.setString(6, DateUtil.formatDateTime() );
                        ps.setString(7, DateUtil.formatDateTime() );
                        Date d = tempSmsTaskDataList.get(i).getAuditTime();
                        if( d == null ){
                            ps.setDate(8, null );
                        }else{
                            ps.setString(8, DateUtil.formatDateTime(tempSmsTaskDataList.get(i).getAuditTime()) );
                        }

                        ps.setDouble(9, tempSmsTaskDataList.get(i).getExpect_deduction());//预计扣费
                        ps.setDouble(10, tempSmsTaskDataList.get(i).getActual_deduction());

                        ps.setInt(11, tempSmsTaskDataList.get(i).getBilling_num());
                        ps.setInt(12, tempSmsTaskDataList.get(i).getError_phone_num());
                        ps.setInt(13, tempSmsTaskDataList.get(i).getBlacklist_phone_num());
						ps.setInt(14, tempSmsTaskDataList.get(i).getTaskType() == 0 ? 0
				                : tempSmsTaskDataList.get(i).getTaskType());
                        ps.setLong(15, tempSmsTaskDataList.get(i).getAisleGroupId());

                        ps.setString(16,tempSmsTaskDataList.get(i).getCallbackUrl());
                        
						if ( tempSmsTaskDataList.get(i).isFree() )
						{
							ps.setInt(17, Define.STATE_AUDIT_FREETRIAL);// 自动审核
						} else
						{
							ps.setInt(17, Define.STATE_AUDIT_NOFREETRIAL);// 待审核
						}
						
						Date timing = tempSmsTaskDataList.get(i).getTiming();
                        if( timing == null ){
                            ps.setDate(18, null );
                        }else{
                            ps.setString(18, DateUtil.formatDateTime(timing) );
                        }
                        if( tempSmsTaskDataList.get(i).isFree() ){
                            ps.setString(19, DateUtil.formatDateTime());//自动审核
                        }else {
                            ps.setString(19, null);//待审核
                        }
						
                        logger.info("getBilling_num=" + tempSmsTaskDataList.get(i).getBilling_num()+ " getCount="+tempSmsTaskDataList.get(i).getCount());

                    }
                    public int getBatchSize() {
                        return tempSmsTaskDataList.size();
                    }
                });

            }
        }catch (Exception e){
            excep = true;
            e.printStackTrace();

            //发生异常需要把信息先缓存起来
            for( SmsTaskData smsTaskData:smsTaskDataList){
                redisClient.rpush( Define.KEY_SMS_DB_EXCEPTION_SEND_TASK,  smsTaskData );
            }

            logger.error( "Exception:" + e.getCause().getClass() + "," + e.getCause().getMessage() );
        }

    }

}
