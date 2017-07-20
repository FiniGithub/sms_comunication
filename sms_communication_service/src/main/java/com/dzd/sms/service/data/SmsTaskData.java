package com.dzd.sms.service.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/3
 * Time: 9:23
 */
public class SmsTaskData implements Serializable {

    Long taskId=0L;
    String name = "v2";             //任务名称
    String aisleName;               //发送通道名称
    Long aisleGroupId;              //通道组ID
    int taskType;               //任务类型
    Date taskSendTime;             //发送任务时间
    Date timing; 				// 定时发送时间
    Date auditTime=null;             //审核 时间
    Long userId = 0L;
    int sendWay=0;                  //发送方式， 0为HTTP,1为CMPP
    String text;
    String callbackUrl;

    Double expect_deduction; //预计扣费
    Double actual_deduction; //实际扣费
    int send_num; //发送数量
    int billing_num; //计费数量
    int error_phone_num=0; //错误号码数量
    int blacklist_phone_num=0;//黑名单数量



    boolean isFree = false;         //是否免审
    boolean isSave=false;           //是否保存
    boolean isPushSendQueue = false;    //是否已存入过队列
    boolean auditState = false;         //通过审核


    int returnState = 0;                //返还状态0 未返还， 1已返还
    //Date auditDate = null;              //通过审核时间
    int savePhoneNumber=0;        //保存号码数量
    int saveSendPhoneNumber=0;   //发送号码数量
    int saveReportPhoneNumber=0; //返回报告号码数量
    int updateReportPhoneNumber = 0;        //更新状态报告的数量

    boolean updateSendLogDatabase = false; //更新发送日志状态 把发送记录的状态全部更新到发送号码上
    boolean updateReportLogDatabase = false; //更新返回报告日志状态到数据库
    
    
    String phongText;   //重发号码
    int resendType;     //重发类型（0：状态重发，1：类型重发）
    int resendState;    //重发转态（0:待发送,1:发送失败）

    //List<SmsMessage> smsMessageList = new ArrayList<SmsMessage>();
    int count=0;
    public SmsTaskData(Long id, int count, Long userId ) {
        this.taskId = id;
        this.count = count;
        this.userId = userId;
    }

	public Date getTiming()
	{
		return timing;
	}

	public void setTiming(Date timing)
	{
		this.timing = timing;
	}

	public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAisleName() {
        return aisleName;
    }

    public void setAisleName(String aisleName) {
        this.aisleName = aisleName;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSavePhoneNumber() {
        return savePhoneNumber;
    }

    public void setSavePhoneNumber(int savePhoneNumber) {
        this.savePhoneNumber = savePhoneNumber;
    }

    public int getSaveSendPhoneNumber() {
        return saveSendPhoneNumber;
    }

    public void setSaveSendPhoneNumber(int saveSendPhoneNumber) {
        this.saveSendPhoneNumber = saveSendPhoneNumber;
    }

    public int getSaveReportPhoneNumber() {
        return saveReportPhoneNumber;
    }

    public void setSaveReportPhoneNumber(int saveReportPhoneNumber) {
        this.saveReportPhoneNumber = saveReportPhoneNumber;
    }

    public boolean isUpdateSendLogDatabase() {
        return updateSendLogDatabase;
    }

    public void setUpdateSendLogDatabase(boolean updateSendLogDatabase) {
        this.updateSendLogDatabase = updateSendLogDatabase;
    }

    public boolean isUpdateReportLogDatabase() {
        return updateReportLogDatabase;
    }

    public void setUpdateReportLogDatabase(boolean updateReportLogDatabase) {
        this.updateReportLogDatabase = updateReportLogDatabase;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public boolean isPushSendQueue() {
        return isPushSendQueue;
    }

    public void setPushSendQueue(boolean pushSendQueue) {
        isPushSendQueue = pushSendQueue;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getExpect_deduction() {
        return expect_deduction;
    }

    public void setExpect_deduction(Double expect_deduction) {
        this.expect_deduction = expect_deduction;
    }

    public Double getActual_deduction() {
        return actual_deduction;
    }

    public void setActual_deduction(Double actual_deduction) {
        this.actual_deduction = actual_deduction;
    }

    public int getSend_num() {
        return send_num;
    }

    public void setSend_num(int send_num) {
        this.send_num = send_num;
    }

    public int getBilling_num() {
        return billing_num;
    }

    public void setBilling_num(int billing_num) {
        this.billing_num = billing_num;
    }

    public int getError_phone_num() {
        return error_phone_num;
    }

    public void setError_phone_num(int error_phone_num) {
        this.error_phone_num = error_phone_num;
    }

    public int getBlacklist_phone_num() {
        return blacklist_phone_num;
    }

    public void setBlacklist_phone_num(int blacklist_phone_num) {
        this.blacklist_phone_num = blacklist_phone_num;
    }

    public Long getAisleGroupId() {
        return aisleGroupId;
    }

    public void setAisleGroupId(Long aisleGroupId) {
        this.aisleGroupId = aisleGroupId;
    }

    public boolean isAuditState() {
        return auditState;
    }

    public void setAuditState(boolean auditState) {
        this.auditState = auditState;
    }

    public Date getTaskSendTime() {
        return taskSendTime;
    }

    public void setTaskSendTime(Date taskSendTime) {
        this.taskSendTime = taskSendTime;
    }

    public int getReturnState() {
        return returnState;
    }

    public void setReturnState(int returnState) {
        this.returnState = returnState;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public int getSendWay() {
        return sendWay;
    }

    public void setSendWay(int sendWay) {
        this.sendWay = sendWay;
    }

    public int getUpdateReportPhoneNumber() {
        return updateReportPhoneNumber;
    }

    public void setUpdateReportPhoneNumber(int updateReportPhoneNumber) {
        this.updateReportPhoneNumber = updateReportPhoneNumber;
    }
//
//    public void addSmsMessage(SmsMessage smsMessage){
//        smsMessageList.add( smsMessage );
//    }
//
//    public List<SmsMessage> getSmsMessageList() {
//        return smsMessageList;
//    }
//
//    public void setSmsMessageList(List<SmsMessage> smsMessageList) {
//        this.smsMessageList = smsMessageList;
//    }
	public String getPhongText() {
		return phongText;
	}

	public void setPhongText(String phongText) {
		this.phongText = phongText;
	}

	public int getResendType() {
		return resendType;
	}

	public void setResendType(int resendType) {
		this.resendType = resendType;
	}

	public int getResendState() {
		return resendState;
	}

	public void setResendState(int resendState) {
		this.resendState = resendState;
	}
}
