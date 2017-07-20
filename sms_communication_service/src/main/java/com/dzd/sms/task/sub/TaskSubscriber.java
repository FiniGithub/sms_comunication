package com.dzd.sms.task.sub;

import com.dzd.cache.redis.manager.Subscriber;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsTaskData;
import com.dzd.utils.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author WHL
 * @Date 2017-3-29.
 */
public class TaskSubscriber extends Subscriber{
    private static Logger logger = LoggerFactory.getLogger(TaskSubscriber.class);
    public void onMessage(String channel, String message) {
        logger.info("Message received. Channel: {}, Msg: {}", channel, message);

        String md5Message = StringUtil.string2MD5(message);
        try{
            if( message.substring(0,1).equals("{")) {
                JSONObject jsonObject = JSONObject.fromObject(message);

                String signal = "";
                if (jsonObject.containsKey("key")) {
                    signal = jsonObject.getString("key");
                }

                if( signal.equals("savePlugin")){
                    executor(channel, message, jsonObject);
                }else {
                    //间隔3秒执行一次同样的命令
                    //if ( SmsServerManager.I.isCanHandle(md5Message, 3)) {
                        executor(channel, message, jsonObject);
                    //}
                }

                if( signal.equals("addSmsUser") || signal.equals("updateSmsUser")){
                    Long userId = jsonObject.getLong("smsUserId");
                    //休息一会，等其它进程完成更新缓
                    try{
                        Thread.sleep(300);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //SmsServerManager.I.openCmpp( userId );
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void executor(String channel, String message,JSONObject jsonObject) {
        //更新用户余额
        if (channel.equals( Define.CHANNEL_UPDATE_USER_BLANK)) {

        } else if (channel.equals( Define.CHANNEL_AB)) {
            if (message.substring(0, 1).equals("{")) {

                String signal = "";
                if (jsonObject.containsKey("key")) {
                    signal = jsonObject.getString("key");
                }

                //审核成功
                if (signal.equals("auditSmsSuccess")) {
                    if (jsonObject.containsKey("smsSendId")) {
                        String taskId = jsonObject.getString("smsSendId");
                        String auditState = jsonObject.getString("auditState");

                        if (taskId != null && !taskId.equals("null")) {
                            String[] ids = StringUtil.split(taskId, ",");

                            //单个任务是需要更新任务内容的
                            if( ids.length == 1 ){

                                if (auditState.equals("0")) {//审核状态 0：不通过，1：通过
                                    //通过的任务，需要更新通道组ID和任务内容
                                    SmsTaskData smsTaskData = SmsServerManager.I.getSmsSendTask( Long.valueOf(ids[0]));
                                    SmsServerManager.I.putSmsTaskToSendQueue( ids[0].getBytes(), false, smsTaskData);
                                } else if (auditState.equals("1")) {
                                    //通过的任务，需要更新通道组ID和任务内容
                                    SmsTaskData smsTaskData = SmsServerManager.I.getSmsSendTask( Long.valueOf(ids[0]));
                                    SmsServerManager.I.putSmsTaskToSendQueue( ids[0].getBytes(), true, smsTaskData);
                                }
                            }else {
                                for (String idString : ids) {
                                    if (auditState.equals("0")) {//审核状态 0：不通过，1：通过
                                        SmsServerManager.I.putSmsTaskToSendQueue(idString.getBytes(), false,null);
                                    } else if (auditState.equals("1")) {
                                        SmsServerManager.I.putSmsTaskToSendQueue(idString.getBytes(), true,null);
                                    }
                                }
                            }
                        }

                    }
                }

                if (signal.equals("addSmsUser")) { //添加代理
                    Long userId = jsonObject.getLong("smsUserId");
                    String userKey = jsonObject.getString("smsUserKey");
                    //SmsServerManager.I.synchronizeUsers( userKey );
                    SmsServerManager.I.synchronizeUsers(userId.intValue());
                    SmsServerManager.I.synchronizeUserBlank(userId);
                }

                if (signal.equals("updateSmsUser")) { //修改代理
                    Long userId = jsonObject.getLong("smsUserId");
                    String userKey = jsonObject.getString("smsUserKey");
                    //SmsServerManager.I.synchronizeUsers( userKey );
                    SmsServerManager.I.synchronizeUsers(userId.intValue());
                    SmsServerManager.I.synchronizeUserBlank(userId);
                    SmsServerManager.I.synchronizeFreeTrial(userId);
                }

                if (signal.equals("allotaAisleGroup")) {//分配通道组
                    String userKey = jsonObject.getString("smsUserKey");
                    Long userId = jsonObject.getLong("smsUserId");
                    SmsServerManager.I.synchronizeAisleGroupUser(userId);
                }

                if (signal.equals("userTopup")) {//充值
                    String userKey = jsonObject.getString("smsUserKey");
                    Long userId = jsonObject.getLong("smsUserId");
                    SmsServerManager.I.synchronizeUserBlank(userId);
                }


                if (signal.equals("addSmsAisle")) {  //添加通道
                    Long aisleId = jsonObject.getLong("smsAisleId");
                    SmsServerManager.I.synchronizeAisle(aisleId);
                }


                if (signal.equals("updateSmsAisle")) {  //修改通道
                    Long aisleId = jsonObject.getLong("smsAisleId");
                    SmsServerManager.I.synchronizeAisle(aisleId);
                    SmsServerManager.I.synchronizeAisleGroupAisle();//更新通道关联缓存， 因为关联缓存中有通道信息
                }


                if (signal.equals("deleteSmsAisle")) {  //删除通道
                    Long aisleId = jsonObject.getLong("smsAisleId");
                    //SmsServerManager.I.delSmsAisle(aisleId);

                    //删除和通道组关联的信息
                    //---这里还没有处理----


                    SmsServerManager.I.synchronizeAisle(aisleId);
                    SmsServerManager.I.synchronizeAisleGroupAisle();//更新通道关联缓存， 因为关联缓存中有通道信息
                }

                if (signal.equals("addSmsAisleGroup")) {  //添加通道组
                    Long aisleGroupId = jsonObject.getLong("smsAisleGroupId");
                    SmsServerManager.I.synchronizeAisleGroup(aisleGroupId);  //更新通道组信息
                    SmsServerManager.I.synchronizeAisleGroupAisle(aisleGroupId); //更新通道组和通道关联 信息
                }

                if (signal.equals("updateSmsAisleGroup")) {  //修改通道组
                    Long aisleGroupId = jsonObject.getLong("smsAisleGroupId");
                    SmsServerManager.I.synchronizeAisleGroup(aisleGroupId);  //更新通道组信息
                    SmsServerManager.I.synchronizeAisleGroupAisle(aisleGroupId); //更新通道组和通道关联 信息
                }

                if (signal.equals("deleteSmsAisleGroup")) {  //删除通道组
                    Long aisleGroupId = jsonObject.getLong("smsAisleGroupId");
                    String userIdString = jsonObject.getString("userIdString");

                    //SmsServerManager.I.delSmsAisleGroup(aisleGroupId);
                   // SmsServerManager.I.delSmsAisleGroupAisle(aisleGroupId);


                    if (userIdString != null && !userIdString.equals("null")) {
                        String[] ids = StringUtil.split(userIdString, ",");
                        for (int i = 0; i < ids.length; i++) {
                            if (ids[i].length() > 0) {
                                //SmsServerManager.I.delSmsAisleGroupUser(Long.valueOf(ids[i]));
                            }
                        }
                    }
                    //SmsServerManager.I.synchronizeAisleGroup( aisleGroupId );  //更新通道组信息
                    //SmsServerManager.I.synchronizeAisleGroupAisle( aisleGroupId ); //更新通道组和通道关联 信息
                }

                if (signal.equals("smsWordShielding")) {
                    SmsServerManager.I.synchronizeShield();
                }

                if (signal.equals("smsBlacklist")) {
                    SmsServerManager.I.synchronizeBlacklist();
                }

                if (signal.equals("deleteSmsBlacklist")) {
                    SmsServerManager.I.synchronizeBlacklist();
                }

                if (signal.equals("updateUserFreeTrial")) {
                    Long userId = jsonObject.getLong("smsUserId");
                    SmsServerManager.I.synchronizeFreeTrial(userId);

                }


                //代理商选择通道
                if (signal.equals("allotaAisleGroup")) {
                    Long userId = jsonObject.getLong("smsUserId");
                    SmsServerManager.I.synchronizeAisleGroupUser(userId);
                }


                if (signal.equals("saveWhitelist")) {
                    SmsServerManager.I.synchronizeWhitelist();
                }

                //代理商选择通道
                if (signal.equals("addUserFreeTrial")) {
                    Long smsUserId = jsonObject.getLong("smsUserId");
                    SmsServerManager.I.synchronizeFreeTrial(smsUserId);

                }
                if (signal.equals("updateUserFreeTrial")) {
                    Long smsUserId = jsonObject.getLong("smsUserId");
                    SmsServerManager.I.synchronizeFreeTrial(smsUserId);
                }
                if (signal.equals("deleteUserFreeTrial")) {
                    Long smsUserId = jsonObject.getLong("smsUserId");
                    SmsServerManager.I.synchronizeFreeTrial(smsUserId);
                }


                if (signal.equals("savePlugin")) {
                    Long pid = jsonObject.getLong("pid");
                    SmsServerManager.I.synchronizePlugins(pid);
                }

            }
        }
    }
}
