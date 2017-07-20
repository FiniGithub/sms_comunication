package com.dzd.phonebook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dzd.base.service.BaseService;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.dao.SysMenuBtnDao;
import com.dzd.phonebook.dao.SysMenuDao;
import com.dzd.phonebook.entity.SysMenu;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.util.DzdParameters;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 菜单服务类
 *
 * @author chenchao
 * @date 2016-6-24
 */
@Service("sysMenuService")
public class SysMenuService<T> extends BaseService<T> {
	private final static Logger log = Logger.getLogger(SysMenuService.class);

	@Autowired
	private SysRoleRelService<SysRoleRel> sysRoleRelService;

	@Autowired
	private SysMenuBtnService<SysMenuBtn> sysMenuBtnService;

	@Autowired
	private SysMenuDao<T> mapper;

	@Autowired
	private SysMenuBtnDao<T> sysMenuBtnDao;

	/**
	 * 保存菜单btn
	 *
	 * @param btns
	 * @throws Exception
	 */
	public void saveBtns(Integer menuid, List<SysMenuBtn> btns) throws Exception {
		if (btns == null || btns.isEmpty()) {
			return;
		}
		for (SysMenuBtn btn : btns) {
			if (btn.getId() != null && "1".equals(btn.getDeleteFlag())) {
				sysMenuBtnService.delete(btn.getId());
				continue;
			}
			btn.setMenuid(menuid);
			if (btn.getId() == null) {
				sysMenuBtnService.add(btn);
			} else {
				sysMenuBtnService.update(btn);
			}
		}

	}

	public void addSysMenu(SysMenu menu) throws Exception {
		super.add((T) menu);
		saveBtns(menu.getId(), menu.getBtns());
	}

	public void updateSysMenu(SysMenu menu) throws Exception {
		super.update((T) menu);
		saveBtns(menu.getId(), menu.getBtns());
	}

	/**
	 * 查询所有系统菜单列表
	 *
	 * @return
	 */
	public List<T> queryByAll() {
		return mapper.queryByAll();
	}

