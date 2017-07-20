package com.dzd.phonebook.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.dzd.phonebook.util.send.api.SmsContentBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.base.util.SessionUtils;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.entity.VertifyCode;
import com.dzd.phonebook.service.SysUserService;
import com.dzd.phonebook.service.UserMessageService;
import com.dzd.phonebook.service.VertifyCodeService;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.send.api.SendSmsUtil;
import com.dzd.sms.application.Define;

import net.sf.json.JSONObject;

/**
 * 代理用户Controller
 * 
 * @author CHENCHAO
 * @date 2017-04-11 10:46:00
 *
 */
@Controller
@RequestMapping("/smsUser")
public class SmsUserController {
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private VertifyCodeService vertifyCodeService;
	
	@SuppressWarnings({ "rawtypes", "unused" })
	@Autowired
	private UserMessageService userMessageService;
	
	@Autowired
	private SysUserService<SysUser> sysUserService;


	/**
	 * 查询用户是否存在
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryUserExistByReg")
	@ResponseBody
	public DzdResponse queryUserExists(HttpServletRequest request) throws Exception {
		DzdResponse dzdResponse = new DzdResponse();
		try {
			Object phone = request.getParameter("phone");
			SysUser sysUser = sysUserService.queryUserExist(phone.toString());
			if (sysUser != null) {
				dzdResponse.setRetMsg("1");
			} else {
				dzdResponse.setRetMsg("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dzdResponse;
	}

	/**
	 * 设置新密码
	 * 
	 * @param request
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updatePwd")
	@ResponseBody
	public DzdResponse updatePwd(HttpServletRequest request, @RequestBody Map<String, Object> data) throws Exception {
		DzdResponse dzdResponse = new DzdResponse();
		try {
			// 1. 基本信息
			SysUser user = SessionUtils.getUser(request);
			Object phone = data.get(Define.REQUESTPARAMETER.PHONE);
			Object code = data.get(Define.RESULTSTATE.CODE);
			Object oldPwd = data.get(Define.REQUESTPARAMETER.OLDPWD);
			Object newPwd = data.get(Define.REQUESTPARAMETER.NEWPWD);

			// 2. 赋值
			VertifyCode v = new VertifyCode();
			v.setPhone(phone.toString());
			v.setVertifycode(code.toString());
			v.setOldPwd(oldPwd.toString());
			v.setNewPwd(newPwd.toString());

			// 3. 返回结果
			JSONObject json = vertifyCodeService.updatePwd(v, user, phone.toString());
			dzdResponse.setData(json);
			dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
			dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
		}
		return dzdResponse;

	}
	
	/**
	 * 找回密码
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/find/findbackpwd")
	@ResponseBody
	public DzdResponse findBackPwd(HttpServletRequest request) throws Exception {
		DzdResponse dzdResponse = new DzdResponse();
		try {
			// 1. 基本信息
			VertifyCode v = new VertifyCode();
			v.setPhone(request.getParameter(Define.REQUESTPARAMETER.PHONE));
			v.setEmail(request.getParameter(Define.REQUESTPARAMETER.EMAIL));
			v.setVertifycode(request.getParameter(Define.REQUESTPARAMETER.PHOCODE));
			v.setImgCode(request.getParameter(Define.REQUESTPARAMETER.IMGCODE));

			// 3. 返回结果
			JSONObject json = vertifyCodeService.findBackPwd(v);
			dzdResponse.setData(json);
		} catch (Exception e) {
			e.printStackTrace();
			dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
		}
		return dzdResponse;

	}
	
	/**
	 * 获取验证码
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getVertifyCode")
	@ResponseBody
	public Object getVertifyCode(HttpServletRequest request) throws Exception
	{
		DzdResponse response = new DzdResponse();
		try
		{
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String imgCode = request.getParameter("imgCode");

			if ( phone == null || "".equals(phone) )
			{
				response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
				response.setData(ErrorCodeTemplate.MSG_SMS_PHONE_IS_EMPTY);
				return response;
			}
			if ( imgCode != null && !imgCode.equals("") && imgCode.equals("checkImgCode") )
			{
				// 验证码为空,返回提示
				Object captcha = request.getParameter("captcha");
				if ( captcha == null )
				{
					response.setData(ErrorCodeTemplate.MSG_VERTIFYCODE_EMPTY);
					response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
					return response;
				}
				// 校验图形验证码
				HttpSession session = request.getSession();
				Object randomString = session.getAttribute("randomString");
				if ( captcha != null && !"".equals(captcha.toString()) && randomString != null
				        && !"".equals(randomString.toString())
				        && captcha.toString().trim().toUpperCase()
				                .equals(randomString.toString().trim().toUpperCase()) )
				{
						response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
						return response;
					// 验证码错误 返回提示
				} else
				{
					response.setData(ErrorCodeTemplate.MSG_VERIFYCODE_ERROR);
					response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
					return response;
				}
				// 2.修改密码,判断用户是否存在 若不存在 返回提示
			} else
			{
				SysUser sysUser = sysUserService.queryUserExist(email);
				if ( sysUser == null )
				{
					response.setData(ErrorCodeTemplate.MSG_SYSUSER_EMPTY_1);
					response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
					return response;
				}
			}
			// 3.查询短信验证码次数,超过二次 返回提示
			List<VertifyCode> todayCodeList = vertifyCodeService.getCodeCountByPhoneAndType(phone,2);
			if ( todayCodeList != null && todayCodeList.size() >= 2 )
			{
				response.setData(ErrorCodeTemplate.MSG_VERTIFYCODE_INPUT_TWO);
				response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
				return response;
			} else
			{
				// 4. 发送短信验证码
				VertifyCode code = new VertifyCode();
				code.setPhone(phone);
				code.setCreate_time(new Date());
				code.setVertifycode(SendSmsUtil.createVertifyCode());
				code.setContent(SmsContentBean.getSendVerifyCodeSmsContentByResetPwd(code));// 内容
                code.setType(2); //2:找回密码短信验证标示
				// 发送验证码短信
				SendSmsUtil.sendSMS(code);
				vertifyCodeService.add(code);
				response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
				return response;
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = "系统异常,请稍后再试!";
			response.setData(msg);
			response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
		}
		return response;
	}
}
