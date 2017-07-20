package com.dzd.phonebook.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.entity.VertifyCode;
import com.dzd.phonebook.service.SmsApiConfigService;
import com.dzd.phonebook.service.UserMessageService;
import com.dzd.phonebook.service.VertifyCodeService;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.MakeSystemUUID;
import com.dzd.phonebook.util.SmsUser;

/**
 * API接口配置信息Controller
 * 
 * @author CHENCHAO
 * @date 2017-04-10 10:32:00
 *
 */
@Controller
@RequestMapping("/smsUserApiConfig")
public class SmsApiConfigController<T> {
	@Autowired
	private SmsApiConfigService smsApiConfigService;
	@Autowired
    private UserMessageService<T> userMessageService;
	@SuppressWarnings("rawtypes")
	@Autowired
	private VertifyCodeService vertifyCodeService;
	
	@RequestMapping("/apiView")
	public String list(HttpServletRequest request,Model model){
		SysUser sysUser = SessionUtils.getUser(request);
		SmsUser smsUser = userMessageService.querySmsUserById(sysUser.getId());
		String reportUrl = smsUser.getReportUrl();// 报告推送地址
		String replyUrl = smsUser.getReplyUrl();// 上行推送地址
		String key = StringUtil.getKeyStr(smsUser.getKey());// key
		if(reportUrl == null || "".equals(reportUrl)){
			reportUrl = "未填写";
		}
		if(replyUrl == null || "".equals(replyUrl)){
			replyUrl = "未填写";
		}
		model.addAttribute("key",key);
		model.addAttribute("reportUrl",reportUrl);
		model.addAttribute("replyUrl",replyUrl);
		return "/app/apiwd/apiwd";
	}
	
	@RequestMapping("/apiDetail")
	public String lists(){
		return "/app/apiwd/jkwd";
	}
	
	/**
	 * 查询API配置信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/apiConfigList")
	@ResponseBody
	public DzdResponse list(HttpServletRequest request){
		DzdResponse dzdResponse = new DzdResponse();
		try{
			Map<String, Object> smsApiConfigs = smsApiConfigService.querySmsApiConfigList();
			dzdResponse.setData(smsApiConfigs);
			dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
		}catch(Exception e){
			e.printStackTrace();
			dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
		}
		return dzdResponse;
	}

	
	/**
	 * 验证验证码,并得到key
	 * @param request
	 * @param data
	 * @return
	 */
	@RequestMapping("/resetKey")
	@ResponseBody
	public DzdResponse setKey(HttpServletRequest request){
		DzdResponse dzdResponse = new DzdResponse();
		try {
			Object phone = request.getParameter("phone");
			Object verifyCode = request.getParameter("verifyCode");
			Object type = request.getParameter("type");// 0:获取key,1:重置key
			if(phone == null || verifyCode == null || type == null ){
				return dzdResponse;
			}
			
			
			// 查询验证码
			VertifyCode code2 = vertifyCodeService.queryCodeByPhoneAndCode(phone.toString(), verifyCode.toString());
			if (code2 == null) {
				String msg = "验证码错误!";
				dzdResponse.setData(msg);
				dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);	
			}else{
				SysUser sysUser = SessionUtils.getUser(request);
				SmsUser smsUser = userMessageService.querySmsUserById(sysUser.getId());
				// 1. 获取key
				if(type.toString().equals("0")){
					String key = smsUser.getKey();
					dzdResponse.setData(key);

				// 2. 重置key
				}else if(type.toString().equals("1")){
					String key = MakeSystemUUID.getUUID();
					smsUser.setKey(key);
					userMessageService.updateSmsUserKey(smsUser);
					dzdResponse.setData(key);
				}
				dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
			}
		} catch (Exception e) {
			dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
			e.printStackTrace();
		}
		return dzdResponse;
		
	}
}