	/**
	 * 获取顶级菜单
	 *
	 * @return
	 */
	public List<T> getRootMenu(Integer menuId) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("menuId", menuId);
		return mapper.getRootMenu(map);
	}

	/**
	 * 获取子菜单
	 *
	 * @return
	 */
	public List<T> getChildMenu(Integer parentId) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("parentId", parentId);
		return mapper.getChildMenu(map);
	}

	/**
	 * 根据用户id查询父菜单
	 *
	 * @param userId
	 * @return
	 */
	public List<T> getRootMenuByUser(Integer userId) {
		return getDao().getRootMenuByUser(userId);
	}

	/**
	 * 根据用户id查询子菜单
	 *
	 * @param userId
	 * @return
	 */
	public List<T> getChildMenuByUser(Integer userId) {
		return getDao().getChildMenuByUser(userId);
	}

	/**
	 * 根据权限id查询菜单
	 *
	 * @param roleId
	 * @return
	 */
	public List<T> getMenuByRoleId(Integer roleId) {
		return getDao().getMenuByRoleId(roleId);
	}

	@Override
	public void delete(Object[] ids) throws Exception {
		super.delete(ids);
		// 删除关联关系
		for (Object id : ids) {
			sysRoleRelService.deleteByObjId((Integer) id, SysRoleRel.RelType.MENU.key);
			sysMenuBtnService.deleteByMenuid((Integer) id);
		}
	}

	public SysMenuDao<T> getDao() {
		return mapper;
	}

	public List<SysMenu> querySysMenuList() {
		List<SysMenu> sysMenus = null;
		List<SysMenu> rootMenus = (List<SysMenu>) mapper.getRootMenu(null);
		if (!CollectionUtils.isEmpty(rootMenus)) {
			sysMenus = new ArrayList<SysMenu>();
			for (SysMenu rootMenu : rootMenus) {
				sysMenus.add(rootMenu);
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("parentId", rootMenu.getId());
				List<T> child = mapper.getChildMenu(map);
				if (!CollectionUtils.isEmpty(child)) {
					sysMenus.addAll((List<SysMenu>) child);
				}
			}
		}
		// 找出菜单对应的按钮
		for (SysMenu sysMenu : sysMenus) {
			List<SysMenuBtn> btnList = sysMenuBtnService.queryByMenuid(sysMenu.getId());
			sysMenu.setBtns(btnList);
		}
		return sysMenus;
	}

	public List<SysMenu> queryRootSysMenuList() {
		List<SysMenu> rootMenus = (List<SysMenu>) mapper.getRootMenu(null);
		if (!CollectionUtils.isEmpty(rootMenus)) {
			for (SysMenu rootMenu : rootMenus) {
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("parentId", rootMenu.getId());
				List<T> child = mapper.getChildMenu(map);
				if (!CollectionUtils.isEmpty(child)) {
					rootMenu.setChild((List<SysMenu>) child);
				}
			}
		}
		return rootMenus;
	}

	/**
	 * 根据用户id查找菜单
	 *
	 * @param dzdParameters
	 * @return
	 */
	public List<SysMenu> queryMenusByUserId(DzdParameters dzdParameters) {
		List<SysMenu> rootMenus = mapper.queryMenusByUserId(dzdParameters);
		List<SysMenu> rootMenuList = null;
		if (!CollectionUtils.isEmpty(rootMenus)) {
			rootMenuList = new ArrayList<SysMenu>();
			// 选出父节点
			for (SysMenu rootMenu : rootMenus) {
				if (rootMenu.getParentId() == null) {
					rootMenuList.add(rootMenu);
				}
			}
			for (SysMenu sysMenu : rootMenuList) {
				List<SysMenu> child = new ArrayList<SysMenu>();
				for (SysMenu menu : rootMenus) {
					if(menu.getParentId() !=null){
						if (sysMenu.getId().intValue() == menu.getParentId().intValue()) {
							child.add(menu);
						}
					}
				}
				sysMenu.setChild(child);
			}
		}
		return rootMenuList;
	}

	/**
	 * 根据用户id查找菜单 子父同一层级
	 *
	 * @param dzdParameters
	 * @return
	 */
	public List<SysMenu> queryParentAndChildMenuByUserId(DzdParameters dzdParameters) {
		List<SysMenu> rootMenus = mapper.queryMenusByUserId(dzdParameters);
		if (!CollectionUtils.isEmpty(rootMenus)) {
			for (SysMenu rootMenu : rootMenus) {
				// 根据菜单id查找按钮
				List<SysMenuBtn> menuBtns = sysMenuBtnService.queryByMenuid(rootMenu.getId());
				if (!CollectionUtils.isEmpty(menuBtns)) {
					rootMenu.setBtns(menuBtns);
				}
			}
		}
		return rootMenus;
	}

	/**
	 * 根据用户id查找菜单 子父同一层级 封装返回用于拦截权限
	 *
	 * @param dzdParameters
	 * @return
	 */
	public List<String> getActionUrls(DzdParameters dzdParameters) {
		List<String> urls = null;

		List<SysMenu> rootMenus = mapper.queryMenusByUserId(dzdParameters);
		if (!CollectionUtils.isEmpty(rootMenus)) {
			urls = new ArrayList<String>();
			for (SysMenu rootMenu : rootMenus) {
				if (!StringUtil.isEmpty(rootMenu.getUrl())) {
					urls.add(rootMenu.getUrl());
				}
				if (!StringUtil.isEmpty(rootMenu.getActions())) {
					String[] actions = StringUtils.split(rootMenu.getActions(), "|");
					if (actions != null && actions.length > 0) {
						for (String action : actions) {
							urls.add(action);
						}
					}
				}
				// 根据菜单id查找按钮
				List<SysMenuBtn> menuBtns = sysMenuBtnService.queryByMenuid(rootMenu.getId());
				if (!CollectionUtils.isEmpty(menuBtns)) {
					for (SysMenuBtn menuBtn : menuBtns) {
						urls.add(menuBtn.getActionUrls());
					}
				}
			}
		}
		return urls;
	}

	public int saveMenu(SysMenu sysMenu) throws Exception {
		int num = 0;
		add((T) sysMenu);
		for (SysMenuBtn sysMenuBtn : sysMenu.getBtns()) {
			sysMenuBtn.setMenuid(sysMenu.getId());
			sysMenuBtnDao.add((T) sysMenuBtn);
		}
		return num;
	}

	public int updateMenu(SysMenu sysMenu) throws Exception {
		int num = 0;
		mapper.updateBySelective((T) sysMenu);
		sysMenuBtnDao.deleteByMenuid(sysMenu.getId());
		for (SysMenuBtn sysMenuBtn : sysMenu.getBtns()) {
			sysMenuBtn.setMenuid(sysMenu.getId());
			sysMenuBtnDao.add((T) sysMenuBtn);
		}
		return num;
	}

	public int deleteMenu(int id) throws Exception {
		int num = 0;
		sysMenuBtnDao.deleteByMenuid(id);
		mapper.delete(id);
		return num;
	}

	public SysMenu queryMenuAndBtnById(int id) throws Exception {
		SysMenu sysMenu = (SysMenu) queryById(id);
		List<SysMenuBtn> btnList = (List<SysMenuBtn>) sysMenuBtnDao.queryByMenuid(id);
		if (sysMenu != null) {
			sysMenu.setBtns(btnList);
		}
		return sysMenu;
	}

	/**
	 * 根据菜单名称查找菜单id
	 */
	public Integer queryMenusByName(){
		return mapper.queryMenusByName();
	}
}
