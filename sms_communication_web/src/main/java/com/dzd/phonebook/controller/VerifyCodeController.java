package com.dzd.phonebook.controller;

import com.dzd.base.util.SessionUtils;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.entity.VertifyCode;
import com.dzd.phonebook.service.SmsUserService;
import com.dzd.phonebook.service.UserMessageService;
import com.dzd.phonebook.service.VertifyCodeService;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.phonebook.util.send.api.SendSmsUtil;
import com.dzd.phonebook.util.send.api.SmsContentBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 验证码Controller
 * Created by Administrator on 2017/6/12.
 */
@Controller
@RequestMapping("/smsVertifyCode")
public class VerifyCodeController {
    @Autowired
    private VertifyCodeService vertifyCodeService;
    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private SmsUserService smsUserService;

    /**
     * 获取验证码
     *
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/getSmsVertifyCode")
    @ResponseBody
    public Object getVertifyCode(HttpServletRequest request) throws Exception {
        DzdResponse response = new DzdResponse();
        try {
            Object phone = request.getParameter("phone");
            Object type = request.getParameter("type");
            if (phone == null || "".equals(phone)) {
                response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return response;
            }
            if (type != null && !type.equals("")) {
                //查询手机号码是否已经注册
                Integer phoneIsHad=smsUserService.queryPhone(phone.toString(),null);
                if(phoneIsHad==0){
                    response.setRetCode("000002");
                    response.setRetMsg("手机号码不存在，请重新输入!");
                    return response;
                }
                // 1.查询短信验证码次数,不能超过1次 返回提示
                List<VertifyCode> todayCodeList = vertifyCodeService.getCodeCountByPhoneAndType(phone.toString(),Integer.parseInt(type.toString()));
                if (todayCodeList != null && todayCodeList.size() >= 1) {
                    response.setData(ErrorCodeTemplate.MSG_VERTIFYCODE_INPUT_ONE);
                    response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                    return response;
                }
            }
            // 1.查询短信验证码次数,超过三次 返回提示
            /*List<VertifyCode> todayCodeList = vertifyCodeService.getCode(phone.toString());
            if (todayCodeList != null && todayCodeList.size() >= 3) {
                response.setData(ErrorCodeTemplate.MSG_VERTIFYCODE_INPUT);
                response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return response;*/

            // 2. 发送短信验证码
            // } else {
            VertifyCode code = new VertifyCode();
            code.setPhone(phone.toString());
            code.setCreate_time(new Date());
            code.setVertifycode(SendSmsUtil.createVertifyCode());
            code.setContent(SmsContentBean.getSendVerifyCodeSmsContent(code));
            if(type != null && !type.equals("")) {
                code.setType(1);
            }else{
                code.setType(0);
            }
            // 发送验证码短信
            SendSmsUtil.sendSMS(code);
            vertifyCodeService.add(code);
            response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            return response;
            // }

        } catch (Exception e) {
            e.printStackTrace();
            response.setData(ErrorCodeTemplate.MSG_SYSTEM_ERROR_MSG);
            response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        }
        return response;
    }

    /**
     * 校验验证码是否正确
     *
     * @param request
     * @return
     */
    @RequestMapping("/checkVerifyCode")
    @ResponseBody
    public DzdResponse checkVerify(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            Object phone = request.getParameter("phone");
            Object verifyCode = request.getParameter("verifyCode");

            if (phone == null) {
                dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_SMS_PHONE_IS_EMPTY);
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }

            if (verifyCode == null) {
                dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_SMS_VERIFY_IS_EMPTY);
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }


            // 查询验证码
            VertifyCode vertify = vertifyCodeService.queryCodeByPhoneAndCode(phone.toString(), verifyCode.toString());
            if (vertify == null) {
                dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_VERIFYCODE_ERROR);
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            } else {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_SYSTEM_ERROR_MSG);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        }
        return dzdResponse;

    }



    /**
     * 校验验证码是否正确 - 发送短信专用
     *
     * @param request
     * @return
     */
    @RequestMapping("/checkVerifyCodeBySendSms")
    @ResponseBody
    public DzdResponse checkVerifyBySendSms(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            Object phone = request.getParameter("phone");
            Object verifyCode = request.getParameter("verifyCode");

            if (phone == null) {
                dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_SMS_PHONE_IS_EMPTY);
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }

            if (verifyCode == null) {
                dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_SMS_VERIFY_IS_EMPTY);
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }


            // 查询验证码
            VertifyCode vertify = vertifyCodeService.queryCodeByPhoneAndCode(phone.toString(), verifyCode.toString());
            if (vertify == null) {
                dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_VERIFYCODE_ERROR);
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            } else {
                SysUser sysUser = SessionUtils.getUser(request);
                SmsUser smsUsers = userMessageService.querySmsUserById(sysUser.getId());
                smsUsers.setVerifyType(1);
                smsUserService.updateSmsUserVerifyType(smsUsers);
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_SYSTEM_ERROR_MSG);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        }
        return dzdResponse;

    }
}
