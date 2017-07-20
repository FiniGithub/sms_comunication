package com.dzd.phonebook.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SysRoleRelDao;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.util.DzdPageParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 
 * <br>
 * <b>功能：</b>SysRoleRelService<br>
 * <b>作者：</b>JEECG<br>
 * <b>日期：</b> Dec 9, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Service("sysRoleRelService")
public class SysRoleRelService<T> extends BaseService<T> {
	
	
	public List<SysRoleRel> queryByRoleId(Integer roleId, Integer relType){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("roleId", roleId);
		param.put("relType", relType);
		return getDao().queryByRoleId(param);
	}
	
	
	public List<SysRoleRel> queryByObjId(Integer objId,Integer relType){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("objId", objId);
		param.put("relType", relType);
		return getDao().queryByObjId(param);
	}
	
	/**
	 * 根据关联对象id,关联类型删除 
	 * @param objId
	 * @param relType
	 */
	public void deleteByObjId(Integer objId,Integer relType){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("objId", objId);
		param.put("relType", relType);
		getDao().deleteByObjId(param);
	}
	
	/**
	 * 根据角色id删除 
	 * @param roleId
	 */
	public void deleteByRoleId(Integer roleId){
		deleteByRoleId(roleId,null);
	}
	
	/**
	 *  根据角色id,关联类型删除 
	 * @param roleId
	 * @param relType
	 */
	public void deleteByRoleId(Integer roleId,Integer relType){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("roleId", roleId);
		param.put("relType", relType);
		getDao().deleteByRoleId(param);
	}
	
	
	

	@Autowired
    private SysRoleRelDao<T> mapper;

		
	public SysRoleRelDao<T> getDao() {
		return mapper;
	}


	/**
	 * 根据角色id查询菜单权限id
	 * @param dzdPageParam
	 * @return
     */
	public List<SysRoleRel> queryMenuByRoleId(DzdPageParam dzdPageParam){
		return mapper.queryMenuByRoleId(dzdPageParam);
	}

	/**
	 * 根据用户id查找角色
	 *
	 * @param dzdPageParam
	 * @return
	 */
	public List<SysRoleRel> queryRoleByUserId(DzdPageParam dzdPageParam){
		return mapper.queryRoleByUserId(dzdPageParam);
	}


	/**
	 * 根据objId 修改用户的角色信息
	 * @param object
	 */
	public void updateByObjId(Object object){
		getDao().updateByObjId(object);
	}

}
