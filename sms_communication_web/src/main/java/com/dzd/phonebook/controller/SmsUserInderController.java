package com.dzd.phonebook.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dzd.phonebook.aop.MethodDescription;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.base.util.SessionUtils;
import com.dzd.phonebook.entity.SmsUserBlank;
import com.dzd.phonebook.entity.SmsUserSignature;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.Instruct;
import com.dzd.phonebook.service.SmsAisleGroupService;
import com.dzd.phonebook.service.SmsSendTaskService;
import com.dzd.phonebook.service.SmsUserBlankService;
import com.dzd.phonebook.service.SmsUserMessageService;
import com.dzd.phonebook.service.SmsUserService;
import com.dzd.phonebook.service.SmsUserSignatureService;
import com.dzd.phonebook.service.UserFreeTrialService;
import com.dzd.phonebook.service.UserMessageService;
import com.dzd.phonebook.util.CalculateMoneyUtil;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.InstructState;
import com.dzd.phonebook.util.RedisUtil;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsAisleGroupType;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.phonebook.util.UserFreeTria;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/smsUser")
public class SmsUserInderController<T> {
    @Autowired
    private SmsUserService<T> smsUserService;
    @Autowired
    private SmsSendTaskService<T> smsSendTaskService;
    @Autowired
    private UserMessageService<T> userMessageService;
    @Autowired
    private SmsUserBlankService smsUserBlankService;
    @Autowired
    private SmsUserMessageService<T> smsUserMessageService;
    @Autowired
    private SmsUserSignatureService<SmsUserSignature> smsUserSignatureService;
    @Autowired
    private UserFreeTrialService<UserFreeTria> userFreeTrialService;
    @Autowired
    private SmsAisleGroupService<T> smsAisleGroupService;

    @RequestMapping("/index")
    public String list(HttpServletRequest request) {
        SysUser user = SessionUtils.getUser(request);
        SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());

