package com.dzd.sms.service.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/4
 * Time: 19:27
 */
public class SmsPullReport implements Serializable
{
	int id;
	String mobile;
	int state;
	int taskId;
	Date createTime;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public int getTaskId()
	{
		return taskId;
	}

	public void setTaskId(int taskId)
	{
		this.taskId = taskId;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

}
