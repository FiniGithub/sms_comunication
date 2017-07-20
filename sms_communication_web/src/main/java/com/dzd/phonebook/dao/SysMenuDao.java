package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SysMenu;
import com.dzd.phonebook.util.DzdParameters;

import java.util.List;

/**
 * 菜单接口
 *
 * @author chenchao
 * @date 2016-6-24
 */
public interface SysMenuDao<T> extends BaseDao<T> {

	/**
	 * 查询所有系统菜单列表
	 *
	 * @return
	 */
	public List<T> queryByAll();

	/**
	 * 获取顶级菜单
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<T> getRootMenu(java.util.Map map);

	/**
	 * 获取子菜单
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<T> getChildMenu(java.util.Map map);

	/**
	 * 根据权限id查询菜单
	 *
	 * @param roleId
	 * @return
	 */
	public List<T> getMenuByRoleId(Integer roleId);

	/**
	 * 根据用户id查询父菜单菜单
	 *
	 * @param userId
	 * @return
	 */
	public List<T> getRootMenuByUser(Integer userId);

	/**
	 * 根据用户id查询子菜单菜单
	 *
	 * @param userId
	 * @return
	 */
	public List<T> getChildMenuByUser(Integer userId);

	/**
	 * 根据用户id查找菜单
	 *
	 * @param dzdParameters
	 * @return
	 */
	List<SysMenu> queryMenusByUserId(DzdParameters dzdParameters);

	/**
	 * 根据菜单名称查找菜单id
	 */
	public Integer queryMenusByName();
}
