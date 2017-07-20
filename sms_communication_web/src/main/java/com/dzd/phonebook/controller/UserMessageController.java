package com.dzd.phonebook.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONException;
import com.dzd.base.entity.BaseEntity;
import com.dzd.base.util.MethodUtil;
import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.page.MD5;
import com.dzd.phonebook.service.ChannelService;
import com.dzd.phonebook.service.Instruct;
import com.dzd.phonebook.service.SmsUserService;
import com.dzd.phonebook.service.SysMenuBtnService;
import com.dzd.phonebook.service.SysUserService;
import com.dzd.phonebook.service.UserMessageService;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.InstructState;
import com.dzd.phonebook.util.RedisUtil;
import com.dzd.phonebook.util.ServiceUrl;
import com.dzd.phonebook.util.SmsAisleGroupHasSmsUser;
import com.dzd.phonebook.util.SmsAisleGroupType;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.phonebook.util.SmsUserMoneyRunning;
import com.dzd.phonebook.util.WebRequestParameters;
import com.github.pagehelper.Page;

import net.sf.json.JSONObject;

/**
 * @Description:账号信息Controller
 * @author:oygy
 * @time:2017年1月12日 下午5:30:46
 */
@Controller
@RequestMapping("/userMessage")
public class UserMessageController extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(UserMessageController.class);


    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private SysUserService<SysUser> sysUserService;

    @Autowired
    private SysMenuBtnService sysMenuBtnService;

    @Autowired
    private SmsUserService smsUserService;

    @Autowired
    private ChannelService channelService;

    /**
     * 查询账户信息
     */
    @RequestMapping("/listview")
    public String list(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        SysUser user = (SysUser) request.getSession().getAttribute("session_user");
        try {
            SmsUser smsUser = userMessageService.querySmsUserById(user.getId());
            model.addAttribute("smsUser", smsUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "userMessage/smUserMessage";
    }


    /**
     * 访问通道页面
     */
    @RequestMapping("pwdlistview")
    public String pwdlistview(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        SysUser user = (SysUser) request.getSession().getAttribute("session_user");
        try {
            SmsUser smsUser = userMessageService.querySmsUserById(user.getId());
            if (smsUser != null) {
                smsUser.setMoney(null);
                smsUser.setAwardMoney(null);
            }
            model.addAttribute("smsUser", smsUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "userMessage/smsUserUpdatePwd";
    }


    /**
     * @Description:修改用户密码
     * @author:liuyc
     * @time:2017年1月4日 下午4:39:53
     */
    @MethodDescription("修改用户密码")
    @RequestMapping(value = "/from/merge", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public DzdResponse updateSmsUserPwd(HttpServletRequest request, Model model) {
        DzdResponse dzdResponse = new DzdResponse();
        Object oldPwdVal = request.getParameter("oldPwd");
        Object newPwdVal = request.getParameter("newPwd");
        if (oldPwdVal == null || newPwdVal == null) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            return dzdResponse;
        }
        try {
            String oldPwd = oldPwdVal.toString();
            String newPwd = newPwdVal.toString();
            SysUser user = SessionUtils.getUser(request);
            if (user == null) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }
            SysUser bean = sysUserService.queryById(user.getId());
            if (bean.getId() == null || BaseEntity.DELETED.YES.key == bean.getDeleted()) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }
            if (StringUtils.isBlank(newPwd)) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }
            // 不是超级管理员，匹配旧密码
            if (!MethodUtil.ecompareMD5(oldPwd, bean.getPwd())) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }
            bean.setPwd(MethodUtil.MD5(newPwd));
            sysUserService.updateBySelective(bean);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdResponse;
    }


    /**
     * @Description:修改用户推送、上行地址
     * @author:liuyc
     * @time:2017年1月4日 下午4:39:53
     */
    @MethodDescription("修改用户推送、上行地址")
    @RequestMapping(value = "/from/userMerge", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public DzdResponse updateUserMerge(HttpServletRequest request, Model model) {
        DzdResponse dzdResponse = new DzdResponse();
        Object reportUrl = request.getParameter("reportUrl");
        Object replyUrl = request.getParameter("replyUrl");
        Object id = request.getParameter("id");

        if (reportUrl == null || replyUrl == null || id == null) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            return dzdResponse;
        }
        try {
            SmsUser smsUser = new SmsUser();

            if (!id.toString().isEmpty()) {
                smsUser.setId(Integer.parseInt(id.toString()));
            }
            if (!reportUrl.toString().isEmpty()) {
                smsUser.setReportUrl(reportUrl.toString());
            }
            if (!replyUrl.toString().isEmpty()) {
                smsUser.setReplyUrl(replyUrl.toString());
            }
            userMessageService.updateSmsUserById(smsUser);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdResponse;
    }
    
    
  /*  public static void main(String[] args) {
        String str = "Wed Jul 02 16:00:20 CST 2014";
    	Date date = parse(str, "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String dd = format.format(date);
    	System.out.println("dd>>>>  " + dd);
    	}
    
    public static Date parse(String str, String pattern, Locale locale) {
    	if (str == null || pattern == null) {
    	return null;
    	}
    	try {
    	return new SimpleDateFormat(pattern, locale).parse(str);
    	} catch (ParseException e) {
    	e.printStackTrace();
    	}
    	return null;
    	}

    	public static String format(Date date, String pattern, Locale locale) {
    	if (date == null || pattern == null) {
    	return null;
    	}
    	return new SimpleDateFormat(pattern, locale).format(date);
    	}*/


    @RequestMapping("/puserBill")
    public String puserBill(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        return "userMessage/puserBill";
    }


    /**
     * @Description:查询登录代理账单流水信息列表
     * @author:oygy
     * @time:2016年12月31日 下午2:01:34
     */
    @RequestMapping(value = "/puserBillList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse puserBillList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");
            Object email = data.get("email");
            Object type = data.get("type");
            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            SmsUser smsUser = userMessageService.querySmsUserById(user.getId());

            if (menuId == null || smsUser == null) {
                return dzdPageResponse;
            }
            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }

            if (!StringUtil.isEmpty(parameters.getStartInput())) {
                sortMap.put("startInput", parameters.getStartInput());
                sortMap.put("endInput", parameters.getEndInput());
            }

            if (email != null && !StringUtil.isEmpty(email.toString())) {
                sortMap.put("email", email.toString());
            }
            if (type != null && !StringUtil.isEmpty(type.toString())) {
                sortMap.put("type", Integer.parseInt(type.toString()));
            }
            if (smsUser.getId() != null && !StringUtil.isEmpty(smsUser.getId().toString())) {
                sortMap.put("smsUserId", smsUser.getId());
            }


            //排序
            if (parameters.getSort() != null && parameters.getOrder() != null) {
                sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
            } else {
                sortMap.put("sortVal", "order by createTime desc");
            }
            dzdPageParam.setCondition(sortMap);
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
            }

            Page<SmsUserMoneyRunning> dataList = smsUserService.querySmsUserPuserBillList(dzdPageParam);
            SmsUserMoneyRunning smsUserm = smsUserService.querySmsUserPuserBillZong(dzdPageParam);
            if (dataList.size() > 0) {
                dataList.get(0).setTopUpNum(smsUserm.getTopUpNum());
                dataList.get(0).setConsumeNum(-smsUserm.getConsumeNum());
                dataList.get(0).setRefundNum(smsUserm.getRefundNum());
            }

            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsUserMoneyRunning instruct : dataList.getResult()) {
                    instruct.setSysMenuBtns(sysMenuBtns);
                }
                dzdPageResponse.setRows(dataList.getResult());
                dzdPageResponse.setTotal(dataList.getTotal());
            }
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }


    @RequestMapping("/statistical")
    public String statistical(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        return "userMessage/puserStatistical";
    }


    /**
     * @Description:查询代理统计信息列表
     * @author:oygy
     * @time:2016年12月31日 下午2:01:34
     */
    @RequestMapping(value = "/puserStatisticalList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse puserStatisticalList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");
            Object email = data.get("email");

            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());

            if (menuId == null || smsUsers == null) {
                return dzdPageResponse;
            }

            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }

            if (!StringUtil.isEmpty(parameters.getStartInput())) {
                sortMap.put("startInput", parameters.getStartInput());
                sortMap.put("endInput", parameters.getEndInput());
            }

            if (smsUsers.getEmail() != null && !StringUtil.isEmpty(smsUsers.getEmail())) {
                sortMap.put("email2", smsUsers.getEmail());
            }


            //排序
            if (parameters.getSort() != null && parameters.getOrder() != null) {
                sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
            }
            dzdPageParam.setCondition(sortMap);
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
            }

            Page<SmsUser> dataList = smsUserService.querySmsUserStatistical(dzdPageParam);
            SmsUser smsUser = smsUserService.querySmsUserStatisticalZong(dzdPageParam);
            if (dataList.size() > 0) {
                dataList.get(0).setSumSendNum(smsUser.getSumSendNum());
                dataList.get(0).setSumConsumeMoney(smsUser.getSumConsumeMoney());
                dataList.get(0).setSumExpectMoney(smsUser.getSumExpectMoney());


                dataList.get(0).setSumFailureNumUs(smsUser.getSumFailureNumUs());
                dataList.get(0).setSumSucceedNumUs(smsUser.getSumSucceedNumUs());
                dataList.get(0).setSumUnknownNumUs(smsUser.getSumUnknownNumUs());

                dataList.get(0).setSumFailureNumMs(smsUser.getSumFailureNumMs());
                dataList.get(0).setSumSucceedNumMs(smsUser.getSumSucceedNumMs());
                dataList.get(0).setSumUnknownNumMs(smsUser.getSumUnknownNumMs());

                dataList.get(0).setSumFailureNumTs(smsUser.getSumFailureNumTs());
                dataList.get(0).setSumSucceedNumTs(smsUser.getSumSucceedNumTs());
                dataList.get(0).setSumUnknownNumTs(smsUser.getSumUnknownNumTs());
            }


            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsUser instruct : dataList.getResult()) {
                    instruct.setSysMenuBtns(sysMenuBtns);
                }
                dzdPageResponse.setRows(dataList.getResult());
                dzdPageResponse.setTotal(dataList.getTotal());
            }
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }


    @RequestMapping("/welcome")
    public String showWelcome(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        SysUser user = (SysUser) request.getSession().getAttribute("session_user");
        SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());
        DzdPageParam dzdPageParam = new DzdPageParam();
        Map<String, Object> sortMap = new HashMap<String, Object>();
        Date date = new Date();//获取时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//转换格式

        sortMap.put("startInput", sdf.format(date));
        sortMap.put("endInput", sdf.format(date));
        if (smsUsers.getEmail() != null && !StringUtil.isEmpty(smsUsers.getEmail())) {
            sortMap.put("email2", smsUsers.getEmail());
        }
        dzdPageParam.setCondition(sortMap);


        if (user.getId() != null) {
            try {
                SmsUser tjsmsUser = smsUserService.querySmsUserStatisticalZong(dzdPageParam);
                SmsUser smsUser = userMessageService.querySmsUserById(user.getId());

                List<SmsAisleGroupHasSmsUser> vipList = smsUserService.queryVipUserHasGroup(smsUser.getUserLevel());

                if (smsUser != null && smsUser.getId() != null) {
                    List<SmsAisleGroupHasSmsUser> ssList = userMessageService.querySmsUserPrice(smsUser.getId());
                    List<SmsAisleGroupType> typelist = channelService.querySmsAisleGroupType();
                    List<SmsAisleGroupHasSmsUser> saList = smsUserService.queryUserHasGroup(smsUser.getId());
                    model.addAttribute("ssList", ssList);
                    //model.addAttribute("smsUser", smsUser);
                    request.getSession().setAttribute("smsUser", smsUser);
                    model.addAttribute("typelist", typelist);
                    model.addAttribute("tjsmsUser", tjsmsUser);
                    model.addAttribute("uid", smsUsers.getId());
                    model.addAttribute("userLevel", smsUsers.getUserLevel());
                    model.addAttribute("saList", saList);
                    model.addAttribute("vipList", vipList);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return "userMessage/welcome";
    }


    /**
     * @Description:根据通道用户关系ID修改关联通道组ID
     * @author:oygy
     * @time:2017年1月4日 下午4:39:53
     */
    @RequestMapping(value = "/from/updateUserAg", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public DzdResponse updateUserAg(HttpServletRequest request, Model model) {
        DzdResponse dzdResponse = new DzdResponse();
        Object smsAisleGroupId = request.getParameter("smsAisleGroupId");
        Object ugid = request.getParameter("ugid");

        if (smsAisleGroupId == null || ugid == null) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            return dzdResponse;
        }
        try {
            SmsAisleGroupHasSmsUser saghs = new SmsAisleGroupHasSmsUser();

            if (!smsAisleGroupId.toString().isEmpty()) {
                saghs.setSmsAisleGroupId(Integer.parseInt(smsAisleGroupId.toString()));
            }
            if (!ugid.toString().isEmpty()) {
                saghs.setId(Integer.parseInt(ugid.toString()));
            }
            userMessageService.updateUserAg(saghs);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);

            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());
            String keys = smsUserService.querySmsUserKey(smsUsers.getId());
            instructSend(InstructState.ALLOTAAISLEGROUP_SUCESS, keys, smsUsers.getId());   //发送动作指令到redis
        } catch (Exception e) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdResponse;
    }

    /**
     * @Description:代理添加通道格子
     * @author:liuyc
     * @time:2017年1月4日 下午4:39:53
     */
    @MethodDescription("代理添加通道格子")
    @RequestMapping(value = "/from/addUserAg", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public DzdResponse addUserAg(HttpServletRequest request, Model model) {
        DzdResponse dzdResponse = new DzdResponse();
        Object uid = request.getParameter("uid");

        if (uid == null) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            return dzdResponse;
        }
        try {
            SmsAisleGroupHasSmsUser saghs = new SmsAisleGroupHasSmsUser();

            if (!uid.toString().isEmpty()) {
                saghs.setSmsUserId(Integer.parseInt(uid.toString()));
            }
            userMessageService.addUserAg(saghs);
            dzdResponse.setData(saghs.getId());
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
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


    /**
     * 访问AIP设置页面
     */
    @RequestMapping("/apiSetting")
    public String apiSetting(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        SysUser user = (SysUser) request.getSession().getAttribute("session_user");
        String plugin_path = userMessageService.querySysConfig("plugin_path");
        SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());
        String key = smsUsers.getKey();
        if (key != null && !key.equals("")) {
            smsUsers.setKey(replaceAction(key, "(?<=.{16}).(?=.{0})"));
        } else {
            smsUsers.setKey("");
        }
        model.addAttribute("smsUsers", smsUsers);
        model.addAttribute("pluginPath", plugin_path);


        return "userMessage/apiSetting";
    }

    /**
     * 实际替换动作
     *
     * @param username username
     * @param regular  正则
     * @return
     */
    private static String replaceAction(String username, String regular) {
        return username.replaceAll(regular, "*");
    }


}
