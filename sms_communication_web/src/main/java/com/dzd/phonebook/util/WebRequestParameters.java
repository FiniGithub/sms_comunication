package com.dzd.phonebook.util;


public class WebRequestParameters  extends BaseRequestParameters{
	private String sysUserName;// 系统用户的名称

	private String categorName;// 分类名称
	
    private String startInput; //开始时间
    private String endInput;   //结束时间

	public String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}

	public String getCategorName() {
		return categorName;
	}

	public void setCategorName(String categorName) {
		this.categorName = categorName;
	}

	public String getStartInput() {
		return startInput;
	}

	public void setStartInput(String startInput) {
		this.startInput = startInput;
	}

	public String getEndInput() {
		return endInput;
	}

	public void setEndInput(String endInput) {
		this.endInput = endInput;
	}


}
