package com.dzd.phonebook.util;

/**
 * 错误代码
 *
 * @author chenchao
 * @date 2016-6-27 10:08:00
 */
public class ErrorCodeTemplate {
    /* 成功 */
    public static final String CODE_SUCESS = "000000";
    /* 失败 */
    public static final String CODE_FAIL = "000001";
    /* 参数异常 */
    public static final String CODE_PARAMETER_ERROR = "000999";

    public static final String MSG_USER_ISNULL = "账户名或密码错误!";
    public static final String MSG_STATE_ERROR = "登录异常,请稍候再试!";
    public static final String MSG_SYSUSER_EMPTY = "用户不存在!";
    public static final String MSG_SYSUSER_PWD_IS_MISS = "密码错误!";
    public static final String MSG_VERTIFYCODE_EMPTY = "请输入验证码!";
    public static final String MSG_REGISTER_MSG = "用户已经存在!";

    public static final String MSG_SUCCESS_MSG = "登录成功!";
    public static final String MSG_SYSTEM_ERROR_MSG = "服务器异常!";

    public static final String MSG_SMS_PHONE_IS_EMPTY = "请输入手机号码!";
    public static final String MSG_SMS_VERIFY_IS_EMPTY = "请输入验证码!";
    public static final String MSG_VERIFYCODE_ERROR = "验证码错误或失效!";
    
    public static final String MSG_VERTIFYCODE_INPUT = "获取次数频繁（一天三次）,请稍候重试!";

    /**
     * 咨询与反馈短信验证码返回信息
     */
    public static final String MSG_VERTIFYCODE_INPUT_ONE= "获取次数频繁（一天一次）!";

    /**
     *  找回密码短信验证码返回信息
     */
    public static final String MSG_VERTIFYCODE_INPUT_TWO= "获取次数频繁（一天二次）,请稍候重试!";

    public static final String MSG_SYSUSER_EMPTY_1 = "账户名无效！";

    public static final String MSG_SUCCESS = "操作成功！";

    public static final String MSG_FAIL = "操作失败！";

    public static final String MSG_MANUAL_ADDITION = "手动添加短信数量！";
}

