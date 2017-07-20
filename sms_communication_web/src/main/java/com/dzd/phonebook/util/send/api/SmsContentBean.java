package com.dzd.phonebook.util.send.api;

import com.dzd.phonebook.entity.VertifyCode;

/**
 * Created by Administrator on 2017/6/15.
 */
public class SmsContentBean {


    /**
     * 发送短信 - 密码
     *
     * @param code
     * @return
     */
    public static String getSendPwdSmsContent(VertifyCode code) {
        String content ="系统重置账户密码：" + code.getNewPwd()
                + "，请登录后及时修改密码【全网信通】";
        return content;
    }

    /**
     * 发送短信 - 验证码
     *
     * @param code
     * @return
     */
    public static String getSendVerifyCodeSmsContent(VertifyCode code) {
        String content = "您的验证码是：" + code.getVertifycode() + "，30分钟内有效。【全网信通】";
        return content;
    }

    /**
     * 发送短信 - 验证码
     *
     * @param code
     * @return
     */
    public static String getSendVerifyCodeSmsContentByResetPwd(VertifyCode code) {
        String content = "验证码"+code.getVertifycode()+"（重置账户密码），30分钟内输入有效！如非本人操作，请注意账户安全！【全网信通】";
        return content;
    }
}
