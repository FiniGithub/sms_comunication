package com.dzd.phonebook.dao;

import java.util.List;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SysLog;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsUser;
import com.github.pagehelper.Page;


/**
 * 用户接口
 *
 * @author chenchao
 * @date 2016-6-24
 */
public interface SysUserDao<T> extends BaseDao<T> {

    /**
     * 检查登录
     *
     * @param email
     * @param pwd
     * @return
     */
    public T queryLogin(SysUser sysUser);

    /**
     * 查询邮箱总数，检查是否存在
     *
     * @param email
     * @return
     */
    public int getUserCountByEmail(String email);

    public List<SysUser> querySysUserList();

    Page<SysUser> queryUserPage(DzdPageParam dzdPageParam);


    public void addSysLog(SysLog syslog);

    public List<SysUser> queryListUserByRoleId(Integer roleId);



    public Integer querySysUserByuserEmal(SmsUser smsUser);

    public void sevaSysRoleRels(Integer objId);

    public Integer queryfirmName(SmsUser smsUser);

    public void updateSysUserPwd(SysUser sysUser);

    public List<SysUser> queryYwSysUserList(Integer uid);

    public Integer queryTdUser(Integer ywId);

    /**
     * 查询用户是否存在
     *
     * @param email
     * @return
     */
    public SysUser queryUserExist(String email);

    public void updateSmsUserByteamId(T t);

    public void updateLoginErrorNumByEmail(T t);

    public Integer queryErrorNumByEmail(String email);
}
