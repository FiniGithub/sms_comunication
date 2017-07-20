package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.Plugin;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;

public interface PluginDao<T> extends BaseDao<T>  {

	public Page<Plugin> queryPluginlistPage(DzdPageParam dzdPageParam);
	
	public void savePlugin(Plugin plugin);
	
	public Integer queryPluginId(String path);
	
	public void updatePlugin(Plugin plugin);
	
	public void deletePlugin(Integer pid);
	
	public Integer querySmsAislePid(Integer pid);
	
}