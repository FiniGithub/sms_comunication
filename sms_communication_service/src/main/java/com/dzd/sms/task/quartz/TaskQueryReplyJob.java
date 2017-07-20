package com.dzd.sms.task.quartz;

import com.dzd.sms.addons.aisle.BaseAisle;
import com.dzd.sms.application.SmsServerManager;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author WHL
 * @Date 2017-4-25.
 */
public class TaskQueryReplyJob  implements Job {

    private  static Logger logger = Logger.getLogger( TaskQueryReplyJob.class );



    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "-TaskQueryReplyJob");

        try{
            taskQueryReply();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 上行报告获取
     */
    public void taskQueryReply() {

        //logger.info(" taskQueryReply..");
        if( SmsServerManager.I.isCanHandle("taskQueryReply",1*60 )) {
//            if (SmsServerManager.I.isWindows) {
//                logger.info("taskQueryReply .. debug no executor!");
//                return;
//            }
            for (Long key : SmsServerManager.I.aisleMap.keySet()) {
                BaseAisle baseAisle = SmsServerManager.I.aisleMap.get(key);
                try {
                    baseAisle.queryReply();
                    logger.info(baseAisle.getSmsAisle().getClassName() + " queryReply");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }




}
