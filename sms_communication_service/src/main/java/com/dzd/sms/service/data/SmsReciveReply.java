package com.dzd.sms.service.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/4
 * Time: 19:27
 */
public class SmsReciveReply  implements Serializable {
    int id;
    String mobile;
    String content;
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
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
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
