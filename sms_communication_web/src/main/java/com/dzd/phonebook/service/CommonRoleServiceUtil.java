package com.dzd.phonebook.service;

import com.dzd.base.util.SessionUtils;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.sms.application.Define;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dzd-technology01 on 2017/6/14.
 */
public class CommonRoleServiceUtil {
    private SysRoleRelService sysRoleRelService;
    private SmsAccountService smsAccountService;

    public List<SysUser> getSysUserBUserList(HttpServletRequest request, SmsUserService userService, SysRoleRelService rolService, SmsAccountService accountService) {
        this.sysRoleRelService = rolService;
        this.smsAccountService = accountService;
        SysUser user = SessionUtils.getUser(request);
        List<SysRoleRel> sysRoleRels = queryRoleByUserId(user.getId(),sysRoleRelService);
        List<SysUser> sysUserList = new ArrayList<SysUser>();  //用户 "归属" 查询一二级用户
        List list2 = new ArrayList();
        //当前登陆用户id
        list2.add(user.getId());

        //查询登陆用户下面所有子归属（下拉列表“归属”）
        for (SysRoleRel sysRoleRel : sysRoleRels) {
            //如果当前登录用户为 “一级管理员”“业务员” 和“销售经理”
            if (sysRoleRel.getRoleId() == Integer.parseInt(Define.ROLEID.ROLE_FIRST_LEVEL_ADMINISTRATOR) ||
                    sysRoleRel.getRoleId() == Integer.parseInt(Define.ROLEID.ROLE_SALES_MANAGER)
                    || sysRoleRel.getRoleId() == Integer.parseInt(Define.ROLEID.ROLE_SALESMAN)) {
                //“一级管理员”“业务员” 和“销售经理”下面的所有“归属”用户id(包括归属用户下面的归属用户，多级子归属)
                list2.addAll(getIds(list2,smsAccountService,0));
                sysUserList = userService.queryBySysUserId(list2);
            }
            //如果当前登录用户为  "客服" 权限与一级管理员平级，即可查看所有角色为“一级管理员”账户下所有用户的子归属和自己用户下面的子归属
            if (sysRoleRel.getRoleId() == Integer.parseInt(Define.ROLEID.ROLE_CUSTOMER_SERVICE)) {
                //查询所有角色为一级管理员的用户
                List<SysRoleRel> roleRels = sysRoleRelService.queryByRoleId(Integer.parseInt(Define.ROLEID.ROLE_FIRST_LEVEL_ADMINISTRATOR), 1);
                for (int i = 0; i < roleRels.size(); i++) {
                    //所有角色为一级管理员的用户id
                    list2.add(roleRels.get(i).getObjId());
                }
                list2.addAll(getIds(list2,smsAccountService,Integer.parseInt(Define.ROLEID.ROLE_CUSTOMER_SERVICE)));
                sysUserList = userService.queryBySysUserId(list2);
            }
        }
        if (user.getSuperAdmin() == 1) {
            //超级管理员
            //查询所有“一、二级”用户
            List<Integer> list = new ArrayList<Integer>();
            list.add(Define.ROLEID.LEVEL_1);
            list.add(Define.ROLEID.LEVEL_2);
            sysUserList = userService.queryByUserLevel(list);
        }
        return sysUserList;
    }

    /**
     * @Description:根据用户ID查询拥有角色
     * @author:oygy
     * @time:2016年12月31日 上午11:12:53
     */
    public List<SysRoleRel> queryRoleByUserId(Integer uid,SysRoleRelService sysRoleRelService) {
        DzdPageParam dzdPageParam = new DzdPageParam();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("sysUserId", uid);
        dzdPageParam.setCondition(condition);
        List<SysRoleRel> sysRoleRels = sysRoleRelService.queryRoleByUserId(dzdPageParam);
        return sysRoleRels;
    }


    /**
     * 根据登陆用户id查询下面所有子归属
     *
     * @param list
     */
    public List getIds(List list,SmsAccountService smsAccountService,Integer roleId) {
        List<Integer> result = smsAccountService.queryBids(list);
        if(roleId==0){  //非“客户”用户登陆查询，防止自己归属自己 查询出现死循环
            for(int i=0;i<list.size();i++){
                if(result.contains(list.get(i))){
                    result.remove(list.get(i));
                }
            }
        }

        if (result.size() > 0) {
            List<Integer> list1 = smsAccountService.queryBids(result);
            if (list1.size() > 0) {
                result.addAll(list1);
            }
            while (list1.size() > 0) {
                list1 = smsAccountService.queryBids(list1);
                if (list1.size() > 0) {
                    result.addAll(list1);
                }
            }
        }
        return result;
    }
}
