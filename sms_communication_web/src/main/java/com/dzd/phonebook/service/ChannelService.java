package com.dzd.phonebook.service;


import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.ChannelDao;
import com.dzd.phonebook.entity.District;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.SmsAisle;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsAisleGroupHasSmsAisle;
import com.dzd.phonebook.util.SmsAisleGroupType;
import com.dzd.phonebook.util.SmsAisleSource;
import com.dzd.phonebook.util.SmsShieldWord;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.phonebook.util.SmsUserLevel;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Description:通道服务类
 * @author:oygy
 * @time:2017年1月10日 上午11:07:06
 */
@Service("channelService")
public class ChannelService<T> extends BaseService<T> {
    private final static Logger log = Logger.getLogger(ChannelService.class);


    @Autowired
    private ChannelDao<T> mapper;

    public ChannelDao<T> getDao() {
        return mapper;
    }


    /**
     * @Description:查询所有的省
     * @author:oygy
     * @time:2017年1月3日 下午5:56:09
     */
    public List<District> queryDistrict() {
        return getDao().queryDistrict();
    }


    /**
     * @Description:查询通道列表
     * @author:ougy
     * @time:2016年12月31日 下午2:18:48
     */
    public Page<SmsAisle> querySmsAisleList(DzdPageParam dzdPageParam) {
        return getDao().querySmsUserListPage(dzdPageParam);
    }

    /**
     * @Description:添加通道
     * @author:oygy
     * @time:2017年1月4日 下午1:37:01
     */
    public void saveSmsAisle(SmsAisle smsAisle) {
        getDao().saveSmsAisle(smsAisle);
    }


    /**
     * @Description:修改通道
     * @author:oygy
     * @time:2017年1月4日 下午1:37:01
     */
    public void updateSmsAisle(SmsAisle smsAisle) {
        getDao().updateSmsAisle(smsAisle);
    }

    /**
     * @Description:根据通道ID删除通道与通道组关系表中的通道数据
     * @author:oygy
     * @time:2017年1月4日 下午3:40:32
     */
    public void deleteAisleHasGroupByaid(Integer id) {
        getDao().deleteAisleHasGroupByaid(id);
    }

    /**
     * @Description:根据通道源标识查询通道源的json数据
     * @author:oygy
     * @time:2017年3月6日 下午5:03:56
     */
    public String queryPluginConfig(String className) {
        return getDao().queryPluginConfig(className);
    }

    /**
     * @Description:查询出所有通道组类型
     * @author:oygy
     * @time:2017年1月3日 下午4:36:44
     */
    public List<SmsAisleGroupType> querySmsAisleGroupType() {
        return getDao().querySmsAisleGroupType();
    }

    /**
     * @Description:查询通道组列表
     * @author:ougy
     * @time:2016年12月31日 下午2:18:48
     */
    public Page<SmsAisleGroup> querySmsAisleGroupList(DzdPageParam dzdPageParam) {
        return getDao().querySmsAisleGroupListPage(dzdPageParam);
    }

    public List<SmsAisle> querySmsAisleListById(Integer id) {
        return getDao().querySmsAisleListById(id);
    }

    public List<SmsAisle> querySmsAisleList() {
        return getDao().querySmsAisleList();
    }

    /**
     * @Description:保存通道组内容
     * @author:ougy
     * @time:2017年1月5日 下午3:32:58
     */
    public void saveSmsAislegroup(SmsAisleGroup SmsAisleGroup) {
        getDao().saveSmsAislegroup(SmsAisleGroup);
    }

    /**
     * @Description:保存通道组内通道
     * @author:ougy
     * @time:2017年1月5日 下午3:32:58
     */
    public void saveSmsAislegroupHasAisle(SmsAisleGroupHasSmsAisle aisleHasGroup) {
        getDao().saveSmsAislegroupHasAisle(aisleHasGroup);
    }

    /**
     * @Description:根据ID查询通道组信息
     * @author:oygy
     * @time:2017年1月5日 下午4:47:53
     */
    public SmsAisleGroup querySmsAisleGroupById(Integer id) {
        return getDao().querySmsAisleGroupById(id);
    }

