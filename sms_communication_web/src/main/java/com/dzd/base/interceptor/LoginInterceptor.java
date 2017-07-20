package com.dzd.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dzd.phonebook.entity.SysUser;

/**
 * 登录拦截器
 * 
 * @author chenchao
 * @date 2016-6-30 14:05:00
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Object menuObj = request.getSession().getAttribute("menuList");
		Object useObj = request.getSession().getAttribute("user");
		if (useObj != null) {
			// 用户类型 1-管理员 2-代理商
			SysUser sysUser = (SysUser) useObj;
			Integer typeId = sysUser.getUserType();
			if(typeId == 1 && menuObj != null){
				return true;
			}else if(typeId == 2){
				return true;
			}else{
				return false;
			}
		} else {
			String con = request.getContextPath();
			response.sendRedirect(con+"/loginview.do#layout");
			return false;
		}
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}
}
