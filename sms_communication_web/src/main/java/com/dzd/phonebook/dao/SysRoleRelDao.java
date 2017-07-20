package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.util.DzdPageParam;

import java.util.List;


/**
 * SysRoleRel Mapper
 *
 * @author Administrator
 */
public interface SysRoleRelDao<T> extends BaseDao<T> {

    public void deleteByRoleId(java.util.Map<String, Object> param);

    public void deleteByObjId(java.util.Map<String, Object> param);


    public List<SysRoleRel> queryByRoleId(java.util.Map<String, Object> param);


    public List<SysRoleRel> queryByObjId(java.util.Map<String, Object> param);

    /**
     * 根据角色id查找七彩菜单
     *
     * @param dzdPageParam
     * @return
     */
    List<SysRoleRel> queryMenuByRoleId(DzdPageParam dzdPageParam);


    /**
     * 根据用户id查找角色
     *
     * @param dzdPageParam
     * @return
     */
    List<SysRoleRel> queryRoleByUserId(DzdPageParam dzdPageParam);

    /**
     * 根据objId 修改用户的角色信息
     * @param object
     */
    public void updateByObjId(Object object);

}
