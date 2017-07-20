package com.dzd.sms.api.service;

import static com.dzd.sms.application.Define.INTERFACE_STATE_BALANCE_NONE;
import static com.dzd.sms.application.Define.INTERFACE_STATE_KEY_INVALID;
import static com.dzd.sms.application.Define.INTERFACE_STATE_KEY_UNENABLE;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.CustomParameter;
import com.dzd.sms.service.data.SmsRequestParameter;
import com.dzd.sms.service.data.SmsUser;
import com.dzd.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @ClassName:     SmsSendBusiness
 * @Description: 短信发送的相关业务操作
 * @author:    hz-liang
 * @date:        2017年3月28日 下午6:04:31
 */
public class SmsQueryBusiness
{
	static Logger logger = Logger.getLogger(SmsQueryBusiness.class);

	/**
	 * 
	 * @Title: setPageLimit
	 * @Description: 设置页数
	 * @author:    hz-liang
	 * @param requestParameter
	 * @param parameter  
	 * @return: void   
	 * @throws
	 */
	public static void setPageLimit(SmsRequestParameter requestParameter, CustomParameter parameter)
	{
		Integer limit = 20;
		if ( requestParameter.getPage_size() != null )
		{
			limit = Integer.valueOf(requestParameter.getPage_size());
			if ( limit > 1000 )
				limit = 1000;
		}
		parameter.setLimit(limit);
	}

	/**
	 * 
	 * @Title: checkUserInfo
	 * @Description: 校验用户账户等信息
	 * @author:    hz-liang
	 * @param requestParameter
	 * @param parameter  
	 * @return: void   
	 * @throws
	 */
	public static void checkUserInfo(SmsRequestParameter requestParameter,
	        CustomParameter parameter, boolean isGetbalance, JSONObject result)
	{
		SmsUser user = SmsServerManager.I.getUser(Long.valueOf(requestParameter.getUid()));// 通过缓存取用户信息
		if ( user == null )
		{
			parameter.setCode(INTERFACE_STATE_BALANCE_NONE.code);
			parameter.setMsg(INTERFACE_STATE_BALANCE_NONE.message);
		} else
		{
			parameter.setUserId(user.getId());

			// MD5校验
			if ( StringUtil.string2MD5(
			        requestParameter.getSysId() + user.getKey() + requestParameter.getTimestamp())
			        .equals(requestParameter.getSign()) )
			{
				if ( user.getState() == 0 )
				{
					parameter.setValid(true);
					parameter.setUserId(user.getId());
					if ( isGetbalance )
					{
						result.put("surplus_num", SmsServerManager.I.getUserSurplusNum(user.getId()));
					}
				} else
				{
					parameter.setValid(false);
					parameter.setCode(INTERFACE_STATE_KEY_UNENABLE.code);
					parameter.setMsg(INTERFACE_STATE_KEY_UNENABLE.message);
				}
			} else
			{
				logger.error(" 签名错误。");
				parameter.setValid(false);
				parameter.setCode(INTERFACE_STATE_KEY_INVALID.code);
				parameter.setMsg(INTERFACE_STATE_KEY_INVALID.message);
			}
		}
	}
}
