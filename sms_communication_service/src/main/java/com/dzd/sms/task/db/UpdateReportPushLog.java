package com.dzd.sms.task.db;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;

import com.dzd.db.mysql.MysqlOperator;
import com.dzd.sms.application.Define;
import com.dzd.sms.service.data.SmsReportPush;
import com.dzd.sms.task.base.BaseTask;
import com.dzd.utils.DateUtil;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2017-1-16.
 */

public class UpdateReportPushLog  extends BaseTask {
    private static Logger logger = Logger.getLogger(UpdateReplyPushLog.class);

    String mkey = Define.KEY_SMS_AISLE_REPORT_PUSH_DB;
    RedisClient redisClient = RedisManager.I.getRedisClient();

    public UpdateReportPushLog() {

        this.singlePackageNumber = 100;
        this.threadNumber = 3;

    }



    /**
     * 判断是否有任务可以执行
     */
    public boolean existTask() {
        Long n = redisClient.llen(mkey);
        if( n.intValue()>0  ){
            return true;
        }
        return false;
    }

    @Override
    public void singleExecutor() {
        List<SmsReportPush> smsReportPushArrayList = new ArrayList<SmsReportPush>();
        try {
            long len = redisClient.llen(mkey);
            if (len > singlePackageNumber) len = singlePackageNumber;
            for (int i = 0; i < len; i++) {
                Object objectValue = redisClient.lpop(mkey);
                if ( objectValue != null) {
                    SmsReportPush smsReportPush = (SmsReportPush) objectValue;
                    smsReportPushArrayList.add( smsReportPush );
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        logger.info(" smsReportPushArrayList.size="+smsReportPushArrayList.size());




        try {
            if ( smsReportPushArrayList.size() > 0) {
                final List<SmsReportPush> tmpSmsReportPushArrayList = smsReportPushArrayList;
                String sql = "update sms_receive_log set receive_state=?,update_time=?, push_num=push_num+1 where sms_task_id=? and phone=?";
                MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        String createDate = DateUtil.formatDateTime();
                        ps.setInt(1, tmpSmsReportPushArrayList.get(i).getPushState());
                        ps.setString(2, createDate);
                        ps.setLong(3, tmpSmsReportPushArrayList.get(i).getTaskId());
                        ps.setString(4, tmpSmsReportPushArrayList.get(i).getMobile());
                        //logger.info(" taskid="+tmpSmsReportPushArrayList.get(i).getTaskId()+" phone="+tmpSmsReportPushArrayList.get(i).getMobile());
                    }
                    public int getBatchSize() {
                        return tmpSmsReportPushArrayList.size();
                    }
                });

            }
        } catch (Exception e) {
            excep = true;
            e.printStackTrace();
            logger.error("Exception:" + e.getCause().getClass() + "," + e.getCause().getMessage());
        }

    }

}
