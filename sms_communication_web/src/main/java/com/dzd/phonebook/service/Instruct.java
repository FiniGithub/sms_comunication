package com.dzd.phonebook.service;

/**
 * @Description:指令
 * @author:oygy
 * @time:2017年1月9日 下午7:05:32
 */
public class  Instruct {
	
	
	  // 定义私有构造方法（防止通过 new SingletonTest()去实例化）
/*    private Instruct() {   
    }   

    // 定义一个SingletonTest类型的变量（不初始化，注意这里没有使用final关键字）
    private static Instruct instance;   

    // 定义一个静态的方法（调用时再初始化SingletonTest，使用synchronized 避免多线程访问时，可能造成重的复初始化问题）
    public static synchronized  Instruct getInstance() {   
        if (instance == null)   
            instance = new Instruct();   
        return instance;   
    } 
	*/

	private  String key;                 //指令
	private  String smsSendId;			//消息ID
	private  String smsUserKey;			//用户唯一键
	private  String smsUserId;			//用户ID
	private  String smsAisleId;			//通道ID 
	private  String smsAisleGroupId;	//通道组ID
	private String  userIdString;		//用户拼接ID
	private String  phone;				//黑名单电话号码
	private String auditState;			//审核状态0：不通过 ，1：审核通过
	private String userFreeTrialId;     //用户免审模板添加ID
	private String pluginUrl;			//插件URL
	private Integer pid;                //插件ID
	
	
	public  String getKey() {
		return key;
	}
	public  void setKey(String key) {
		this.key = key;
	}
	public String getSmsSendId() {
		return smsSendId;
	}
	public void setSmsSendId(String smsSendId) {
		this.smsSendId = smsSendId;
	}
	public String getSmsUserKey() {
		return smsUserKey;
	}
	public void setSmsUserKey(String smsUserKey) {
		this.smsUserKey = smsUserKey;
	}
	public String getSmsUserId() {
		return smsUserId;
	}
	public void setSmsUserId(String smsUserId) {
		this.smsUserId = smsUserId;
	}
	public String getSmsAisleId() {
		return smsAisleId;
	}
	public void setSmsAisleId(String smsAisleId) {
		this.smsAisleId = smsAisleId;
	}
	public String getSmsAisleGroupId() {
		return smsAisleGroupId;
	}
	public void setSmsAisleGroupId(String smsAisleGroupId) {
		this.smsAisleGroupId = smsAisleGroupId;
	}
	public String getUserIdString() {
		return userIdString;
	}
	public void setUserIdString(String userIdString) {
		this.userIdString = userIdString;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAuditState() {
		return auditState;
	}
	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}
	public String getUserFreeTrialId() {
		return userFreeTrialId;
	}
	public void setUserFreeTrialId(String userFreeTrialId) {
		this.userFreeTrialId = userFreeTrialId;
	}
	public String getPluginUrl() {
		return pluginUrl;
	}
	public void setPluginUrl(String pluginUrl) {
		this.pluginUrl = pluginUrl;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}

}
