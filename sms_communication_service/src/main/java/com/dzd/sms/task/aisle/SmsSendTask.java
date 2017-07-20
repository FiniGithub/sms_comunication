package com.dzd.sms.task.aisle;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;
import com.dzd.sms.addons.aisle.BaseAisle;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.PhoneInfo;
import com.dzd.sms.service.data.SmsAisleGroupAisleRelation;
import com.dzd.sms.service.data.SmsMessage;
import com.dzd.sms.service.data.SmsTaskData;
import com.dzd.sms.task.base.BaseTask;
import com.dzd.utils.DateUtil;

import com.dzd.utils.StringUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dzd.sms.application.Define.KEY_SMS_TASK;


/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/5
 * Time: 8:59
 */

public class SmsSendTask extends BaseTask {
    private static Logger logger = Logger.getLogger(SmsSendTask.class);

    RedisClient redisClient = RedisManager.I.getRedisClient();

    public SmsSendTask() {
        threadNumber = 10;
    }


    public boolean existTask() {
        return redisClient.llen(KEY_SMS_TASK) > 0;
    }

    @Override
    public void singleExecutor() {

        long taskNum = redisClient.llen(KEY_SMS_TASK);

        //判断队列是否有内容
        if (taskNum > 0) {

            //打印消息
            logger.info("task queue num:" + taskNum);

            //先取出发送任务
            SmsTaskData smsTaskData = (SmsTaskData) redisClient.lpop(KEY_SMS_TASK);

            //加一判断， 防止异常
            if (smsTaskData == null) {
                return;
            }
            logger.info("start-taskid=" + smsTaskData.getTaskId());

            //是否通过审核
            if (!smsTaskData.isAuditState()) {

                String messageKey = SmsServerManager.I.getSmsMessageQueueKey(smsTaskData.getTaskId());

                String messageSendKey = (Define.KEY_SMS_TASK_MESSAGE_AUDITFAIL + smsTaskData.getTaskId());

                //2. 需要分流
                String messageByteKey = SmsServerManager.I.getSmsMessageQueueKey(smsTaskData.getTaskId());
                Long messageLen = redisClient.llen(messageByteKey);
                int phoneNumber = 0;
                for (int i = 0; i < messageLen; i++) {
                    Object messageByteValue = redisClient.lpopString(messageByteKey);
                    if (messageByteValue != null) {
                        List<String> mobiles = new ArrayList<String>();
                        String smsMessageStr = (String) messageByteValue;
                        String[] smsMessageS = StringUtil.split(smsMessageStr, "-");
                        for (int x = 0; x < smsMessageS.length; x++) {
                            String row = smsMessageS[x];
                            String[] cols = StringUtil.split(row, ",");
                            String mobile = cols[1];
                            mobiles.add(mobile);
                        }
                        phoneNumber += mobiles.size();
                        redisClient.rpush(messageSendKey, StringUtil.arrayToString(mobiles, "-"));
                    }
                }
                BaseAisle aisleObject = (BaseAisle) SmsServerManager.I.getAisle("SmsAuditFailAisle");
                aisleObject.setMessageQueueKey(messageSendKey);
                aisleObject.sendTotalNum = phoneNumber;
                aisleObject.send(smsTaskData);

            } else {

                //获取发送通道组ID
                Long groupId = smsTaskData.getAisleGroupId();

                //获取通道组下面的通道列表
                //SmsAisleSelectParam smsAisleSelectParam = smsUserDataTool.getAisleGroupRelation(groupId);

                //List<SmsAisleGroupAisleRelation> smsAisleList = smsAisleSelectParam.getSmsAisleGroupAisleRelationList();

                //获取通道组包括的通道信息列表
                List<SmsAisleGroupAisleRelation> smsAisleList = SmsServerManager.I.getAisleGroupRelationList(groupId);


                //没有通道信息直接处理为失败
                if (smsAisleList == null || smsAisleList.size() == 0) {
                    String messageByteKey = SmsServerManager.I.getSmsMessageQueueKey(smsTaskData.getTaskId());
                    String cacheKey = "null-" + smsTaskData.getTaskId();
                    int phoneNumber = 0;
                    Long messageLen = redisClient.llen(messageByteKey);
                    for (int i = 0; i < messageLen; i++) {
                        Object messageByteValue = redisClient.lpopString(messageByteKey);
                        if (messageByteValue != null) {
                            List<String> mobiles = new ArrayList<String>();
                            String smsMessageStr = (String) messageByteValue;
                            String[] smsMessageS = StringUtil.split(smsMessageStr, "-");
                            for (int x = 0; x < smsMessageS.length; x++) {
                                String row = smsMessageS[x];
                                String[] cols = StringUtil.split(row, ",");
                                String mobile = cols[1];
                                mobiles.add(mobile);
                            }
                            phoneNumber += mobiles.size();
                            redisClient.rpush(cacheKey, StringUtil.arrayToString(mobiles, "-"));
                        }
                    }


                    BaseAisle aisleObject = (BaseAisle) SmsServerManager.I.getAisle("SmsNullAisle");
                    aisleObject.setMessageQueueKey(cacheKey);
                    aisleObject.sendTotalNum = phoneNumber;
                    aisleObject.send(smsTaskData);
                    return;//--------------------重要更新----------------
                }

                //优先计算的通道组
                List<SmsAisleGroupAisleRelation> smsAisleList1 = new ArrayList<SmsAisleGroupAisleRelation>();


                //是否有不识别的号码直接失败
                boolean isNullCode = false;

                //是否有黑名单号码
                boolean isBlacklist = false;
                int blacklistNum = 0; //黑名单数量

                //超过发送次数
                boolean isMaxSendNumber = false;


                //分流后的数量统计
                Map<String, Integer> partitionNum = new HashMap<String, Integer>();

                //1. 按关键字分流
                String content = smsTaskData.getText();
                for (SmsAisleGroupAisleRelation sga : smsAisleList) {
                    //这里使用的作务ID， 在方法中进一步处理
                    sga.calCacheKey(smsTaskData.getTaskId());
                    String k = sga.getKeyword();

                    logger.info(" aisleName=" + sga.getSmsAisle().getName() + " keyword=" + sga.getKeyword() + " " + sga.getSmsAisle().getRegionId().intValue() + " RegionId=" + sga.getSmsAisle().getRegionId().intValue() + " cachekey=" + new String(sga.getCacheKey()));
                    if (k != null && k.length() > 1) {
                        String[] keywords = StringUtil.split(k, ",");
                        for (String words : keywords) {
                            if (content.indexOf(words) != -1) {
                                //smsAisleAll = sga.getSmsAisle();
                                smsAisleList1.add(sga);
                            }
                        }
                    }
                }
                String blacklistKey = (Define.KEY_SMS_TASK_MESSAGE_BLACKLIST + smsTaskData.getTaskId());
                String nullKey = (Define.KEY_SMS_TASK_MESSAGE_NULL + smsTaskData.getTaskId());
                String maxSendNumberKey = (Define.KEY_SMS_TASK_MESSAGE_MAXSENDNUMBER + smsTaskData.getTaskId());
                boolean exsitsBlacklist = SmsServerManager.I.existsBlacklist();
                String whitePhoneString = redisClient.get(Define.KEY_CACHE_WHITELIST);
                boolean exsitsWhitelist = whitePhoneString != null && whitePhoneString.length() > 10;


                RedisClient redisClient2 = RedisManager.I.newRedisClient();
                redisClient2.openResource();

                //2. 需要分流
                String messageByteKey = SmsServerManager.I.getSmsMessageQueueKey(smsTaskData.getTaskId());
                Long messageLen = redisClient.llen(messageByteKey);
                for (int i = 0; i < messageLen; i++) {
                    Object messageByteValue = redisClient.lpopString(messageByteKey);
                    if (messageByteValue != null) {
                        Map<String, List<String>> partitionAisle = new HashMap<String, List<String>>();
                        String smsMessageStr = (String) messageByteValue;
                        String[] smsMessageS = StringUtil.split(smsMessageStr, "-");
                        for (int x = 0; x < smsMessageS.length; x++) {
                            String row = smsMessageS[x];
                            String cacheKey = "-";
                            String[] cols = StringUtil.split(row, ",");
                            String mobile = cols[1];
                            SmsMessage smsMessage = new SmsMessage(mobile, cols[0], 0.0);
                            smsMessage.setProvince(cols[2]);
                            smsMessage.setRegionId(Long.valueOf(cols[3]));
                            smsMessage.setSupplier(Integer.valueOf(cols[4]));
                            smsMessage.setNum(Integer.valueOf(cols[5]));
                            //smsMessageList.add(smsMessage);
                            //System.out.println("add phone to dababase ="+smsMessage.getMobile());

//                            //发送限10次， 并不在白名单 中
//                            if( SmsServerManager.I.getTodaySendNumber( smsMessage.getMobile())>10 && ( !exsitsWhitelist || whitePhoneString.indexOf( smsMessage.getMobile()) == -1 ) ) {
//                                isMaxSendNumber = true;
//                                cacheKey = maxSendNumberKey;
//                                //redisClient.rpush( maxSendNumberKey, messageByteValue );
//
//                                logger.info("getTodaySendNumber smsMessage mobile="+smsMessage.getMobile());
//
//                                //黑名单号码， 并不在白名单 中
//                            }else if ( exsitsBlacklist && SmsServerManager.I.isBlacklistPhone(smsMessage.getMobile()) && ( !exsitsWhitelist || whitePhoneString.indexOf( smsMessage.getMobile()) == -1 ) ) {
//                                isBlacklist = true;
//                                blacklistNum+=1;
//                                //redisClient.rpush( blacklistKey, messageByteValue);
//
//                                cacheKey = blacklistKey;
//                                logger.info("exsitsBlacklist smsMessage mobile="+smsMessage.getMobile());
//
//                            } else { //正常号码， 判断需要走那个通道

                            if (true) {
                                //logger.info("passed smsMessage mobile="+smsMessage.getMobile());


                                boolean isCheck = true;
                                String className = ""; //通道类名


                                //按关键词进行通道分流
                                for (SmsAisleGroupAisleRelation sga : smsAisleList1) {
                                    boolean isOk = false;
                                    //0：联通，1：移动，2：电信
                                    if (smsMessage.getSupplier().equals(sga.getOperatorId())) {
                                        isOk = true;
                                    }
                                    //运营商和地址都匹配成功
                                    if (isCheck && isOk) {
                                        isCheck = false;
                                        className = sga.getSmsAisle().getClassName();
                                        cacheKey = sga.getCacheKey2();
                                    }
                                }

                                //没有区配到通道
                                if (isCheck) {
                                    //开始计算号码通道分流
                                    for (SmsAisleGroupAisleRelation sga : smsAisleList) {
                                        boolean isOk = false;
                                        //运营商id；0：联通，1：移动，2：电信 -1:未知
                                        if (smsMessage.getSupplier().equals(sga.getOperatorId())) {
                                            isOk = true;
                                        }
                                        //运营商匹配成功
                                        if (isCheck && isOk) {
                                            //redisClient.rpush(sga.getCacheKey2(), messageByteValue);
                                            isCheck = false;
                                            className = sga.getSmsAisle().getClassName();
                                            cacheKey = sga.getCacheKey2();
                                        }

                                        //logger.info(" group id=" + sga.getGroupId() + " aisle id="+sga.getSmsAisle().getId());
                                    }
                                    //logger.info(" mobile=" + smsMessage.getMobile() + "x=" + (i) + "," + DateUtil.formatTime() + " checkOk=" + isCheck + " class=" + className   + " Supplier=" + phoneInfo.getSupplier().intValue() + " group size="+ smsAisleList.size();

                                }


                                //没有区配到通道
                                if (isCheck) {
                                    //开始计算号码通道分流
                                    for (SmsAisleGroupAisleRelation sga : smsAisleList) {
                                        if (sga.getSmsAisle().isUnicom() && sga.getSmsAisle().isMobile() && sga.getSmsAisle().istelecom() && sga.getSmsAisle().getRegionId().intValue() == -1) {
                                            //redisClient.rpush(sga.getCacheKey2(), messageByteValue);
                                            isCheck = false;
                                            className = sga.getSmsAisle().getClassName();

                                            cacheKey = sga.getCacheKey2();
                                            //logger.info("last mobile=" + smsMessage.getMobile() + "x=" + (i) + "," + DateUtil.formatTime() + " checkOk=" + isCheck + " class=" + className + "   group size="+ smsAisleList.size())
                                        }
                                    }
                                }

                                //没有区配通道， 这里需要转失败
                                if (isCheck) {
                                    isNullCode = true;
                                    //redisClient.rpush( nullKey, messageByteValue);
                                    cacheKey = nullKey;
                                }

                            }//完成黑名单

                            if (partitionAisle.containsKey(cacheKey)) {
                                partitionAisle.get(cacheKey).add(mobile);
                            } else {
                                List<String> s = new ArrayList<String>();
                                s.add(mobile);
                                partitionAisle.put(cacheKey, s);
                            }

                        }//完成分


                        //按分通道后缓存到REDIS
                        for (String k : partitionAisle.keySet()) {
                            logger.info("k=" + k + ",len=" + partitionAisle.get(k).size());
                            redisClient2.rpush(k, StringUtil.arrayToString(partitionAisle.get(k), "-"));
                            if (partitionNum.containsKey(k)) {
                                partitionNum.put(k, partitionAisle.get(k).size() + partitionNum.get(k));
                            } else {
                                partitionNum.put(k, partitionAisle.get(k).size());
                            }
                        }

                    }//messageValue=null
                }


                redisClient2.closeResource();

                logger.info("-------------------------------------");
                logger.info("end-taskid=" + smsTaskData.getTaskId() + " len=" + messageLen + " 通道发送=" + smsAisleList.size()
                        + " 空号=" + " " + isNullCode
                        + " 黑名单=" + " " + isBlacklist
                        + " MaxSendNumber=" + isMaxSendNumber);

                //分通道发送
                for (SmsAisleGroupAisleRelation sga : smsAisleList) {
                    if (redisClient.llen(sga.getCacheKey2()) > 0) {
                        String className = sga.getSmsAisle().getClassName();

                        if (SmsServerManager.I.hasBaseAisleAdapter(className)) {
                            //插件方式获取
                            BaseAisle baseAisle = SmsServerManager.I.newBaseAisle(className, sga.getSmsAisle(), sga.getCacheKey2());
                            baseAisle.sendTotalNum = partitionNum.get(sga.getCacheKey2());
                            baseAisle.send(smsTaskData);
                        } else {
                            BaseAisle aisleObject = (BaseAisle) SmsServerManager.I.getAisle(className);
                            aisleObject.sendTotalNum = partitionNum.get(sga.getCacheKey2());
                            aisleObject.setSmsAisle(sga.getSmsAisle());
                            aisleObject.setAisleId(sga.getSmsAisle().getId());
                            aisleObject.setMessageQueueKey(sga.getCacheKey2());
                            aisleObject.send(smsTaskData);
                        }

                        logger.info(" aisleName=" + sga.getSmsAisle().getName() + " llen=" + redisClient.llen(sga.getCacheKey2()) + " " + sga.getSmsAisle().getRegionId().intValue() + " RegionId=" + sga.getSmsAisle().getRegionId().intValue() + " cachekey=" + new String(sga.getCacheKey()));

                    }
                }


                //空号转成失败
                if (isNullCode) {
                    BaseAisle aisleObject = (BaseAisle) SmsServerManager.I.getAisle("SmsNullAisle");
                    aisleObject.setMessageQueueKey(nullKey);
                    aisleObject.sendTotalNum = partitionNum.get(nullKey);
                    aisleObject.send(smsTaskData);
                }

                //黑名单转成失败
                if (isBlacklist) {
                    BaseAisle aisleObject = (BaseAisle) SmsServerManager.I.getAisle("SmsBlacklistAisle");
                    aisleObject.setMessageQueueKey(blacklistKey);
                    aisleObject.sendTotalNum = partitionNum.get(blacklistKey);
                    aisleObject.send(smsTaskData);

                    smsTaskData.setBlacklist_phone_num(blacklistNum);
                    SmsServerManager.I.cacheSmsTaskDate(smsTaskData);
                }

                if (isMaxSendNumber) {
                    BaseAisle aisleObject = (BaseAisle) SmsServerManager.I.getAisle("SmsMaxNumberAisle");
                    aisleObject.setMessageQueueKey(maxSendNumberKey);
                    aisleObject.sendTotalNum = partitionNum.get(maxSendNumberKey);
                    aisleObject.send(smsTaskData);
                }

            }//通过审核处理完成


            //黑名单转失败

        }
    }
}
