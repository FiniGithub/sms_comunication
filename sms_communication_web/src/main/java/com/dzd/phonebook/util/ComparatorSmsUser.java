package com.dzd.phonebook.util;

import java.util.Collections;
import java.util.Comparator;

import com.github.pagehelper.Page;

/** * 
@author  作者 
E-mail: * 
@date 创建时间：2017年5月17日 下午3:30:20 * 
@version 1.0 * 
@parameter  * 
@since  * 
@return  */
public class ComparatorSmsUser
{

	public static void comparatorListByAuditTime(Page<SmsUser> dataList){
		Collections.sort(dataList, new Comparator<SmsUser>(){  
			  
	        /*  
	         * 返回负数表示：o1 小于o2，  
	         * 返回0 表示：o1和o2相等，  
	         * 返回正数表示：o1大于o2。  
	         */  
	        public int compare(SmsUser o1, SmsUser o2) {  
	          
	            if(o1.getAuditTime().compareTo(o2.getAuditTime())==1){  
	                return -1;  
	            }  
	            if(o1.getAuditTime().equals(o2.getAuditTime())){  
	                return 0;  
	            }  
	            return 1;  
	        }  
	    }); 
	}
	
	public static void comparatorListBySucceedNum(Page<SmsUser> dataList){
		Collections.sort(dataList, new Comparator<SmsUser>(){  
			  
	        /*  
	         * 返回负数表示：o1 小于o2，  
	         * 返回0 表示：o1和o2相等，  
	         * 返回正数表示：o1大于o2。  
	         */  
	        public int compare(SmsUser o1, SmsUser o2) {  
	          
	            if(o1.getSendNum().compareTo(o2.getSendNum())==1){  
	                return -1;  
	            }  
	            if(o1.getSendNum().equals(o2.getSendNum())){  
	                return 0;  
	            }  
	            return 1;  
	        }  
	    }); 
	}

}
