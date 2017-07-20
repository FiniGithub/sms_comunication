package com.dzd.phonebook.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.dzd.phonebook.entity.SmsFileConfig;

/**
 * 发送短信工具类
 * @author CHENCHAO
 * @date 2017-03-30 11:19:00
 *
 */
public class SmsSendUtil {
	
	/**
	 * 获取短信条数
	 * @param content
	 * @return
	 */
	public static Integer getSmsLength(String content){
		Integer  smsLength = 1;
		if (content.length() > 70) {
			int smsTextLen = content.length() - 70;
			smsLength = (Integer) (smsTextLen / 67) + 1;
			// 尾数加1条
			if (smsTextLen % 67 > 0) {
				smsLength += 1;
			}
		}
		return smsLength;
	}
	
	
	
	/**
	 * 读取服务器上的文件，获取号码
	 * @param configList
	 * @return
	 */
	public static List<String> getPhoneListByConfig(List<SmsFileConfig> configList){
		List<String> phoneList = new ArrayList<String>();
		try{
			String catalinaHome = System.getProperty("catalina.home");
			
			for ( SmsFileConfig config : configList )
			{
				// 文件路径
				String fileName = config.getFileName();
				String txtPath = catalinaHome + "/" + fileName;
				File file = new File(txtPath);
				List<String> phones = FileUploadUtil.getPhoneListByFile(fileName, file);
				phoneList.addAll(phones);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return phoneList;
	}
	
	
	
	
	/**
	 * 获取号码字符串
	 * @param phoneList
	 * @param mobile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getValidPhoneList(List<String> phoneList){
		try{
			// 获取有效的号码数量
			Map<String, Object> map = FileUploadUtil.getPhoneMap(phoneList, null);
			List<String> validList =   (List<String>) map.get("validList");
			return validList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
