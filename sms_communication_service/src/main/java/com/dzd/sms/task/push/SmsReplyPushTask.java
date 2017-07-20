package com.dzd.sms.task.push;


import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsReplyPush;
import com.dzd.sms.service.data.SmsSendLog;
import com.dzd.sms.service.data.SmsTaskData;
import com.dzd.sms.service.data.SmsUser;
import com.dzd.sms.task.base.BaseTask;
import com.dzd.utils.*;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by Administrator on 2017-1-16.
 */

public class SmsReplyPushTask extends BaseTask {
    private static Logger logger = Logger.getLogger(SmsReplyPushTask.class);

    String mkey;



    public SmsReplyPushTask() {
        this.mkey = Define.KEY_SMS_AISLE_REPLY_PUSH;
        this.isMutiThread = true;
        this.singlePackageNumber = 100;
        this.threadNumber = 1;
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

        Map<String, List<SmsReplyPush>> stringListMap = new HashedMap();
        try {
            long len = redisClient.llen(mkey);
            if (len > singlePackageNumber) len = singlePackageNumber;
            for (int i = 0; i < len; i++) {
                Object byteValue = redisClient.lpop(mkey);
                if (byteValue != null) {
                    SmsReplyPush smsReplyPush = (SmsReplyPush) byteValue;
                    logger.info(" smsReplyPush ---------- "+smsReplyPush.getPhone()+" "+smsReplyPush.getContent() );
                    //if( smsReplyPush.getTaskId().longValue()<1){
                        String[] aids = smsReplyPush.getAisleIds().toArray(new String[ smsReplyPush.getAisleIds().size()]);
                        if( aids.length>0 ) {
                            List<SmsSendLog> smsSendLogList = SmsServerManager.I.getSmsSendLogs(aids, smsReplyPush.getPhone());
                            List<Long> uniq = new ArrayList<Long>(); //为保证一个用户只推一次
                            List<SmsUser> smsUsers = new ArrayList<SmsUser>();
                            for (SmsSendLog smsSendLog : smsSendLogList) {
                                String taskId = smsSendLog.getTaskId().toString();
                                SmsTaskData smsTaskData = SmsServerManager.I.getSmsTaskDataFromCache( taskId );


                                if( smsTaskData == null ) {
                                    logger.error("smsTaskData is null taskId="+taskId );
                                }else{
                                    if (uniq.contains(smsTaskData.getUserId())) {

                                    } else {
                                        logger.info(" smsReplyPush----userid=" + smsTaskData.getUserId());
                                        SmsUser smsUser = SmsServerManager.I.getUser(smsTaskData.getUserId());

                                        smsReplyPush.setTaskId(smsSendLog.getTaskId());
                                        smsReplyPush.setUserId(smsTaskData.getUserId());

                                        if( smsTaskData.getSendWay() == 0 ) {
                                            if (smsUser != null && smsUser.getReplyUrl() != null && smsUser.getReplyUrl().substring(0, 1).toUpperCase().equals("H")) {
                                                smsReplyPush.setPushUrl(smsUser.getReplyUrl());
                                            } else {
                                                smsReplyPush.setPushUrl(smsTaskData.getCallbackUrl());
                                            }
                                            //这里需要推送下游代理商
                                            pushReply(smsReplyPush);
                                        }else{
                                            smsReplyPush.setPushUrl(smsTaskData.getCallbackUrl());
                                            //CmppServiceManager.pushReply( smsTaskData.getUserId().toString(), smsReplyPush );
                                        }
                                        uniq.add(smsTaskData.getUserId());

                                    }
                                }


                            }

                            if( smsSendLogList.size()==0){
                                logger.info("从缓存中，没有找到通道信息"+aids+ " " + smsReplyPush.getPhone());
                            }
                        }else{
                            logger.info(" aids length=0");
                        }
                    //}else {
                    //    //这里需要推送下游代理商
                    //    pushReply(smsReplyPush);
                    //}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SmsServerManager.I.taskMap.get("UpdateReplyPushLog").signalNotEmpty();

        logger.info("singleExecutor");
    }


    /**
     * @param smsReplyPush
     */
    public void pushReply(SmsReplyPush smsReplyPush) {
        logger.info(" push url =" + smsReplyPush.getPushUrl());

        if( smsReplyPush.getPushUrl() == null ){
            logger.error(" pushReply url=" + smsReplyPush.getPushUrl() );
            smsReplyPush.setState(Define.PUSH_STATE_URL_NULL);
        }else {
            String re = "";
            try {
                String apiKey = "";
                Long userId = smsReplyPush.getUserId();
                SmsUser user = SmsServerManager.I.getUser(userId);
                apiKey = user.getKey();

                Map<String, String> params = new HashMap<String, String>();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", smsReplyPush.getTaskId());
                jsonObject.put("mobile", smsReplyPush.getPhone());
                jsonObject.put("reply_time", DateUtil.formatDateTime(smsReplyPush.getCreateTime()));
                jsonObject.put("text", smsReplyPush.getContent());

                String md5 = StringUtil.string2MD5(
                        jsonObject.getString("id") + ","
                                + jsonObject.getString("mobile") + ","
                                + jsonObject.getString("reply_time") + ","
                                + jsonObject.getString("text") + "," + apiKey);
                jsonObject.put("_sign", md5);
                params.put("sms_reply", URLEncoder.encode(jsonObject.toString(), "UTF-8"));
                //String re = NetUtil.post(this.sendUrl, params);
                re = NetUtil.post(smsReplyPush.getPushUrl(), params);
                logger.info(re + " param json=" + jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            smsReplyPush.setState(Define.PUSH_STATE_YES);

        }
        //加到保存数据库队列
        redisClient.rpush( Define.KEY_SMS_AISLE_REPLY_PUSH_DB , smsReplyPush);


    }

}
