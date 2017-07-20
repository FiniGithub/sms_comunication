package com.dzd.sms.task.push;

import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsReport;
import com.dzd.sms.service.data.SmsReportPush;
import com.dzd.sms.task.base.BaseTask;
import com.dzd.utils.NetUtil;
import com.dzd.utils.SerializeUtil;
import com.dzd.utils.StringUtil;
import com.dzd.utils.ThreadUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017-1-14.
 */
public class SmsReportPushTask extends BaseTask {

    private  static Logger logger = Logger.getLogger(SmsReportPushTask.class );

    String mkey;
    public SmsReportPushTask(){
        this.mkey = Define.KEY_SMS_AISLE_REPORT_PUSH;
        this.isMutiThread = true;
        this.singlePackageNumber = 100;
        this.threadNumber = 5;

    }

    /**
     * 判断是否有任务可以执行
     */
    public boolean existTask() {
        return redisClient.llen(mkey) > 0;
    }

    @Override
    public void singleExecutor() {
        Map<String,List<SmsReportPush>> stringListMap = new HashedMap();
        Map<String,List<SmsReportPush>> cmppListMap = new HashedMap();
        long len = 0;
        int n = 0;//测试用
        try {
            len = redisClient.llen(mkey);
            if (len > singlePackageNumber) len = singlePackageNumber;
            for (int i = 0; i < len; i++) {
                Object byteValue = redisClient.lpopString(mkey);
                if (byteValue != null) {
                    ++n;

                    //jsonObject.put("sid", smsReportPush.getTaskId());
                    //jsonObject.put("user_receive_time", smsReportPush.getReceiveDate());
                    //jsonObject.put("state", smsReportPush.getState());
                    //jsonObject.put("mobile", smsReportPush.getMobile());
                    String smsReportPushStr = (String)byteValue;
                    String[] smsReportPushS = StringUtil.split(smsReportPushStr,"☆");
                    for(int x=0;x<smsReportPushS.length; x++) {
                        String[] cols = StringUtil.split(smsReportPushS[x],",");

                        SmsReportPush smsReportPush = new SmsReportPush( );
                        smsReportPush.setTaskId( Long.valueOf(cols[0]) );
                        smsReportPush.setReceiveDate( cols[1] );
                        smsReportPush.setState( cols[2] );
                        smsReportPush.setMobile( cols[3] );
                        smsReportPush.setPushWay( Integer.valueOf(cols[4]));


                        if( smsReportPush.getPushWay() == 0 ) {
                            //这里需要推送下游代理商
                            if (stringListMap.containsKey(smsReportPush.getPushUrl())) {
                                stringListMap.get(smsReportPush.getPushUrl()).add(smsReportPush);
                            } else {
                                List<SmsReportPush> tmpList = new ArrayList<SmsReportPush>();
                                tmpList.add(smsReportPush);
                                stringListMap.put(smsReportPush.getPushUrl(), tmpList);
                            }
                        }else{
                            String userIdStr = smsReportPush.getUserId().toString();
                            if ( cmppListMap.containsKey( userIdStr )) {
                                cmppListMap.get( userIdStr ).add(smsReportPush);
                            } else {
                                List<SmsReportPush> tmpList = new ArrayList<SmsReportPush>();
                                tmpList.add(smsReportPush);
                                cmppListMap.put(userIdStr, tmpList);
                            }

                        }

                    }////////////for end



                }else{
                    logger.info(" byteValue is null ");
                }
            }

            for (String key : stringListMap.keySet()) {
                pushReport( key, stringListMap.get(key));
            }

            for (String key : cmppListMap.keySet()) {
                //CmppServiceManager.pushReport( key, cmppListMap.get(key));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        SmsServerManager.I.taskMap.get("UpdateReportPushLog").signalNotEmpty();

        logger.info("SmsReportPushTask singleExecutor len="+len+" process n="+n );
    }


    /**
     * 推送消息给代理商
     其中json数据示例为：
     [{
     "sid": 9527, //短信id （数据类型：64位整型，对应Java和C#的long，不可用int解析)
     "user_receive_time": "2014-03-17 22:55:23",//用户接受时间
     " state ": "", //运营商返回的代码，如："DB:0103"
     "mobile": "15205201314" //接受手机号
     }
     */
    public void pushReport(String url, List<SmsReportPush> smsReplyPushArrayList){
        //推送状态默认为已推送
        int pushState = Define.PUSH_STATE_ERROR;
        try {
            logger.info(" push url =" + url);
            if (url == null) {
                pushState = Define.PUSH_STATE_URL_NULL;
            } else {
                String re = "";
                try {
                    JSONArray jsonArray = new JSONArray();
                    for (SmsReportPush smsReportPush : smsReplyPushArrayList) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("sid", smsReportPush.getTaskId());
                        jsonObject.put("user_receive_time", smsReportPush.getReceiveDate());
                        jsonObject.put("state", smsReportPush.getState());
                        jsonObject.put("mobile", smsReportPush.getMobile());
                        jsonArray.add(jsonObject);
                    }
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("sms_status", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
                    //String re = NetUtil.post(this.sendUrl, params);
                    re = NetUtil.post(url, params);
                    logger.info("push report result=" + re + " params=" + jsonArray.toString());
                    pushState = Define.PUSH_STATE_YES;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //加到保存数据库队列
            for(SmsReportPush smsReportPush:smsReplyPushArrayList ){
                smsReportPush.setPushState( pushState );
                redisClient.rpush(Define.KEY_SMS_AISLE_REPORT_PUSH_DB , smsReportPush);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
