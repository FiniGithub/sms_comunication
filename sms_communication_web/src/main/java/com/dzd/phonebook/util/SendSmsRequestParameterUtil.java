package com.dzd.phonebook.util;

import com.dzd.base.util.SessionUtils;
import com.dzd.phonebook.entity.SmsFileConfig;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.ChannelService;
import com.dzd.phonebook.service.SmsFileConfigService;
import com.dzd.phonebook.service.UserMessageService;
import com.dzd.sms.application.Define;
import com.dzd.sms.service.data.SmsRequestParameter;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/7.
 */
public class SendSmsRequestParameterUtil {

    /**
     * 发送短信设置参数
     *
     * @param data
     * @param request
     * @param userMessageService
     * @param channelService
     * @return
     */
    public static SmsRequestParameter sendSmsRequestParameter(Map<String, Object> data, HttpServletRequest request, UserMessageService userMessageService,
                                                              ChannelService channelService, SmsFileConfigService smsFileConfigService) {
        SmsRequestParameter parmeter = new SmsRequestParameter();
        try {
            // 1.基本信息
            String uuid = data.get("uuid").toString();
            SysUser user = SessionUtils.getUser(request);
            SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());
            Integer uid = user.getId();
            if (smsUsers != null) {
                uid = smsUsers.getId();// 用户id
            }
            Integer repetNum = formatPhoneNumber(data.get("repet"));// 错误号码
            Integer invalid = formatPhoneNumber(data.get("invalid"));// 重复号码
            Integer errorNum = repetNum + invalid;// 错误号码总数
            // 发送内容
            String content = "";
            if (data.get("content") != null) {
                content = (String) data.get("content");
            }
            // 定时发送
            Date timing = null;
            int sendType = 0;// 立即发送
            if (data.get("timing") != null && !data.get("timing").equals("")) {
                sendType = 1;// 定时发送
                String timeStr = data.get("timing") + ":00";
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                timing = dateFormat.parse(timeStr);
                System.out.println("定时发送时间：" + timing);
            }


            // 2. 获取通道信息
            SmsAisleGroup smsAisleGroup = channelService.querySmsAisleGroupById(smsUsers.getAisleGroupId());
            String dredgeAM = smsAisleGroup.getDredgeAM();// 开通时间段 开始时间
            String dredgePM = smsAisleGroup.getDredgePM();// 开通时间段 截止时间
            Integer unregTypeId = smsAisleGroup.getUnregTypeId(); // 退订格式 0-关，1-开
            Integer state = smsAisleGroup.getState();


            // 3.过滤有效号码
            List<SmsFileConfig> configList = smsFileConfigService.querySmsFileConfigList(uuid, null);

            List<String> phoneList = SmsSendUtil.getPhoneListByConfig(configList);
            Map<String, Object> validPhoneMap = FileUploadUtil.getPhoneMap(phoneList, "");
            List<String> validPhoneList = (List<String>) validPhoneMap.get("validList");
            String fileName = uuid + ".txt";
            FileUploadUtil.writerFile(validPhoneList, fileName);// 保存有效号码文件


            // 4.读取有效号码文件,得到所有号码
            String catalinaHome = System.getProperty("catalina.home");
            String path = catalinaHome + FileUploadUtil.PHONE_PATH;
            String txtPath = path + "/" + uuid + ".txt";
            List<String> mobileList = FileUploadUtil.readDataByTxt(new File(txtPath));
            Map<String, Object> param = new HashMap<String, Object>();
            param.put(Define.STATICAL.SMSID, uid);


            // 5.设置发送短信的对象属性
            String smsUserSign = userMessageService.querySmsUserSign(param);
            Long groupTypId = Long.valueOf(smsUsers.getAisleGroupId());
            parmeter.setValidPhoneMap(validPhoneMap);
            parmeter.setUid(uid.toString());
            parmeter.setMobile(mobileList);
            parmeter.setText(content);
            parmeter.setErrorPhoneNumber(errorNum);
            parmeter.setTiming(timing);
            parmeter.setIid("110");
            parmeter.setGroupTypeId(groupTypId);
            parmeter.setSmsUserSign(smsUserSign);
            parmeter.setDredgeAM(dredgeAM);// 通道开启时间
            parmeter.setDredgePM(dredgePM);// 通道结束时间
            parmeter.setUnregTypeId(unregTypeId);// 退订格式 0-关，1-开
            parmeter.setSendType(sendType);// 发送类型
            parmeter.setSmsUserState(smsUsers.getState());// 用户状态 0：启用；1：禁用
            parmeter.setAisleGroupState(state);// 通道组状态 1：启用 2：停用
            parmeter.setAisleLongNum(smsAisleGroup.getSmsLength());// 通道组长短信计费
            parmeter.setShieldingFieldId(smsAisleGroup.getShieldingFieldId());// 通道组敏感词id
            parmeter.setVerifyType(smsUsers.getVerifyType());// 是否为首次登陆，超过50条需要验证码 0:需要,1：不需要
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parmeter;
    }


    /**
     * object转int 号码
     *
     * @param obj
     * @return
     */
    public static Integer formatPhoneNumber(Object obj) {
        Integer num = 0;
        if (obj != null) {
            num = Integer.parseInt(obj.toString());
        }
        return num;
    }
}
