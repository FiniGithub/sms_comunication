package com.dzd.phonebook.service;

import java.util.Date;
import java.util.List;

import com.dzd.phonebook.util.send.api.SmsContentBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.base.service.BaseService;
import com.dzd.base.util.MethodUtil;
import com.dzd.phonebook.dao.VertifyCodeDao;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.entity.VertifyCode;
import com.dzd.phonebook.util.InstructSendUtil;
import com.dzd.phonebook.util.InstructState;
import com.dzd.phonebook.util.MakeSystemUUID;
import com.dzd.phonebook.util.send.api.SendSmsUtil;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.sms.application.SmsServerManager;

import net.sf.json.JSONObject;

/**
 * 验证码服务类
 *
 * @author CHENCHAO
 * @date 2017-04-11 11:25:00
 */
@Service("VertifyCodeService")
public class VertifyCodeService<T> extends BaseService<T> {
    @Autowired
    private SysUserService<SysUser> sysUserService;
    @Autowired
    private SmsUserService<SmsUser> smsUserService;

    @Autowired
    private VertifyCodeDao<T> vertifyCodeDao;

    @Override
    public VertifyCodeDao<T> getDao() {
        return vertifyCodeDao;
    }

    public Integer getCodeCountByToday(String phone) {
        Integer count = vertifyCodeDao.getCodeCountByToday(phone);
        if (count == null) {
            count = 0;
        }
        return count;
    }

    public List<VertifyCode> getCode(String phone) {
        return vertifyCodeDao.getCodeCountByPhone(phone);
    }

    /**
     * 查询当天发送短信验证码的次数
     * @param phone
     * @param type
     * @return
     */
    public List<VertifyCode> getCodeCountByPhoneAndType(@Param("phone") String phone,@Param("type") Integer type){
        return vertifyCodeDao.getCodeCountByPhoneAndType(phone,type);
    }

    /**
     * 注册
     *
     * @param user
     * @param verifyCode
     * @return
     */
    public JSONObject register(SysUser user, String verifyCode, String imgCode) {
        JSONObject json = new JSONObject();
        try {
            boolean flag = true;
            int code = 0;
            String msg = "";
            String phone = user.getEmail().toString();
            String pwd = user.getPwd();

            // 1.校验手机号码
            if (phone == null || "".equals(phone)) {
                flag = false;
                code = 1;
                msg = "手机号码不能为空!";

            }

            // 2.检验密码
            else if (pwd == null || "".equals(pwd)) {
                flag = false;
                code = 2;
                msg = "密码不能为空!";
            } else if (pwd.length() < 6) {
                flag = false;
                code = -2;
                msg = "密码长度不能小于6!";
            }

            // 3. 校验图形验证码
            else if (imgCode == null || "".equals(imgCode)) {
                flag = false;
                code = -1;
                msg = "图形验证码不能为空!";
            }

            // 检验验证码
            else if (verifyCode == null || "".equals(verifyCode)) {
                flag = false;
                code = 3;
                msg = "短信验证码不能为空!";
            }

            // 4. 校验用户是否存在
            else {
                VertifyCode code2 = vertifyCodeDao.queryCodeByPhoneAndCode(phone, verifyCode);
                if (code2 == null) {
                    code = 4;
                    flag = false;
                    msg = "短信验证码错误!";
                } else {
                    SysUser sysUser = sysUserService.queryUserExist(phone);
                    if (sysUser != null) {
                        flag = false;
                        code = 5;
                        msg = "账户已经存在!";
                    }
                }
            }

            // 5.注册
            if (flag) {
                // 1.sys_user插入数据
                user.setPwd(MethodUtil.MD5(pwd));
                user.setState(1);
                user.setSuperAdmin(0);
                user.setDeleted(0);
                user.setNickName(phone);
                sysUserService.add(user);

                // 2.sms_user插入数据
                int sysUserId = user.getId();
                String key = MakeSystemUUID.getUUID();
                SmsUser smsUser = new SmsUser();
                smsUser.setName(phone);
                smsUser.setEmail(phone);
                smsUser.setPhone(phone);
                smsUser.setSysUserId(sysUserId);
                smsUser.setKey(key);

                smsUserService.addSmsUser(smsUser);
                code = 0;
                flag = true;
                msg = "注册成功,立即登录。";

                // 3.发送指令到redis
                InstructSendUtil.instructSend(InstructState.ADDSMSUSER_SUCESS, smsUser.getId());
            }

            json.put("flag", flag);
            json.put("code", code);
            json.put("msg", msg);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("flag", false);
            json.put("code", 1);
            json.put("msg", "发生异常,请稍后再试!");
        }
        return json;
    }

