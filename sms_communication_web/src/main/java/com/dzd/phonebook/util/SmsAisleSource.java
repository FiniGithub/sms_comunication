package com.dzd.phonebook.util;

/**
 * @Description:通道源
 * @author:oygy
 * @time:2017年1月11日 上午11:21:41
 */
public class SmsAisleSource {

	private String smsAisleKey;			//通道源的键 （id唯一的）
	private String smsAisleName;		//通道源的名称
	
	
	public String getSmsAisleKey() {
		return smsAisleKey;
	}
	public void setSmsAisleKey(String smsAisleKey) {
		this.smsAisleKey = smsAisleKey;
	}
	public String getSmsAisleName() {
		return smsAisleName;
	}
	public void setSmsAisleName(String smsAisleName) {
		this.smsAisleName = smsAisleName;
	}
	
	

}
