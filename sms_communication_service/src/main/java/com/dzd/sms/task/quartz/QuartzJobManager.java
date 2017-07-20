package com.dzd.sms.task.quartz;

import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.dzd.quartz.QuartzManager;

/**
 * @Author WHL
 * @Date 2017-4-24.
 */
public class QuartzJobManager {
	
	 private  static Logger logger = Logger.getLogger(QuartzJobManager.class );
	
    public static QuartzJobManager I = new QuartzJobManager();
    Scheduler sche =null; 

    public QuartzJobManager(){

        try {
        	sche =new StdSchedulerFactory().getScheduler();
            //每分钟更新一次
            QuartzManager.addJob(sche, "更新任务状态", TaskStateUpdateJob.class, "*/10 * * * * ?");
            //每分钟更新一次
            QuartzManager.addJob(sche, "获取状态报告", TaskQueryReportJob.class, "*/30 * * * * ?");

            //每分钟更新一次
            QuartzManager.addJob(sche, "获取上行", TaskQueryReplyJob.class, "0 */1 * * * ?");

            //"0 */10 * * * ?   //间隔10分执行
            QuartzManager.addJob(sche, "任务结算并返还", TaskSettlementJob.class, "0 */1 * * * ?");

            QuartzManager.addJob(sche, "数据库保存异常", DbExceptionProcessJob.class, "0 */1 * * * ?");

            QuartzManager.addJob(sche, "定时刷新任务状态为成功", TaskStateUpdateToSucessJob.class, "0 0 5 ? * *");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    

    /**
     * 创建定时发送任务的定时器
     * @Description:
     * @author:oygy
     * @time:2017年4月28日 上午10:25:48
     */
	public void addTimeTask(Date excuteTime , long taskId) {
		try {
            String jobId = taskId+"";
			logger.info("startTask 设定执行时间："+excuteTime);
			JobDetail jobDetail = new JobDetail( jobId, "TimeTask", MtTaskJob.class);
			Trigger trigger = new SimpleTrigger("TimeTask"+taskId,"TimeTask", excuteTime);
			
			Scheduler sche =new StdSchedulerFactory().getScheduler();
			
			if ( Arrays.asList(sche.getTriggerNames("TimeTask")).contains("TimeTask" + taskId) )
			{
				sche.deleteJob(jobId, "TimeTask");
			}

            sche.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
    
    
/*	public static void main(String[] args) {
		for (int i = 1; i < 3; i++) {
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟  
		String dstr="2017-04-28 10:23:00"; 
		Long s = 11L;
		if(i==2){
			dstr="2017-04-28 10:24:00"; 
			s=22L;
		} 
		java.util.Date date;
		try {
			date = sdf.parse(dstr);
			addTimeTask(date,s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		}
	}*/
	
    
    public void start(){

    }
}
