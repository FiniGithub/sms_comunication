package com.dzd.phonebook.controller;

import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.*;
import com.dzd.phonebook.service.*;
import com.dzd.phonebook.util.*;
import com.dzd.sms.application.Define;
import com.github.pagehelper.Page;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 操作日志
 * Created by wangran on 2017/6/02.
 */
@Controller
@RequestMapping("/log")
public class SysOperationLogController extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(SysOperationLogController.class);

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private SysRoleRelService sysRoleRelService;

    @Autowired
    private SmsAccountService smsAccountService;

    @RequestMapping("/listview")
    public String list(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        return "log/log";
    }

    /**
     * @Description:操作日志列表
     * @author:wangran
     * @time:2017年6月2日
     */
    @RequestMapping(value = "/logList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse logList(HttpServletRequest request, @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        DzdPageParam dzdPageParam = new DzdPageParam();
        Map<String, Object> sortMap = new HashMap<String, Object>();
        SysUser user = SessionUtils.getUser(request);
        List<SysRoleRel> sysRoleRels = queryRoleByUserId(user.getId());
        WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
        if (parameters == null) {
            return dzdPageResponse;
        }
        try {
            Object email = data.get("email");
            if (email != null && !StringUtil.isEmpty(email.toString())) {
                sortMap.put("userName", email);
            }
            if (!StringUtil.isEmpty(parameters.getStartInput())) {
                sortMap.put("startInput", parameters.getStartInput());
                sortMap.put("endInput", parameters.getEndInput());
            }
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }

            //如果登陆用户不是超级管理员
            if (user != null && user.getSuperAdmin() != 1) {
                List<Integer> list = new ArrayList<Integer>();
                list.add(user.getId());
                //查询登录用户下面的所有归属用户sys_user_id
                list.addAll(new CommonRoleServiceUtil().getIds(list, smsAccountService, 0));
                sortMap.put("smsUserVal", list);
            }

            sortMap.put("sortVal", "order by createTime desc");
            dzdPageParam.setCondition(sortMap);

            Page<SysLog> dataList = sysLogService.querySysLogPage(dzdPageParam);
            if (!CollectionUtils.isEmpty(dataList)) {
                dzdPageResponse.setRows(dataList.getResult());
                dzdPageResponse.setTotal(dataList.getTotal());
            }
        } catch (Exception e) {
            logger.error("====================》操作日志查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    /**
     * @Description:根据用户ID查询拥有角色
     * @author:oygy
     * @time:2016年12月31日 上午11:12:53
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
