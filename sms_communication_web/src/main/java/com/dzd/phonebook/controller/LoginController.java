package com.dzd.phonebook.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.base.annotation.Auth;
import com.dzd.base.entity.BaseEntity;
import com.dzd.base.entity.TreeNode;
import com.dzd.base.util.Constant;
import com.dzd.base.util.MethodUtil;
import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.TreeUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.entity.SysLoginLog;
import com.dzd.phonebook.entity.SysMenu;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.page.CaptchaUtil;
import com.dzd.phonebook.service.ChannelService;
import com.dzd.phonebook.service.SmsUserService;
import com.dzd.phonebook.service.SysLoginLogService;
import com.dzd.phonebook.service.SysMenuService;
import com.dzd.phonebook.service.SysRoleRelService;
import com.dzd.phonebook.service.SysUserService;
import com.dzd.phonebook.service.UserMessageService;
import com.dzd.phonebook.util.CusAccessObjectUtil;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdParameters;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsUser;

/**
 * 登录controller
 *
 * @author chenchao
 * @date 2016-6-24 16:11:00
 */
@Controller
@RequestMapping("/")
public class LoginController {
    public static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private SysUserService<SysUser> sysUserService;

    @Autowired
    private SysMenuService<SysMenu> sysMenuService;

    @Autowired
    private SmsUserService smsUserService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private SysRoleRelService sysRoleRelService;

    @Autowired
    private SysLoginLogService sysLoginLogService;

    /**
     * @Description:登录页面访问
     * @author:oygy
     * @time:2016年12月30日 上午11:12:18
     */
    @Auth(verifyURL = false)
    @RequestMapping(value = "loginview")
    public String showPage() {
        return "login";
    }

