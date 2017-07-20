package com.dzd.phonebook.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SmsUserDao;
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
 * @Description:会员服务类
 * @author:oygy
 * @time:2017年1月10日 上午11:08:06
 */
@Service("smsUserService")
public class SmsUserService<T> extends BaseService<T> {

    private final static Logger log = Logger.getLogger(SmsUserService.class);

    @Autowired
    private SmsUserDao<T> mapper;

    public SmsUserDao<T> getDao() {
        return mapper;
    }
    
    /**
     * @Description:传递新增屏蔽词参数
     * @param: word
     * @author: Fini
     */
    public void insertShieldingWord(String word) {
    	getDao().insertShieldingWord(word);
    }
    
    /**
     * @Description:传递模糊删除条件
     * @param: word
     * @author: Fini
     */
    public void deleteShieldingWordFuzzy(String word) {
		getDao().deleteShieldingWordFuzzy(word);
	}
    
    /**
     * @Description:传递精确删除条件
     * @param: word
     * @author: Fini
     */
    public void deleteShieldingWordPrecise(String word) {
		getDao().deleteShieldingWordPrecise(word);
	}
    
    /**
     * @Description:传递模糊查询条件
     * @param: word
     * @author: Fini
     */
    public List<SmsShieldWord> queryShieldingWordFuzzy(String word) {
		return getDao().queryShieldingWordFuzzy(word);
	}
    
    /**
     * @Description:传递精确查询条件
     * @param: word
     * @author: Fini
     */
    public List<SmsShieldWord> queryShieldingWordPrecise(String word) {
		return getDao().queryShieldingWordPrecise(word);
	}
    
    /**
     * @Description:传递参数更新
     * @param:smsSendTask
     * @author: Fini
     */
    public void updateSmsContent(SmsSendTask smsSendTask) {
		getDao().updateSendTaskContent(smsSendTask);
	}
    
    /**
     * @Description:传递参数查询
     * @param:id
     * @author: Fini 
     */
    public List<SmsSendTask> querySmsSendTaskListById(String id) {
		return getDao().querySendTaskContentById(id);
	}
    
    /**
     * @Description:获取查询代理列表
     * @author: Fini
     */
    public List<SmsSendTask> querySmsSendTaskList() {
    	return getDao().querySmsSendTask();
    }
    
    /**
     * @Description:获取查询代理列表
     * @author: Fini
     */
    public List<SmsSendTaskPhone> querySmsSendTaskPhoneList() {
    	return getDao().querySmsSendTaskPhone();
    }
    
    /**
     * @Description:获取查询代理列表
     * @author: Fini
     */
    public List<SmsUser> querySmsAgentStatisticsList() {
    	return getDao().querySmsAgentStatistics();
    }
    
    /**
     * @Description:查询代理列表
     * @author:ougy
     * @time:2016年12月31日 下午2:18:48
     */
    public Page<SmsUser> querySmsUserList(DzdPageParam dzdPageParam) {
        return getDao().querySmsUserListPage(dzdPageParam);
    }


    /**
     * @Description:统计短信条数
     * @author:oygy
     * @time:2017年3月29日 下午4:43:35
     */
    public Integer querySmsStatistical(DzdPageParam dzdPageParam) {
        return getDao().querySmsStatistical(dzdPageParam);
    }

    /**
     * @Description:添加代理钱包
     * @author:oygy
     * @time:2016年12月31日 下午5:23:24
     */
    public void addSmsUserBlank(SmsUserBlank smsUserBlank) {
        getDao().addSmsUserBlank(smsUserBlank);
    }

    /**
     * @Description:添加代理信息
     * @author:oygy
     * @time:2016年12月31日 下午5:29:16
     */
    public void saveSmsUser(SmsUser smsUser) {
        getDao().saveSmsUser(smsUser);
    }

    /**
     * @Description:修改代理信息
     * @author:oygy
     * @time:2016年12月31日 下午5:29:16
     */
    public void updateSmsUser(SmsUser smsUser) {
        getDao().updateSmsUser(smsUser);
    }

