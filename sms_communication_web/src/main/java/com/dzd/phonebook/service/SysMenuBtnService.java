package com.dzd.phonebook.service;

import java.util.List;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SysMenuBtnDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <br>
 * <b>功能：</b>SysMenuBtnService<br>
 * <b>作者：</b>JEECG<br>
 * <b>日期：</b> Dec 9, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Service("sysMenuBtnService")
public class SysMenuBtnService<T> extends BaseService<T> {
    private final static Logger log = Logger.getLogger(SysMenuBtnService.class);

    public List<T> queryByAll() {
        return getDao().queryByAll();
    }


    public List<T> queryByMenuid(Integer menuid) {
        return getDao().queryByMenuid(menuid);
    }

    public List<T> queryByMenuid2(Object param) {
        return getDao().queryByMenuid2(param);
    }


    public List<T> queryByMenuUrl(String url) {
        return getDao().queryByMenuUrl(url);
    }

    public void deleteByMenuid(Integer menuid) {
        getDao().deleteByMenuid(menuid);
    }

    public List<T> getMenuBtnByUser(Integer userid) {
        return getDao().getMenuBtnByUser(userid);
    }

    /**
     * 根据用户ID和菜单ID查询菜单按钮
     *
     * @param menuId
     * @param userId
     * @return
     */
    public List<T> queryMenuListByRole(Integer menuId, Integer userId) {
        return getDao().queryMenuListByRole(menuId, userId);
    }

    @Autowired
    private SysMenuBtnDao<T> mapper;


    public SysMenuBtnDao<T> getDao() {
        return mapper;
    }

}
