package com.dzd.phonebook.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SmsUserMessage;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsAisleGroupHasSmsUser;
import com.dzd.phonebook.util.SmsAisleGroupType;
import com.dzd.phonebook.util.SmsRechargeUser;
import com.dzd.phonebook.util.SmsSendTask;
import com.dzd.phonebook.util.SmsSendTaskPhone;
import com.dzd.phonebook.util.SmsShieldWord;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.phonebook.util.SmsUserBlank;
import com.dzd.phonebook.util.SmsUserMoneyRunning;
import com.github.pagehelper.Page;

/**
 * 代理接口
 *
 * @author chenchao
 * @date 2016-6-24
 */
public interface SmsUserDao<T> extends BaseDao<T> {

    void updateSmsUserVerifyType(SmsUser smsUser);

    Page<SmsUser> querySmsUserListPage(DzdPageParam dzdPageParam);


    Page<SmsUserMessage> pushManagePage(DzdPageParam dzdPageParam);

    public Integer querySmsStatistical(DzdPageParam dzdPageParam);


    /**
     * 查询用户的类型
     *
     * @param groupId
     * @return
     */
    public SmsAisleGroupType queryGroupTypeBySmsUserId(Integer groupId);

    public void addSmsUserBlank(SmsUserBlank smsUserBlank);

    public void saveSmsUser(SmsUser smsUser);


    public void updateSmsUser(SmsUser smsUser);

    public void updateSmsUserBlank(SmsUserBlank smsUserBlank);

    public void deleteSmsAislegroupHasUser(Integer smsUid);

    public List<SmsAisleGroupHasSmsUser> queryUserHasGroup(Integer smsUid);

    public List<SmsAisleGroupHasSmsUser> queryVipUserHasGroup(Integer userLevel);

    public Integer querySurplusNum(Integer smsUid);

    public void updateSmsUserBlankMoney(SmsUser smsUser);


    public void saveSmsUserMoneyRunning(SmsUserMoneyRunning smr);

    public String querySmsUserKey(Integer smsUserId);

    public Page<SmsUser> querySmsUserStatisticalListPage(DzdPageParam dzdPageParam);

    public SmsUser querySmsUserStatisticalZong(DzdPageParam dzdPageParam);

    public Long querySmsUserStatisticalByUid(Integer smsUserId);

    public Page<SmsUser> querySmsUserStatisticalPage(DzdPageParam dzdPageParam);

    public Page<SmsUser> querySmsUserDLPage(DzdPageParam dzdPageParam);

    public Page<SmsUserMoneyRunning> querySmsUserPuserBillListPage(DzdPageParam dzdPageParam);

    /**
     * 导出消费记录
     *
     * @return
     */
    public List<SmsUserMoneyRunning> querySmsUserRechargeBillList(SmsUserMoneyRunning running);

    /**
     * 消费记录
     *
     * @param dzdPageParam
     * @return
     */
    public Page<SmsUserMoneyRunning> querySmsUserPuserConsumeBillListPage(DzdPageParam dzdPageParam);

    public SmsUserMoneyRunning querySmsUserPuserBillZong(DzdPageParam dzdPageParam);

    public List<SmsUser> stSmsUserPuserBillList();

    public void saeveStSmsUserPuserBill(SmsUser smsUser);

    public List<SmsUser> querySmsUserListTj();

    public SmsUserMoneyRunning querySmsUserMoneyRunningStatistical(Integer userId);

    public SmsUser querySendListStatistical(String email);

    public List<SmsUser> fillSmsUserStatisticalListPage(DzdPageParam filldzdPageParam);

    public List<SmsAisleGroup> querySmsGroupById(Integer gtype);

    public SmsUser querySmsUserBySysUid(Integer sysId);

    public Integer queryCountNewSmsUser();

    public Integer queryCountUserSmsNum(Map<String, Object> sortMap);

    public List<SmsUser> queryCountSmsAgentStatistics(Map<String, Object> sortMap);

    public void addSmsUser(SmsUser smsUser);

    public void updateSmsUserInfo(SmsUser smsUser);


    Page<SmsRechargeUser> rechargeRecordListPage(DzdPageParam dzdPageParam);

    public Integer queryCountTodaySmsNum(Map<String, Object> sortMap);

    public Date queryLastSmsUserTime();

    public void updateLastSmsUserTime(Date lastTime);

    public void addLastSmsUserTime(Date lastTime);

    public void addPushManage(SmsUserMessage smsUserMessage);

    /**
     * 关于充值请调用这个方法 - 新增
     *
     * @param moneyRunning
     */
    public void saveUserMoneyRunning(SmsUserMoneyRunning moneyRunning);

    /**
     * 修改用户信息
     *
     * @param smsUser
     */
    public void updateUser(SmsUser smsUser);
    
    /**
     * 增加屏蔽词
     * @param word
     */
    public void insertShieldingWord(String word);
    
    /**
     * 模糊删除屏蔽词
     * @param word
     */
    public void deleteShieldingWordFuzzy(String word);
    
    /**
     * 精确删除屏蔽词
     * @param word
     */
    public void deleteShieldingWordPrecise(String word);
    
    /**
     * 模糊查询屏蔽词
     * @param word
     */
    public List<SmsShieldWord> queryShieldingWordFuzzy(String word);
    
    /**
     * 精确查询屏蔽词
     * @param word
     */
    public List<SmsShieldWord> queryShieldingWordPrecise(String word);
    
    /**
     * 根据id修改短信内容
     * @param smsSendTask
     */
    public void updateSendTaskContent(SmsSendTask smsSendTask);
    
    /**
     * 根据id获取content，并返回Map
     * @param id
     */
    public List<SmsSendTask> querySendTaskContentById(String id);
    
    /**
     * 查询sms_send_task表中相关字段数据
     * @return
     */
    public List<SmsSendTask> querySmsSendTask(); 
    
    /**
     * 查询sms_send_task_phone表中相关字段数据
     * @return
     */
    public List<SmsSendTaskPhone> querySmsSendTaskPhone();
		
    /**
     * 查询短信平台统计信息
     * @return
     */
    public List<SmsUser> querySmsAgentStatistics();
		
    /**
     * 查询登录的用户下是否有
     *
     * @param smsUserId
     * @return
     */
    public List<SmsUser> querySmsBUserById(Integer smsUserId);


    /**
     * 根据用户等级查询用户信息
     *
     * @param list
     * @return
     */
    public List<SmsUser> queryByUserLevel(List list);

    /**
     * 修改用户等级
     *
     * @param smsUser
     */
    public void updateUserLevel(SmsUser smsUser);

    /**
     * 查询超级管理员和一级管理员用户
     *
     * @param list 角色id
     * @return
     */
    public List<SmsUser> queryRoleLevel(List list);

    /**
     * 查询业务员或者销售经理下面所有业务员（归属）
     * @param list
     * @return
     */
    public List<SmsUser> queryBySysUserId(List list);
    
    /**
     * 查询电话号码是否存在
     * @param map
     * @return
     */
    public Integer queryPhone(Map map);
    
		
}