    /**
     * 修改用户密码
     * @param v
     * @param user
     * @param phone
     * @return
     */
    public JSONObject updatePwd(VertifyCode v, SysUser user, String phone) {
        JSONObject json = new JSONObject();
        try {
            boolean flag = true;
            int code = 0;
            String msg = "";
            String verifyCode = v.getVertifycode();
            String oldPwdStr = v.getOldPwd();
            String newPwdStr = v.getNewPwd();

            if (verifyCode == null || "".equals(verifyCode)) {
                code = 1;
                flag = false;
                msg = "验证码不能为空!";
            }
            if (oldPwdStr == null || "".equals(oldPwdStr)) {
                code = 1;
                flag = false;
                msg = "新密码不能为空!";
            }
            if (newPwdStr == null || "".equals(newPwdStr)) {
                code = 1;
                flag = false;
                msg = "确认密码不能为空!";
            }
            if (!oldPwdStr.equals(newPwdStr)) {
                code = 1;
                flag = false;
                msg = "两次密码输入不一致!";
            }
            if (oldPwdStr.length() < 6 || oldPwdStr.length() > 12) {
                code = 1;
                flag = false;
                msg = "密码长度在6~12之间!";
            }

            if (flag) {
                VertifyCode code2 = vertifyCodeDao.queryCodeByPhoneAndCode(v.getPhone(), verifyCode);
                if (code2 == null) {
                    code = 1;
                    flag = false;
                    msg = "验证码错误!";
                } else {
                    // 修改密码
                    SysUser bean = null;
                    if (user != null) {
                        bean = sysUserService.queryById(user.getId());
                    } else {
                        bean = sysUserService.queryUserExist(phone);
                    }

                    if (bean != null) {
                        bean.setPwd(MethodUtil.MD5(newPwdStr));
                        sysUserService.updateBySelective(bean);
                        code = 0;
                        flag = true;
                        msg = "修改成功,请登录!";
                    } else {
                        code = 1;
                        flag = false;
                        msg = "用户不存在!";
                    }

                }
            }

            json.put("flag", flag);
            json.put("code", code);
            json.put("msg", msg);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("flag", false);
            json.put("code", 1);
            json.put("msg", "系统异常,请稍后再试!");
        }
        return json;
    }

    /**
     * 找回密码
     * @param v
     * @return
     */
    public JSONObject findBackPwd(VertifyCode v) {
        JSONObject json = new JSONObject();
        try {
            boolean flag = true;
            int code = 0;
            String msg = "";
            String verifyCode = v.getVertifycode();
            String imgCode = v.getImgCode();

            if (verifyCode == null || "".equals(verifyCode)) {
                code = 1;
                flag = false;
                msg = "手机验证码不能为空!";
            }
            if (imgCode == null || "".equals(imgCode)) {
                code = 1;
                flag = false;
                msg = "图文验证码不能为空!";
            }

            if (flag) {
                VertifyCode code2 = vertifyCodeDao.queryCodeByPhoneAndCode(v.getPhone(), verifyCode);
                if (code2 == null) {
                    code = 1;
                    flag = false;
                    msg = "验证码错误!";
                } else {
                    // 修改密码
                    SysUser bean = sysUserService.queryUserExist(v.getEmail());
                    if (bean != null) {
                        com.dzd.sms.service.data.SmsUser smsUser = SmsServerManager.I.getUserBySysId(bean.getId().longValue());
                        if (smsUser != null && !v.getPhone().equals(smsUser.getPhone())) {
                            code = 1;
                            flag = false;
                            msg = "用户名与号码不匹配!";
                        } else {
                            // 4. 发送短信验证码
                            VertifyCode vertifyCode = new VertifyCode();
                            vertifyCode.setPhone(v.getPhone());
                            vertifyCode.setEmail(v.getEmail());
                            vertifyCode.setCreate_time(new Date());

                            String newPwdStr = SendSmsUtil.createNewPassword();
                            vertifyCode.setNewPwd(newPwdStr);// 新密码
                            vertifyCode.setContent(SmsContentBean.getSendPwdSmsContent(vertifyCode));// 内容


                            // 发送新密码
                            SendSmsUtil.sendSMS(vertifyCode);

                            bean.setPwd(MethodUtil.MD5(newPwdStr));
                            sysUserService.updateBySelective(bean);
                            code = 0;
                            flag = true;
                            msg = "修改成功,请查看短信密码，并重新登录!";
                        }
                    } else {
                        code = 1;
                        flag = false;
                        msg = "用户不存在!";
                    }

                }
            }

            json.put("flag", flag);
            json.put("code", code);
            json.put("msg", msg);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("flag", false);
            json.put("code", 1);
            json.put("msg", "系统异常,请稍后再试!");
        }
        return json;
    }

    public VertifyCode queryCodeByPhoneAndCode(String phone, String verifyCode) {
        VertifyCode code2 = vertifyCodeDao.queryCodeByPhoneAndCode(phone, verifyCode);
        return code2;
    }
}