    /**
     * @Description:根据通道组ID查询组中所有通道信息
     * @author:oygy
     * @time:2017年1月5日 下午5:19:24
     */
    public List<SmsAisleGroupHasSmsAisle> queryAisleHasGroupListById(Integer id) {
        return getDao().queryAisleHasGroupListById(id);
    }

    /**
     * @Description:根据通道组ID查询组中所有通道信息
     * @author:CHENCHAO
     * @time:2017年06月07日 上午09:29:45
     */
    public SmsAisleGroup querySmsAisleInfoById(Long id) {
        return getDao().querySmsAisleInfoById(id);
    }

    /**
     * @Description:修改通道组信息
     * @author:oygy
     * @time:2017年1月5日 下午6:14:02
     */
    public void updateSmsAislegroup(SmsAisleGroup SmsAisleGroup) {
        getDao().updateSmsAislegroup(SmsAisleGroup);
    }

    /**
     * @Description:根据通道组ID删除通道关联表中的数据
     * @author:oygy
     * @time:2017年1月5日 下午6:16:39
     */
    public void deleteSmsAislegroupHasAisle(Integer id) {
        getDao().deleteSmsAislegroupHasAisle(id);
    }

    /**
     * @Description: 根据通道组ID删除通道组用户关联表的数据
     * @author:oygy
     * @time:2017年1月5日 下午6:45:38
     */
    public void deleteUserAisleGroup(Integer id) {
        getDao().deleteUserAisleGroup(id);
    }

    /**
     * @Description:根据通道组ID删除通道组信息
     * @author:oygy
     * @time:2017年1月5日 下午6:46:40
     */
    public void deleteAisleGroup(Integer id) {
        getDao().deleteAisleGroup(id);
    }

    /**
     * @Description:根据类型ID查询出所有通道组的ID,NAME
     * @author:oygy
     * @time:2017年1月6日 上午10:28:08
     */
    public List<SmsAisleGroup> querySmsAisleGroupIdName(Integer typeid) {
        return getDao().querySmsAisleGroupIdName(typeid);
    }

    /**
     * @Description: 查询所有通道源
     * @author:oygy
     * @time:2017年1月11日 上午11:25:27
     */
    public List<SmsAisleSource> queryAisleSourceList() {
        return getDao().queryAisleSourceList();
    }

    /**
     * @Description:根据屏蔽词类型查询屏蔽词
     * @author:oygy
     * @time:2017年3月30日 上午10:38:13
     */
    public List<SmsShieldWord> querySmsShieldWordByType(Integer swType) {
        return getDao().querySmsShieldWordByType(swType);
    }

    /**
     * @Description:根据通道组ID查询出所有拥有此通道组的用户的ID
     * @author:oygy
     * @time:2017年1月11日 下午5:40:52
     */
    public String querySmsAisleGrouphasUserById(Integer aisleGroupid) {
        return getDao().querySmsAisleGrouphasUserById(aisleGroupid);
    }

    /**
     * @Description:根据通道组类型查询默认通道组是否存在
     * @author:oygy
     * @time:2017年3月31日 下午4:56:24
     */
    public Integer queryAisleGroupDft(Map<String, Object> map) {
        return getDao().queryAisleGroupDft(map);
    }

    /**
     * @Description:查询所有的代理用户
     * @author:liuyc
     * @time:2017年4月5日 下午3:15:46
     */
    public List<SmsUser> querySmsUserList(SysUser user) {
        return getDao().querySmsUserList(user);
    }


    /**
     * @Description:根据通道组ID、运营商查询通道、通道组
     * @author:CHENCHAO
     * @time:2017年06月08日 上午09:45:45
     */
    public SmsAisleGroupHasSmsAisle querySmsAisleHasSmsAisleGroupByOperatorId(Long smsAisleGroupId, Integer operatorId) {
        return getDao().querySmsAisleHasSmsAisleGroupByOperatorId(smsAisleGroupId, operatorId);
    }
}