    /**
     * @Description:修改代理钱包
     * @author:oygy
     * @time:2016年12月31日 下午5:23:24
     */
    public void updateSmsUserBlank(SmsUserBlank smsUserBlank) {
        getDao().updateSmsUserBlank(smsUserBlank);
    }

    /**
     * @Description:根据用户ID删除通道组与用户关系表中的所有数据
     * @author:oygy
     * @time:2017年1月6日 下午3:01:14
     */
    public void deleteSmsAislegroupHasUser(Integer smsUid) {
        getDao().deleteSmsAislegroupHasUser(smsUid);
    }

    /**
     * @Description:
     * @author:oygy
     * @time:2017年1月6日 下午3:28:30
     */
    public List<SmsAisleGroupHasSmsUser> queryUserHasGroup(Integer smsUid) {
        return getDao().queryUserHasGroup(smsUid);
    }

    /**
     * @Description:根据用户级别查询出用户所有符合该级别的vip通道组
     * @author:oygy
     * @time:2017年3月2日 上午10:25:49
     */
    public List<SmsAisleGroupHasSmsUser> queryVipUserHasGroup(Integer userLevel) {
        return getDao().queryVipUserHasGroup(userLevel);
    }

    /**
     * @Description:根据用户ID查询用户余额
     * @author:oygy
     * @time:2017年1月6日 下午3:28:30
     */
    public Integer querySurplusNum(Integer smsUid) {
        Integer num = getDao().querySurplusNum(smsUid);
        if (num == null) {
            return 0;
        }
        return num;
    }

    /**
     * @Description:根据用户ID给用户钱包充值
     * @author:oygy
     * @time:2017年1月11日 下午2:03:29
     */
    public void updateSmsUserBlankMoney(SmsUser smsUser) {
        getDao().updateSmsUserBlankMoney(smsUser);
    }


    /**
     * 添加金额操作明细
     *
     * @Description:
     * @author:oygy
     * @time:2017年1月14日 下午5:07:50
     */
    public void saveSmsUserMoneyRunning(SmsUserMoneyRunning smr) {
        getDao().saveSmsUserMoneyRunning(smr);
    }

    /**
     * @Description:根据用户ID查询出用户的KEY
     * @author:oygy
     * @time:2017年1月11日 下午3:54:31
     */
    public String querySmsUserKey(Integer smsUserId) {
        return getDao().querySmsUserKey(smsUserId);
    }

    /**
     * @Description:查询代理统计列表
     * @author:ougy
     * @time:2016年12月31日 下午2:18:48
     */
    public Page<SmsUser> querySmsUserStatisticalList(DzdPageParam dzdPageParam) {
        return getDao().querySmsUserStatisticalListPage(dzdPageParam);
    }

    /**
     * @Description:统计所有代理发送信息
     * @author:ougy
     * @time:2016年12月31日 下午2:18:48
     */
    public SmsUser querySmsUserStatisticalZong(DzdPageParam dzdPageParam) {
        return getDao().querySmsUserStatisticalZong(dzdPageParam);
    }

    /**
     * 查询今日消费
     *
     * @param smsUserId
     * @return
     */
    public String querySmsUserStatisticalByUid(Integer smsUserId) {
        Long count = getDao().querySmsUserStatisticalByUid(smsUserId);
        if (count == 0) {
            return "0";
        }
        return count.toString();
    }

    /**
     * @Description:根据用户查询代理每天统计列表
     * @author:ougy
     * @time:2016年12月31日 下午2:18:48
     */
    public Page<SmsUser> querySmsUserStatistical(DzdPageParam dzdPageParam) {
        return getDao().querySmsUserStatisticalPage(dzdPageParam);
    }

    public Page<SmsUser> querySmsUserDLPage(DzdPageParam dzdPageParam) {
        return getDao().querySmsUserDLPage(dzdPageParam);
    }

    /**
     * @Description:查询代理账单流水信息列表
     * @author:ougy
     * @time:2016年12月31日 下午2:18:48
     */
    public Page<SmsUserMoneyRunning> querySmsUserPuserBillList(DzdPageParam dzdPageParam) {
        return getDao().querySmsUserPuserBillListPage(dzdPageParam);
    }

