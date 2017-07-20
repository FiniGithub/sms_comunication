package com.dzd.phonebook.entity;

import java.util.Date;

/**
 * 验证码实体类
 * @author CHENCHAO
 * @date 2017-04-11 11:20:00
 *
 */
public class VertifyCode {
	private int id;
	private String phone;// 号码
	private String email;// 
	private Date create_time;// 创建时间
	private String vertifycode;// 验证码
	private String imgCode;// 验证码
	private Integer apartTime;
	private String content;// 短信内容

	/**
	 * 旧密码
	 */
	private String oldPwd;

	/**
	 * 新密码
	 */
	private String newPwd;

	/**
	 * 验证码类型
	 */
	private Integer type;
	
	public String getImgCode()
	{
		return imgCode;
	}
	public void setImgCode(String imgCode)
	{
		this.imgCode = imgCode;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getVertifycode() {
		return vertifycode;
	}
	public void setVertifycode(String vertifycode) {
		this.vertifycode = vertifycode;
	}
	public String getOldPwd() {
		return oldPwd;
	}
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	public Integer getApartTime() {
		return apartTime;
	}
	public void setApartTime(Integer apartTime) {
		this.apartTime = apartTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
