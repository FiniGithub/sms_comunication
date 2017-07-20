package com.dzd.phonebook.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.SmsUserSignature;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.SmsAisleGroupService;
import com.dzd.phonebook.service.SmsUserSignatureService;
import com.dzd.phonebook.service.UserFreeTrialService;
import com.dzd.phonebook.service.UserMessageService;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.FreeTria;
import com.dzd.phonebook.util.JspResponseBean;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.phonebook.util.UserFreeTria;
import com.dzd.phonebook.util.WebRequestParameters;
import com.github.pagehelper.Page;

/**
 * 签名Controller
 * 
 * @author CHENCHAO
 * @date 2017-04-05 21:58:00
 *
 */
@Controller
@RequestMapping("/smsUser")
public class SmsUserSignatureController extends WebBaseController {
	public static final Logger log = LoggerFactory.getLogger(SmsUserSignatureController.class);
	@SuppressWarnings("rawtypes")
	@Autowired
	private UserMessageService userMessageService;
	@Autowired
	private SmsAisleGroupService smsAisleGroupService;






	/**
	 * 查询通道信息签名
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/signatureList", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public DzdResponse modelist(HttpServletRequest request) throws Exception {
		DzdResponse dzdResponse = new DzdResponse();
		try {
			SysUser user = (SysUser) request.getSession().getAttribute("session_user");
			SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());
			// 查询签名
			Map<String,Object> map = null;
			if (smsUsers != null) {
				map = smsAisleGroupService.querySmsAisleGroupSign(smsUsers);
			}
			dzdResponse.setData(map);
			dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
			dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
		}
		return dzdResponse;

	}


}