    /**
     * 导出充值记录
     * @return
     */
    public List<SmsUserMoneyRunning> querySmsUserRechargeBillList(SmsUserMoneyRunning running) {
        return getDao().querySmsUserRechargeBillList(running);
    }

    /**
     * 消费记录
     * @param dzdPageParam
     * @return
     */
    public Page<SmsUserMoneyRunning> querySmsUserPuserConsumeBillListPage(DzdPageParam dzdPageParam){
        return getDao().querySmsUserPuserConsumeBillListPage(dzdPageParam);
    }

    /**
     * @Description:统计所有代理账单流水信息
     * @author:ougy
     * @time:2016年12月31日 下午2:18:48
     */
    public SmsUserMoneyRunning querySmsUserPuserBillZong(DzdPageParam dzdPageParam) {
        return getDao().querySmsUserPuserBillZong(dzdPageParam);
    }

    /**
     * 定时统计
     *
     * @Description:统计前一天代理数据
     * @author:oygy
     * @time:2017年2月16日 下午2:34:15
     */
    public List<SmsUser> stSmsUserPuserBillList() {
        return getDao().stSmsUserPuserBillList();
    }

    /**
     * @Description:添加钱一天代理统计数据
     * @author:oygy
     * @time:2017年2月16日 下午4:04:19
     */
    public void saeveStSmsUserPuserBill(SmsUser smsUser) {
        getDao().saeveStSmsUserPuserBill(smsUser);
    }

    /**
     * @Description:查询出所有用户信息
     * @author:oygy
     * @time:2017年2月17日 下午4:59:06
     */
    public List<SmsUser> querySmsUserListTj() {
        return getDao().querySmsUserListTj();
    }

    /**
     * 账单流水统计
     *
     * @param userId
     * @return
     */
    public SmsUserMoneyRunning querySmsUserMoneyRunningStatistical(Integer userId) {
        return getDao().querySmsUserMoneyRunningStatistical(userId);
    }

    /**
     * 发送统计
     *
     * @param email
     * @return
     */
    public SmsUser querySendListStatistical(String email) {
        return getDao().querySendListStatistical(email);
    }

    /**
     * @Description:根据通道组类型查询通道组
     * @author:oygy
     * @time:2017年3月31日 下午2:28:10
     */
    public List<SmsAisleGroup> querySmsGroupById(Integer gtype) {
        return getDao().querySmsGroupById(gtype);
    }

    /**
     * @Description:根据系统用户ID查询代理账户信息
     * @author:oygy
     * @time:2017年4月6日 下午2:57:24
     */
    public SmsUser querySmsUserBySysUid(Integer sysId) {
        return getDao().querySmsUserBySysUid(sysId);
    }

    /**
     * 新增
     *
     * @param smsUser
     */
    public void addSmsUser(SmsUser smsUser) {
        getDao().addSmsUser(smsUser);
        ;
    }

    public void updateSmsUserInfo(SmsUser smsUser) {
        getDao().updateSmsUserInfo(smsUser);
    }

    public Map<String, String> fillSmsUserStatisticalListPage(DzdPageParam filldzdPageParam) {
        Map<String, String> fillSmsUserMap = new HashMap<String, String>();
        List<SmsUser> fillSmsUsers = getDao().fillSmsUserStatisticalListPage(filldzdPageParam);
        if (CollectionUtils.isEmpty(fillSmsUsers)) {
            return fillSmsUserMap;
        }
        for (SmsUser smsUser : fillSmsUsers) {
            fillSmsUserMap.put(smsUser.getEmail(), smsUser.getNickName());
        }
        return fillSmsUserMap;
    }

    /**
     * @Description:统计新增自主注册代理数量
     * @author:oygy
     * @time:2017年4月13日 下午5:23:35
     */
    public Integer queryCountNewSmsUser() {
        return getDao().queryCountNewSmsUser();
    }

    /**
     * @Description:统计用户剩余条数
     * @author:oygy
     * @time:2017年4月13日 下午5:31:22
     */
    public Integer queryCountUserSmsNum(Map<String, Object> sortMap) {
        return getDao().queryCountUserSmsNum(sortMap);
    }

