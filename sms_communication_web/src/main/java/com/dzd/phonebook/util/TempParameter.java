package com.dzd.phonebook.util;

import java.util.Date;

/** * 
@author  作者 
E-mail: * 
@date 创建时间：2017年5月22日 上午10:36:50 * 
@version 1.0 * 
@parameter  * 
@since  * 
@return  */
public class TempParameter
{
	private Object menuId;
	private String offset;
	private Object pagenum;
	private Object pagesize;
	private String limit;
	private String logTime;
	private Object content;
	private Object state;
	private Object ids;
	private Object smsUser;
	private Object phone;
	private Object bgztSelect;
	private boolean export;
	private Date startInput;
	private Date endInput;
	private String uid;
	private String sid;
	
	public String getSid()
	{
		return sid;
	}
	public void setSid(String sid)
	{
		this.sid = sid;
	}
	public String getUid()
	{
		return uid;
	}
	public void setUid(String uid)
	{
		this.uid = uid;
	}
	public Date getStartInput()
	{
		return startInput;
	}
	public void setStartInput(Date startInput)
	{
		this.startInput = startInput;
	}
	public Date getEndInput()
	{
		return endInput;
	}
	public void setEndInput(Date endInput)
	{
		this.endInput = endInput;
	}
	public boolean isExport()
	{
		return export;
	}
	public void setExport(boolean export)
	{
		this.export = export;
	}
	public String getLimit()
	{
		return limit;
	}
	public void setLimit(String limit)
	{
		this.limit = limit;
	}
	public Object getMenuId()
	{
		return menuId;
	}
	public void setMenuId(Object menuId)
	{
		this.menuId = menuId;
	}
	public String getOffset()
	{
		return offset;
	}
	public void setOffset(String offset)
	{
		this.offset = offset;
	}
	public Object getPagenum()
	{
		return pagenum;
	}
	public void setPagenum(Object pagenum)
	{
		this.pagenum = pagenum;
	}
	public Object getPagesize()
	{
		return pagesize;
	}
	public void setPagesize(Object pagesize)
	{
		this.pagesize = pagesize;
	}
	public String getLogTime()
	{
		return logTime;
	}
	public void setLogTime(String logTime)
	{
		this.logTime = logTime;
	}
	public Object getContent()
	{
		return content;
	}
	public void setContent(Object content)
	{
		this.content = content;
	}
	public Object getState()
	{
		return state;
	}
	public void setState(Object state)
	{
		this.state = state;
	}
	public Object getIds()
	{
		return ids;
	}
	public void setIds(Object ids)
	{
		this.ids = ids;
	}
	public Object getSmsUser()
	{
		return smsUser;
	}
	public void setSmsUser(Object smsUser)
	{
		this.smsUser = smsUser;
	}
	public Object getPhone()
	{
		return phone;
	}
	public void setPhone(Object phone)
	{
		this.phone = phone;
	}
	public Object getBgztSelect()
	{
		return bgztSelect;
	}
	public void setBgztSelect(Object bgztSelect)
	{
		this.bgztSelect = bgztSelect;
	}
	
}
