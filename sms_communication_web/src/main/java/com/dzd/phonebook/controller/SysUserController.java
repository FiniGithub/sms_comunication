package com.dzd.phonebook.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.service.*;
import com.dzd.phonebook.util.*;
import com.dzd.sms.application.Define;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.base.util.HtmlUtil;
import com.dzd.base.util.MethodUtil;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.entity.SysRole;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.entity.SysUser;
import com.github.pagehelper.Page;

/**
 * 系统用户controller
 * 
 * @author chenchao
 * @date 2016-6-29 11:29
 *
 */
@Controller
@RequestMapping("/sysUser")
public class SysUserController extends AbstractController {

	public static final Logger log = LoggerFactory.getLogger(SysUserController.class);

	@Autowired
	private SysUserService<SysUser> sysUserService;
	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private SysRoleRelService sysRoleRelService;

	@Autowired
	private SysMenuBtnService sysMenuBtnService;

	@Autowired
	private SmsUserService smsUserService;

	@RequestMapping("/listview")
	public String list(HttpServletRequest request, Model model) throws Exception {
		Object menuId = request.getParameter("id");
		model.addAttribute("menuId", menuId);
       SysUser user = (SysUser)request.getSession().getAttribute("session_user");
       List<SysRoleRel> sysRoleRels = queryRoleByUserId(user.getId());
	   for (SysRoleRel sysRoleRel : sysRoleRels) {
    	   if(sysRoleRel.getRoleId() == 51){  //如果当前登录用户为经理
    		   model.addAttribute("sateType", 1);
        	   break;
    	   }
       } 
		List<SysUser> list =  sysUserService.querySysUserList();
		model.addAttribute("sysUserList", list);
		
		return "sysUser/sysuserlist";
	}

	@RequestMapping("/listviewto")
	public String listviewto() throws Exception {
		return "sysUser/addsysuser";
	}

