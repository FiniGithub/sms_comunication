package com.dzd.phonebook.util;

import java.util.Date;
import java.util.Map;

import com.dzd.base.page.BasePage;

/**
 * 
 * @ClassName:     SmsRechargeUser
 * @Description: 客户充值记录
 * @author:    hz-liang
 * @date:        2017年4月14日 下午4:44:08
 */
public class SmsRechargeUser extends BasePage
{
	private int smsUserId;// 用户ID
	private String orderNo;// 订单号
	private Float rechargeMoney;// 充值金额
	private int rechargeNum;// 充值条数
	private String description; // 描述信息
	private Date createTime;// 创建时间
	private Date updateTime;// 更新时间
	private String operation;// 操作
	private int state;// 状态
	private int type;// 类型
	private String email;// 账号
	private String name;// 客户名称
	private String teamName;// 团队名称
	private Integer teamId;// 团队ID
	private int sumRechargeNum;// 充值条数总和
	private Float sumRechargeMoney;// 充值金额总和
	private Map<String, String> teamMap;// 团队ID、name信息

	public Map<String, String> getTeamMap()
	{
		return teamMap;
	}

	public void setTeamMap(Map<String, String> teamMap)
	{
		this.teamMap = teamMap;
	}

	public Integer getTeamId()
	{
		return teamId;
	}

	public void setTeamId(Integer teamId)
	{
		this.teamId = teamId;
	}

	public String getTeamName()
	{
		return teamName;
	}

	public void setTeamName(String teamName)
	{
		this.teamName = teamName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getSumRechargeNum()
	{
		return sumRechargeNum;
	}

	public void setSumRechargeNum(int sumRechargeNum)
	{
		this.sumRechargeNum = sumRechargeNum;
	}

	public Float getSumRechargeMoney()
	{
		return sumRechargeMoney;
	}

	public void setSumRechargeMoney(Float sumRechargeMoney)
	{
		this.sumRechargeMoney = sumRechargeMoney;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public int getSmsUserId()
	{
		return smsUserId;
	}

	public void setSmsUserId(int smsUserId)
	{
		this.smsUserId = smsUserId;
	}

	public String getOrderNo()
	{
		return orderNo;
	}

	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}

	public Float getRechargeMoney()
	{
		return rechargeMoney;
	}

	public void setRechargeMoney(Float rechargeMoney)
	{
		this.rechargeMoney = rechargeMoney;
	}

	public int getRechargeNum()
	{
		return rechargeNum;
	}

	public void setRechargeNum(int rechargeNum)
	{
		this.rechargeNum = rechargeNum;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public String getOperation()
	{
		return operation;
	}

	public void setOperation(String operation)
	{
		this.operation = operation;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}
}
