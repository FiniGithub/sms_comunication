package com.dzd.phonebook.controller.pay;

import javax.servlet.http.HttpServletRequest;


import com.alibaba.fastjson.JSONArray;
import com.dzd.base.util.SessionUtils;
import com.dzd.phonebook.entity.SmsRechargeOrder;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.*;
import com.dzd.phonebook.util.*;
import com.dzd.sms.flow.engine.FlowEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/smsUserPay")
public class PayController {
    public static final Logger logger = LoggerFactory.getLogger(PayController.class);
    @Autowired
    private SmsUserService smsUserService;
    @Autowired
    private SmsRechargeOrderService smsRechargeOrderService;
    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private SmsAisleGroupService smsAisleGroupService;
    @Autowired
    private SysRoleRelService sysRoleRelService;

    @RequestMapping("/payIndex")
    public String paylist(HttpServletRequest request, Model model) {
        SysUser sysUser = SessionUtils.getUser(request);
        SmsUser smsUser = userMessageService.querySmsUserById(sysUser.getId());
        Integer superAdmin = sysUser.getSuperAdmin();// 是否为管理员
        // 用户集合
        List<SmsUser> smsUserList = userMessageService.queryRechargeSmsUserList(smsUser.getId(), superAdmin);
        net.sf.json.JSONArray bankArray = RechargeVariable.initCardType();// 银行集合

        // 用户角色id
        List<SysRoleRel> sysRoleRels = queryRoleByUserId(sysUser.getId());
        if (sysRoleRels != null && sysRoleRels.size() > 0) {
            model.addAttribute("roleId", sysRoleRels.get(0).getRoleId());
        }


        model.addAttribute("smsUserList", smsUserList);
        model.addAttribute("isShow", smsUser.getNetworkChargingState());
        model.addAttribute("bankArray", bankArray);
        return "/app/pay/pay_index";
    }

    /**
     * 网上充值
     *
     * @return
     */
    @RequestMapping("/pay")
    public String saveRechargeOrder(HttpServletRequest request) {
        try {
            // 1. 基本参数设置
            logger.info("saveRechargeOrder start...");
            Object money = request.getParameter("money");// 充值套餐
            Object smsNumber = request.getParameter("smsNumber");// 短信条数
            Object smsUserId = request.getParameter("smsUserId");// 充值用户id
            Object bankCode = request.getParameter("bankId");// 银行ID
            if (money == null || bankCode == null || smsNumber == null) {
                return "/app/pay/error";
            }
            Integer smsNum = Integer.parseInt(smsNumber.toString());
            SysUser sysUser = SessionUtils.getUser(request);
            Integer uid = 0;
            String account = "";
            if (smsUserId == null || smsUserId.equals("") || smsUserId.equals(" ")) {
                SmsUser smsUser = userMessageService.querySmsUserById(sysUser.getId());
                uid = smsUser.getId();
                account = smsUser.getEmail();
            } else {
                uid = Integer.parseInt(smsUserId.toString());
                account = sysUser.getEmail();
            }



            // 2. 获取通道对应的价格区间,并计算短信价格
            SmsAisleGroup smsAisleGroup = smsAisleGroupService.querySmsAisleGroupByIdByPay(account, request);
            Float smsMoney = CalculateMoneyUtil.calculateSmsNumMoney(smsNum, smsAisleGroup);
            if (smsMoney == 0) {
                return "/app/pay/error";
            }

            // 3. 充值记录入库
            SmsRechargeOrder smsRechargeOrder = new SmsRechargeOrder();
            smsRechargeOrder.setOrderNo(RechargeVariable.getOrderNumber(uid));
            smsRechargeOrder.setUserId(Integer.parseInt(sysUser.getId().toString()));
            smsRechargeOrder.setSmsUserId(uid);
            smsRechargeOrder.setMoney(smsMoney);
            smsRechargeOrder.setSmsNumber(smsNum);
            smsRechargeOrder.setStatus(RechargeVariable.RECHARGE_ORDER_STATUS_UNPOST);
            smsRechargeOrderService.insertSmsRechargeOrder(smsRechargeOrder);


            // 4. 页面回传值,跳转银行支付
            String orderid = smsRechargeOrder.getOrderNo();
            String amount = smsRechargeOrder.getMoney() * 10 * 10 + "";// 将元转换为分
            String bankid = bankCode.toString();

            request.setAttribute("orderId", orderid);
            request.setAttribute("orderAmount", amount);
            request.setAttribute("bankId", bankid);
            logger.info("saveRechargeOrder end");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error on saveRechargeOrder", e);
        }
        return "/app/pay/send";
    }


    /**
     * 获取用户价格段位
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/getSmsUserRechargeMoney")
    @ResponseBody
    public DzdResponse getSmsUserRechargeMoney(HttpServletRequest request) throws Exception {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            String account = request.getParameter("account");
            // 获取通道对应的价格区间
            SmsAisleGroup smsAisleGroup = smsAisleGroupService.querySmsAisleGroupByIdByPay(account, request);
            dzdResponse.setData(smsAisleGroup);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            e.printStackTrace();
            dzdResponse.setRetMsg(RechargeVariable.RECHARGE_ORDER_SYS_MSG);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        }
        return dzdResponse;
    }

    /**
     * 获取短信的价格
     *
     * @param request
     * @return
     */
    @RequestMapping("/getMoneyBySmsNum")
    @ResponseBody
    public DzdResponse getMoneyBySmsNum(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            Object smsNum = request.getParameter("smsNum");
            String uid = request.getParameter("uid");
            if (smsNum == null) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }
            // 获取通道对应的价格区间
            SmsAisleGroup smsAisleGroup = smsAisleGroupService.querySmsAisleGroupByIdByPay(uid, request);
            Float money = CalculateMoneyUtil.calculateSmsNumMoney(Integer.parseInt(smsNum.toString()), smsAisleGroup);
            dzdResponse.setData(money);
        } catch (Exception e) {
            e.printStackTrace();
            dzdResponse.setRetMsg(RechargeVariable.RECHARGE_ORDER_SYS_MSG);
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
