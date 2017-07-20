package com.dzd.phonebook.controller;

import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.SmsRechargeOrder;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.*;
import com.dzd.phonebook.util.*;
import com.dzd.sms.application.Define;
import com.github.pagehelper.Page;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 账户中心---短信账户
 * Created by wangran on 2017/5/18.
 */
@Controller
@RequestMapping("/account")
public class AccountController extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private SysMenuBtnService sysMenuBtnService;

    @Autowired
    private SmsUserService smsUserService;

    @Autowired
    private SmsAccountService smsAccountService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private SysRoleRelService sysRoleRelService;

    @Autowired
    private SmsRechargeOrderService smsRechargeOrderService;


    @RequestMapping("/listview")
    public String list(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);

        SysUser user = SessionUtils.getUser(request);
        // 查询归属
        CommonRoleServiceUtil commonRoleService = new CommonRoleServiceUtil();
        List<SysUser> sysUserList = commonRoleService.getSysUserBUserList(request, smsUserService, sysRoleRelService, smsAccountService);
        List<SmsAisleGroupType> list = channelService.querySmsAisleGroupType();

        if (user.getSuperAdmin() != 1) {
            List<SysRoleRel> sysRoleRels = queryRoleByUserId(user.getId());
            model.addAttribute("roleType", sysRoleRels.get(0).getRoleId());
        }
        model.addAttribute("sysUser", user);
        model.addAttribute("sysUserList", sysUserList);
        model.addAttribute("typeList", list);
        return "account/account";
    }

    /**
     * @Description:短信账户列表
     * @author:oygy
     * @time:2016年12月31日 下午2:01:34
     */
    @RequestMapping(value = "/accountList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse accountList(HttpServletRequest request, @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");  //菜单id
            Object state = data.get("state");  //用户状态0：启用  1：禁用
            Object aisleType = data.get("aisleType");  //通道类型
            Object email = data.get("email"); //账号
            Object nickName = data.get("nickName"); //归属

            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            List<SysRoleRel> sysRoleRels = queryRoleByUserId(user.getId());

            if (menuId == null || user == null || sysRoleRels == null) {
                return dzdPageResponse;
            }
            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }
            if (state != null && !StringUtil.isEmpty(state.toString())) {
                sortMap.put("state", state.toString());
            }
            if (aisleType != null && !StringUtil.isEmpty(aisleType.toString())) {
                sortMap.put("aisleType", aisleType.toString());
            }
            if (email != null && !StringUtil.isEmpty(email.toString())) {
                sortMap.put("email", email.toString());
            }
            if (nickName != null && !StringUtil.isEmpty(nickName.toString())) {
                sortMap.put("nickName", nickName.toString());
            }
            //登陆用户不是超级管理员
            if (user != null && user.getSuperAdmin() != 1) {
                List<Integer> list = new ArrayList<Integer>();
                list.add(user.getId());
                for (SysRoleRel sysRoleRel : sysRoleRels) {
                    //如果当前登录用户为  "客服" 权限与“一级管理员”平级，即可查看一级管理员账户下所有用户的短信账户（并可充值）和充值记录。
                    if (sysRoleRel.getRoleId() == Integer.parseInt(Define.ROLEID.ROLE_CUSTOMER_SERVICE)) {
                        //查询所有为角色为“一级管理员”的用户
                        List<SysRoleRel> roleRels = sysRoleRelService.queryByRoleId(Integer.parseInt(Define.ROLEID.ROLE_FIRST_LEVEL_ADMINISTRATOR), 1);
                        for (int i = 0; i < roleRels.size(); i++) {
                            //所有为“一级管理员”的角色的用户id
                            list.add(roleRels.get(i).getObjId());
                        }
                        //所有子归属
                        // sortMap.put("smsUserVal", "select sys_user_id from sms_user where sys_user_id="+user.getId()+" or bId = "+user.getId()+"");
                        list.addAll(new CommonRoleServiceUtil().getIds(list, smsAccountService, Integer.parseInt(Define.ROLEID.ROLE_CUSTOMER_SERVICE)));
                        sortMap.put("smsUserVal", list);
                    } else {
                        //其他登陆用户下面的所有 归属 用户id
                        // sortMap.put("smsUserVal", "select sys_user_id from sms_user where sys_user_id="+user.getId()+" or bId = "+user.getId()+"");
                        list.addAll(new CommonRoleServiceUtil().getIds(list, smsAccountService, 0));
                        sortMap.put("smsUserVal", list);
                    }
                }
            }
            //排序
            if (parameters.getSort() != null && parameters.getOrder() != null) {
                sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
            } else {
                sortMap.put("sortVal", "order by surplusNum asc,usedNum asc");
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

            Page<SmsUser> dataList = smsAccountService.querySmsAccountUserListPage(dzdPageParam);

            dzdPageResponse.setData(sysMenuBtns);
            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsUser instruct : dataList.getResult()) {
                    instruct.setSysMenuBtns(sysMenuBtns);
                }
                dzdPageResponse.setRows(dataList.getResult());
                dzdPageResponse.setTotal(dataList.getTotal());
            }
        } catch (Exception e) {
            logger.error("====================》短信账户列表查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    /**
     * 用户添加短信条数
     *
     * @param request
     * @param
     * @return
     */
    @MethodDescription("用户添加短信条数")
    @RequestMapping(value = "/from/moneyMerge", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse moneyMerge(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            Object id = request.getParameter("czid");     //获取充值账号ID
            Object czmoney = request.getParameter("czmoney"); //充值短信条数
            Object type = request.getParameter("type");      //充值类型
            Object money = request.getParameter("money"); //充值金额
            Object sysUserId = request.getParameter("sysUserId"); //被操作用户id

            //点单号
            String order = RechargeVariable.getOrderNumber(Integer.parseInt(sysUserId.toString()));

            //充值记录
            SmsRechargeOrder smsRechargeOrder = new SmsRechargeOrder();

            if (money != null && !StringUtil.isEmpty(money.toString())) {
                smsRechargeOrder.setMoney(Float.valueOf(money.toString()));
            }
            SmsUser smsUser = new SmsUser();
            Integer saList = smsUserService.querySurplusNum(Integer.parseInt(id.toString()));
            Integer czmoneyValue = Integer.parseInt(czmoney.toString());
            Date createTime = new Date();
            smsUser.setId(Integer.parseInt(id.toString()));
            if (type.equals("8")) {
                //核减：原账户剩余短信中扣减
                smsUser.setSurplusNum(-czmoneyValue);
            } else {
                smsUser.setSurplusNum(czmoneyValue);
            }
            smsUser.setCreateTime(createTime);
            smsUserService.updateSmsUserBlankMoney(smsUser);


            log.info("-----------------》用户添加短信条数");
            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            SmsUserMoneyRunning smr = new SmsUserMoneyRunning();
            smr.setSmsUserId(Integer.parseInt(id.toString()));
            smr.setUid(user.getId());
            smr.setBeforeNum(saList);   //操作前条数
            smr.setType(Integer.parseInt(type.toString()));
            if (type.equals("8")) {
                //核减：原账户剩余短信中扣减
                smr.setAfterNum(saList - czmoneyValue);   //操作后条数
                smr.setOperateNum(-czmoneyValue);                //操作条数
            } else {
                smr.setAfterNum(saList + czmoneyValue);   //操作后条数
                smr.setOperateNum(czmoneyValue);                //操作条数
            }
            smr.setCreateTime(createTime);          //操作时间

            smr.setOrderNo(order);
            smr.setComment(ErrorCodeTemplate.MSG_MANUAL_ADDITION);

            smsUserService.saveSmsUserMoneyRunning(smr);

            //添加到充值流水表
            smsRechargeOrder.setUserId(user.getId());
            smsRechargeOrder.setOrderNo(order);
            smsRechargeOrder.setSmsUserId(Integer.parseInt(sysUserId.toString()));
            smsRechargeOrder.setSmsNumber(Integer.parseInt(czmoney.toString()));
            smsRechargeOrder.setStatus(1);
            smsRechargeOrderService.insertSmsRechargeOrder(smsRechargeOrder);

            String keys = smsUserService.querySmsUserKey(smsUser.getId());
            //发送动作指令到redis
            instructSend(InstructState.USERTOPUP_SUCESS, keys, smsUser.getId());

            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_SUCCESS);
        } catch (Exception e) {
            logger.error("====================》用户添加短信失败：" + e.getMessage());
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_FAIL);
            e.printStackTrace();
        }
        return dzdResponse;
    }

    /**
     * @Description:修改用户状态
     * @author:wangran
     * @time:2017年5月18日 上午16:38:29
     */
    @MethodDescription("修改用户状态")
    @RequestMapping(value = "/editState", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DzdResponse merge(HttpServletRequest request, @RequestBody Map<String, Object> data) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            Object id = data.get("id");
            Object state = data.get("state");

            SmsUser smsUser = new SmsUser();
            smsUser.setId(Integer.parseInt(id.toString()));
            smsUser.setState(Integer.parseInt(state.toString()));

            //修改用户状态
            smsUserService.updateUser(smsUser);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);

        } catch (Exception e) {
            logger.error("====================》用户状态修改失败：" + e.getMessage());
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
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
