package com.dzd.phonebook.dao;

import java.util.List;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SmsReceiveReplyPush;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;

public interface SmsReceiveReplyPushDao<T> extends BaseDao<T> {

	public List<T> queryByMenuid(Integer menuid);

	public List<T> queryByMenuUrl(String url);

	public void deleteSmsReceive(Integer smsReceiveReplyPushId);

	public List<T> getMenuBtnByUser(Integer userid);

	public List<T> queryByAll();

	public List queryNultilist();

	public Page<SmsReceiveReplyPush> queryUserPage(DzdPageParam dzdPageParam);
}
