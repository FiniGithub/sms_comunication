package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SmsRechargeOrder;

/**
 * 充值记录接口
 * Created by CHENCHAO on 2017/5/22.
 */
public interface SmsRechargeOrderDao<T> extends BaseDao<T> {

    /**
     * 插入充值记录
     *
     * @param smsRechargeOrder
     */
    void insertSmsRechargeOrder(SmsRechargeOrder smsRechargeOrder);

    /**
     * 修改
     * @param order
     */
    public void updateSmsRechargeOrder(SmsRechargeOrder order);

    /**
     * 根据订单号查询充值订单
     *
     * @param orderId
     * @return
     */
    public SmsRechargeOrder queryRechargeOrderByOrderId(String orderId);
}
