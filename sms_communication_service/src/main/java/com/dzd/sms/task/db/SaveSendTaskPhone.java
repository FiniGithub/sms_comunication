package com.dzd.sms.task.db;


import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;
import com.dzd.db.mysql.MysqlOperator;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsMessage;
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

import static com.dzd.sms.application.Define.KEY_SMS_DB_EXCEPTION_SEND_TASK_PHONE;


/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/6
 * Time: 10:50
 */

public class SaveSendTaskPhone  extends BaseTask {

    private  static Logger logger = Logger.getLogger(SaveSendTaskPhone.class );

    String mkey = Define.KEY_SMS_TASK_MESSAGE_DB2;
    RedisClient redisClient = RedisManager.I.getRedisClient();

    int no = 0;

    public SaveSendTaskPhone(){
        this.singlePackageNumber = 1;
        this.threadNumber = 3;
    }

    /**
     * 判断是否有任务可以执行
     */
    public boolean existTask(){
        return redisClient.llen(mkey)>0;
    }



    @Override
    public void singleExecutor() {

        no++;//debug

        List<SmsMessage> smsMessageList = new ArrayList<SmsMessage>();
        Map<String,Integer> smsMessageNumForTask = new HashMap<String, Integer>();
        try {
            long len = redisClient.llen( mkey );

            logger.info(no+".SaveSendTaskPhone-singleExecutor-start.len=" +len );

            if( len>singlePackageNumber) len = singlePackageNumber;



            for( int i=0; i<len; i++ ){
                Object value= redisClient.lpopString( mkey );
                //logger.info(value);
                if( value != null ) {
                    String smsMessageStr = (String)value;
                    String[] smsMessageS = StringUtil.split(smsMessageStr,"-");
                    for(int x=0;x<smsMessageS.length; x++){
                        String[] cols = StringUtil.split(smsMessageS[x],",");
                        SmsMessage smsMessage = new SmsMessage( cols[1],cols[0],0.0 );
                        smsMessage.setProvince(cols[2]);
                        smsMessage.setRegionId(Long.valueOf(cols[3]));
                        smsMessage.setSupplier(Integer.valueOf(cols[4]));
                        smsMessage.setNum(Integer.valueOf(cols[5]));
                        smsMessageList.add(smsMessage);
                        //System.out.println("add phone to dababase ="+smsMessage.getMobile());
                    }
                }
            }

        }catch (Exception e){
            excep = true;
            e.printStackTrace();
        }


        try {
            if( smsMessageList.size()>0) {
                logger.info(no+".SaveSendTaskPhone-insert.start.len=" +smsMessageList.size() );
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
            excep = true;
            e.printStackTrace();

            RedisClient redisClientNew = RedisManager.I.newRedisClient();
            redisClientNew.openResource();
            //发生异常需要把信息先缓存起来
            for( SmsMessage smsMessage:smsMessageList){
                redisClientNew.rpush(KEY_SMS_DB_EXCEPTION_SEND_TASK_PHONE,  smsMessage );
            }
            redisClientNew.closeResource();

            logger.error( no+".SaveSendTaskPhone-insert-Exception:" + e.getCause().getClass() + "," + e.getCause().getMessage() );
        }


    }
}
