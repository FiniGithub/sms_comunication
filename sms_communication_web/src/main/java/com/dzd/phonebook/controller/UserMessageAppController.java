package com.dzd.phonebook.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.service.*;
import com.dzd.phonebook.util.*;
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

import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.SmsUserMessage;
import com.dzd.phonebook.entity.SysUser;
import com.github.pagehelper.Page;

/**
 * 统计Controller
 *
 * @author CHENCHAO
 * @date 2017-04-05 20:29:00
 */
@Controller
@RequestMapping("/smsUser")
public class UserMessageAppController<T> extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(UserMessageController.class);
    @SuppressWarnings("rawtypes")
    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private SmsUserService<T> smsUserService;
    @Autowired
    private SysMenuBtnService sysMenuBtnService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SmsUserMessageService<T> smsUserMessageService;
    @Autowired
    private SysRoleRelService sysRoleRelService;
    @Autowired
    private SmsAccountService smsAccountService;

    @RequestMapping("/msglist")
    public String msgList() {
        return "app/message";
    }

    @RequestMapping("/statisticalList")
    public String statistical() {
        return "app/userSendMassSms/fstj";
    }

    /**
     * 充值记录
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/puserBill")
    public String puserBill(HttpServletRequest request, Model model) throws Exception {
        // 获取菜单id
        Object menuId = request.getParameter("id");
        if (menuId == null) {
            return null;
        }

        String typeId = request.getParameter("typeId");
        if (typeId != null) {
            model.addAttribute("typeId", typeId);
        }
        // 查询归属
        CommonRoleServiceUtil commonRoleService = new CommonRoleServiceUtil();
        List<SysUser> sysUserList = commonRoleService.getSysUserBUserList(request, smsUserService, sysRoleRelService, smsAccountService);

        // 用户角色id
        SysUser sysUser = SessionUtils.getUser(request);
        List<SysRoleRel> sysRoleRels = queryRoleByUserId(sysUser.getId());
        if (sysRoleRels != null && sysRoleRels.size() > 0) {
            model.addAttribute("roleId", sysRoleRels.get(0).getRoleId());
        }
        model.addAttribute("sysUserList", sysUserList);
        model.addAttribute("menuId", menuId);
        return "app/userSendMassSms/czjl";
    }

    /**
     * 消费记录
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/puserConsume")
    public String puserConsume(HttpServletRequest request, Model model) throws Exception {
        // 获取菜单id
        Object menuId = request.getParameter("id");
        if (menuId == null) {
            return null;
        }
        String typeId = request.getParameter("typeId");
        if (typeId != null) {
            model.addAttribute("typeId", typeId);
        }
        model.addAttribute("menuId", menuId);
        return "app/userSendMassSms/xfjl";
    }

    /**
     * 查询代理统计信息列表
     */
    @RequestMapping(value = "/puserStatisticalList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse puserStatisticalList(HttpServletRequest request, HttpServletResponse response,
                                            @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }

            SysUser user = SessionUtils.getUser(request);
            SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());

            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }

            if (smsUsers.getEmail() != null && !StringUtil.isEmpty(smsUsers.getEmail())) {
                sortMap.put("email2", smsUsers.getEmail());
            }

            if (!StringUtil.isEmpty(parameters.getStartInput())) {
                sortMap.put("startInput", parameters.getStartInput());
                sortMap.put("endInput", parameters.getEndInput());
            }

            // 排序
