package com.dzd.phonebook.aop;


import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import com.dzd.base.util.StringUtil;
import org.apache.ibatis.cache.Cache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dzd.base.util.SessionUtils;
import com.dzd.phonebook.entity.SysLog;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.SysUserService;

/**
 * @author oygy
 * @ClassName: AspectPagination
 * @Description: TODO
 * @date 2015年10月27日 下午3:45:01
 */
@Aspect
@Component
public class AspectPagination {
    private Logger logger = LoggerFactory.getLogger(AspectPagination.class);

	@Autowired
	private SysUserService<SysUser> sysUserService;

    @Around(value = "execution(* com.dzd.phonebook.controller..*(..))")
    public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable {
		logger.debug("EherLog>>>>###### "+pjd.getSignature()+" cost time start");
		long start = System.currentTimeMillis();
		String texts = "";
		MethodSignature joinPointObject = (MethodSignature) pjd.getSignature();
		Method method = joinPointObject.getMethod();
        boolean flag = method.isAnnotationPresent(MethodDescription.class) ;
        if(flag){
        	MethodDescription annotation2 = method.getAnnotation(MethodDescription.class);
            logger.debug(method.getName() + "方法上的注解："+annotation2.value());
            texts=annotation2.value();
        }

	    SysUser user= new SysUser();
	    Object[] parames = pjd.getArgs();
	    if(parames.length>0){
	    	try {
	    		HttpServletRequest req = (HttpServletRequest)parames[0];
		    	if(req!=null){
		    		user = (SysUser)req.getSession().getAttribute("session_user");
		    	}
			} catch (Exception e) {
				// TODO: handle exception
			}
	    }

		Object obj= pjd.proceed();

		if(user==null){
			 Object[] parames2 = pjd.getArgs();
			    if(parames2.length>=0){
			    	HttpServletRequest req2 = (HttpServletRequest)parames2[0];
			    	if(req2!=null){
			    		user = (SysUser)req2.getSession().getAttribute("session_user");
			    	}
			    }
		}
		if(user!=null && !StringUtil.isEmpty(user.getEmail()) && !("").equals(texts)){
			//做数据库插入操作
			String content = user.getEmail()+"："+texts;
			logger.debug("EherLog>>>>###### "+content);
			SysLog syslog = new SysLog();
			syslog.setContent(texts);
			syslog.setUserName(user.getEmail());
			syslog.setSysUserId(user.getId());
		//	syslog.setRemark(content);
			sysUserService.addSysLog(syslog);
		}

		long cost = System.currentTimeMillis()-start;
		logger.debug("EherLog>>>>###### "+pjd.getSignature()+" cost time: ["+cost+"] millis" );
		return obj;
    }
}
