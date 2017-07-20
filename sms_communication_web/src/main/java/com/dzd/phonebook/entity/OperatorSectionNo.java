package com.dzd.phonebook.entity;

import java.io.Serializable;

/**
 * 文件配置信息
 * 
 * @author CHENCHAO
 * @date 2017-03-30 10:36:00
 *
 */
public class OperatorSectionNo implements Serializable
{
	private static final long serialVersionUID = 483115491412449722L;
	
	private int type;// 运营商类型 
	private String sectionNo;// 号段信息
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public String getSectionNo()
	{
		return sectionNo;
	}
	public void setSectionNo(String sectionNo)
	{
		this.sectionNo = sectionNo;
	}
	
}
