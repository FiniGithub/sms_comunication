package com.dzd.phonebook.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dzd.phonebook.aop.MethodDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.phonebook.entity.SysMenu;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.service.SysMenuBtnService;
import com.dzd.phonebook.service.SysMenuService;
import com.dzd.phonebook.util.AbstractController;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.JspResponseBean;
import com.dzd.phonebook.util.MenuTree;

/**
 * 菜单controller
 * 
 * @author chenchao
 * @date 2016-6-29
 *
 */
@Controller
@RequestMapping("/sysMenu")
public class SysMenuController extends AbstractController {

	@Autowired
	private SysMenuService<SysMenu> sysMenuService;

	@SuppressWarnings("rawtypes")
	@Autowired
	private SysMenuBtnService sysMenuBtnService;

	public static final Logger log = LoggerFactory.getLogger(SysMenuController.class);

	@RequestMapping("/menu")
	public String menu() throws Exception {
		return "sysMenu/sysmenulist";
	}

	@RequestMapping("/addsysuser")
	public String addSysUser() throws Exception {
		return "sysUser/addsysuser";
	}

	/**
	 * 菜单列表
	 * 
	 * @param request
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/menulist", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public DzdResponse list(HttpServletRequest request, @RequestBody Map<String, Object> data) throws Exception {
		DzdResponse dzdPageResponse = new DzdResponse();
		try {
			List<SysMenu> dataList = sysMenuService.querySysMenuList();
			JspResponseBean jspResponseBean = new JspResponseBean();
			jspResponseBean.setDataList(dataList);
			dzdPageResponse.setData(jspResponseBean);
			dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
		} catch (Exception e) {
			log.error(null, e);
			dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
			e.printStackTrace();
		}
		return dzdPageResponse;
	}

	/**
	 * 新增、修改菜单
	 * 
	 * @param request
	 * @param data
	 * @return
	 */
	@MethodDescription("新增、修改菜单")
	@RequestMapping(value = "/from/merge", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public DzdResponse merge(HttpServletRequest request, @RequestBody Map<String, Object> data) {
		DzdResponse dzdPageResponse = new DzdResponse();
		try {
			SysMenu sysMenu = getRequestParameters(SysMenu.class, data);
			if (sysMenu.getId() == null) {
				sysMenu.setDeleted(0);
				sysMenuService.saveMenu(sysMenu);
			} else {
				sysMenuService.updateMenu(sysMenu);
			}
			dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
		} catch (Exception e) {
			log.error(null, e);
			dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
			e.printStackTrace();
		}
		return dzdPageResponse;
	}

	/**
	 * 根据编号查询菜单
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/formEdit")
	@ResponseBody
	public DzdResponse sysMunuEdit(HttpServletRequest request, Model model) {
		DzdResponse dzdPageResponse = new DzdResponse();
		try {
			Object id = request.getParameter("id");
			if (id == null) {
				dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
				return dzdPageResponse;
			}
			SysMenu sysMenu = sysMenuService.queryMenuAndBtnById(Integer.parseInt(id.toString()));
			JspResponseBean jspResponseBean = new JspResponseBean();
			jspResponseBean.setData(sysMenu);
			dzdPageResponse.setData(jspResponseBean);
			dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
		} catch (Exception e) {
			log.error(null, e);
			dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
			e.printStackTrace();
		}
		return dzdPageResponse;
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@MethodDescription("删除菜单")
	@RequestMapping(value = "/from/delete", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public DzdResponse postsDelete(HttpServletRequest request, Model model) {
		DzdResponse response = new DzdResponse();
		try {
			String sysMenuId = request.getParameter("sysMenuId");
			sysMenuService.deleteMenu(Integer.parseInt(sysMenuId));
			response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
		} catch (Exception e) {
			response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 菜单列表
	 * 
	 * @param request
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryMenuList", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public DzdResponse menuList(HttpServletRequest request, @RequestBody Map<String, Object> data) throws Exception {
		DzdResponse dzdPageResponse = new DzdResponse();
		try {
			JspResponseBean jspResponseBean = new JspResponseBean();
			List<SysMenu> dataList = sysMenuService.queryRootSysMenuList();
			List<MenuTree> menuTreeList = null;
			if (!CollectionUtils.isEmpty(dataList)) {
				menuTreeList = new ArrayList<MenuTree>();
				// 第一级菜单
				for (SysMenu sysMenu : dataList) {
					MenuTree menuTree = new MenuTree();
					menuTree.setParentId("root");
					menuTree.setId(sysMenu.getId());
					menuTree.setText(sysMenu.getName());
					menuTree.setType("menu");
					menuTree.setTopsId("0");
					menuTreeList.add(menuTree);
					List<MenuTree> childMenuTreeList = new ArrayList<MenuTree>();
					if (!CollectionUtils.isEmpty(sysMenu.getChild())) {
						// 第三级菜单
						for (SysMenu menu : sysMenu.getChild()) {
							MenuTree childMenuTree = new MenuTree();
							childMenuTree.setParentId(sysMenu.getId() + "");
							childMenuTree.setId(menu.getId());
							childMenuTree.setText(menu.getName());
							childMenuTree.setType("menu");
							childMenuTree.setTopsId("0");
							childMenuTreeList.add(childMenuTree);
							List<SysMenuBtn> menuBtns = sysMenuBtnService.queryByMenuid(menu.getId());
							menu.setBtns(menuBtns);
							List<MenuTree> btnList = new ArrayList<MenuTree>();
							// 第三级按钮
							for (SysMenuBtn menuBtn : menuBtns) {
								MenuTree btnTree = new MenuTree();
								btnTree.setParentId(menu.getId() + "");
								btnTree.setId(menuBtn.getId());
								btnTree.setText(menuBtn.getBtnName());
								btnTree.setType("button");
								btnTree.setTopsId("2");
								btnList.add(btnTree);
							}
							childMenuTree.setNodes(btnList);
						}
					}
					if (childMenuTreeList.size() > 0) {
						menuTree.setNodes(childMenuTreeList);
					}
				}
			}
			jspResponseBean.setDataList(menuTreeList);
			dzdPageResponse.setData(jspResponseBean);
			dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
		} catch (Exception e) {
			log.error(null, e);
			dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
			e.printStackTrace();
		}
		return dzdPageResponse;
	}

}