        // 查询消息条数、短信余额、账户类型
        Integer groupId = smsUsers.getGroupTypeId();
        Integer msgCount = smsUserMessageService.queryMessageCount(smsUsers.getEmail(), groupId);
        Integer surplusNum = smsUserBlankService.querySurplusNumByUid(smsUsers.getId());
        SessionUtils.setBlank(request, surplusNum);
        SessionUtils.setMessageCount(request, msgCount);
        if (groupId != null) {
            SmsAisleGroupType groupType = smsUserService.queryGroupTypeBySmsUserId(groupId);
            SessionUtils.setSmsUserType(request, groupType);
        }
        return "app/index";
    }

    @MethodDescription("接口API下载")
    @RequestMapping("/qwAPI")
    public String qwAPI() {
        return "/app/apiwd/qw_api";
    }

    /**
     * 今日统计
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/today")
    @ResponseBody
    public DzdResponse todayStatistics(HttpServletRequest request, HttpServletResponse response) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            Integer surplusNum = 0;
            String todayCount = "0";
            // 1. 基本属性
            SysUser user = SessionUtils.getUser(request);
            SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());
            if (smsUsers != null) {
                Integer uid = smsUsers.getId();// 用户id

                // 2. 查询可发短信条数
                surplusNum = smsUserBlankService.querySurplusNumByUid(uid);

                // 3. 查询今日消费短信条数
                todayCount = smsUserService.querySmsUserStatisticalByUid(uid);
            }

            // 4. 返回数据
            JSONObject json = new JSONObject();
            json.put("todayCount", todayCount);
            json.put("surplusNum", surplusNum);

            dzdResponse.setData(json);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            e.printStackTrace();
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        }
        return dzdResponse;
    }

    /**
     * 获取用户账户类型
     *
     * @return
     */
    @RequestMapping("/getGroupTypeState")
    @ResponseBody
    public DzdResponse getGroupType(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            SysUser user = SessionUtils.getUser(request);
            SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());
            Integer groupTypeId = smsUsers.getGroupTypeId();
            dzdResponse.setData(groupTypeId);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            e.printStackTrace();
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        }
        return dzdResponse;
    }

    /**
     * 用户自主选择通道
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "updateGroupType", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DzdResponse updateGroupType(HttpServletRequest request, @RequestBody Map<String, Object> data) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            Object tid = data.get("tid");
            // 1.基本信息
            SysUser user = SessionUtils.getUser(request);
            SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());
            Integer tids = Integer.parseInt(tid.toString());
            String email = smsUsers.getEmail();

            // 2.根据类型查询通道组
            SmsAisleGroup smsAisleGroup = userMessageService.querySmsAisleGroupByTid(tids);
            if (smsAisleGroup != null) {

                // 3.修改用户信息，通道组
                smsUsers.setAisleGroupId(smsAisleGroup.getId());
                smsUsers.setGroupTypeId(tids);
                smsUsers.setEmail(email);
                smsUserService.updateSmsUserInfo(smsUsers);

                // 4.新增用户余额
                SmsUserBlank blank = smsUserBlankService.queryUserBlank(smsUsers.getId());
                if (blank == null) {
                    SmsUserBlank b = new SmsUserBlank();
                    b.setUserId(smsUsers.getId());
                    b.setSurplusNum(10);// 默认选择之后，免费送10条短信
                    smsUserBlankService.addUserBlank(b);

                    String keys = smsUserService.querySmsUserKey(smsUsers.getId());
                    instructSend(InstructState.USERTOPUP_SUCESS, keys, smsUsers.getId()); // 发送动作指令到redis

                    // 5.新增默认新增签名和免审模板
                    String sign = "千讯信通";
                    SmsUserSignature signature = new SmsUserSignature();
                    signature.setSignature(sign);
                    signature.setContent(sign);
                    signature.setFreeTrialState(1);
                    signature.setSmsUserId(smsUsers.getId());
                    smsUserSignatureService.add(signature);

                    String tria = "您的验证码是：@";
                    UserFreeTria userFreeTria = new UserFreeTria();
                    userFreeTria.setName("系统验证码");
                    userFreeTria.setContent(tria);
                    userFreeTria.setFreeTrialType(1);
                    userFreeTria.setFreeTrialState(1);
                    userFreeTria.setSmsUserId(smsUsers.getId());
                    userFreeTrialService.saveUserFreeTria(userFreeTria);// 新增
                    instructSend(InstructState.ADDUSERFREETRIAL_SUCESS, keys, smsUsers.getId()); // 发送动作指令到redis
                }
                Integer surplusNum = smsUserBlankService.querySurplusNumByUid(smsUsers.getId());
                SessionUtils.setBlank(request, surplusNum);
                dzdResponse.setData(surplusNum);

                // 5.获取用户类型
                SmsAisleGroupType groupType = smsUserService.queryGroupTypeBySmsUserId(smsUsers.getGroupTypeId());
                SessionUtils.setSmsUserType(request, groupType);
            }
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            e.printStackTrace();
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        }
        return dzdResponse;
    }


    /**
     * 获取用户配置的短信通道价格段
     *
     * @return
     */
    @RequestMapping("/getAisleMoney")
    @ResponseBody
    public DzdResponse getAisleMoney(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            SysUser sysUser = SessionUtils.getUser(request);
            SmsUser smsUsers = userMessageService.querySmsUserById(sysUser.getId());
            Integer aisleGroupId = smsUsers.getAisleGroupId();
            if (aisleGroupId == null) {
                dzdResponse.setData("-100");
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }

            // 查询价格区间
            SmsAisleGroup smsAisleGroup = smsAisleGroupService.querySmsAisleGroupById(aisleGroupId);

            dzdResponse.setData(smsAisleGroup);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            System.out.print(e);
        }
        return dzdResponse;
    }





    /**
     * @Description:代理数据处理动作发送
     * @author:oygy
     * @time:2017年1月11日 下午2:45:22
     */
    private void instructSend(String keys, String smsUserKey, Integer smsUserId) {
        Instruct instruct = new Instruct();
        instruct.setKey(keys);
        instruct.setSmsUserKey(smsUserKey);
        instruct.setSmsUserId(smsUserId + "");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonStr = mapper.writeValueAsString(instruct);
            RedisUtil.publish(InstructState.AB, jsonStr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
