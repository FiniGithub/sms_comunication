package com.dzd.phonebook.controller.pay;

import com.dzd.phonebook.entity.SmsRechargeOrder;
import com.dzd.phonebook.entity.SmsUserBlank;
import com.dzd.phonebook.service.Instruct;
import com.dzd.phonebook.service.SmsRechargeOrderService;
import com.dzd.phonebook.service.SmsUserBlankService;
import com.dzd.phonebook.service.SmsUserService;
import com.dzd.phonebook.util.InstructState;
import com.dzd.phonebook.util.RechargeVariable;
import com.dzd.phonebook.util.RedisUtil;
import com.dzd.phonebook.util.SmsUserMoneyRunning;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 支付成功，回调
 * Created by CHENCHAO on 2017/5/24.
 */
@RequestMapping("/pay")
@Controller
public class PayCallbacksController {
    @Autowired
    private SmsRechargeOrderService smsRechargeOrderService;
    @Autowired
    private SmsUserBlankService smsUserBlankService;
    @Autowired
    private SmsUserService smsUserService;

    /**
     * 回调地址
     * @param request
     * @return
     */
    @RequestMapping("/payCallBacks")
    public String callbacks(HttpServletRequest request) {
        String status = request.getParameter("status");
        String orderNo = request.getParameter("orderNo");
        String amount = request.getParameter("amount");
        try {
            // 1.根据订单号查询订单
            SmsRechargeOrder order = smsRechargeOrderService.queryRechargeOrderByOrderId(orderNo);
            if (null == order) {
                System.out.println("无此订单,订单号为:" + orderNo);
                return "notFound";
            } else {
                Integer orderStatus = order.getStatus();
                // 2.如果订单已经支付成功,则直接返回
                if (RechargeVariable.RECHARGE_ORDER_STATUS_SUCCESS == orderStatus)
                    return null;

                // 3.修改订单状态
                order.setStatus(RechargeVariable.RECHARGE_ORDER_STATUS_SUCCESS);
                order.setUpdated(new Date());
                smsRechargeOrderService.updateSmsRechargeOrder(order);

                order = smsRechargeOrderService.queryRechargeOrderByOrderId(orderNo);
                if (order.getStatus() == RechargeVariable.RECHARGE_ORDER_STATUS_SUCCESS) {
                    // 4. 修改用户余额
                    Integer uid = order.getSmsUserId();
                    SmsUserBlank smsUserBlank = smsUserBlankService.queryUserBlank(uid);
                    Integer surplusNum = smsUserBlank.getSurplusNum();// 充值前条数
                    Integer orderSmsNum = order.getSmsNumber();// 充值条数
                    Integer afterSmsNum = surplusNum + orderSmsNum; // 充值后的总条数
                    smsUserBlank.setSurplusNum(afterSmsNum);
                    smsUserBlankService.updateUserBlank(smsUserBlank);
                    // 发送动作指令到redis
                    String keys = smsUserService.querySmsUserKey(uid);
                    instructSend(InstructState.USERTOPUP_SUCESS, keys, uid); // 发送动作指令到redis

                    // 5. 添加到消费记录
                    SmsUserMoneyRunning moneyRunning = new SmsUserMoneyRunning();
                    moneyRunning.setUid(order.getUserId());// 操作人id
                    moneyRunning.setType(3);// 快钱充值
                    moneyRunning.setSmsUserId(uid);// 被操作人id
                    moneyRunning.setComment("快钱充值");// 备注
                    moneyRunning.setBeforeNum(surplusNum);// 操作前条数
                    moneyRunning.setAfterNum(afterSmsNum);// 操作后条数
                    moneyRunning.setOperateNum(orderSmsNum);// 操作条数
                    moneyRunning.setOrderNo(orderNo);// 订单编号
                    moneyRunning.setCreateTime(new Date());// 创建时间
                    smsUserService.saveSmsUserMoneyRunning(moneyRunning);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "/error";
        }
        return "/";
    }

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
