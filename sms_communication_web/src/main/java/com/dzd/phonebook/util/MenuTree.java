package com.dzd.phonebook.util;

import java.util.List;

public class MenuTree {
	private int id;
	private String text;
	private String parentId;// 上级菜单id
	private List<MenuTree> nodes;// 菜单下级
	private String type;// 按钮菜单类型
	private String topsId;// 按钮菜单类型

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<MenuTree> getNodes() {
		return nodes;
	}

	public void setNodes(List<MenuTree> nodes) {
		this.nodes = nodes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTopsId() {
		return topsId;
	}

	public void setTopsId(String topsId) {
		this.topsId = topsId;
	}
	
}
