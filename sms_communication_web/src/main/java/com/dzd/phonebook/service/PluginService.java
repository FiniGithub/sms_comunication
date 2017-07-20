package com.dzd.phonebook.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.PluginDao;
import com.dzd.phonebook.entity.Plugin;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsSendTask;
import com.github.pagehelper.Page;

/**
 * @Description:插件Service
 * @author:oygy
 * @time:2017年1月13日 上午9:28:39
 */
@Service("pluginService")
public class PluginService<T> extends BaseService<T> {
	private final static Logger log = Logger.getLogger(PluginService.class);

	  	@Autowired
	    private PluginDao<T> mapper;

	    public PluginDao<T> getDao() {
	        return mapper;
	    }

	public Page<Plugin> queryPluginlist(DzdPageParam dzdPageParam){
		return getDao().queryPluginlistPage(dzdPageParam);
	}


	/**
	 * 
	 * @Description:保存插件信息
	 * @author:oygy
	 * @time:2017年3月3日 下午5:19:31
	 */
	public void savePlugin(Plugin plugin){
		getDao().savePlugin(plugin);
	}
	
	/**
	 * 
	 * @Description:根据上传的jar包名判断此包是否已存在
	 * @author:oygy
	 * @time:2017年3月3日 下午5:47:41
	 */
	public Integer queryPluginId(String path){
		return getDao().queryPluginId(path);
	}
	
	/**
	 * 
	 * @Description:根据上传jar包名修改插件内容
	 * @author:oygy
	 * @time:2017年3月3日 下午5:48:50
	 */
	public void updatePlugin(Plugin plugin){
		getDao().updatePlugin(plugin);
	}
	
	/**
	 * 
	 * @Description:删除插件
	 * @author:oygy
	 * @time:2017年3月6日 上午9:42:08
	 */
	public void deletePlugin(Integer pid){
		getDao().deletePlugin(pid);
	}
	
	/**
	 * 
	 * @Description:查询通道中是否用引用当前通道
	 * @author:oygy
	 * @time:2017年3月6日 上午10:35:21
	 */
	public Integer querySmsAislePid(Integer pid){
		return getDao().querySmsAislePid(pid);
	}
	
	
}