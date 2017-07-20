package com.dzd.sms.service.data;

import com.dzd.utils.MathUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-1-9.
 */
public class SmsUserBlank implements Serializable {
    Long id = 0L;

    Long userId = 0L;

    Double balance = 0.0;
    
    int surplus_num = 0;

    public int getSurplus_num()
	{
		return surplus_num;
	}

	public void setSurplus_num(int surplus_num)
	{
		this.surplus_num = surplus_num;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = MathUtil.number(balance,3);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