    /**
     * @Description:统计所有用户每天发送情况
     * @author:oygy
     * @time:2017年4月13日 下午6:05:18
     */
    public List<SmsUser> queryCountSmsAgentStatistics(Map<String, Object> sortMap) {
        return getDao().queryCountSmsAgentStatistics(sortMap);
    }

    /**
     * @Description:统计今日购买数量
     * @author:oygy
     * @time:2017年4月14日 上午11:38:27
     */
    public Integer queryCountTodaySmsNum(Map<String, Object> sortMap) {
        return getDao().queryCountTodaySmsNum(sortMap);
    }

    /**
     * @Description:查询最后查询时间
     * @author:oygy
     * @time:2017年4月14日 下午4:16:48
     */
    public Date queryLastSmsUserTime() {
        return getDao().queryLastSmsUserTime();
    }

    public void updateLastSmsUserTime(Date lastTime) {
        getDao().updateLastSmsUserTime(lastTime);
    }

    public void addLastSmsUserTime(Date lastTime) {
        getDao().addLastSmsUserTime(lastTime);
    }

    /**
     * @Description:查询消息推送列表
     * @author:ougy
     * @time:2016年12月31日 下午2:18:48
     */
    public Page<SmsUserMessage> pushManageList(DzdPageParam dzdPageParam) {
        return getDao().pushManagePage(dzdPageParam);
    }

    /**
     * @Description:新增推送
     * @author:oygy
     * @time:2017年4月18日 下午2:29:34
     */
    public void addPushManage(SmsUserMessage smsUserMessage) {
        getDao().addPushManage(smsUserMessage);
    }

    /**
     * 查询用户的类型
     *
     * @param groupId
     * @return
     */
    public SmsAisleGroupType queryGroupTypeBySmsUserId(Integer groupId) {
        return getDao().queryGroupTypeBySmsUserId(groupId);
    }

    public Page<SmsRechargeUser> rechargeRecordList(DzdPageParam dzdPageParam) {
        return getDao().rechargeRecordListPage(dzdPageParam);
    }


    /**
     * 关于充值请调用这个方法 - 新增
     *
     * @param moneyRunning
     */
    public void saveUserMoneyRunning(SmsUserMoneyRunning moneyRunning) {
        getDao().saveUserMoneyRunning(moneyRunning);
    }


    /**
     * 修改用户信息
     *
     * @param smsUser
     */
    public void updateUser(SmsUser smsUser) {
        getDao().updateUser(smsUser);
    }

    /**
     * 查询登录的用户下是否有
     * @param smsUserId
     * @return
     */
    public List<SmsUser> querySmsBUserById(Integer smsUserId){
        List<SmsUser> bsmsUserList = getDao().querySmsBUserById(smsUserId);
        if(bsmsUserList==null || bsmsUserList.size() ==0){
            return null;
        }
        return bsmsUserList;
    }

    /**
     * 根据用户等级查询用户信息
     * @param list
     * @return
     */
    public List<SmsUser> queryByUserLevel(List list){
        return mapper.queryByUserLevel(list);
    }

    /**
     * 修改用户等级
     * @param smsUser
     */
    public void updateUserLevel(SmsUser smsUser){
         mapper.updateUserLevel(smsUser);
    }

    /**
     * 查询超级管理员和一级管理员用户
     * @param list 角色id
     * @return
     */
    public List<SmsUser> queryRoleLevel(List list){
        return mapper.queryRoleLevel(list);
    }

    /**
     * 查询业务员或者销售经理下面所有业务员（归属）
     * @param list
     * @return
     */
    public List<SmsUser> queryBySysUserId(List list){
        return mapper.queryBySysUserId(list);
    }

    /**
     * 更改发送短信是否需要验证
     *
     * @param smsUser
     */
    public void updateSmsUserVerifyType(SmsUser smsUser) {
        mapper.updateSmsUserVerifyType(smsUser);
    }


    /**
     * 查询电话号码是否存在
     * @param phone
     * @param id
     * @return
     */
    public Integer queryPhone(String phone,Integer id){
        Map map=new HashMap();
        map.put("phone",phone);
        map.put("id",id);
        return mapper.queryPhone(map);
    }
}
