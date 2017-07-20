package com.dzd.phonebook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dzd.base.entity.BaseEntity;
import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SysRoleDao;
import com.dzd.phonebook.dao.SysRoleRelDao;
import com.dzd.phonebook.entity.SysRole;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色服务类
 *
 * @author chenchao
 * @date 2016-6-24
 */
@Service("sysRoleService")
public class SysRoleService<T> extends BaseService<T> {
	private final static Logger log = Logger.getLogger(SysRoleService.class);

	@Autowired
	private SysRoleRelService<SysRoleRel> sysRoleRelService;

	@Autowired
	private SysRoleRelDao<SysRoleRel> sysRoleRelDao;

	@Autowired
	private SysRoleDao<SysRole> sysRoleDao;

	/**
	 * 添加角色&菜单关系
	 */
	public void addRoleMenuRel(Integer roleId, Integer[] menuIds) throws Exception {
		if (roleId == null || menuIds == null || menuIds.length < 1) {
			return;
		}
		for (Integer menuid : menuIds) {
			SysRoleRel rel = new SysRoleRel();
			rel.setRoleId(roleId);
			rel.setObjId(menuid);
			rel.setRelType(SysRoleRel.RelType.MENU.key);
			sysRoleRelService.add(rel);
		}
	}

	/**
	 * 添加角色&菜单关系
	 */
	public void addRoleBtnRel(Integer roleId, Integer[] btnIds) throws Exception {
		if (roleId == null || btnIds == null || btnIds.length < 1) {
			return;
		}
		for (Integer btnid : btnIds) {
			SysRoleRel rel = new SysRoleRel();
			rel.setRoleId(roleId);
			rel.setObjId(btnid);
			rel.setRelType(SysRoleRel.RelType.BTN.key);
			sysRoleRelService.add(rel);
		}
	}

	/**
	 * 添加
	 *
	 * @param role
	 * @param menuIds
	 * @throws Exception
	 */
	public void add(SysRole role, Integer[] menuIds, Integer[] btnIds) throws Exception {
		super.add((T) role);
		addRoleMenuRel(role.getId(), menuIds);
		addRoleBtnRel(role.getId(), btnIds);
	}

	/**
	 * 删除
	 *
	 * @param id
	 * @throws Exception
	 */
	public void delete(Integer[] ids) throws Exception {
		super.delete(ids);
		for (Integer id : ids) {
			// 清除关联关系
			sysRoleRelService.deleteByRoleId(id);
		}
	}

	/**
	 * 修改
	 *
	 * @param role
	 * @param menuIds
	 * @throws Exception
	 */
	public void update(SysRole role, Integer[] menuIds, Integer[] btnIds) throws Exception {
		super.update((T) role);
		// 如果角色被禁用则删除关联的用户关系
		if (BaseEntity.STATE.DISABLE.key == role.getState()) {
			sysRoleRelService.deleteByRoleId(role.getId(), SysRoleRel.RelType.USER.key);
		}
		// 清除关联关系
		sysRoleRelService.deleteByRoleId(role.getId(), SysRoleRel.RelType.MENU.key);
		sysRoleRelService.deleteByRoleId(role.getId(), SysRoleRel.RelType.BTN.key);
		addRoleMenuRel(role.getId(), menuIds);
		addRoleBtnRel(role.getId(), btnIds);

	}

	/**
	 * 查询全部有效的权限
	 *
	 * @return
	 */
	public List<T> queryAllList() {
		return getDao().queryAllList();
	}

	/**
	 * 查询全部有效的权限
	 *
	 * @return
	 */
	public List<T> queryByUserid(Integer userid) {
		return getDao().queryByUserid(userid);
	}

	@Autowired
	private SysRoleDao<T> mapper;

	public SysRoleDao<T> getDao() {
		return mapper;
	}

	public Page<SysRole> queryRoleList(DzdPageParam dzdPageParam) {
		return getDao().queryRolePage(dzdPageParam);
	}

	/**
	 * 新增角色
	 *
	 * @param sysRole
	 * @param menuIds
	 * @return
	 */
	public int saveRole(SysRole sysRole, List<String> menuIds,List<String> toparr) {
		int num = 0;
		mapper.add((T) sysRole);
		if (menuIds != null && menuIds.size() > 0) {
			for (int i = 0; i < menuIds.size(); i++) {
				SysRoleRel sysRoleRel = new SysRoleRel();
				sysRoleRel.setObjId(Integer.parseInt(menuIds.get(i)));
				sysRoleRel.setRelType(Integer.parseInt(toparr.get(i)));
				sysRoleRel.setRoleId(sysRole.getId());
				sysRoleRelDao.add(sysRoleRel);
			}
		}
		return num;
	}

	/**
	 * 修改角色
	 *
	 * @param sysRole
	 * @param menuIds
	 * @return
	 */
	public int updateRole(SysRole sysRole, List<String> menuIds,List<String> toparr) {
		int num = 0;
		mapper.updateBySelective((T) sysRole);
		sysRoleDao.deleteByRoleId(sysRole.getId());
		if (menuIds != null && menuIds.size() > 0) {
			for (int i = 0; i < menuIds.size(); i++) {
				SysRoleRel sysRoleRel = new SysRoleRel();
				sysRoleRel.setObjId(Integer.parseInt(menuIds.get(i)));
				sysRoleRel.setRelType(Integer.parseInt(toparr.get(i)));
				sysRoleRel.setRoleId(sysRole.getId());
				sysRoleRelDao.add(sysRoleRel);
			}
		}
		return num;
	}

	/**
	 * 删除角色
	 *
	 * @param id
	 * @return
	 */
	public int deleteRole(Integer id) {
		int num = 0;
		Map<Object, Object> mapList = new HashMap<Object,Object>();
		mapList.put("value1", 0);
		mapList.put("value2", 2);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("roleId", id);
		param.put("relType", mapList);
		sysRoleRelDao.deleteByRoleId(param);
		mapper.delete(id);
		return num;
	}

}
