package com.dzd.sms.addons.aisle;


import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.*;
import com.dzd.utils.DateUtil;

import com.dzd.utils.StringUtil;
import com.dzd.utils.ThreadUtil;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.dzd.utils.StringUtil.convertClassToBean;

/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/3
 * Time: 14:47
 */

public   class BaseAisle {


    private  static Logger logger = Logger.getLogger(BaseAisle.class );

    public Long aisleId = 0L;
    public int theadNumber = 5;//默认3个线程数量
    public int singlePackageNumber = 2000; //单个包号码数量
    public boolean isFastSendAisle = false; //是否快速直接发送的信息

    public String aisleClassName = null;

    public int sendTotalNum = 0;


    public SmsAisle smsAisle;
    public String messageQueueKey; //发送缓存KEY
    public BaseAisleAdapter baseAisleAdapter  =null;

    RedisClient redisClient = RedisManager.I.getRedisClient();
    public ThreadPoolTaskExecutor taskExecutor2 = null;
    public ThreadPoolTaskExecutor getTaskExecutor(){
        if( taskExecutor2 == null ) {
            taskExecutor2 = ThreadUtil.getPoolTskExecutor(theadNumber);
        }
        return taskExecutor2;
        //return taskExecutor;
    }
    /**
     * 单个线程发送
     */
    public class SingleThread extends Thread {
        SmsTaskData smsTaskData;
        public SingleThread(  SmsTaskData smsTaskData ){
            this.smsTaskData = smsTaskData;
        }
        @Override
        public void run(){

            Long messageLen = redisClient.llen( messageQueueKey);
            List<String> mobiles = new ArrayList<String>();
            for( int i =0; i<messageLen; i++ ) {
                Object messageValue = redisClient.lpop(messageQueueKey);
                if (messageValue != null) {
                    SmsMessage smsMessage = (SmsMessage)messageValue;
                    mobiles.add(smsMessage.getMobile());
                    logger.info("single ,mobile="+smsMessage.getMobile()+ "x="+( i )+","+ DateUtil.formatTime());
                    //saveMessage(messageByteValue);
                }
            }
            sendSinglePackage(smsTaskData, mobiles );
        }
    }
        /**
     * 初始化
     */

    public void init(){
        this.aisleClassName = convertClassToBean( this.getClass().getSimpleName() );
    }

    public void initAdapter(String aisleClassName, BaseAisleAdapter aisleAdapter){
        this.baseAisleAdapter = aisleAdapter;

        int tn = aisleAdapter.getThreadNumber();
        if( tn>0 ){
            this.theadNumber = tn;
        }
        int tpn = aisleAdapter.getSinglePackageNumber();
        if( tpn>0 ){
            this.singlePackageNumber = tpn;
        }

        this.aisleClassName = aisleClassName;

    }
    /**
     * 发送
     * @param smsTaskData
     */
    public void send(  SmsTaskData smsTaskData){
        //单条快速发送
        if( isFastSendAisle ){
            new SingleThread( smsTaskData ).start();
        }else{
            int messageLen = sendTotalNum;//redisClient.llen( messageQueueKey);
            //int _threadNumber = new Long(messageLen/singlePackageNumber).intValue();

            //可以分包的次数
            int _threadNumber = new  Double( Math.floor( messageLen/singlePackageNumber) ).intValue();
            int remainderNumber = messageLen % singlePackageNumber;
            //如果分包的次数大于 发送的线程数， 则当前分包次数为线程数
            if( _threadNumber>theadNumber ) _threadNumber = theadNumber;


            if( _threadNumber<=1 ){
                executorRemainder(smsTaskData, messageQueueKey, messageLen, 1, 0);
            }else {
                //分包最少2次
                if (_threadNumber < 2) _threadNumber = 2;

                //每个包的数量
                int packageNumber = singlePackageNumber;

                //包的数量不能小于，实际号码的长度
                if (packageNumber > messageLen) {
                    packageNumber = messageLen;
                }
                for (int i = 0; i < _threadNumber; i++) {
                    if (i == 0 && remainderNumber > 0) {
                        executorRemainder(smsTaskData, messageQueueKey, packageNumber + remainderNumber, _threadNumber, i);
                    } else {
                        executor(smsTaskData, messageQueueKey, packageNumber, _threadNumber, i);
                    }
                }
            }
        }
    }

