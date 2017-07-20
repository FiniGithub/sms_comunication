package com.dzd.phonebook.dao;

import java.util.List;
import java.util.Map;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsAisleGroupHasSmsUser;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.sms.service.data.SmsPullReport;
import com.dzd.sms.service.data.SmsReciveReply;
import org.apache.ibatis.annotations.Param;

public interface UserMessageDao<T> extends BaseDao<T> {

    public SmsUser querySmsUserById(Integer sysId);

    /**
     * 根据用户账号查询用户信息
     * @param account
     * @return
     */
    public SmsUser querySmsUserBySmsUserAccount(String account);

    public List<SmsUser> querySmsUserByBId(Integer id);

    /**
     * 查询充值账号
     *
     * @param smsId
     * @param superAdmin
     * @return
     */
    public List<SmsUser> queryRechargeSmsUserList(@Param("smsId") Integer smsId,@Param("superAdmin") Integer superAdmin);

    public List<SmsPullReport> getSmsPullReport(Map<String, Object> param);

    public List<SmsReciveReply> getSmsReply(Map<String, Object> param);

    public List<String> querySmsUserSign(Map<String, Object> param);

    public List<SmsAisleGroupHasSmsUser> querySmsUserPrice(Integer id);

    public void updateSmsUserById(SmsUser smsUser);

    public void updateUserAg(SmsAisleGroupHasSmsUser smsAisleGroupHasSmsUser);

    public void addUserAg(SmsAisleGroupHasSmsUser smsAisleGroupHasSmsUser);

    public void updateSmsUserKey(SmsUser smsUser);

    public String querySmsUserKeyById(Integer uid);

    public String querySysConfig(String key);

    /**
     * 根据类型查找通道组
     *
     * @param tid
     * @return
     */
    public SmsAisleGroup querySmsAisleGroupByTid(Integer tid);
}
