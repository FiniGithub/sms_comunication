package com.dzd.phonebook.entity;

import com.dzd.base.page.BasePage;

import java.util.Date;
import java.util.List;

public class Plugin extends BasePage {
	
	
	private List<SysMenuBtn> sysMenuBtns;//菜单拥有的按钮
	
	private Integer id;			//   id主键
	private String path;		//   插件jar包所在路径
	private String name;		//   插件名称
	private String className;   //   通道标识
	private String aisleName;   //   通道名称
	private String config;		//   插件内容
	private String intro;		//   插件功能简介
	private Integer state;		//   插件状态 0=未启用,1=启用中
	private Integer deleted;	//   删除状态 0=未删除,1=已删除
	private Date createTime;//   创建时间
	private Date updateTime;//   修改时间
	private Integer createBy;	//   创建人id
	private Integer updateBy;	//   修改人id
	private byte[] jar;			//jar包二进制
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public Integer getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}
	public Integer getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public List<SysMenuBtn> getSysMenuBtns() {
		return sysMenuBtns;
	}
	public void setSysMenuBtns(List<SysMenuBtn> sysMenuBtns) {
		this.sysMenuBtns = sysMenuBtns;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getAisleName() {
		return aisleName;
	}
	public void setAisleName(String aisleName) {
		this.aisleName = aisleName;
	}
	public byte[] getJar() {
		return jar;
	}
	public void setJar(byte[] jar) {
		this.jar = jar;
	}
	
}