/*
            if (parameters.getSort() != null && parameters.getOrder() != null) {
				sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
			} else {
				sortMap.put("sortVal", "order by auditTime desc");
			}*/
            dzdPageParam.setCondition(sortMap);


            Page<SmsUser> dataList = smsUserService.querySmsUserDLPage(dzdPageParam);
            // 统计
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
                dzdPageResponse.setRows(dataList.getResult());
                dzdPageResponse.setTotal(dataList.getTotal());
            }
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    /**
     * 充值记录
     */
    @RequestMapping(value = "/puserBillList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse puserBillList(HttpServletRequest request, HttpServletResponse response,
                                     @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            // 1. 基本参数设置
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");
            Object roleId = request.getParameter("roleId");
            Object email = data.get("email");
            Object type = data.get("type");
            Object bid = data.get("bid");
            if (menuId == null) {
                return dzdPageResponse;
            }

            SysUser sysUser = SessionUtils.getUser(request);
            SmsUser smsUser = userMessageService.querySmsUserById(sysUser.getId());

            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }


            sortMap.put("roleId", roleId);
            sortMap.put("superAdmin", sysUser.getSuperAdmin());

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
            if (bid != null && !StringUtil.isEmpty(bid.toString())) {
                sortMap.put("smsUserVal", Integer.parseInt(bid.toString()));
            }
            if (smsUser.getId() != null && !StringUtil.isEmpty(smsUser.getId().toString())) {
                sortMap.put("smsUserId", smsUser.getId());
            }
            sortMap.put("sysUserId", sysUser.getId());

            // 排序
            if (parameters.getSort() != null && parameters.getOrder() != null) {
                sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
            } else {
                sortMap.put("sortVal", "order by createTime desc");
            }
            dzdPageParam.setCondition(sortMap);


            // 2. 查询菜单按钮
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                if (sysUser.getSuperAdmin() == 1) {// 管理员查询所有菜单按钮
                    sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
                } else {// 其余角色查询配置好的按钮
                    sysMenuBtns = sysMenuBtnService.queryMenuListByRole(Integer.parseInt(menuId.toString()), sysUser.getId());
                }
            }

            // 3. 查询集合数据
            Page<SmsUserMoneyRunning> dataList = smsUserService.querySmsUserPuserBillList(dzdPageParam);
            dzdPageResponse.setData(sysMenuBtns);
            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsUserMoneyRunning smsSendTask : dataList.getResult()) {
                    smsSendTask.setSysMenuBtns(sysMenuBtns);
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


    /**
     * 查询消费记录
     */
    @RequestMapping(value = "/puserConsumeBillList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse puserConsumeBillList(HttpServletRequest request, @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            // 1. 基本参数设置
            Object menuId = request.getParameter("menuId");
            if (menuId == null) {
                return dzdPageResponse;
            }

            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object email = data.get("email");
            SysUser sysUser = SessionUtils.getUser(request);
            SmsUser smsUser = userMessageService.querySmsUserById(sysUser.getId());

            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }
            sortMap.put("superAdmin", sysUser.getSuperAdmin());

            if (!StringUtil.isEmpty(parameters.getStartInput())) {
                sortMap.put("startInput", parameters.getStartInput());
                sortMap.put("endInput", parameters.getEndInput());
            }

            if (email != null && !StringUtil.isEmpty(email.toString())) {
                sortMap.put("email", email.toString());
            }
            if (smsUser.getId() != null && !StringUtil.isEmpty(smsUser.getId().toString())) {
                sortMap.put("smsUserId", smsUser.getId());
            }

            // 排序
            if (parameters.getSort() != null && parameters.getOrder() != null) {
                sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
            } else {
                sortMap.put("sortVal", "order by createTime desc");
            }
            dzdPageParam.setCondition(sortMap);

            // 2. 查询菜单按钮
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                if (sysUser.getSuperAdmin() == 1) {// 管理员查询所有菜单按钮
                    sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
                } else {// 其余角色查询配置好的按钮
                    sysMenuBtns = sysMenuBtnService.queryMenuListByRole(Integer.parseInt(menuId.toString()), sysUser.getId());
                }
            }

            // 3. 查询集合数据
            Page<SmsUserMoneyRunning> dataList = smsUserService.querySmsUserPuserConsumeBillListPage(dzdPageParam);
            dzdPageResponse.setData(sysMenuBtns);
            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsUserMoneyRunning running : dataList.getResult()) {
                    running.setSysMenuBtns(sysMenuBtns);
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

    /**
     * 统计账单流水发送量
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/billStatistical")
    @ResponseBody
    public DzdResponse listCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            SysUser user = SessionUtils.getUser(request);
            SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());
            Integer uid = smsUsers.getId();// 用户id
            SmsUserMoneyRunning running = smsUserService.querySmsUserMoneyRunningStatistical(uid);
            dzdResponse.setData(running);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            e.printStackTrace();
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        }
        return dzdResponse;
    }

    /**
     * 查询推送消息
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/messageList")
    @ResponseBody
    public DzdResponse messageList(HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);

            // 1. 用户信息
            SysUser user = SessionUtils.getUser(request);
            SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());
            String smsUserEmail = smsUsers.getEmail();// 邮箱
            Integer groupTypeId = smsUsers.getGroupTypeId();// 类型

            // 2. 分页参数
            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }
            if (smsUserEmail != null && !StringUtil.isEmpty(smsUserEmail.toString())) {
                sortMap.put("smsUserEmail", smsUserEmail);
            }
            if (groupTypeId != null && !StringUtil.isEmpty(groupTypeId.toString())) {
                sortMap.put("smsUserTypeId", groupTypeId);
            }
            sortMap.put("pushIndex", 0);// 0 不推送,1 推送

            dzdPageParam.setCondition(sortMap);

            // 3. 查询推送的消息
            Page<SmsUserMessage> dataList = smsUserMessageService.queryMessagePage(dzdPageParam);

            // 4.设置页面数据
            if (!CollectionUtils.isEmpty(dataList)) {
                dzdPageResponse.setRows(dataList.getResult());
                dzdPageResponse.setTotal(dataList.getTotal());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    /**
     * 查询推送到首页的消息
     *
     * @param request
     * @return
     */
    @RequestMapping("/queryMsgForIndex")
    @ResponseBody
    public DzdResponse queryMsgIndex(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            // 1. 用户信息
            SysUser user = SessionUtils.getUser(request);
            SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());
            String smsUserEmail = smsUsers.getEmail();// 邮箱
            Integer groupTypeId = smsUsers.getGroupTypeId();// 类型

            SmsUserMessage message = smsUserMessageService.queryMsgForIndex(smsUserEmail, groupTypeId);
            dzdResponse.setData(message);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            e.printStackTrace();
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        }
        return dzdResponse;
    }

    /**
     * 新增、修改消息
     */
    @MethodDescription("操作消息")
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/from/messageMerge", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DzdResponse groupmerge(HttpServletRequest request, HttpServletResponse response,
                                  @RequestBody Map<String, Object> data) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            // 1. 接收参数
            Object id = data.get("id");
            Object title = data.get("title");
            Object content = data.get("content");
            Object state = data.get("state");

            // 2. 赋值
            SmsUserMessage message = new SmsUserMessage();
            if (id != null && !id.toString().isEmpty()) {
                message.setId(Integer.parseInt(id.toString()));
            }
            if (title != null && !title.toString().isEmpty()) {
                message.setTitle(title.toString());
            }
            if (content != null && !content.toString().isEmpty()) {
                message.setContent(content.toString());
            }
            if (state != null && !state.toString().isEmpty()) {
                message.setState(Integer.parseInt(state.toString()));
            }

            // 3. 执行操作
            if (message.getId() == null) {
                smsUserMessageService.add((T) message);// 新增
            } else {
                smsUserMessageService.updateBySelective((T) message); // 修改
            }

            // 查询未读消息
            SysUser user = SessionUtils.getUser(request);
            SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());
            Integer gid = smsUsers.getGroupTypeId();
            Integer msgCount = smsUserMessageService.queryMessageCount(smsUsers.getEmail(), gid);
            SessionUtils.setMessageCount(request, msgCount);
            dzdResponse.setData(msgCount);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        }
        return dzdResponse;
    }

    /**
     * @Description:根据用户ID查询拥有角色
     * @author:wangran
     * @time:2017年5月19日 上午11:12:53
     */
    private List<SysRoleRel> queryRoleByUserId(Integer uid) {
        DzdPageParam dzdPageParam = new DzdPageParam();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("sysUserId", uid);
        dzdPageParam.setCondition(condition);
        List<SysRoleRel> sysRoleRels = sysRoleRelService.queryRoleByUserId(dzdPageParam);
        return sysRoleRels;
    }

}
