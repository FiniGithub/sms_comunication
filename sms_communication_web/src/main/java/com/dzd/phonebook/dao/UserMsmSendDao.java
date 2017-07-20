package com.dzd.phonebook.dao;

import java.util.List;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SmsReceiveReplyPush;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsSendLog;
import com.dzd.phonebook.util.SmsSendTask;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;


/**
 * 代理发送消息Dao
 *
 * @Description:
 * @author:oygy
 * @time:2017年1月19日 上午10:40:49
 */
public interface UserMsmSendDao<T> extends BaseDao<T> {

    Page<SmsSendTask> queryMsmSendListPage(DzdPageParam dzdPageParam);

    public SmsSendTask queryMsmSendCount(DzdPageParam dzdPageParam);

    public List<String> queryMsmSendPhoneByid(Integer msmSendId);

    Page<SmsSendLog> queryMsmSendDetailsListPage(DzdPageParam dzdPageParam);

    public Integer querySmsSendById(Integer smsTaskId);

    public void updateSmsSendById(Integer smsTaskId);

    public Page<SmsReceiveReplyPush> queryUserPage(DzdPageParam dzdPageParam);

    public List<SmsAisleGroup> querySmsGroupByUserId(Integer uid);

    List<SmsAisleGroup> queryTaskAisleGroup(@Param("uid") Integer uid, @Param("bid") Integer bid, @Param("superAdmin") Integer superAdmin);

    List<SmsAisleGroup> queryTaskAisleType();

}
