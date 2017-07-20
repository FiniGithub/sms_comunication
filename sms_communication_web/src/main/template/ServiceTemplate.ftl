package ${servicePackage};

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.base.service.BaseService;
import ${daoPackage}.${className}Dao;

/**
 * 
 * <br>
 * <b>功能：</b>${className}Service<br>
 * <b>作者：</b>WHL<br>
 * <b>日期：</b> 2016-6-30 <br>
 * <b>版权所有：<b>版权所有(C) 2016，www.dzd.com<br>
 */
@Service("$!{lowerName}Service")
public class ${className}Service<T> extends BaseService<T> {
	private final static Logger log= Logger.getLogger(${className}Service.class);
	

	

	@Autowired
    private ${className}Dao<T> dao;

		
	public ${className}Dao<T> getDao() {
		return dao;
	}

}
