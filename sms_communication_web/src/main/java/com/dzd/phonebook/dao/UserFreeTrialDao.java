package com.dzd.phonebook.dao;

import java.util.List;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.FreeTria;
import com.dzd.phonebook.util.UserFreeTria;
import com.github.pagehelper.Page;
/**
 * 代理模板Dao
 * @Description:
 * @author:oygy
 * @time:2017年1月19日 上午10:41:29
 */
public interface UserFreeTrialDao<T> extends BaseDao<T> {

	  public  Page<UserFreeTria>  queryUserFreeTriaByPage(DzdPageParam dzdPageParam);
	  
	  public  Page<UserFreeTria>  sysQueryUserFreeTriaByPage(DzdPageParam dzdPageParam);
	  
	  public  Page<UserFreeTria>  sysQueryFreeTrialByPage(DzdPageParam dzdPageParam);
	  
	  public  Page<UserFreeTria>  sysQuerySignatureByPage(DzdPageParam dzdPageParam);
	  
	  public  List<FreeTria>  queryFreeTriaById(Integer id);
	  
	  public void saveUserFreeTria(UserFreeTria userFreeTria);
	  
	  public void sysSaveUserFreeTria(UserFreeTria userFreeTria);
	  
	  public void sysAddFreeTrial(UserFreeTria userFreeTria);
	  
	  public void sysSaveUserSignature(UserFreeTria userFreeTria);
	  
	  public Integer queryCountUserFreeTria(UserFreeTria userFreeTria);
	  
	  public Integer queryCountUserSignature(UserFreeTria userFreeTria);
	  
	  public UserFreeTria queryUserFreeTriaById(Integer id);
	  
	  public void updateUserFreeTria(UserFreeTria userFreeTria);
	  
	  public void deleteUserFreeTriaById(Integer id);
	  
	  public void auditUserFreeTriaById(UserFreeTria userFreeTria);
	  
	  public void updateFreeTriaById(UserFreeTria userFreeTria);
	  
	  public void updateSysSignatureaById(UserFreeTria userFreeTria);
	
	  /**
	   * 查询用户对应的模板
	   * @param userId
	   * @return
	   */
	  public List<T> queryFreeTriaBySmsUserId(Integer userId);
	  
	  public FreeTria queryFreeTriaByContent(String content);
}
