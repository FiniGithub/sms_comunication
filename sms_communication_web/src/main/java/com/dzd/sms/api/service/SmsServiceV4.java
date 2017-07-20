package com.dzd.sms.api.service;

import static com.dzd.sms.application.Define.PUSH_STATE_YES;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.util.CollectionUtils;

import com.dzd.db.mysql.MysqlOperator;
import com.dzd.phonebook.util.AccessTimeControlUtil;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.CustomParameter;
import com.dzd.sms.service.data.SmsMessage;
import com.dzd.sms.service.data.SmsPullReport;
import com.dzd.sms.service.data.SmsReciveReply;
import com.dzd.sms.service.data.SmsRequestParameter;
import com.dzd.sms.service.data.SmsState;
import com.dzd.sms.service.data.SmsUser;
import com.dzd.utils.DateUtil;
import com.dzd.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * Created by IDEA Author: WHL Date: 2016/12/31 Time: 10:18
 */
public class SmsServiceV4 
{

	static Logger logger = Logger.getLogger(SmsServiceV4.class);
	
	/**
	 * 发送短信服务
	 *
	 * @return
	 */
	public JSONObject send(JSONObject result, SmsRequestParameter requestParameter, CustomParameter parameter)
	{
		// 1. 初始化返回结果
		result.put("sid", 0); // 任务ID
		result.put("count", 0);
		result.put("code", 0);

		// 2. 初始化相关临时变量
		parameter.setSmsContent(requestParameter.getText());
		List<SmsMessage> smsMessageList = new ArrayList<SmsMessage>();
		List<String> validMobileList = new ArrayList<String>();

		Long uid = Long.valueOf(requestParameter.getUid());
		SmsUser user = SmsServerManager.I.getUser(uid);

		// 3. 先验证apikey--MD5验证
		if ( requestParameter.isCheck() )
		{
			SmsSendBusiness.checkApiKey(requestParameter, parameter,
			        Long.valueOf(requestParameter.getSysId()), user);
		}

		parameter.setUserId(user.getId());

		// 4. 验证短信内容是否有屏蔽词
		SmsSendBusiness.checkShieldKeyWord(parameter);

		// 5. 加内容签名 、验证签名 、验证通道配置信息、计算内容长度
		user.setSignature(requestParameter.getSmsUserSign());
		parameter.setAisleSmsLength(requestParameter.getAisleLongNum());
		SmsSendBusiness.singNatureCheckAndGroupUserRelation(requestParameter, parameter, user);

		// 6. 过滤手机号码、设置相关发送详情信息：区域、价格、短信内容长度等
		SmsSendBusiness.filterPhoneNumber(requestParameter, result, smsMessageList, validMobileList,
		        parameter);

		// 7. 验证帐户余额
		SmsSendBusiness.checkAccountBalance(parameter);

		// 8. 扣款
		SmsSendBusiness.debitFromAccount(parameter);

		// 是否需要审核--签名与内容均匹配才属于免审
//		if ( !parameter.isAudit() )
//		{
//			parameter.setAudit(!SmsServerManager.I.isFreeTrial(parameter.getUserId(),
//			        parameter.getSmsAuditContent()));
//		}
		//不再需要审核，直接发送
		parameter.setAudit(false);
		
		// 9. 发送提交
		SmsSendBusiness.submitSendInfo(requestParameter, result, smsMessageList, validMobileList,
		        parameter, requestParameter.getGroupTypeId(), user);

		SmsState smsState = parameter.getSmsState();
		// 提交完成， 返回状态码
		logger.info("count=" + smsMessageList.size() + " billing_num=" + parameter.getSurplus_num()
		        + " code=" + smsState.code);

		result.put("audit", parameter.isAudit());
		result.put("code", smsState.code);
		result.put("msg", smsState.message);
		return result;
	}

	/**
	 * 获取发送状态报告
	 */
	public void updateSmsPullReport(List<SmsPullReport> smsPullReports)
	{
		try
		{
			final List<SmsPullReport> tmpSmsReportList = smsPullReports;

			new Thread(new Runnable()
			{
				public void run()
				{
					String sql = "update sms_send_task_phone set push_state=?, update_time=? where id=? ";
					try
					{
						MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter()
			            {
				            public void setValues(PreparedStatement ps, int i) throws SQLException
				            {
					            String dateTime = DateUtil.formatDateTime();
					            ps.setInt(1, PUSH_STATE_YES);
					            ps.setString(2, dateTime);
					            ps.setLong(3, tmpSmsReportList.get(i).getId());
					            // System.out.println(" taskid="
			                    // +tmpSmsMessageList.get(i).getTaskId()+",mobile="
			                    // +
			                    // tmpSmsMessageList.get(i).getMobile());
				            }

				            public int getBatchSize()
				            {
					            return tmpSmsReportList.size();
				            }
			            });
					} catch (Exception e)
					{
						logger.error("updateSmsPullReport failed.", e);
					}
				}
			}).start();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 获取回复信息列表
	 */
	public List<SmsReciveReply> updateSmsReply(List<SmsReciveReply> smsReplys)
	{
		try
		{
			final List<SmsReciveReply> tmpSmsReplyList = smsReplys;
			new Thread(new Runnable()
			{
				public void run()
				{
					String sql = "update  sms_receive_reply_push set state=?, update_time=? where id=? ";
					MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter()
			        {
				        public void setValues(PreparedStatement ps, int i) throws SQLException
				        {
					        String dateTime = DateUtil.formatDateTime();
					        ps.setInt(1, PUSH_STATE_YES);
					        ps.setString(2, dateTime);
					        ps.setLong(3, tmpSmsReplyList.get(i).getId());
					        // System.out.println(" taskid="
			                // +tmpSmsMessageList.get(i).getTaskId()+",mobile="
			                // +
			                // tmpSmsMessageList.get(i).getMobile());
				        }

				        public int getBatchSize()
				        {
					        return tmpSmsReplyList.size();
				        }
			        });
				}
			}).start();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return smsReplys;
	}

	public boolean cehckNullParameter(SmsRequestParameter requestParameter, String key)
	{
		if ( Define.INTERFACEKEY.SEND.equals(key) )
		{
			return StringUtils.isEmpty(requestParameter.getUid())
			        || CollectionUtils.isEmpty(requestParameter.getMobile())
			        || StringUtils.isEmpty(requestParameter.getText())
			        || StringUtils.isEmpty(requestParameter.getTimestamp())
			        || StringUtils.isEmpty(requestParameter.getSign());
		} else if ( Define.INTERFACEKEY.PULL_STATUS.equals(key)
		        	|| Define.INTERFACEKEY.GET_BALANCE.equals(key) )
		{
			return StringUtils.isEmpty(requestParameter.getUid())
			        || StringUtils.isEmpty(requestParameter.getTimestamp())
			        || StringUtils.isEmpty(requestParameter.getSign());
		} else if ( Define.INTERFACEKEY.GET_REPLY.equals(key) )
		{
			return StringUtils.isEmpty(requestParameter.getUid())
			        || StringUtils.isEmpty(requestParameter.getTimestamp())
			        || StringUtils.isEmpty(requestParameter.getSign())
			        || StringUtils.isEmpty(requestParameter.getStart_time())
			        || StringUtils.isEmpty(requestParameter.getEnd_time());
		}

		return true;
	}

	public void compleRequestParameter(SmsRequestParameter requestParameter)
	{
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String sign = StringUtil.string2MD5(1 + "5a7ed0bf8096e5e433364c219c0d7757" + timestamp);

		requestParameter.setTimestamp(timestamp);
		requestParameter.setSign(sign);
	}
}
