package com.dzd.sms.task.quartz;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;
import com.dzd.db.mysql.MysqlOperator;
import com.dzd.sms.application.Define;
import com.dzd.sms.service.data.SmsMessage;
import com.dzd.utils.DateUtil;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dzd.sms.application.Define.KEY_SMS_DB_EXCEPTION_SEND_TASK_PHONE;

/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/5/6
 * Time: 8:46
 */
public class DbExceptionProcessJob   implements Job {
    private  static Logger logger = Logger.getLogger( DbExceptionProcessJob.class );
    RedisClient redisClientNew = RedisManager.I.newRedisClient();

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        logger.info( "DbExceptionProcessJob-start");
        redisClientNew.openResource();

        List<SmsMessage> smsMessageList = new ArrayList<SmsMessage>();

        //发生异常需要把信息先缓存起来
        long len = redisClientNew.llen(Define.KEY_SMS_DB_EXCEPTION_SEND_TASK_PHONE);
        for(int i=0; i<len; i++ ) {
            Object obj = redisClientNew.lpop(Define.KEY_SMS_DB_EXCEPTION_SEND_TASK_PHONE);
            if( obj!=null ){
                SmsMessage smsMessage = (SmsMessage)obj;
                smsMessageList.add(smsMessage);
            }
        }
        redisClientNew.closeResource();

        try {
            if( smsMessageList.size()>0) {
                logger.info("DbExceptionProcessJob-insert.start.len=" +smsMessageList.size() );
                final List<SmsMessage> tempSmsMessageList = smsMessageList;
                String sql = "insert into sms_send_task_phone(phone,sms_send_task_id,state,fee,create_time,region,iid,supplier,billing_num) values(?,?,?,?,?,?,?,?,?)";
                MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        String mobile = tempSmsMessageList.get(i).getMobile();
                        String sms_send_task_id = tempSmsMessageList.get(i).getTaskId();
                        String createDate = DateUtil.formatDateTime();
                        ps.setString(1, mobile);
                        ps.setString(2, sms_send_task_id);
                        ps.setInt(3, Define.STATE_TASK_PHONE_DEFAULT);
                        ps.setDouble(4, tempSmsMessageList.get(i).getFee());
                        ps.setString(5, createDate);
                        ps.setString(6,tempSmsMessageList.get(i).getProvince());
                        ps.setLong(7,tempSmsMessageList.get(i).getRegionId());
                        ps.setInt(8, tempSmsMessageList.get(i).getSupplier());
                        ps.setInt(9, tempSmsMessageList.get(i).getNum());
                    }
                    public int getBatchSize() {
                        return tempSmsMessageList.size();
                    }
                });
            }
        }catch (Exception e){

            e.printStackTrace();

            RedisClient redisClientNew = RedisManager.I.newRedisClient();
            redisClientNew.openResource();
            //发生异常需要把信息先缓存起来
            for( SmsMessage smsMessage:smsMessageList){
                redisClientNew.rpush(KEY_SMS_DB_EXCEPTION_SEND_TASK_PHONE,  smsMessage );
            }
            logger.error( "DbExceptionProcessJob-insert-Exception:" + e.getCause().getClass() + "," + e.getCause().getMessage() );
        }

    }
}
