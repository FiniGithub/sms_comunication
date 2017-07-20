package com.dzd.sms.task.db;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;

import com.dzd.db.mysql.MysqlOperator;
import com.dzd.sms.application.Define;
import com.dzd.sms.service.data.SmsReplyPush;
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

public class UpdateReplyPushLog   extends BaseTask {
    private static Logger logger = Logger.getLogger(UpdateReplyPushLog.class);
    String mkey = Define.KEY_SMS_AISLE_REPLY_PUSH_DB;
    RedisClient redisClient = RedisManager.I.getRedisClient();

    public UpdateReplyPushLog() {
        this.singlePackageNumber = 100;
        this.threadNumber = 3;
    }



    /**
     * 判断是否有任务可以执行
     */
    public boolean existTask() {
        return redisClient.llen(mkey) > 0;
    }

    @Override
    public void singleExecutor() {
        List<SmsReplyPush> smsReplyPushArrayList = new ArrayList<SmsReplyPush>();
        try {
            long len = redisClient.llen(mkey);
            if (len > singlePackageNumber) len = singlePackageNumber;
            for (int i = 0; i < len; i++) {
                Object objectValue = redisClient.lpop(mkey);
                if (objectValue != null) {
                    SmsReplyPush smsReplyPush = (SmsReplyPush) objectValue;
                    smsReplyPushArrayList.add( smsReplyPush );
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        //这里插入， 是因为在收到回复时， 并没有计算出需要回复状态的列表
        try {

            logger.info("smsReplyPushArrayList.size()="+smsReplyPushArrayList.size());
            if (smsReplyPushArrayList.size() > 0) {
                final List<SmsReplyPush> tmpSmsReplyPushList = smsReplyPushArrayList;
                String sql = "insert into  sms_receive_reply_push( sms_aisle_id, phone, content, update_time, create_time,sms_user_id,sms_send_task_id,region,state )" +
                        " values(?,?,?,?,?,?,?,?,? )";
                MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {

                        ps.setLong(1, tmpSmsReplyPushList.get(i).getAisleId());
                        ps.setString(2, tmpSmsReplyPushList.get(i).getPhone());
                        ps.setString(3, tmpSmsReplyPushList.get(i).getContent());
                        ps.setString(4, DateUtil.formatDateTime( tmpSmsReplyPushList.get(i).getUpdateTime() ) );
                        ps.setString(5, DateUtil.formatDateTime( tmpSmsReplyPushList.get(i).getCreateTime() ) );
                        ps.setLong(6, tmpSmsReplyPushList.get(i).getUserId());
                        ps.setLong(7, tmpSmsReplyPushList.get(i).getTaskId());
                        ps.setString(8, tmpSmsReplyPushList.get(i).getRegion());
                        ps.setInt(9, tmpSmsReplyPushList.get(i).getState());
                    }

                    public int getBatchSize() {
                        return tmpSmsReplyPushList.size();
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
