package com.dzd.sms.api.controller;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.phonebook.service.UserMessageService;
import com.dzd.sms.api.service.SmsServiceV4;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsTaskData;
import com.dzd.utils.SerializeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import static com.dzd.sms.application.Define.KEY_SMS_TASK_CACHE;

/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/5/2
 * Time: 16:54
 */
@Controller
@RequestMapping("/test")
public class TestInterface {

    static Logger logger = Logger.getLogger(TestInterface.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    private UserMessageService userMessageService;

    @RequestMapping(value = "/gettask")
    @ResponseBody
    public Object gettask(HttpServletRequest request) {
        String taskId = request.getParameter("id");
        //存放进缓存
        SmsTaskData smsTaskData = (SmsTaskData) (SmsServerManager.I.redisClient.hgetObject(KEY_SMS_TASK_CACHE,taskId ));
        System.out.println( "taskid="+taskId+", smsTaskData="+smsTaskData );
        return  smsTaskData;
    }

}
