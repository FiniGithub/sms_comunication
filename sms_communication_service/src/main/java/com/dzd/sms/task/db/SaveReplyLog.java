package com.dzd.sms.task.db;


import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;
import com.dzd.db.mysql.MysqlOperator;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsReply;
import com.dzd.sms.service.data.SmsReplyPush;
import com.dzd.sms.task.base.BaseTask;
import com.dzd.utils.DateUtil;

import com.dzd.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/6
 * Time: 12:12
 */

public class SaveReplyLog extends BaseTask {

    private static Logger logger = Logger.getLogger(SaveReplyLog.class);

    String mkey = Define.KEY_SMS_AISLE_REPLY;
    RedisClient redisClient = RedisManager.I.getRedisClient();

    public SaveReplyLog() {
        this.singlePackageNumber = 1;
        this.threadNumber = 2;
    }



    /**
     * 判断是否有任务可以执行
     */
    public boolean existTask() {
        return redisClient.llen(mkey) > 0;
    }

    @Override
    public void singleExecutor() {

        List<SmsReply> smsReplyArrayList = new ArrayList<SmsReply>();
        List<SmsReplyPush> smsReplyPushArrayList = new ArrayList<SmsReplyPush>();
        try {
            long len = redisClient.llen(mkey);
            if (len > singlePackageNumber) len = singlePackageNumber;
            for (int i = 0; i < len; i++) {
                Object objectValue = redisClient.lpopString(mkey);
                if ( objectValue != null) {
                    String smsReplyStr = (String)objectValue;
                    String[] smsReplyS = StringUtil.split(smsReplyStr,"☆");
                    for(int x=0;x<smsReplyS.length; x++) {
                        String[] cols = StringUtil.split(smsReplyS[x], "★");

                        SmsReply smsReply = new SmsReply(".");
                        smsReply.setAisleClassName(cols[0]);
                        smsReply.setMobile(cols[1]);
                        smsReply.setContent(cols[2]);
                        smsReply.setAisleId(Long.valueOf(cols[3]));
                        smsReply.setAisleCode(cols[4]);

                        //防止数据为空
                        try {
                            SmsReplyPush smsReplyPush = new SmsReplyPush();
                            smsReplyPush.setAisleId(smsReply.getAisleId());
                            smsReplyPush.setContent(smsReply.getContent());
                            //smsReplyPush.setUserId(smsTaskData.getUserId());
                            smsReplyPush.setCreateTime(new Date());
                            smsReplyPush.setUpdateTime(new Date());
                            smsReplyPush.setPhone(smsReply.getMobile());
                            smsReplyPush.setRegion("-");
                            smsReplyPush.setState(Define.PUSH_STATE_NO);//推送状态（1：未推送，2：已推送）注：推送给下家
                            String className = smsReply.getAisleClassName();
                            //smsReplyPush.setAisleIds( smsUserDataTool.getSmsAisleIds( className ));

                            //2017-03-21 修改为ID来做查找多个ID
                            smsReplyPush.setAisleIds(SmsServerManager.I.getSmsAisleIds(smsReply.getAisleId()));

                            //smsReplyPush.setTaskId(smsTaskData.getTaskId());
                            //smsReplyPush.setPushUrl(smsTaskData.getCallbackUrl());
                            smsReplyPushArrayList.add(smsReplyPush);

                            //添加到队列
                            redisClient.rpush( Define.KEY_SMS_AISLE_REPLY_PUSH,  smsReplyPush);
                            //}
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        //回复的日志
                        smsReplyArrayList.add(smsReply);
                    }
                }
            }


            SmsServerManager.I.taskMap.get("SmsReplyPushTask").signalNotEmpty();

        } catch (Exception e) {
            excep = true;
            e.printStackTrace();
        }





        try {
            if (smsReplyArrayList.size() > 0) {
                final List<SmsReply> tmpSmsReplyList = smsReplyArrayList;
                String sql = "insert into sms_receive_reply_log( phone, content, sms_aisle_id,aisle_code, create_time ) values(?,?,?,?,?)";
                MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        String createDate = DateUtil.formatDateTime();
                        ps.setString(1, tmpSmsReplyList.get(i).getMobile());
                        ps.setString(2, tmpSmsReplyList.get(i).getContent());
                        ps.setLong(3, tmpSmsReplyList.get(i).getAisleId());
                        ps.setString(4, tmpSmsReplyList.get(i).getAisleCode());
                        ps.setString(5, createDate);
                    }

                    public int getBatchSize() {
                        return tmpSmsReplyList.size();
                    }
                });

            }
        } catch (Exception e) {
            excep = true;
            e.printStackTrace();

            //发生异常需要把信息先缓存起来
            for (SmsReply smsReply : smsReplyArrayList) {
                redisClient.rpush( Define.KEY_SMS_DB_EXCEPTION_REPLY_LOG, smsReply );
            }

            logger.error("Exception:" + e.getCause().getClass() + "," + e.getCause().getMessage());

        }






    }
}