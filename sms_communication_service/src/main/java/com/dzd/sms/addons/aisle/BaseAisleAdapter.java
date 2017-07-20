package com.dzd.sms.addons.aisle;


import com.dzd.sms.service.data.SmsAisle;
import com.dzd.sms.service.data.SmsTaskData;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @Author WHL
 * @Date 2017-3-8.
 */
public interface BaseAisleAdapter extends Cloneable{

    public Long getAisleId();
    public void setAisleId(Long id);
    public int getThreadNumber();
    public void setThreadNumber(int num);
    public int getSinglePackageNumber();
    public void setSinglePackageNumber(int n);

    public String getAisleClassName();
    public void setAisleClassName(String aisleClassName);
    public SmsAisle getSmsAisle();
    public void setSmsAisle(SmsAisle smsAisle);
    public SmsTaskData getSmsTaskData();
    public void setSmsTaskData(SmsTaskData smsTaskData);

    public JSONObject getParams();
    public void setParams(JSONObject param);


    //以下是子类实现的方法
    public  boolean sendSinglePackage(SmsTaskData smsTaskData, List<String> mobileList);
    public  boolean queryReply();
    public  boolean queryReport();
    public  Double queryBalance();
    public  String pushReport(Map<String, String> params);
    public  String pushReply(Map<String, String> params);
    public BaseAisleAdapter clone();
}