	@RequestMapping("/addsysuser")
	public String addSysUser() throws Exception {
		return "sysUser/addsysuser";
	}
	
	
	
 
	/**
	 * 用户列表
	 * 
	 * @param request
	 * @param response
	 * @param data
	 * @throws Exception
	 */
	@RequestMapping(value = "/userlist", method = RequestMethod.POST)
	@ResponseBody
	public void list(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> data)
			throws Exception {
		try {
			DzdResponse dzdPageResponse = new DzdResponse();
			WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
			if (parameters == null) {
				return;
			}
			Object menuId = request.getParameter("menuId");
			if (menuId == null) {
				return;
			}
			DzdPageParam dzdPageParam = new DzdPageParam();
			Map<String, Object> sortMap = new HashMap<String, Object>();
			if (!StringUtil.isEmpty(parameters.getSysUserName())) {
				// 根据名称筛选
				sortMap.put("sysUserName", parameters.getSysUserName().trim());
			}
			// 排序
			if (parameters.getSort() != null && parameters.getOrder() != null) {
				sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
			}else{
				sortMap.put("sortVal", "order by createTime desc");
			}
			
			SysUser user = (SysUser)request.getSession().getAttribute("session_user");
            if(user !=null && user.getId()!=1){
            	sortMap.put("smsUserVal", "select id from sys_user where id="+user.getId()+" or superiorId = "+user.getId()+""); 
            }
			if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
				dzdPageParam.setStart(parameters.getPagenum());
				dzdPageParam.setLimit(parameters.getPagesize());
			}
			dzdPageParam.setCondition(sortMap);
			List<SysMenuBtn> sysMenuBtns = null;
			if (menuId != null) {
				sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
			}
			Page<SysUser> dataList = sysUserService.queryUserList(dzdPageParam);
			
			if (!CollectionUtils.isEmpty(dataList)) {
				for (SysUser sysUser : dataList.getResult()) {
					sysUser.setSysMenuBtns(sysMenuBtns);
				}
				dzdPageResponse.setRows(dataList.getResult());
				dzdPageResponse.setTotal(dataList.getTotal());
			}
			HtmlUtil.writerJson(response, dzdPageResponse);
		} catch (Exception e) {
			log.error(null, e);
			e.printStackTrace();
		}
	}



	@RequestMapping(value = "/form/{sysUserId}")
	public String sysUserForm(HttpServletRequest request, Model model, @PathVariable String sysUserId) {
		try {
			if (sysUserId == null) { // 新增
				return "sysuser/addsysuser";
			} else {
				SysUser sysUser = sysUserService.queryById(Integer.parseInt(sysUserId));
				model.addAttribute("sysUser", sysUser);
			}
		} catch (Exception e) {
			log.error(null, e);
			e.printStackTrace();
			return "errorpage/404";
		}
		return "sysuser/addsysuser";
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param data
	 * @return
	 */
	@MethodDescription("操作系统用户")
	@RequestMapping(value = "/from/merge", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public DzdResponse merge(HttpServletRequest request, @RequestBody Map<String, Object> data) {
		DzdResponse dzdPageResponse = new DzdResponse();
		try {
			SysUser sysUser = getRequestParameters(SysUser.class, data);
			Object roleId = data.get("roleIds");
			Object sateType = data.get("sateType");
			SysUser user = (SysUser)request.getSession().getAttribute("session_user");
			
			List<String> roleIds = null;
			if (roleId != null) {
				roleIds = (List<String>) roleId;
			}
			if(sateType!=null && !"".equals(sateType) && "1".equals(sateType)){
				sysUser.setSuperiorId(user.getId());  //设置上级ID(团队ID)
				roleIds.add("48");
			}
			
			if (sysUser.getId() == null) {
				sysUser.setState(1);
				sysUser.setDeleted(0);
				sysUser.setSuperAdmin(0);
				String newPwd = "123456";
				String pwd = MethodUtil.MD5(newPwd);
				sysUser.setPwd(pwd);
				sysUserService.saveUser(sysUser, roleIds);
			} else {
				sysUserService.updateUser(sysUser, roleIds);
				
			}
			List<SysUser> list =  sysUserService.querySysUserList();

			//修改sms_user等级
			SmsUser smsUser=new SmsUser();
			smsUser.setSysUserId(Integer.parseInt(data.get("id").toString()));
			//一级用户
			if(roleIds.contains(Define.ROLEID.ROLE_ADMINISTRATOR) ||
					roleIds.contains(Define.ROLEID.ROLE_FIRST_LEVEL_ADMINISTRATOR) ||
					roleIds.contains(Define.ROLEID.ROLE_CUSTOMER_SERVICE)){
               smsUser.setUserLevel(Define.ROLEID.LEVEL_1);
			}else if(roleIds.contains(Define.ROLEID.ROLE_SALES_MANAGER) ||
					roleIds.contains(Define.ROLEID.ROLE_USER_ADMINISTRATOR) ||
					(roleIds.contains(Define.ROLEID.ROLE_SALESMAN))){
				//二级用户
				smsUser.setUserLevel(Define.ROLEID.LEVEL_2);
			}else{   //roleids ==60
				//三级用户
				smsUser.setUserLevel(Define.ROLEID.LEVEL_3);
			}
			//修改用户等级
            smsUserService.updateUserLevel(smsUser);

			dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
			dzdPageResponse.setData(list);
		} catch (Exception e) {
			log.error(null, e);
			dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
			e.printStackTrace();
		}
		return dzdPageResponse;
	}

	/**
	 * 删除用户
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@MethodDescription("删除系统用户")
	@RequestMapping(value = "/from/delete", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public DzdResponse postsDelete(HttpServletRequest request, Model model) {
		DzdResponse response = new DzdResponse();
		try {
			String sysUserId = request.getParameter("sysUserId");
			sysUserService.deleteUser(Integer.parseInt(sysUserId));
			
			List<SysUser> list =  sysUserService.querySysUserList();
			response.setData(list);
			response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
		} catch (Exception e) {
			response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
			e.printStackTrace();
		}
		return response;
	}


	
	/**
	 * 根据编号查询用户
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/formEdit")
	@ResponseBody
	public DzdResponse sysUserEdit(HttpServletRequest request, Model model) {
		DzdResponse dzdPageResponse = new DzdResponse();
		try {
			Object id = request.getParameter("id");
			if (id == null) {
				dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
				return dzdPageResponse;
			}
			SysUser sysUser = sysUserService.queryById(Integer.parseInt(id.toString()));
			DzdPageParam dzdPageParam = new DzdPageParam();
			// 查询角色列表
			Page<SysRole> dataList = sysRoleService.queryRoleList(dzdPageParam);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("sysUserId", id);
			dzdPageParam.setCondition(condition);
			// 查询用户拥有的角色
			List<SysRoleRel> sysRoleRels = sysRoleRelService.queryRoleByUserId(dzdPageParam);
			sysUser.setSysRoleRels(sysRoleRels);
			JspResponseBean jspResponseBean = new JspResponseBean();
			if (!CollectionUtils.isEmpty(dataList)) {
				jspResponseBean.setDataList(dataList.getResult());
			}
			jspResponseBean.setData(sysUser);
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
	 * 批量删除系统用户
	 *
	 * @param request
	 * @param data
	 * @return
	 */
	@MethodDescription("批量删除系统用户")
	@RequestMapping(value = "formDelete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public DzdResponse userDelete(HttpServletRequest request, @RequestBody Map<String, Object> data) {
		DzdResponse response = new DzdResponse();
		try {
			Object ids = data.get("uids");
			if (ids == null) {
				response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
				return response;
			}
			List<Integer> uids = (List<Integer>) ids;
			if (uids == null || uids.size() == 0) {
				response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
			} else {
				sysUserService.deleteUsers(uids);
				response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
			}
		} catch (Exception e) {
			log.error(null, e);
			response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
			e.printStackTrace();
		}
		return response;
	}
	
	 /**
	  * 
	  * @Description:根据用户ID查询拥有角色
	  * @author:oygy
	  * @time:2016年12月31日 上午11:12:53
	  */
	 private List<SysRoleRel> queryRoleByUserId(Integer uid){
	    DzdPageParam dzdPageParam = new DzdPageParam();
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("sysUserId", uid);
		dzdPageParam.setCondition(condition);
		List<SysRoleRel> sysRoleRels = sysRoleRelService.queryRoleByUserId(dzdPageParam);
		return sysRoleRels;
	 }
}
