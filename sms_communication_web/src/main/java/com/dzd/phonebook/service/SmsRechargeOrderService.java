package com.dzd.phonebook.service;

import com.dzd.base.dao.BaseDao;
import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SmsRechargeOrderDao;
import com.dzd.phonebook.entity.SmsRechargeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 充值记录服务类
 * Created by CHENCHAO on 2017/5/22.
 */
@Service("SmsRechargeOrderService")
public class SmsRechargeOrderService<T> extends BaseService<T> {
    @Autowired
    private SmsRechargeOrderDao<T> smsRechargeOrderDao;

    public SmsRechargeOrderDao<T> getDao() {
        return smsRechargeOrderDao;
    }

    /**
     * 新增
     *
     * @param smsRechargeOrder
     */
    public void insertSmsRechargeOrder(SmsRechargeOrder smsRechargeOrder) {
        getDao().insertSmsRechargeOrder(smsRechargeOrder);
    }

    /**
     * 修改
     * @param order
     */
    public void updateSmsRechargeOrder(SmsRechargeOrder order){
        getDao().updateSmsRechargeOrder(order);
    }

    /**
     * 根据订单号查询充值订单
     *
     * @param orderId
     * @return
     */
    public SmsRechargeOrder queryRechargeOrderByOrderId(String orderId) {
        return getDao().queryRechargeOrderByOrderId(orderId);
    }


}
