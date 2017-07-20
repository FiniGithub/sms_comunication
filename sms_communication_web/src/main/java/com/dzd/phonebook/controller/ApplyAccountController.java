package com.dzd.phonebook.controller;

import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.*;
import com.dzd.phonebook.util.*;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 账户中心---申请账户
 * Created by wangran on 2017/5/22.
 */
@Controller
@RequestMapping("/applyaccount")
public class ApplyAccountController extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(ApplyAccountController.class);

    @Autowired
    private SysMenuBtnService sysMenuBtnService;

    @Autowired
    private SysUserService<SysUser> sysUserService;

    @Autowired
    private SmsUserService smsUserService;

    @Autowired
    private SysRoleRelService sysRoleRelService;

    @Autowired
    private SmsApplyAccountService smsApplyAccountService;

    @Autowired
    private SmsManageMentAccountService smsManagementAccountService;

    @Autowired
    private SmsUserBlankService smsUserBlankService;

    @Autowired
    private SmsAccountService smsAccountService;

    @RequestMapping("/listview")
    public String list(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        //菜单id
        model.addAttribute("menuId", menuId);
        return "account/applyAccount";
    }

    /**
     * @Description:申请账户
     * @author:wangran
     * @time:2017年5月18日 下午2:01:34
     */
    @RequestMapping(value = "/applyAccountlist", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse applayAccount(HttpServletRequest request, @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");
            Object phone = data.get("phone");

            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            List<SysRoleRel> sysRoleRels = queryRoleByUserId(user.getId());

            if (menuId == null || user == null || sysRoleRels == null) {
                return dzdPageResponse;
            }
            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }

            if (phone != null && !StringUtil.isEmpty(phone.toString())) {
                sortMap.put("phone", Integer.parseInt(phone.toString()));
            }

            if (user != null && user.getSuperAdmin() != 1) {
                //  sortMap.put("smsUserVal", "select id from sys_user where id="+user.getId()+" or superiorId = "+user.getId()+"");
                List<Integer> list = new ArrayList<Integer>();
                list.add(user.getId());
                //查询登录用户下面的所有归属用户sys_user_id
                list.addAll(new CommonRoleServiceUtil().getIds(list, smsAccountService, 0));
                sortMap.put("smsUserVal", list);
            }
            //排序
            sortMap.put("sortVal", "order by createTime desc");
            dzdPageParam.setCondition(sortMap);

            //菜单按钮权限
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                if (user.getSuperAdmin() == 1) {// 管理员查询所有菜单按钮
                    sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
                } else {// 其余角色查询配置好的按钮
                    sysMenuBtns = sysMenuBtnService.queryMenuListByRole(Integer.parseInt(menuId.toString()), user.getId());
                }
            }
            dzdPageResponse.setData(sysMenuBtns);


            Page<SmsUser> dataList = smsApplyAccountService.queryApplyAccountlistPage(dzdPageParam);

            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsUser instruct : dataList.getResult()) {
                    instruct.setSysMenuBtns(sysMenuBtns);
                }
                dzdPageResponse.setRows(dataList.getResult());
                dzdPageResponse.setTotal(dataList.getTotal());
            }
        } catch (Exception e) {
            logger.error("====================》申请账户查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return dzdPageResponse;
    }


    /**
     * 账户管理新增或修改
     *
     * @param request
     * @return
     */
    @MethodDescription("申请账户新增或修改")
    @RequestMapping(value = "/from/merge", method = RequestMethod.POST)
    public String merge(HttpServletRequest request, SmsUser smsUser) {
        Object menuId = request.getParameter("menuId");
        try {
            SysUser sysUser = new SysUser();                  //分配给代理账户的系统账号
            Integer id = smsUser.getId();
            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            Integer proposerId = user.getId();                //当前登陆人id

            if (StringUtil.isEmpty(smsUser.getSignature())) {
                smsUser.setSignature(null);
            }

            if (id == null) {
                log.info("-----------------》新增申请账户");
                //1.分配系统账号
                sysUser.setState(1);
                sysUser.setDeleted(0);
                sysUser.setUserType(2);
                sysUser.setSuperAdmin(0);
                sysUser.setEmail("");

                sysUserService.saveUser(sysUser, null);


                //2.添加申请账户信息
                smsUser.setSysUserId(sysUser.getId());
                //上级ID(申请人id)
                smsUser.setProposerId(Integer.parseInt(proposerId.toString()));
                //设置注册状态默认为“未注册”
                smsUser.setCheckState(0);

                smsApplyAccountService.saveSmsUserApplyAccount(smsUser);

            } else {
                log.info("-----------------》修改申请账户");
                smsUserService.updateSmsUser(smsUser);
            }
        } catch (Exception e) {
            logger.error("====================》申请账户新增或者修改失败：" + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/applyaccount/listview.do?id=" + menuId;
    }

    /**
     * @Description:根据新增账户信息查询该账号是否已分配手机号是否存在
     * @author:wangran
     * @time:2017年5月19日 上午11:56:29
     */
    @RequestMapping(value = "/querySmsUserbyname", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse merge(HttpServletRequest request) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            SmsUser smsUser = new SmsUser();
            String id = request.getParameter("id");
            String username = request.getParameter("name");      //获取分配的账户名
            if (id != null && !"".equals(id)) {
                smsUser.setId(Integer.parseInt(id.toString()));
            }
            smsUser.setName(username);
            int num = smsApplyAccountService.querySmsUserName(smsUser);
            if (num > 0) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            } else {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            }
        } catch (Exception e) {
            logger.error("====================》根据新增账户信息查询该账号是否已分配失败：" + e.getMessage());
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    /**
     * @Description:根据ID查询账户信息
     * @author:wangran
     * @time:2017年5月20日 上午11:55:53
     */
    @RequestMapping(value = "/formEdit")
    @ResponseBody
    public DzdResponse edit(HttpServletRequest request) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            Object id = request.getParameter("id");
            if (id == null) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdPageResponse;
            }
            SmsUser smsUser = smsManagementAccountService.querySmsManageMentUser(Integer.parseInt(id.toString()));
            JspResponseBean jspResponseBean = new JspResponseBean();
            if (smsUser != null) {
                //解密密码
                jspResponseBean.setData(smsUser);
                dzdPageResponse.setData(jspResponseBean);
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            } else {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            }
        } catch (Exception e) {
            logger.error("====================》据ID查询账户信息失败：" + e.getMessage());
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    /**
     * @Description:申请账户删除
     * @author:wangran
     * @time:2017年5月20日 上午12:55:53
     */
    @MethodDescription("申请账户删除")
    @RequestMapping(value = "/apply/delete", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse apply_delete(HttpServletRequest request, @RequestParam("ids[]") List<Integer> ids, @RequestParam("sysUserIds[]") List<Integer> sysUserIds) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            //删除账户sms_user
            smsApplyAccountService.deleteApplyAccount(ids);
            //删除账户信息表
            smsUserBlankService.deleteSmsUserBlank(ids);
            //删除用户sys_user
            sysUserService.deleteUsers(sysUserIds);

            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            logger.error("====================》据删除申请账户信息失败：" + e.getMessage());
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    /**
     * 申请新增页面跳转
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/applyview")
    public String applyview(HttpServletRequest request, Model model) throws Exception {
        String menuId = request.getParameter("menuId");
        String id = request.getParameter("id");
        if (id != null && !id.equals("")) {
            SmsUser smsUser = smsApplyAccountService.queryApplyAccountById(Integer.parseInt(id));
            model.addAttribute("smsUser", smsUser);
        }
        model.addAttribute("menuId", menuId);
        return "account/apply";
    }

    /**
     * @Description:根据用户ID查询拥有角色
     * @author:wangran
     * @time:2017年5月19日 上午11:12:53
     */
    private List<SysRoleRel> queryRoleByUserId(Integer uid) {
        DzdPageParam dzdPageParam = new DzdPageParam();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("sysUserId", uid);
        dzdPageParam.setCondition(condition);
        List<SysRoleRel> sysRoleRels = sysRoleRelService.queryRoleByUserId(dzdPageParam);
        return sysRoleRels;
    }
}
