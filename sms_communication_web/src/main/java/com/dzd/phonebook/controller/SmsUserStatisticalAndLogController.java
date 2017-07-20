package com.dzd.phonebook.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
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

import com.dzd.base.util.HtmlUtil;
import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.business.MsmSendBusiness;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.MsmSendService;
import com.dzd.phonebook.service.SmsUserService;
import com.dzd.phonebook.service.SysMenuBtnService;
import com.dzd.phonebook.util.ComparatorSmsUser;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.SmsSendLog;
import com.dzd.phonebook.util.TempParameter;
import com.dzd.phonebook.util.WebRequestParameters;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsUser;
import com.github.pagehelper.Page;

/**
 * @author lianghaunzheng
 *         E-mail: *
 * @version 1.0 *
 * @date 创建时间：2017年5月16日 上午11:10:10 *
 * @parameter *
 * @return
 * @since *
 */
@Controller
@RequestMapping("/statisticalandlog")
public class SmsUserStatisticalAndLogController extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(SmsUserStatisticalAndLogController.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    private MsmSendService msmSendService;

    @SuppressWarnings("rawtypes")
    @Autowired
    private SysMenuBtnService sysMenuBtnService;

    @SuppressWarnings("rawtypes")
    @Autowired
    private SmsUserService smsUserService;

    private MsmSendBusiness msmSendBusiness = new MsmSendBusiness();

    private Page<com.dzd.phonebook.util.SmsUser> dataList = new Page<com.dzd.phonebook.util.SmsUser>();

    public Page<com.dzd.phonebook.util.SmsUser> getDataList() {
        return dataList;
    }

    public void setDataList(Page<com.dzd.phonebook.util.SmsUser> dataList) {
        this.dataList = dataList;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/statisticalday")
    public String queryStatisticalDayInfo(HttpServletRequest request, Model model) throws Exception {
        List<String> aisleNames = msmSendService.queryAisleNames();// 查询所有的通道名称
        List<String> nickNames = msmSendService.querynickNames();// 查询所有客户所属
        model.addAttribute(Define.REQUESTPARAMETER.AISLENAMES, aisleNames);
        model.addAttribute(Define.REQUESTPARAMETER.NICKNAMES, nickNames);

        Object menuId = request.getParameter(Define.REQUESTPARAMETER.ID);
        model.addAttribute(Define.REQUESTPARAMETER.MENUID, menuId);

        return "statistical/todayStatistical"; // 跳转JSP页面
    }

    @RequestMapping("/statisticalHistory")
    public String queryStatisticalHistoryInfo(HttpServletRequest request, Model model) {
        Object menuId = request.getParameter(Define.REQUESTPARAMETER.ID);
        model.addAttribute(Define.REQUESTPARAMETER.MENUID, menuId);
        return "statistical/historyStatistical";// 跳转JSP页面
    }


    @RequestMapping("/daylog")
    public String queryDayLog(HttpServletRequest request, Model model) {
        Object menuId = request.getParameter(Define.REQUESTPARAMETER.ID);
        model.addAttribute(Define.REQUESTPARAMETER.MENUID, menuId);
        return "statistical/dayLog";// 跳转JSP页面
    }


    @RequestMapping("/historylog")
    public String queryHistoryLog(HttpServletRequest request, Model model) {
        Object menuId = request.getParameter(Define.REQUESTPARAMETER.ID);
        model.addAttribute(Define.REQUESTPARAMETER.MENUID, menuId);
        return "statistical/historyLog";// 跳转JSP页面
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/statisticaldayinfo")
    @ResponseBody
    public DzdResponse queryStatisticalDayInfo(HttpServletRequest request,
                                               HttpServletResponse response, @RequestBody Map<String, Object> data) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            DzdPageParam dzdPageParam = new DzdPageParam();

            // 查询数据入参param
            Map<String, Object> param = preParameter(data, request, dzdPageParam);

            String menuId = request.getParameter(Define.REQUESTPARAMETER.MENUID);
            // 2. 查询菜单按钮
            SysUser user = SessionUtils.getUser(request);
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                if (user.getSuperAdmin() == 1) {// 管理员查询所有菜单按钮
                    sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId));
                } else {// 其余角色查询配置好的按钮
                    sysMenuBtns = sysMenuBtnService.queryMenuListByRole(Integer.parseInt(menuId), user.getId());
                }
            }

            // 查询发送信息：账号、数量、通道、归属等信息
            Page<SmsSendLog> statisticalUserInfos = msmSendService
                    .queryStatisticalUserInfo(dzdPageParam);

            if (CollectionUtils.isEmpty(statisticalUserInfos)) {
                return dzdResponse;
            }

            // 补全每个账号发送数量的发送总量、已发送、发送成功、发送失败信息
            for(SmsSendLog SmsSendLog :statisticalUserInfos.getResult()){
            	param.put(Define.REQUESTPARAMETER.SMSUSERID, SmsSendLog.getSmsUserId());
            	SmsSendLog.setSendNum(msmSendService.queryStatisticalSendNum(param));
            }
            Map<String, SmsSendLog> statisticalSucceedNum = msmSendService
                    .queryStatisticalSucceedNum(param);
            Map<String, SmsSendLog> statisticalFailureNum = msmSendService
                    .queryStatisticalFailureNum(param);

            // 补全成功失败总和数据信息
            SmsSendLog totalSmsSendLog = fillTotalNum(statisticalUserInfos, statisticalSucceedNum,
                    statisticalFailureNum);

            statisticalUserInfos.getResult().add(0, totalSmsSendLog);

            dzdResponse.setData(sysMenuBtns);
            if (!CollectionUtils.isEmpty(statisticalUserInfos)) {
                for (SmsSendLog smsSendLog : statisticalUserInfos.getResult()) {
                    smsSendLog.setSysMenuBtns(sysMenuBtns);
                }
                dzdResponse.setRows(statisticalUserInfos.getResult());
                dzdResponse.setTotal(statisticalUserInfos.getTotal());
            }
        } catch (Exception e) {
            logger.error(null, e);
            e.printStackTrace();
        }
        return dzdResponse;
    }

    /**
     * @param statisticalUserInfos
     * @param statisticalSucceedNum
     * @param statisticalFailureNum
     * @return
     * @throws
     * @Title: fillTotalNum
     * @Description: 补全成功失败总和数据信息
     * @author: hz-liang
     * @return: SmsSendLog
     */
    private SmsSendLog fillTotalNum(Page<SmsSendLog> statisticalUserInfos,
                                    Map<String, SmsSendLog> statisticalSucceedNum,
                                    Map<String, SmsSendLog> statisticalFailureNum) {
        SmsSendLog smsSendLogNum = null;
        int totalSendNum = 0;
        int totalSucceedNum = 0;
        int totalFailureNum = 0;
        for (SmsSendLog smsSendLog : statisticalUserInfos) {
            smsSendLogNum = statisticalSucceedNum.get(smsSendLog.getSmsUserId());
            smsSendLog.setSucceedNum(smsSendLogNum != null
                    && smsSendLog.getSmsUserId().equals(smsSendLogNum.getSmsUserId())
                    ? smsSendLogNum.getSucceedNum() : 0);

            smsSendLogNum = statisticalFailureNum.get(smsSendLog.getSmsUserId());
            smsSendLog.setFailureNum(smsSendLogNum != null
                    && smsSendLog.getSmsUserId().equals(smsSendLogNum.getSmsUserId())
                    ? smsSendLogNum.getFailureNum() : 0);

            totalSendNum += smsSendLog.getSendNum();
            totalSucceedNum += smsSendLog.getSucceedNum();
            totalFailureNum += smsSendLog.getFailureNum();
        }

        SmsSendLog totalSmsSendLog = new SmsSendLog();
        totalSmsSendLog.setSendNum(totalSendNum);
        totalSmsSendLog.setSucceedNum(totalSucceedNum);
        totalSmsSendLog.setFailureNum(totalFailureNum);
        return totalSmsSendLog;
    }

    /**
     * @param data
     * @param dzdPageParam
     * @return
     * @throws
     * @Title: preParameter
     * @Description: 查询数据入参param
     * @author: hz-liang
     * @return: Map<String,Object>
     */
    private Map<String, Object> preParameter(Map<String, Object> data, HttpServletRequest request, DzdPageParam dzdPageParam) {
        Map<String, Object> param = new HashMap<String, Object>();

        Object smsUserEmail = data.get(Define.REQUESTPARAMETER.EMAIL);
        Object aisleName = data.get(Define.REQUESTPARAMETER.AISLENAME);
        Object nickName = data.get(Define.REQUESTPARAMETER.NICKNAME);

        SysUser user = SessionUtils.getUser(request);
        if (user.getId() != 1) {
            SmsUser smsUser = SmsServerManager.I.getUserBySysId(Long.valueOf(user.getId()));
            String uid = user.getId().toString();
            if (smsUser != null) {
                uid = smsUser.getId().toString();// 用户id
            }
            if (!StringUtil.isEmpty(uid.toString())) {
                param.put(Define.REQUESTPARAMETER.UID, uid);
            }
        }


        if (smsUserEmail != null && !StringUtil.isEmpty(smsUserEmail.toString())) {
            param.put(Define.REQUESTPARAMETER.SMSUSEREMAIL, smsUserEmail);
        }
        if (aisleName != null && !StringUtil.isEmpty(aisleName.toString())) {
            param.put(Define.REQUESTPARAMETER.AISLENAME, aisleName);
        }
        if (nickName != null && !StringUtil.isEmpty(nickName.toString())) {
            param.put(Define.REQUESTPARAMETER.NICKNAME, nickName);
        }

        dzdPageParam.setCondition(param);
        return param;
    }

    /**
     * copy from MsmSendController make by oygy
     *
     * @Description:统计中心--日志统计
     * @author:oygy
     * @time:2016年12月31日 下午2:01:34
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/log")
    @ResponseBody
    public void log(HttpServletRequest request, HttpServletResponse response,
                    @RequestBody Map<String, Object> data) throws Exception {
        try {
            SmsSendLog smsSendLog = new SmsSendLog();
            JSONObject jsonObject = new JSONObject();

            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class,
                    data);
            if (parameters == null) {
                HtmlUtil.writerJson(response, jsonObject);
            }

            // URL入参数据构造
            TempParameter tempParameter = msmSendBusiness.preTemParameter(request, data);

            if (tempParameter.getMenuId() == null) {
                HtmlUtil.writerJson(response, jsonObject);
            }

            // 构建查询入参数据
            msmSendBusiness.preParameter(request, tempParameter, smsSendLog, parameters);
            // 构建页码入参数据
            msmSendBusiness.prePageParameter(tempParameter, smsSendLog);

            // 相关按钮数据
            SysUser user = SessionUtils.getUser(request);
            List<SysMenuBtn> sysMenuBtns = null;
            String menuId = tempParameter.getMenuId().toString();
            if (menuId != null) {
                if (user.getSuperAdmin() == 1) {// 管理员查询所有菜单按钮
                    sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId));
                } else {// 其余角色查询配置好的按钮
                    sysMenuBtns = sysMenuBtnService.queryMenuListByRole(Integer.parseInt(menuId), user.getId());
                }
            }

            // 查询发送日志信息
            List<SmsSendLog> dataList = msmSendService.queryByList(smsSendLog);

            jsonObject.put(Define.RESULTSTATE.BTN, sysMenuBtns);

            if (CollectionUtils.isEmpty(dataList)) {
                HtmlUtil.writerJson(response, jsonObject);
            }

            // 构造出参数据
            msmSendBusiness.constructResult(msmSendService, response, tempParameter.getLogTime(), smsSendLog, jsonObject,
                    sysMenuBtns, dataList);

        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }

    }

    /**
     * copy from MsmSendController make by oygy
     *
     * @Description:
     * @author:oygy
     * @time:2016年12月31日 下午2:01:34
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/statisticalList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse puserStatisticalList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");
            Object logTime = request.getParameter("logTime");
            Object email = data.get("email");
            Object aisleName = data.get("aisleName");
            Object date_export = data.get("date_export");
            Object nickName = data.get(Define.REQUESTPARAMETER.NICKNAME);

            if (menuId == null) {
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
            
            if (logTime != null && !StringUtil.isEmpty(logTime.toString())) {
            	sortMap.put("logTime", logTime.toString());
            }

            if (aisleName != null && !StringUtil.isEmpty(aisleName.toString())) {
                sortMap.put("aisleName", aisleName.toString());
            }
            if (nickName != null && !StringUtil.isEmpty(nickName.toString())) {
            	sortMap.put("nickName", nickName.toString());
            }

            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            if (user.getId() != 1) {
                com.dzd.sms.service.data.SmsUser smsUserDate = SmsServerManager.I
                        .getUserBySysId(Long.valueOf(user.getId()));
                String uid = user.getId().toString();
                sortMap.put("sid", uid);
                if (smsUserDate != null) {
                    uid = smsUserDate.getId().toString();// 用户id
                }
                if (!StringUtil.isEmpty(uid)) {
                    sortMap.put("uid", uid);
                }
            }

            //排序
            if (parameters.getSort() != null && parameters.getOrder() != null) {
                sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
            }
            dzdPageParam.setCondition(sortMap);
            
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                if (user.getSuperAdmin() == 1) {// 管理员查询所有菜单按钮
                    sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
                } else {// 其余角色查询配置好的按钮
                    sysMenuBtns = sysMenuBtnService.queryMenuListByRole(Integer.parseInt(menuId.toString()), user.getId());
                }
            }
            dzdPageResponse.setData(sysMenuBtns);

            if (user != null && user.getId() != 1) {
                sortMap.put("smsUserVal", "select id from sys_user where id=" + user.getId() + " or superiorId = " + user.getId() + "");
            }

            // 查询代理统计信息列表
            Page<com.dzd.phonebook.util.SmsUser> dataList = smsUserService.querySmsUserStatisticalList(dzdPageParam);

            if (CollectionUtils.isEmpty(dataList)) {
                return dzdPageResponse;
            }

            // 查询列表头信息
            com.dzd.phonebook.util.SmsUser smsUser = smsUserService.querySmsUserStatisticalZong(dzdPageParam);
            // 设置列表头信息，使用同一个对象返回

            dzdPageResponse.setData(smsUser);

            Map<String, Integer> totalNumMap = new HashMap<String, Integer>();
            for (com.dzd.phonebook.util.SmsUser smsUserInfo : dataList) {
                smsUserInfo.setUnknownFailureNum(smsUserInfo.getUnknownNumMs()
                        + smsUserInfo.getUnknownNumTs() + smsUserInfo.getUnknownNumUs());

                smsUserInfo.setUnknownSucceedNum(0);

                smsUserInfo.setSucceedNum(smsUserInfo.getSucceedNumMs()
                        + smsUserInfo.getSucceedNumTs() + smsUserInfo.getSucceedNumUs());

                smsUserInfo.setFailureNum(smsUserInfo.getFailureNumMs()
                        + smsUserInfo.getFailureNumTs() + smsUserInfo.getFailureNumUs());

                msmSendBusiness.setTotalNumMap(totalNumMap, smsUserInfo);
            }

            msmSendBusiness.setTotalSmsUser(dataList, totalNumMap);

            ComparatorSmsUser.comparatorListBySucceedNum(dataList);
            ComparatorSmsUser.comparatorListByAuditTime(dataList);

/*			if ( dataList.size() > 0 )
		{
			dataList.get(0).setSumSendNum(smsUser.getSumSendNum());
			dataList.get(0).setSumBillingNum(smsUser.getSumBillingNum());
			dataList.get(0).setSumActualNum(smsUser.getSumActualNum());

			dataList.get(0).setSumFailureNumUs(smsUser.getSumFailureNumUs());
			dataList.get(0).setSumSucceedNumUs(smsUser.getSumSucceedNumUs());
			dataList.get(0).setSumUnknownNumUs(smsUser.getSumUnknownNumUs());

			dataList.get(0).setSumFailureNumMs(smsUser.getSumFailureNumMs());
			dataList.get(0).setSumSucceedNumMs(smsUser.getSumSucceedNumMs());
			dataList.get(0).setSumUnknownNumMs(smsUser.getSumUnknownNumMs());

			dataList.get(0).setSumFailureNumTs(smsUser.getSumFailureNumTs());
			dataList.get(0).setSumSucceedNumTs(smsUser.getSumSucceedNumTs());
			dataList.get(0).setSumUnknownNumTs(smsUser.getSumUnknownNumTs());
		}*/


            if (!CollectionUtils.isEmpty(dataList)) {
                for (com.dzd.phonebook.util.SmsUser instruct : dataList.getResult()) {
                    instruct.setSysMenuBtns(sysMenuBtns);
                }
                dzdPageResponse.setRows(dataList.getResult());
                dzdPageResponse.setTotal(dataList.getTotal());

                if (date_export.equals("date_export")) {
                    this.setDataList(dataList);
                }
            }
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

}
