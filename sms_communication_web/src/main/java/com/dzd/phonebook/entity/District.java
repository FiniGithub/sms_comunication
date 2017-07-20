package com.dzd.phonebook.entity;

import com.dzd.base.page.BasePage;

import java.util.List;

/**
 * 地区实体类
 * 
 * @author chenchao
 * @date 2016-5-9
 *
 */
public class District extends BasePage {
	private Integer id;
	private String districtName;// 地区名称
	private String districtEn;// 地区拼音
	private Integer parentId;// 省份编号
	/**首字母拼音**/
	private String firstPy;


	/**区号**/
	private String area;


	private List<SysMenuBtn> sysMenuBtns;// 菜单拥有的按钮
	private int childNumber;// 字节点的个数

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List<SysMenuBtn> getSysMenuBtns() {
		return sysMenuBtns;
	}

	public void setSysMenuBtns(List<SysMenuBtn> sysMenuBtns) {
		this.sysMenuBtns = sysMenuBtns;
	}

	public int getChildNumber() {
		return childNumber;
	}

	public void setChildNumber(int childNumber) {
		this.childNumber = childNumber;
	}

	public String getDistrictEn() {
		return districtEn;
	}

	public void setDistrictEn(String districtEn) {
		this.districtEn = districtEn;
	}

	public String getFirstPy() {
		return firstPy;
	}

	public void setFirstPy(String firstPy) {
		this.firstPy = firstPy;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "District [id=" + id + ", districtName=" + districtName + ", districtEn=" + districtEn + ", parentId="
				+ parentId + ", sysMenuBtns=" + sysMenuBtns + ", childNumber=" + childNumber + "]";
	}

}
