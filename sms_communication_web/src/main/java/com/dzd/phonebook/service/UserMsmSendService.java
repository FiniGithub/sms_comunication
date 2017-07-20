package com.dzd.phonebook.service;


import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.UserMsmSendDao;
import com.dzd.phonebook.entity.SmsReceiveReplyPush;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsSendLog;
import com.dzd.phonebook.util.SmsSendTask;
import com.github.pagehelper.Page;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 发送消息服务类
 *
 * @author ougy
 * @date 2016-6-24
 */
@Service("userMsmSendService")
public class UserMsmSendService<T> extends BaseService<T> {


    @Autowired
    private UserMsmSendDao<T> mapper;

    public UserMsmSendDao<T> getDao() {
        return mapper;
    }


    /**
     * @Description:查询消息列表
     * @author:ougy
     * @time:2016年12月31日 下午2:18:48
     */
    public Page<SmsSendTask> queryMsmSendList(DzdPageParam dzdPageParam) {
        return getDao().queryMsmSendListPage(dzdPageParam);
    }


    /**
     * 查询任务所有的通道组
     *
     * @param uid
     * @param bid
     * @return
     */
    public List<SmsAisleGroup> queryTaskAisleGroup(Integer uid, Integer bid, Integer superAdmin) {
        return getDao().queryTaskAisleGroup(uid, bid, superAdmin);
    }

    public List<SmsAisleGroup> queryTaskAisleType(){
        return getDao().queryTaskAisleType();
    }

    /**
     * @Description:发送消息统计数量
     * @author:oygy
     * @time:2017年4月1日 上午10:56:47
     */
    public SmsSendTask queryMsmSendCount(DzdPageParam dzdPageParam) {
        return getDao().queryMsmSendCount(dzdPageParam);
    }

    /**
     * @Description:根据消息ID查询出本批次发送的所有手机号码
     * @author:oygy
     * @time:2017年1月7日 下午4:46:17
     */
    public List<String> queryMsmSendPhoneByid(Integer msmSendId) {
        return getDao().queryMsmSendPhoneByid(msmSendId);
    }

    /**
     * @Description:查询消息详情列表
     * @author:oygy
     * @time:2017年1月9日 上午11:42:42
     */
    public Page<SmsSendLog> queryMsmSendDetailsList(DzdPageParam dzdPageParam) {
        return getDao().queryMsmSendDetailsListPage(dzdPageParam);
    }

    /**
     * 根据消息ID查询消息发送状态
     *
     * @Description:
     * @author:oygy
     * @time:2017年1月13日 下午6:33:17
     */
    public Integer querySmsSendById(Integer smsTaskId) {
        return getDao().querySmsSendById(smsTaskId);
    }

    /**
     * 根据消息ID修改消息发送状态为终止发送
     *
     * @Description:
     * @author:oygy
     * @time:2017年1月13日 下午6:33:17
     */
    public void updateSmsSendById(Integer smsTaskId) {
        getDao().updateSmsSendById(smsTaskId);
    }

    /**
     * 查询用户的回复信息
     *
     * @Description:
     * @author:oygy
     * @time:2017年1月16日 上午9:59:43
     */
    public Page<SmsReceiveReplyPush> queryUserList(DzdPageParam dzdPageParam) {
        return getDao().queryUserPage(dzdPageParam);
    }

    /**
     * @Description:根据用户ID查询所拥有的通道组
     * @author:oygy
     * @time:2017年3月14日 下午4:08:40
     */
    public List<SmsAisleGroup> querySmsGroupByUserId(Integer uid) {
        return getDao().querySmsGroupByUserId(uid);
    }

}
