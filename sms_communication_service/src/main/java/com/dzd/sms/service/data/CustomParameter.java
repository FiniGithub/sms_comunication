package com.dzd.sms.service.data;

import static com.dzd.sms.application.Define.INTERFACE_STATE_SUCCESS;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IDEA </br>
 * Author: hz-liang </br>
 * Date: 2017/3/28 </br>
 * Time: 15:32 </br>
 */
public class CustomParameter implements Serializable
{
	boolean valid = true;
	Long taskId = 0l;
	Long aisleGroupId = 0l;
	Long userId = 1L;
	SmsState smsState = INTERFACE_STATE_SUCCESS;
	boolean isAudit = true; // 是否需要审核
	int errorPhoneNumber = 0; // 错误号码数
	int vailPhoneNumber = 0; //
	int allPhoneNumber = 0; //
	// int sendType= Integer.valueOf(type);
	String smsAuditContent = ""; // 验证审核的内容
	// 短信内容长度计算
	Integer singleSmsLength;// (Integer)(smsContent.length()/67);
	Integer aisleSmsLength;// 通道配置长短信计费
	String errorPhones = "";
	Integer limit;// 页数大小
	int code;
	String msg;
	String smsContent;
	int surplus_num;// 发送短信条数
	Date timing; // 定时发送时间

	public Integer getAisleSmsLength() {
		return aisleSmsLength;
	}

	public void setAisleSmsLength(Integer aisleSmsLength) {
		this.aisleSmsLength = aisleSmsLength;
	}

	public Date getTiming()
	{
		return timing;
	}

	public void setTiming(Date timing)
	{
		this.timing = timing;
	}

	public int getAllPhoneNumber()
	{
		return allPhoneNumber;
	}

	public void setAllPhoneNumber(int allPhoneNumber)
	{
		this.allPhoneNumber = allPhoneNumber;
	}

	public int getSurplus_num()
	{
		return surplus_num;
	}

	public void setSurplus_num(int surplus_num)
	{
		this.surplus_num = surplus_num;
	}

	public String getSmsContent()
	{
		return smsContent;
	}

	public void setSmsContent(String smsContent)
	{
		this.smsContent = smsContent;
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		if ( msg == null )
		{
			msg = "查询成功";
		}
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public Integer getLimit()
	{
		return limit;
	}

	public void setLimit(Integer limit)
	{
		this.limit = limit;
	}

	public boolean isValid()
	{
		return valid;
	}

	public void setValid(boolean valid)
	{
		this.valid = valid;
	}

	public Long getTaskId()
	{
		return taskId;
	}

	public void setTaskId(Long taskId)
	{
		this.taskId = taskId;
	}

	public Long getAisleGroupId()
	{
		return aisleGroupId;
	}

	public void setAisleGroupId(Long aisleGroupId)
	{
		this.aisleGroupId = aisleGroupId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public SmsState getSmsState()
	{
		return smsState;
	}

	public void setSmsState(SmsState smsState)
	{
		this.smsState = smsState;
	}

	public boolean isAudit()
	{
		return isAudit;
	}

	public void setAudit(boolean isAudit)
	{
		this.isAudit = isAudit;
	}

	public int getErrorPhoneNumber()
	{
		return errorPhoneNumber;
	}

	public void setErrorPhoneNumber(int errorPhoneNumber)
	{
		this.errorPhoneNumber = errorPhoneNumber;
	}

	public int getVailPhoneNumber()
	{
		return vailPhoneNumber;
	}

	public void setVailPhoneNumber(int vailPhoneNumber)
	{
		this.vailPhoneNumber = vailPhoneNumber;
	}

	public String getSmsAuditContent()
	{
		return smsAuditContent;
	}

	public void setSmsAuditContent(String smsAuditContent)
	{
		this.smsAuditContent = smsAuditContent;
	}

	public Integer getSingleSmsLength()
	{
		if ( singleSmsLength == null )
		{
			singleSmsLength = 1;
		}
		return singleSmsLength;
	}

	public void setSingleSmsLength(Integer singleSmsLength)
	{
		this.singleSmsLength = singleSmsLength;
	}

	public String getErrorPhones()
	{
		return errorPhones;
	}

	public void setErrorPhones(String errorPhones)
	{
		this.errorPhones = errorPhones;
	}

}