    @RequestMapping(value = "captcha", method = RequestMethod.GET)
    @ResponseBody
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CaptchaUtil.outputCaptcha(request, response);
    }

    /**
     * 登录
     *
     * @param request
     * @param data
     * @return
     */
    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request, @RequestBody Map<String, String> data) {
        boolean bugFlag = false;

        Map<String, Object> response = new HashMap<String, Object>();
        String account = data.get("account");
        String password = data.get("password");
        String captcha = data.get("captcha");
        String type = data.get("type");
        //登录用户ip
        String ip = CusAccessObjectUtil.getIpAddress(request);
        SysUser sysUser = sysUserService.queryUserExist(account);
        try {
            HttpSession session = request.getSession();
            Object randomString = session.getAttribute("randomString");
            // 1.账户名密码登录
            if (type != null && !"".equals(type) && type.equals("login")) {
                if (bugFlag) {// debug 模式 不需要输入验证码
                    response = userLogin(account, password, request, session,ip);// 调用登录方法
                } else {
                    // 2. 用户不存在
                    if (sysUser == null) {
                        response.put("errorNum", 3);// 登录错误次数
                        response.put("msg", ErrorCodeTemplate.MSG_SYSUSER_EMPTY);
                        response.put("retCode", ErrorCodeTemplate.CODE_FAIL);
                        return response;
                    }else{
                        SysUser u = sysUserService.queryLogin(account, MethodUtil.MD5(password));
                        if(u == null){
                            response.put("errorNum", 3);// 登录错误次数
                            response.put("msg", ErrorCodeTemplate.MSG_SYSUSER_PWD_IS_MISS);
                            response.put("retCode", ErrorCodeTemplate.CODE_FAIL);
                            //保存登录日志
                            saveLoginLog(ErrorCodeTemplate.MSG_SYSUSER_PWD_IS_MISS, ip, account,sysUser.getId());
                            return response;
                        }
                    }

                    // 3. 登录
                    if (captcha != null && !"".equals(captcha) && randomString != null
                            && !"".equals(randomString.toString())
                            && captcha.trim().toUpperCase().equals(randomString.toString().trim().toUpperCase())) {
                        response = userLogin(account, password, request, session,ip);// 调用登录方法
                    } else if (captcha == null || captcha == "") {
                        response.put("msg", ErrorCodeTemplate.MSG_VERTIFYCODE_EMPTY);
                        response.put("retCode", ErrorCodeTemplate.CODE_FAIL);
                    } else {
                        response.put("msg", ErrorCodeTemplate.MSG_VERIFYCODE_ERROR);
                        response.put("retCode", ErrorCodeTemplate.CODE_FAIL);
                    }
                }
            }

			/*// 4. 注册后直接登录,无需校验验证码
            else if (type != null && !"".equals(type) && type.equals("register")) {
				response = userLogin(account, password, request, session);// 调用登录方法
			}*/
            else {
                response.put("msg", ErrorCodeTemplate.MSG_STATE_ERROR);
                response.put("retCode", ErrorCodeTemplate.CODE_FAIL);
            }
        } catch (Exception e) {
            log.error(null, e);
            response.put("msg", ErrorCodeTemplate.MSG_STATE_ERROR);
            response.put("retCode", ErrorCodeTemplate.CODE_FAIL);
        }
        return response;
    }

    /**
     * 保存用户登录日志
     *
     * @param ip
     * @param msg
     */
    public void saveLoginLog(String msg, String ip, String email, int userid) {
        try {
            SysLoginLog sysLoginLog = new SysLoginLog();
            sysLoginLog.setEmail(email);
            sysLoginLog.setLoginState(msg);
            sysLoginLog.setIp(ip);
            sysLoginLog.setSysUserId(userid);

            sysLoginLogService.saveLoginLog(sysLoginLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     *
     * @param account
     * @param password
     * @param request
     * @param session
     */
    public Map<String, Object> userLogin(String account, String password, HttpServletRequest request,
                                         HttpSession session,String ip) {
        Map<String, Object> response = new HashMap<String, Object>();

        SysUser u = sysUserService.queryLogin(account, MethodUtil.MD5(password));
        if (u == null) {
            Integer errorNum = sysUserService.queryErrorNumByEmail(account);
            if (errorNum == null) {
                errorNum = 0;
            }
            errorNum++;
            SysUser sysUser = new SysUser();
            sysUser.setEmail(account);
            sysUser.setErrorNum(errorNum);
            sysUserService.updateLoginErrorNumByEmail(sysUser);

            response.put("errorNum", errorNum);// 登录错误次数
            response.put("msg", ErrorCodeTemplate.MSG_SYSUSER_EMPTY);
            response.put("retCode", ErrorCodeTemplate.CODE_FAIL);
        } else {
            SysUser sysUser = new SysUser();
            sysUser.setEmail(account);
            sysUser.setErrorNum(0);
            sysUserService.updateLoginErrorNumByEmail(sysUser);

            // 设置User到Session
            SessionUtils.setUser(request, u);
            SysUser user = SessionUtils.getUser(request);
            // 设置代理商User到Session
            SmsUser smsUsers = userMessageService.querySmsUserById(user.getId());
            SessionUtils.setSmsUser(request, smsUsers);

            List<SysMenu> rootMenus = null;
            // List<SysMenuBtn> childBtns = null;
            // 超级管理员
            if (user != null && Constant.SuperAdmin.YES.key == user.getSuperAdmin()) {
                rootMenus = sysMenuService.queryRootSysMenuList();
            } else {
                // 根据用户查询角色再查询菜单
                DzdParameters dzdParameters = new DzdParameters();
                dzdParameters.setUserId(user.getId());
                rootMenus = sysMenuService.queryMenusByUserId(dzdParameters);
                // 根据用户id查找菜单 子父同一层级
                List<String> allUrls = sysMenuService.getActionUrls(dzdParameters);
                // 将用户对应的菜单放入session中
                SessionUtils.setAccessUrl(request, allUrls);// 设置可访问的URL
            }
            // 查询通道开通的时段
            SmsAisleGroup smsAisleGroup = channelService.querySmsAisleGroupById(smsUsers.getAisleGroupId());
            SessionUtils.setSmsAisleGroup(request, smsAisleGroup);


            // 更改用户首次登陆发送短信要输入验证码
            smsUsers.setVerifyType(0);
            smsUserService.updateSmsUserVerifyType(smsUsers);


            response.put("userType", user.getUserType());// 用户类型(1-代理管理,2-代理商)
            session.setAttribute("user", u);
            session.setAttribute("menuList", rootMenus);
            response.put("msg", ErrorCodeTemplate.MSG_SUCCESS_MSG);
            response.put("retCode", ErrorCodeTemplate.CODE_SUCESS);

            //保存登录日志
            saveLoginLog(ErrorCodeTemplate.MSG_SUCCESS_MSG, ip, account,u.getId());
        }
        return response;
    }

    /**
     * 查询登录次数
     *
     * @param request
     * @param data
     * @return
     */
    @RequestMapping(value = "queryErrorNum", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object queryErrorNum(HttpServletRequest request, @RequestBody Map<String, String> data) {
        String account = data.get("account");
        Integer errorNum = sysUserService.queryErrorNumByEmail(account);
        return errorNum;
    }

    /**
     * 咨询与反馈
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/zixunform")
    public String form() throws Exception {
        return "messageBoard/consult";
    }

    /**
     * 登陆页面 找回密码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/forgotPassword")
    public String forgotPassword() throws Exception {
        return "messageBoard/changepw";
    }

    /**
     * 登陆页面 公告须知
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/announcement")
    public String announcement() throws Exception {
        return "messageBoard/notice";
    }

    /**
     * 欢迎界面
     *
     * @return
     */
    @Auth(verifyURL = false)
    @RequestMapping(value = "welcome")
    public String showWelcome(HttpServletRequest request, Model model) {
		/*SysUser user = (SysUser) request.getSession().getAttribute("session_user");
		if (user.getId() != null) {
			try {
				int nums = 0;
				List<SysRoleRel> sysRoleRels = queryRoleByUserId(user.getId());
				int num = 0;
				for (SysRoleRel sysRoleRel : sysRoleRels) {
					if (sysRoleRel.getRoleId() == 3) { // 如果当前登录用户为管理员
						nums = 1;
						break;
					}
				}
				Map<String, Object> sortMap = new HashMap<String, Object>();
				if (user.getId() == 1 || nums == 1) { // 如果登入的用户是超级管理员或者管理员
					// 1.查询出新增会员
					Integer newSmsUserNum = smsUserService.queryCountNewSmsUser();
					// 2.查询出所有用户所剩下的条数
					Integer userSmsNum = smsUserService.queryCountUserSmsNum(sortMap);
					// 3.统计所有用户近7天发送数据
					List<SmsUser> smsAgentlist = smsUserService.queryCountSmsAgentStatistics(sortMap);
					// 4.查询所有用户今日购买条数
					Integer todaySmsNum = smsUserService.queryCountTodaySmsNum(sortMap);
					model.addAttribute("newSmsUserNum", newSmsUserNum);
					model.addAttribute("userSmsNum", userSmsNum);
					model.addAttribute("todaySmsNum", todaySmsNum);
					model.addAttribute("smsAgentlist", smsAgentlist);
					return "welcome";
				}
				for (SysRoleRel sysRoleRel : sysRoleRels) {
					if (sysRoleRel.getRoleId() == 51) { // 如果当前登录用户为经理
						nums = 2;
						break;
					}
				}
				if (nums == 2) {
					sortMap.put("smsUserVal", "select id from sys_user where id=" + user.getId() + " or superiorId = "
							+ user.getId() + "");
					// 1.查询出团队下用户所剩下的条数
					Integer userSmsNum = smsUserService.queryCountUserSmsNum(sortMap);
					// 3.统计团队下用户近7天发送数据
					List<SmsUser> smsAgentlist = smsUserService.queryCountSmsAgentStatistics(sortMap);
					// 4.查询所有用户今日购买条数
					Integer todaySmsNum = smsUserService.queryCountTodaySmsNum(sortMap);
					model.addAttribute("userSmsNum", userSmsNum);
					model.addAttribute("smsAgentlist", smsAgentlist);
					model.addAttribute("todaySmsNum", todaySmsNum);

					return "welcome";
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}*/

        return "dzd_default";
    }

    /**
     * @Description:根据用户ID查询拥有角色
     * @author:oygy
     * @time:2016年12月31日 上午11:12:53
     */
    private List<SysRoleRel> queryRoleByUserId(Integer uid) {
        DzdPageParam dzdPageParam = new DzdPageParam();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("sysUserId", uid);
        dzdPageParam.setCondition(condition);
        List<SysRoleRel> sysRoleRels = sysRoleRelService.queryRoleByUserId(dzdPageParam);
        return sysRoleRels;
    }

    /**
     * 注销
     *
     * @param request
     * @return
     */
    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "logout")
    public String logOut(HttpServletRequest request) {
        // 清空session
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/loginview.do#exit";
    }
    
    
    /**
     * 右侧菜单退出系统
     * @return
     */
    @RequestMapping("/exitMenu")
    public String exit(HttpServletRequest request){
    	// 清空session
        HttpSession session = request.getSession();
        session.invalidate();
    	return "redirect:/welcome.do";
    }

    @MethodDescription("修改密码")
    @RequestMapping(value = "updpassword")
    public String updatePwd(HttpServletRequest request, Model model) {
        Object id = request.getParameter("id");
        model.addAttribute("id", id);
        return "systemUser/updatePwd";
    }

    /**
     * 修改密码
     *
     * @param request
     * @param response
     * @param data
     * @return
     * @throws Exception
     */
    @Auth(verifyURL = false)
    @RequestMapping("updatePwd")
    @ResponseBody
    public DzdResponse updatePwd(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdResponse = new DzdResponse();
        Object idVal = data.get("id");
        Object oldPwdVal = data.get("oldPwd");
        Object newPwdVal = data.get("newPwd");
        if (idVal == null || oldPwdVal == null || newPwdVal == null) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            return dzdResponse;
        }
        try {
            // Integer id = Integer.parseInt(idVal.toString());
            String oldPwd = oldPwdVal.toString();
            String newPwd = newPwdVal.toString();
            SysUser user = SessionUtils.getUser(request);
            if (user == null) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }
            SysUser bean = sysUserService.queryById(user.getId());
            if (bean.getId() == null || BaseEntity.DELETED.YES.key == bean.getDeleted()) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }
            if (StringUtils.isBlank(newPwd)) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }
            // 不是超级管理员，匹配旧密码
            if (!MethodUtil.ecompareMD5(oldPwd, bean.getPwd())) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }
            bean.setPwd(MethodUtil.MD5(newPwd));
            sysUserService.updateBySelective(bean);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdResponse;
    }

    /**
     * 构建树形数据
     *
     * @return
     */
    @SuppressWarnings("unused")
    private List<TreeNode> treeMenu(List<SysMenu> rootMenus, List<SysMenu> childMenus) {
        TreeUtil util = new TreeUtil(rootMenus, childMenus);
        return util.getTreeNode();
    }

}
