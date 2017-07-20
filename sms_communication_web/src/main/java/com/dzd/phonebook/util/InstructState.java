package com.dzd.phonebook.util;
/**
 * @Description:指令类型
 * @author:oygy
 * @time:2017年1月10日 下午12:02:10
 */
public class InstructState {
	
	
	
	/* redis订阅 通道 */
	public static final String   AB = "ab";

	/* 消息审核通过成功 */
	public static final String   AUDITSMS_SUCESS = "auditSmsSuccess";
	
	/* 添加代理用户 */
	public static final String   ADDSMSUSER_SUCESS = "addSmsUser";
	
	/* 修改代理用户 */
	public static final String   UPDATESMSUSER_SUCESS = "updateSmsUser";
	
	/* 分配通道组给代理用户 */
	public static final String   ALLOTAAISLEGROUP_SUCESS = "allotaAisleGroup";
	
	/* 分配通道组给代理用户 */
	public static final String   USERTOPUP_SUCESS = "userTopup";
	
	/* 添加通道 */
	public static final String   ADDSMSAISLE_SUCESS = "addSmsAisle";
	
	/* 修改通道 */
	public static final String   UPDATESMSAISLE_SUCESS = "updateSmsAisle";
	
	/* 删除通道 */
	public static final String   DELETESMSAISLE_SUCESS = "deleteSmsAisle";
	
	/* 添加通道组 */
	public static final String   ADDSMSAISLEGROUP_SUCESS = "addSmsAisleGroup";
	
	/* 修改通道组 */
	public static final String   UPDATESMSAISLEGROUP_SUCESS = "updateSmsAisleGroup";
	
	/* 删除通道组 */
	public static final String   DELETESMSAISLEGROUP_SUCESS = "deleteSmsAisleGroup";
	
	/* 屏蔽词 */
	public static final String   SMSWORDSHIELDING_SUCESS = "smsWordShielding";
	
	/* 黑名单 */
	public static final String   SMSBLACKLIST_SUCESS = "smsBlacklist";
	
	/* 删除黑名单 */
	public static final String   DELETESMSBLACKLIST_SUCESS = "deleteSmsBlacklist";
	
	/* 删除黑名单 */
	public static final String   STOPSMSSEND_SUCESS = "stopSmsSend";
	
	/* 添加用户免审模板 */
	public static final String   ADDUSERFREETRIAL_SUCESS = "addUserFreeTrial";
	
	/*修改用户免审模板 */
	public static final String   UPDATEUSERFREETRIAL_SUCESS = "updateUserFreeTrial";
	
	/*删除用户免审模板 */
	public static final String   DELETEUSERFREETRIAL_SUCESS = "deleteUserFreeTrial";
	
	/*上传插件 */
	public static final String   SAVEPLUGIN_SUCESS = "savePlugin";
	
	/*删除插件 */
	public static final String   DELETEPLUGIN_SUCESS = "deletePlugin";
	
	/*白名单 */
	public static final String  SAVEWHITELIST_SUCESS = "saveWhitelist";
	
	/*代理商注册 */
	public static final String  ZC_SUCESS = "zc";
	
	
}
