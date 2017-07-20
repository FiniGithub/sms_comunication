package com.dzd.sms.task.db;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;

import com.dzd.db.mysql.MysqlOperator;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.*;
import com.dzd.sms.task.base.BaseTask;

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
 * Time: 12:12
 */
public class SaveReportLog extends BaseTask {

    private static Logger logger = Logger.getLogger(SaveReportLog.class);

    //发送号码缓存KEY
    String sendLogKey = Define.KEY_SMS_AISLE_SEND_RESULT;
    String mkey = Define.KEY_SMS_AISLE_REPORT;


    static int noInit = 0;//debug

    public SaveReportLog() {
        this.singlePackageNumber = 1;
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

        int no = ++noInit;//debug
        RedisClient redisClient = RedisManager.I.newRedisClient();
        redisClient.openResource();
        List<SmsReport> smsReportListTmp = new ArrayList<SmsReport>();
        List<SmsReport> smsReportList = new ArrayList<SmsReport>();
        List<SmsMessage> smsMessageList = new ArrayList<SmsMessage>();
        List<SmsReportPush> smsReportPushList = new ArrayList<SmsReportPush>();
        Map<String, SmsTaskData> smsTaskDataMap = new HashMap<String, SmsTaskData>();
        Map<String, Integer> smsReportNumForTask = new HashMap<String, Integer>();

        Map<String, List<String>> updateTaskPhoneMap = new HashMap<String, List<String>>();

        List<String> pushReportList = new ArrayList<String>();

        try {
            long len = redisClient.llen(mkey);

            logger.info(no + ".SaveReportLog-singleExecutor-start.len=" + len);

            StringBuilder midListStr = new StringBuilder();
            if (len > singlePackageNumber) len = singlePackageNumber;
            for (int i = 0; i < len; i++) {
                Object objectValue = redisClient.lpopString(mkey);
//                logger.info( "objectvalue="+objectValue);
                if (objectValue != null) {
                    String smsReportStr = (String) objectValue;
                    String[] smsReportS = StringUtil.split(smsReportStr, "☆");
                    for (int x = 0; x < smsReportS.length; x++) {


                        String[] cols = StringUtil.split(smsReportS[x], ",");
//                        logger.info("x="+x+","+smsReportS[x]);
//                        if( cols[0].length()<32 || cols[0].length()>32){
//                            logger.error("-------------------------");
//
//                            logger.info("x="+x+","+smsReportStr);
//                        }

                        SmsReport smsReport = new SmsReport(cols[0]);
                        smsReport.setMobile(cols[1]);
                        smsReport.setState(cols[2]);
                        smsReport.setStateCode(Integer.valueOf(cols[3]));
                        smsReport.setReceiveDate(cols[4]);
                        smsReport.setPushNum(8);   //默认为8
                        smsReportListTmp.add(smsReport);
                        midListStr.append(",").append(smsReport.getMid());
                    }//end for
                }//end !=null
            }//end for i..len
            String[] mids = {"0"};
            if (midListStr.length() > 1) {
                mids = StringUtil.split(midListStr.substring(1).toString(), ",");
            }
            List<String> midList = redisClient.hmget(sendLogKey, mids);
            logger.info("mids.length=" + mids.length + ",midList.size=" + midList.size()+",smsReportListTmp.size="+smsReportListTmp.size());

//            Map<String, String> midTaskIdMap = new HashMap<String, String>();
//            for (int i = 0; i < mids.length; i++) {
//                midTaskIdMap.put(mids[i], midList.get(i));
//            }
            //for (SmsReport smsReport : smsReportListTmp) {

            for (int i = 0; i < smsReportListTmp.size(); i++) {
                SmsReport smsReport = smsReportListTmp.get(i);
                //更新消息
                //String fieldKey = smsReport.getMid();
                String taskId = midList.get(i);//midTaskIdMap.get(fieldKey);
                if (taskId != null) {
                    String phoneMapKey = new StringBuilder().append(taskId).append(",").append(smsReport.getState()).append(",").append(smsReport.getStateCode()).append(",").append(smsReport.getReceiveDate()).toString();
                    if (updateTaskPhoneMap.containsKey(phoneMapKey)) {
                        updateTaskPhoneMap.get(phoneMapKey).add("'" + smsReport.getMobile() + "'");
                    } else {
                        List<String> p = new ArrayList<String>();
                        p.add("'" + smsReport.getMobile() + "'");
                        updateTaskPhoneMap.put(phoneMapKey, p);
                    }


                    smsReport.setPushNum(7); //存在发送日志为7

                    //发送任务信息
                    SmsTaskData smsTaskData = null;
                    if (smsTaskDataMap.containsKey(taskId)) {
                        smsTaskData = smsTaskDataMap.get(taskId);
                    } else {
                        smsTaskData = (SmsTaskData) redisClient.hgetObject(Define.KEY_SMS_TASK_CACHE, taskId);
                    }

                    if (!smsReportNumForTask.containsKey(taskId)) {
                        smsReportNumForTask.put(taskId, 1);
                    } else {
                        smsReportNumForTask.put(taskId, 1 + smsReportNumForTask.get(taskId));
                    }


                    //有任务信息， 发送推送报告
                    if (smsTaskData != null) {

                        smsReport.setUserId(smsTaskData.getUserId());
                        smsReport.setPushNum(6);//存在发送任务为6

                        //if( smsSendResult.getState().equals(STATE_SENDING) && smsTaskData.getCallbackUrl() !=null ) {
                        if (smsTaskData.getCallbackUrl() != null) {

                            smsReport.setPushNum(0); //存在推送URL为0

                            //推送消息报告
                            SmsReportPush smsReportPush = new SmsReportPush();
                            smsReportPush.setPushUrl(smsTaskData.getCallbackUrl());
                            smsReportPush.setMobile(smsReport.getMobile());
                            smsReportPush.setMid(smsReport.getMid());
                            smsReportPush.setReceiveDate(smsReport.getReceiveDate());
                            smsReportPush.setPushState(Define.PUSH_STATE_NO);
                            smsReportPush.setState(String.valueOf(smsReport.getStateCode()));
                            smsReportPush.setTaskId(smsTaskData.getTaskId());
                            smsReportPush.setPushWay(smsTaskData.getSendWay());
                            smsReportPush.setUserId(smsTaskData.getUserId());
                            smsReportPush.setSubmitDate(smsTaskData.getTaskSendTime());
                            smsReportPushList.add(smsReportPush);
                            pushReportList.add(new StringBuilder().append(taskId).append(",").append(smsReport.getReceiveDate()).append(",").append(smsReport.getStateCode()).append(",").append(smsReport.getMobile()).append(",").append(smsTaskData.getSendWay()).toString());

                            //保存到推送队列
                            //redisClient.rpush( Define.KEY_SMS_AISLE_REPORT_PUSH ,  smsReportPush );

                            //logger.info(" jedisCluster.rpush " + Define.KEY_SMS_AISLE_REPORT_PUSH + " CallbackUrl= " + smsTaskData.getCallbackUrl());
                        } else {
                            //logger.info(" jedisCluster.rpush " + Define.KEY_SMS_AISLE_REPORT_PUSH + " CallbackUrl= " + smsTaskData.getCallbackUrl() + " 不用推送...smsSendResult.state=" + smsReport.getState());
                        }
                    } else {
                        smsReport.setUserId(-2l);
                    }

                    //设置状态报告的任务ID
                    smsReport.setTaskId(taskId);


                    //更新号码发送状态
                    SmsMessage smsMessage = new SmsMessage(smsReport.getMobile(), taskId, 0.0);
                    smsMessage.setState(smsReport.getStateCode());
                    smsMessageList.add(smsMessage);

                } else {
                    smsReport.setTaskId("-1");
                    smsReport.setUserId(-1l);
                }

                smsReportList.add(smsReport);
            }



        ////////////////////////批量删除//////////////////////
        //删除发送号码记录缓存，报告有了， 不需要再匹配
        redisClient.hdel(sendLogKey, mids);


    }

    catch(
    Exception e
    )

    {
        excep = true;
        e.printStackTrace();
    }

    try

    {
        if (pushReportList.size() > 0) {
            redisClient.rpushString(Define.KEY_SMS_AISLE_REPORT_PUSH, StringUtil.arrayToString(pushReportList, "☆"));
        }
    }

    catch(
    Exception e
    )

    {
        e.printStackTrace();
    }


    //保存状态报告
    try

    {
        if (smsReportList.size() > 0) {
            logger.info(no + ".SaveReport-insert.start.len=" + smsReportList.size());
            final List<SmsReport> tmpSmsReportList = smsReportList;
            String sql = "insert into sms_receive_log( phone, smid, sms_task_id, sms_user_id,state,state_code, create_time,push_num ) values(?, ?, ?, ?, ?,? ,?,? )";
            MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    String createDate = tmpSmsReportList.get(i).getReceiveDate();
                    ps.setString(1, tmpSmsReportList.get(i).getMobile());
                    ps.setString(2, tmpSmsReportList.get(i).getMid());
                    ps.setString(3, tmpSmsReportList.get(i).getTaskId());
                    ps.setLong(4, tmpSmsReportList.get(i).getUserId());
                    ps.setString(5, tmpSmsReportList.get(i).getState());
                    ps.setInt(6, tmpSmsReportList.get(i).getStateCode());
                    ps.setString(7, createDate);
                    ps.setInt(8, tmpSmsReportList.get(i).getPushNum());
                }

                public int getBatchSize() {
                    return tmpSmsReportList.size();
                }
            });

        }
    }

    catch(
    Exception e
    )

    {
        excep = true;
        e.printStackTrace();
        //发生异常需要把信息先缓存起来
        for (SmsReport smsReport : smsReportList) {
            redisClient.rpush(Define.KEY_SMS_TASK_EXCEPTION_CACHE, smsReport);
        }

    }

    try

    {
        for (Map.Entry<String, Integer> entry : smsReportNumForTask.entrySet()) {
            //必需添加
            SmsServerManager.I.addSmsTaskSaveReportPhoneNumber(entry.getKey(), entry.getValue());
        }
    }

    catch(
    Exception e
    )

    {
        e.printStackTrace();
        logger.error("SmsServerManager.I.addSmsTaskSaveReportPhoneNumber." + smsReportNumForTask.size());
    }


    String updateSql = "";

    //更新发送号码状态
    try

    {
        if (smsMessageList.size() > 0) {


            logger.info(no + ".SaveReport-update-insert.start.len=" + smsMessageList.size());

            //String phoneMapKey = new StringBuilder().append(taskId).append(",").append(smsReport.getState()).append(",").append(smsReport.getStateCode()).append(",").append(smsReport.getReceiveDate()).toString();

            for (String key : updateTaskPhoneMap.keySet()) {
                String[] parts = StringUtil.split(key, ",");
                updateSql = "UPDATE sms_send_task_phone  AS p SET p.state =" + parts[2] + ",p.receive_state ='" + parts[1] + "',p.receive_code =" + parts[2] + ",p.receive_time = '" + parts[3] + "' WHERE p.sms_send_task_id=" + parts[0] + " AND p.phone IN (" + StringUtil.arrayToString(updateTaskPhoneMap.get(key), ",") + ")";
                //logger.info(updateSql);
                try {
                    MysqlOperator.I.execute(updateSql);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info(no + ".SaveReport-Exception-updateSql=" + updateSql);
                    //发生异常需要把信息先缓存起来
                    redisClient.rpushString(Define.KEY_SMS_DB_EXCEPTION_REPORT_LOG_UPDATE, updateSql);


                }
            }

//
//                final List<SmsMessage> tmpSmsMessageList = smsMessageList;
//                String sql = "update   sms_send_task_phone set state=?, update_time=? where sms_send_task_id=? and phone=? ";
//                MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter() {
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        String dateTime = DateUtil.formatDateTime();
//                        ps.setInt(1, tmpSmsMessageList.get(i).getState());
//                        ps.setString(2, dateTime);
//                        ps.setString(3, tmpSmsMessageList.get(i).getTaskId());
//                        ps.setString(4, tmpSmsMessageList.get(i).getMobile());
//                        //System.out.println(" taskid=" +tmpSmsMessageList.get(i).getTaskId()+",mobile=" + tmpSmsMessageList.get(i).getMobile());
//                    }
//
//                    public int getBatchSize() {
//                        return tmpSmsMessageList.size();
//                    }
//                });

        }
    }

    catch(
    Exception e
    )

    {
        excep = true;
        e.printStackTrace();

        logger.error("SaveReportLog.update.Exception:" + e.getCause().getClass() + "," + e.getCause().getMessage());

    }

    //
    logger.info("closeResource..");
    redisClient.closeResource();


}


}
