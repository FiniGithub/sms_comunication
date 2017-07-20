package com.dzd.sms.task.quartz;

import com.dzd.sms.addons.aisle.BaseAisle;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.task.base.BaseTask;
import com.dzd.utils.ThreadUtil;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author WHL
 * @Date 2017-4-25.
 */
public class TaskQueryReportJob implements Job {


    private  static Logger logger = Logger.getLogger( TaskQueryReportJob.class );

    //用多线程的获取
    public final static int threadNumber = 10;
    public ThreadPoolTaskExecutor taskExecutor = ThreadUtil.getPoolTskExecutor(TaskQueryReportJob.threadNumber);
    public Map<Long,Boolean> runAisleObject = new ConcurrentHashMap<Long,Boolean>();


    //定时器调用方法
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "-TaskQueryReportJob");
        try {
            ThreadPoolTaskExecutor t = taskExecutor;
            final TaskQueryReportJob _self = this;

            if (t.getActiveCount() < t.getMaxPoolSize()) {
                taskExecutor.execute(new Runnable() {
                    public void run() {
                        _self.singleExecutor();
                    }
                });
            } else {
                System.out.println("TaskQueryReportJob" + t.getActiveCount() + " MaxPoolSize=" + t.getMaxPoolSize());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //线程执行方法
    public void singleExecutor(){
        try{
            taskQueryReport();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 状态报告获取
     */
    public void taskQueryReport() {

        logger.info("taskQueryReport..getActiveCount="+ taskExecutor.getActiveCount() );

        int sperSeconds= 10;//60间隔时间
        //if( SmsServerManager.I.isCanHandle("taskQueryReport", sperSeconds)) {

//            if (SmsServerManager.I.isWindows) {
//                logger.info("taskQueryReport .. debug no executor!");
//                return;
//            }

            for (Long key : SmsServerManager.I.aisleMap.keySet()) {
                BaseAisle baseAisle = SmsServerManager.I.aisleMap.get(key);
                if( !runAisleObject.containsKey( baseAisle.getAisleId()) || runAisleObject.get( baseAisle.getAisleId()) == false ) {
                    try {
                        runAisleObject.put(baseAisle.getAisleId(), true);

                        logger.info("aisleid:"+baseAisle.getAisleId()+", aisleName:"+baseAisle.getClass());
                        //最多100次
                        int n = 100;
                        while (--n > 0 && baseAisle.queryReport()) {
                            try {
                                Thread.sleep(10);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        runAisleObject.put(baseAisle.getAisleId(), false);
                    }
                }
            }
        //}
    }


}
