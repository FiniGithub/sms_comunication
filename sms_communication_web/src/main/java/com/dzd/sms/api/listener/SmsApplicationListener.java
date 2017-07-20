package com.dzd.sms.api.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.dzd.sms.application.SmsServerManager;

/**
 * 
 * @Description:监听系统初始化
 * @author:oygy
 * @time:2017年3月21日 上午10:38:40
 */
@Service
public class SmsApplicationListener implements ApplicationListener<ContextRefreshedEvent>
{

	static ApplicationContext applicationContext = null;

	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent)
	{
		if ( contextRefreshedEvent.getApplicationContext().getParent() == null )
		{
			System.out.println("onApplicationEvent=" + contextRefreshedEvent.toString());

			if ( applicationContext == null )
			{
				applicationContext = contextRefreshedEvent.getApplicationContext();
				
				SmsServerManager.I.start();
			}
		}
	}

	/**
	 * 
	 * @Description:获取类
	 * @author:oygy
	 * @time:2017年3月21日 上午10:43:40
	 */
	public static Object getBean(String beanName)
	{
		return applicationContext.getBean(beanName);
	}
}
