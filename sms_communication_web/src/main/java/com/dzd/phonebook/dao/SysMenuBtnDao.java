package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;



/**
 * SysMenuBtn Mapper
 * @author Administrator
 *
 */
public interface SysMenuBtnDao<T> extends BaseDao<T> {
	
	public List<T> queryByMenuid(Integer menuid);
	
	public List<T> queryByMenuid2(Object param);
	
	public List<T> queryByMenuUrl(String url); 
	
	public void deleteByMenuid(Integer menuid);

	public List<T> getMenuBtnByUser(Integer userid);

	public List<T> queryMenuListByRole(@Param("menuId") Integer menuId,@Param("userId") Integer userId);
	
	public List<T> queryByAll();
}