    //


    List<String> saveMobiles = new ArrayList<String>();
    public synchronized List<String> getMobiles( final String messageKey, int n ){
        List<String> mobiles = new ArrayList<String>();

        //取出的数据，小于需要的数据时， 再从缓存中取
        if( saveMobiles.size()<n ) {
            Long messageLen = redisClient.llen(messageKey);
            for (int i = 0; i < messageLen; i++) {
                Object messageValue = redisClient.lpop(messageKey);
                if (messageValue != null) {
                    String mobile = (String) messageValue;
                    String[] mobileArr = StringUtil.split(mobile, "-");
                    for (String val : mobileArr) {
                        if (mobiles.size() < n) {
                            mobiles.add(val);
                            i = messageLen.intValue();
                        } else {
                            saveMobiles.add(val);
                        }
                    }
                }
            }
        }

        if( mobiles.size()<n ) {
            int s = saveMobiles.size();
            if( s>(n-mobiles.size()) ){
                s = (n-mobiles.size());
            }

            System.out.println("-------------"+s+"----------------");
            List<String> ms = saveMobiles.subList(0,s);
            for(String m:ms){
                mobiles.add(m);
            }
            ms.clear();
        }
        return mobiles;
    }

    /**
     * 执行线程
     *
     */
    public void executor(  final SmsTaskData smsTaskData, final String messageKey,  final int packageNumber,  final Integer threadNumber, final int no ) {
        try {
            //用系统线程池来执行
            getTaskExecutor().execute(new Runnable() {
                public void run() {
                    List<String> mobiles = getMobiles(messageKey,packageNumber);
                    logger.info( "1-executor number="+  no +"task.id="+smsTaskData.getTaskId().toString()+",messageLen="+packageNumber + " message key= " +new String(messageKey)+" singlePackageNumber="+singlePackageNumber+" mobiles.size="+mobiles.size());
                    int i = 10000;
                    while(i-->0 && mobiles.size()>0){
                        sendSinglePackage(smsTaskData, mobiles);
                        mobiles = getMobiles(messageKey,packageNumber);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void executorRemainder(  final SmsTaskData smsTaskData, final String messageKey,  final int packageNumber,  final Integer threadNumber, final int no ) {
        try {
            //用系统线程池来执行
            getTaskExecutor().execute(new Runnable() {
                public void run() {
                    List<String> mobiles = getMobiles(messageKey,packageNumber);
                    logger.info( "2-executor number="+  no +"task.id="+smsTaskData.getTaskId().toString()+",messageLen="+packageNumber + " message key= " +new String(messageKey)+" singlePackageNumber="+singlePackageNumber+" mobiles.size="+mobiles.size());
                    sendSinglePackage(smsTaskData, mobiles);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void executor2(  final SmsTaskData smsTaskData, final String messageKey,  final Integer singlePackageNumber,  final Integer threadNumber, final int no ) {
        try {
            //用系统线程池来执行
            getTaskExecutor().execute(new Runnable() {
                public void run() {
                    int x = 0;
                    //这里编写处理业务代码
                    Long messageLen = redisClient.llen( messageKey);
                    while( messageLen>0 ){
                        //线程ID大于1的发送指定数量的包
                        if( no>0 && messageLen> singlePackageNumber*threadNumber){
                            List<String> mobiles = new ArrayList<String>();
                            for( int i =0; i<singlePackageNumber; i++ ){
                                Object messageValue = redisClient.lpop( messageKey );
                                if( messageValue !=null ) {
                                    SmsMessage smsMessage = (SmsMessage) messageValue;
                                    logger.debug("1-executor number="+  no +"task.id="+(smsTaskData.getTaskId())+",mobile="+smsMessage.getMobile()+ "x="+(++x)+","+ DateUtil.formatTime());
                                    mobiles.add( smsMessage.getMobile() );
                                    saveMessage( messageValue );
                                }
                            }
                            logger.info( "1-executor number="+  no +"task.id="+smsTaskData.getTaskId().toString()+",messageLen="+messageLen + " message key= " +new String(messageKey)+" singlePackageNumber="+singlePackageNumber+" mobiles.size="+mobiles.size());

                            sendSinglePackage(smsTaskData, mobiles);

                            //线程ID=0的线程发送多余的包
                        }else if( no == 0){

                            //是否需要分包
                            if( messageLen>singlePackageNumber*2){
                                List<String> mobiles = new ArrayList<String>();
                                for( int i =0; i<singlePackageNumber; i++ ){
                                    Object messageValue = redisClient.lpop( messageKey );
                                    if( messageValue !=null ) {
                                        SmsMessage smsMessage = (SmsMessage)messageValue;
                                        logger.debug("2-executor number="+  no +"task.id="+(smsTaskData.getTaskId())+",mobile="+smsMessage.getMobile()+ ",x="+(++x)+","+ DateUtil.formatTime());
                                        mobiles.add( smsMessage.getMobile() );
                                        saveMessage( messageValue );
                                    }
                                }
                                logger.info( "2-executor number="+  no +"task.id="+smsTaskData.getTaskId().toString()+",messageLen="+messageLen + " message key= " +new String(messageKey)+" singlePackageNumber="+singlePackageNumber+" mobiles.size="+mobiles.size());

                                sendSinglePackage(smsTaskData, mobiles);

                                //不用分包， 把尾数全部发完
                            }else{
                                  List<String> mobiles = new ArrayList<String>();
                                for( int i =0; i<singlePackageNumber; i++ ){
                                    Object messageValue = redisClient.lpop( messageKey );
                                    if( messageValue !=null ) {
                                        SmsMessage smsMessage = (SmsMessage) messageValue;
                                        //System.out.println( "messageByteValue=" +messageByteValue+ " smsMessage=" + smsMessage);
                                        logger.debug("3-executor number="+  no +"task.id="+(smsTaskData.getTaskId())+",mobile="+smsMessage.getMobile()+ "x="+(++x)+","+ new Date().getTime());
                                        mobiles.add( smsMessage.getMobile() );
                                        saveMessage( messageValue );
                                    }
                                }
                                logger.info( "3-executor number="+  no +"task.id="+smsTaskData.getTaskId().toString()+",messageLen="+messageLen +" messageByteKey=" +new String(messageKey)+" singlePackageNumber="+singlePackageNumber+" mobiles.size="+mobiles.size());

                                sendSinglePackage(smsTaskData, mobiles);
                            }


                        }else{

                            //其它线程等待一下
                            try{
                                Thread.sleep(10);
                            }catch (Exception e){

                            }
                        }



//批量不支持队列
//                        List<byte[]> messageList = SmsWarehouse.getInstance().lrange( messageByteKey, 0, singlePackageNumber);
//                        for( byte[] messageByteValue:messageList){
//                            SmsMessage smsMessage = (SmsMessage) SerializeUtil.unserialize(messageByteValue);
//                            System.out.println("task.id="+(smsTaskData.getTaskId())+",mobile="+smsMessage.getMobile()+ "x="+(++x));
//                        }




                        messageLen = redisClient.llen( messageKey);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //存数据库
    public void saveMessage(Object messageObject){
        //SmsWarehouse.getInstance().getJedisCluster().lpush( KEY_SMS_TASK_MESSAGE_DB.getBytes(),messageByte );
    }

    public static void saveSendResult( List<SmsSendResult> smsSendResult){
        logger.info("saveSendResult.size="+smsSendResult.size());
        if( smsSendResult.size()>0) {
            SmsServerManager.I.saveSendResult(smsSendResult);
        }
    }

    //保存发送结果
    public static void saveSendResult( SmsSendResult smsSendResult){

        //logger.info(" aisle class Name ="+ this.getClass().getName());

//        //1.先缓存
//        SmsWarehouse.getInstance().getJedisCluster().hset( resultSaveKey.getBytes(), smsSendResult.getMid().getBytes(), SerializeUtil.serialize( smsSendResult ) );
//
//        //2.加到保存数据库队列
//        SmsWarehouse.getInstance().getJedisCluster().rpush( resultSaveKeyDB.getBytes(),  SerializeUtil.serialize( smsSendResult ) );
//
//        //3 失败状态，加到状态报告发送结果中
//        //不包括已提交和网络错误
//        if( smsSendResult.getState().intValue() != STATE_SENDING && smsSendResult.getState().intValue() != STATE_SENDED_NETWORK_ERROR ) {
//            SmsReport smsReport = new SmsReport( smsSendResult.getMid() );
//            smsReport.setMobile( smsSendResult.getMobile() );
//            smsReport.setState("ERROR");
//            smsReport.setStateCode( smsSendResult.getState() );
//            saveReport(smsReport);
//        }

        SmsServerManager.I.saveSendResult(smsSendResult);

    }
    public static void saveReport(List<SmsReport> smsReport){
        logger.info("saveReport.size="+smsReport.size());
        if( smsReport.size()>0 ) {
            SmsServerManager.I.saveReport(smsReport);
        }

    }
    //保存回执信息
    public static void saveReport(SmsReport smsReport){
        //logger.info(" aisle class Name ="+ this.getClass().getName());
        //SmsWarehouse.getInstance().getJedisCluster().rpush( KEY_SMS_AISLE_REPORT_PUSH.getBytes(), SerializeUtil.serialize( smsReport ) );
        //SmsWarehouse.getInstance().getJedisCluster().rpush( reportSaveKey.getBytes(),  SerializeUtil.serialize( smsReport ) );
        SmsServerManager.I.saveReport( smsReport );
    }
    public static void saveReply(Object obj, List<SmsReply> smsReply){
        logger.info("saveReply.size="+smsReply.size());
        if( smsReply.size()>0) {
            SmsServerManager.I.saveReply(convertClassToBean(obj.getClass().getSimpleName()), smsReply);
        }
    }
    //保存回复内容信息
    public static void saveReply(Object obj, SmsReply smsReply){
        //smsReply.setAisleClassName( aisleClassName );
        //SmsWarehouse.getInstance().getJedisCluster().rpush( replySaveKey.getBytes(),   SerializeUtil.serialize( smsReply ) );
        SmsServerManager.I.saveReply( convertClassToBean( obj.getClass().getSimpleName() ), smsReply);
    }

    //保存发送保日志
    public static void saveSendPackage(SmsSendPackage smsSendPackage){
        SmsServerManager.I.saveSendPackage( smsSendPackage );
    }

    //以下是子类实现的方法
    public boolean sendSinglePackage(SmsTaskData smsTaskData, List<String> mobileList ){
        if( baseAisleAdapter!=null){
            return baseAisleAdapter.sendSinglePackage(smsTaskData,mobileList);
        }
        return false;
    };
    public boolean queryReply(){
        if( baseAisleAdapter!=null){
            return baseAisleAdapter.queryReply();
        }
        return false;
    };
    public boolean queryReport(){
        if( baseAisleAdapter!=null){
            return baseAisleAdapter.queryReport();
        }
        return false;
    };
    public   Double queryBalance(){
        if( baseAisleAdapter!=null){
           return baseAisleAdapter.queryBalance();
        }
        return  0.0;
    };
    public  String pushReport( Map<String,String> params ){
        if( baseAisleAdapter!=null){
            return baseAisleAdapter.pushReport(params );
        }
        return "";
    };
    public  String pushReply( Map<String,String> params ){
        if( baseAisleAdapter!=null){
            return baseAisleAdapter.pushReply(params );
        }
        return "";
    };


    public SmsAisle getSmsAisle() {
        return smsAisle;
    }

    public void setSmsAisle(SmsAisle smsAisle) {
        this.smsAisle = smsAisle;
        this.setAisleId( smsAisle.getId() );

        if( baseAisleAdapter!=null){
            baseAisleAdapter.setSmsAisle( smsAisle );
        }
    }

    public String getMessageQueueKey() {
        return messageQueueKey;
    }

    public void setMessageQueueKey(String messageQueueKey) {
        this.messageQueueKey = messageQueueKey;
    }

    public Long getAisleId() {
        return aisleId;
    }

    public void setAisleId(Long aisleId) {
        this.aisleId = aisleId;
    }
}
