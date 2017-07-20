package com.dzd.phonebook.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.dzd.base.util.SessionUtils;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.util.SmsUser;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SmsAisleGroupDao;
import com.dzd.phonebook.util.SmsAisleGroup;

import javax.servlet.http.HttpServletRequest;

@Service("SmsAisleGroupService")
public class SmsAisleGroupService<T> extends BaseService<T> {
    @Autowired
    private SmsAisleGroupDao<T> smsAisleGroupDao;
    @Autowired
    private UserMessageService userMessageService;

    @Override
    public SmsAisleGroupDao<T> getDao() {
        return smsAisleGroupDao;
    }

    /**
     * 查询通道组信息
     *
     * @param aisleGroupId
     * @return
     */
    public SmsAisleGroup querySmsAisleGroupById(Integer aisleGroupId) {
        return getDao().querySmsAisleGroupById(aisleGroupId);
    }


    /**
     * 查询通道组信息,支付
     *
     * @param account
     * @param request
     * @return
     */
    public SmsAisleGroup querySmsAisleGroupByIdByPay(String account, HttpServletRequest request) {
        // 用户id为空,则为登录的uid
        SmsUser smsUser = null;
        if (account == null || account.equals(" ") || account.equals("")) {
            SysUser sysUser = SessionUtils.getUser(request);
            smsUser = userMessageService.querySmsUserById(sysUser.getId());
        } else {
            // 获取用户通道id
            smsUser = userMessageService.querySmsUserBySmsUserAccount(account);
        }
        if(smsUser == null ){
            return null;
        }

        Integer aisleGroupId = smsUser.getAisleGroupId();
        return getDao().querySmsAisleGroupById(aisleGroupId);
    }


    /**
     * 获取通道组签名、公告、提示
     *
     * @return
     */
    public Map<String, Object> querySmsAisleGroupSign(SmsUser smsUsers) {
        Map<String, Object> map = new HashedMap();
        Integer aisleGroupId = smsUsers.getAisleGroupId();
        SmsAisleGroup smsAisleGroup = getDao().querySmsAisleGroupById(aisleGroupId);
        List<String> signList = new ArrayList<String>();
        String notice = "";
        String hint = "";
        if (smsAisleGroup != null) {
            // 公告
            notice = smsAisleGroup.getNotice();

            // 提示
            hint = smsAisleGroup.getHint();

            // 通道签名
            String sign = smsAisleGroup.getSignature();
            if (sign != null && !sign.equals("")) {
                String[] signStr = sign.split(",");
                Collections.addAll(signList, signStr);
            } else { // 通道没有签名，则显示用户绑定的签名
                String userSign = smsUsers.getSignature();
                if (smsUsers.getSignatureType() == 0 && userSign != null && !userSign.equals("")) {// 绑定签名
                    String[] signStr = userSign.split(",");
                    Collections.addAll(signList, signStr);
                }
            }


        }
        map.put("notice", notice);
        map.put("hint", hint);
        map.put("signList", signList);
        return map;
    }


}
