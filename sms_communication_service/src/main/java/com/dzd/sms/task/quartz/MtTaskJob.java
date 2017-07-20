package com.dzd.sms.task.quartz;

import com.dzd.sms.application.SmsServerManager;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;

public class MtTaskJob implements Job {
	
	 private  static Logger logger = Logger.getLogger( MtTaskJob.class );
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("execute start......");
		
		String jobName = context.getJobDetail().getName();
		logger.info("job name:"+jobName);
		System.out.println("-----------------------任务ID:"+jobName);


		//发送任务， 必需是任务审核成功了
		SmsServerManager.I.sendSmsTask(jobName);

		
		
		logger.info("execute end......");
		//System.out.println(TimeUtil.getAllFormat(new Date()));
		System.out.println("动态定时任务 job name："+jobName);
	}
}
