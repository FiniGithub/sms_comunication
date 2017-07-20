package com.dzd.phonebook.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.UserFreeTrialDao;
import com.dzd.phonebook.dao.UserMsmSendDao;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.FreeTria;
import com.dzd.phonebook.util.UserFreeTria;
import com.github.pagehelper.Page;

/**
 * 免审模板服务类
 *
 * @author ougy
 * @date 2016-6-24
 */
@Service("userFreeTrialService")
public class UserFreeTrialService<T> extends BaseService<T> {
	  private final static Logger log = Logger.getLogger(UserFreeTrialService.class);

	   @Autowired
	    private  UserFreeTrialDao<T> mapper;

	    public  UserFreeTrialDao<T> getDao() {
	        return mapper;
	    }
	    
	    
	   public  Page<UserFreeTria>  queryUserFreeTriaByList(DzdPageParam dzdPageParam){
		   return getDao().queryUserFreeTriaByPage(dzdPageParam);
	   }
	   
	   public  Page<UserFreeTria>  sysQueryUserFreeTriaByList(DzdPageParam dzdPageParam){
		   return getDao().sysQueryUserFreeTriaByPage(dzdPageParam);
	   }
	   
	   public  Page<UserFreeTria>  sysQueryFreeTrialByList(DzdPageParam dzdPageParam){
		   return getDao().sysQueryFreeTrialByPage(dzdPageParam);
	   }
	   
	   public  Page<UserFreeTria>  sysQuerySignatureByList(DzdPageParam dzdPageParam){
		   return getDao().sysQuerySignatureByPage(dzdPageParam);
	   }
	   
	   public  List<FreeTria>  queryFreeTriaById(Integer id){
		   return getDao().queryFreeTriaById(id);
	   }
	   
	   
	   public void saveUserFreeTria(UserFreeTria userFreeTria){
		   getDao().saveUserFreeTria(userFreeTria);
	   }
	   
	   public void sysSaveUserFreeTria(UserFreeTria userFreeTria){
		   getDao().sysSaveUserFreeTria(userFreeTria);
	   }
	   
	   public void sysAddFreeTrial(UserFreeTria userFreeTria){
		   getDao().sysAddFreeTrial(userFreeTria);
	   }
	   
	   public void sysSaveUserSignature(UserFreeTria userFreeTria){
		   getDao().sysSaveUserSignature(userFreeTria);
	   }
	   
	   /**
	    * @Description:查询是否已存在改报备模板
	    * @author:oygy
	    * @time:2017年4月6日 下午3:14:03
	    */
	   public Integer queryCountUserFreeTria(UserFreeTria userFreeTria){
		   return getDao().queryCountUserFreeTria(userFreeTria);
	   }
	   
	   /**
	    * @Description:查询是否已存在改报备签名
	    * @author:oygy
	    * @time:2017年4月6日 下午3:14:03
	    */
	   public Integer queryCountUserSignature(UserFreeTria userFreeTria){
		   return getDao().queryCountUserSignature(userFreeTria);
	   }
	   
	   
	   public void updateUserFreeTria(UserFreeTria userFreeTria){
		   getDao().updateUserFreeTria(userFreeTria);
	   }
	   
	 
	   public UserFreeTria queryUserFreeTriaById(Integer id){
		  return getDao().queryUserFreeTriaById(id);
	   }
	   
	   public void deleteUserFreeTriaById(Integer id){
		   getDao().deleteUserFreeTriaById(id);
	   }
	   
	   public void auditUserFreeTriaById(UserFreeTria userFreeTria){
		   getDao().auditUserFreeTriaById(userFreeTria);
	   } 
	   
	   public void updateFreeTriaById(UserFreeTria userFreeTria){
		   getDao().updateFreeTriaById(userFreeTria);
	   } 
	   
	   public void updateSysSignatureaById(UserFreeTria userFreeTria){
		   getDao().updateSysSignatureaById(userFreeTria);
	   } 
	   
	   
	   /**
		* 查询用户对应的模板
		* @param userId
		* @return
		*/
		public List<T> queryFreeTriaBySmsUserId(Integer userId){
		   return getDao().queryFreeTriaBySmsUserId(userId);
		}
		
		public FreeTria queryFreeTriaByContent(String content){
			return getDao().queryFreeTriaByContent(content);
		}
}
