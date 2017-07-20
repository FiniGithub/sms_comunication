package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SysRole;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * 角色接口
 *
 * @author chenchao
 * @date 2016-6-24
 */
public interface SysRoleDao<T> extends BaseDao<T> {

	/**
	 * 查询全部有效的权限
	 * 
	 * @return
	 */
	public List<T> queryAllList();

	/**
	 * 根据用户Id查询权限
	 * 
	 * @return
	 */
	public List<T> queryByUserid(Integer userid);

	Page<SysRole> queryRolePage(DzdPageParam dzdPageParam);

	/**
	 * 根据角色id删除所有关联的菜单
	 * 
	 * @param id
	 * @return
	 */
	int deleteByRoleId(Object id);
}
