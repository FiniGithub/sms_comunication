package com.dzd.sms.task.quartz;

import com.dzd.sms.application.SmsServerManager;
import com.dzd.utils.DateUtil;
import com.dzd.utils.MathUtil;
import com.dzd.utils.SerializeUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author WHL
 * @Date 2017-4-24.
 */
public class TaskSettlementJob implements Job {


    private  static Logger logger = Logger.getLogger(TaskSettlementJob.class );

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "-TaskSettlementJob");
    }


}
