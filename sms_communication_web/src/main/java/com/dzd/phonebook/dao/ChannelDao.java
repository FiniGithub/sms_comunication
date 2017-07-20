package com.dzd.phonebook.dao;

import java.util.List;
import java.util.Map;

import com.dzd.base.dao.BaseDao;
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
import org.apache.ibatis.annotations.Param;


/**
 * 代理接口
 *
 * @author chenchao
 * @date 2016-6-24
 */
public interface ChannelDao<T> extends BaseDao<T> {


    public List<District> queryDistrict();


    public Page<SmsAisle> querySmsUserListPage(DzdPageParam dzdPageParam);

    public void saveSmsAisle(SmsAisle smsAisle);

    public void updateSmsAisle(SmsAisle smsAisle);

    public void deleteAisleHasGroupByaid(Integer id);

    public String queryPluginConfig(String className);

    public List<SmsAisleGroupType> querySmsAisleGroupType();

    public Page<SmsAisleGroup> querySmsAisleGroupListPage(DzdPageParam dzdPageParam);

    public List<SmsAisle> querySmsAisleListById(Integer id);

    public List<SmsAisle> querySmsAisleList();

    public void saveSmsAislegroup(SmsAisleGroup SmsAisleGroup);

    public void saveSmsAislegroupHasAisle(SmsAisleGroupHasSmsAisle aisleHasGroup);

    public SmsAisleGroup querySmsAisleGroupById(Integer id);

    public List<SmsAisleGroupHasSmsAisle> queryAisleHasGroupListById(Integer id);

    /**
     * @Description:根据通道组ID查询组中所有通道信息
     * @author:CHENCHAO
     * @time:2017年06月07日 上午09:29:45
     */
    public SmsAisleGroup querySmsAisleInfoById(Long id);


    /**
     * @Description:根据通道组ID、运营商查询通道、通道组
     * @author:CHENCHAO
     * @time:2017年06月08日 上午09:45:45
     */
    public SmsAisleGroupHasSmsAisle querySmsAisleHasSmsAisleGroupByOperatorId(@Param("gid") Long gid, @Param("operatorId") Integer operatorId);

    public void updateSmsAislegroup(SmsAisleGroup SmsAisleGroup);

    public void deleteSmsAislegroupHasAisle(Integer id);

    public void deleteUserAisleGroup(Integer id);

    public void deleteAisleGroup(Integer id);

    public List<SmsAisleGroup> querySmsAisleGroupIdName(Integer typeid);

    public List<SmsAisleSource> queryAisleSourceList();

    public List<SmsShieldWord> querySmsShieldWordByType(Integer swType);

    public String querySmsAisleGrouphasUserById(Integer aisleGroupid);

    public Integer queryAisleGroupDft(Map<String, Object> map);

    public List<SmsUser> querySmsUserList(SysUser user);

}
