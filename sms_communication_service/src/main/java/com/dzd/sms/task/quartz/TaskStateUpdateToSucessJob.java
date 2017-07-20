package com.dzd.sms.task.quartz;

import static com.dzd.sms.application.Define.KEY_SMS_TASK_CACHE;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.core.RowMapper;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;
import com.dzd.db.mysql.MysqlOperator;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsTaskData;
import com.dzd.utils.DateUtil;

/** * 
@author  作者 
E-mail: * 
@date 创建时间：2017年6月21日 下午4:51:34 * 
@version 1.0 * 
@parameter  * 
@since  * 
@return  */
public class TaskStateUpdateToSucessJob implements Job
{

	private static Logger logger = Logger.getLogger(TaskStateUpdateToSucessJob.class);
	// REDIS操作对象
	public RedisClient redisClient = RedisManager.I.getRedisClient();

	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		try
		{
			update();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void update()
	{
		System.out.println(
		        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":更新任务状态");

		// 更新指定任务的状态
		Set<byte[]> taskKeys = redisClient.hkeysObject(KEY_SMS_TASK_CACHE);
		int len = taskKeys.size();
		logger.info("task-update-num=" + len);

		Iterator<byte[]> iter = taskKeys.iterator();
		int i = 0;
		while ( i++ < len && iter.hasNext() )
		{
			byte[] key = iter.next();
			// System.out.println("key="+key);
			if ( key != null )
			{
				Object value = redisClient.hgetObject(KEY_SMS_TASK_CACHE, new String(key));
				// System.out.println("value="+value);
				if ( value != null )
				{
					SmsTaskData smsTaskData = (SmsTaskData) value;
					if ( smsTaskData != null )
					{
						/*
						 * 业务需求：凌晨5点时任务基本定型，推送任务都已返还，故而将可能存在的未返还任务进行结算，task--结算，
						 * taskphone--成功， 并将缓存的任务、号码数据删除，不需要对其进行重新结算
						 * 
						 */
						try
						{
							if ( smsTaskData.getReturnState() == 0 )
							{
								logger.info("return task id=" + smsTaskData.getTaskId()
								        + ",  updateState time="
								        + DateUtil.formatDateTime(new Date()));

								// 删除任务及发送号码缓存
								SmsServerManager.I.delSmsTaskCache(smsTaskData);

								// 审核通过，或免审的任务需要结算
								if ( smsTaskData.isAuditState() || smsTaskData.isFree() )
								{
									// 查询是否返还的任务
									Integer count = 0;
									Integer fee = 0;
									// 成功的数量
									int successNum = 0;
									// 查询需要返还的数量
									Map<Integer, Integer> re = getTaskSendResult(
									        smsTaskData.getTaskId());
									if ( re != null )
									{
										// 返还的数
										int returnNum = 0;

										for ( Integer stateKey : re.keySet() )
										{
											if ( stateKey.intValue() >= 99 )
											{
												successNum += re.get(stateKey);
											} else if ( stateKey.intValue() != 9
											        && stateKey.intValue() != 25 )
											{
												returnNum += re.get(stateKey);
											}
										}

										if ( returnNum > 0 )
										{
											// 给用户返款
											SmsServerManager.I.addUserBalance(
											        smsTaskData.getUserId(), returnNum,
											        "任务ID：" + smsTaskData.getTaskId());
										}
										logger.info("返还金额=" + fee + ",失败数量=" + count);
									} else
									{
										logger.info("失败-返还金额=" + fee + ",失败数量=" + count);
									}

									// 更新任务返还状态， 及返还金额
									updateTaskReturnState(smsTaskData.getTaskId(), successNum);
									// 更新任务号码返还状态
									updateTaskPhoneReturnState(smsTaskData.getTaskId());
								} 
							}

						} catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * 查询返还的数量和金额
	 * 警告： state=25 为审核失败， 即为终止发送
	 * state=99为提交成功
	 * state=100为发送成功
	 * 以上几种状态不返还
	 */
	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	public Map<Integer, Integer> getTaskSendResult(Long taskId)
	{
		Map<Integer, Integer> re = new HashedMap();
		try
		{

			String sql = "select state, sum(billing_num) as f from sms_send_task_phone where sms_send_task_id="
			        + taskId + " group by state ";

			// String sql = "select count(id) as c, sum(billing_num) as f from
			// sms_send_task_phone where sms_send_task_id=" + taskId + " and
			// (state<>25 and state<99) and state<>0 ";
			// String sql = "select count(id) as c, sum(billing_num) as f from
			// sms_send_task_phone where sms_send_task_id=" + taskId + " and
			// state<99 ";

			logger.info(sql);
			List<Map<Integer, Integer>> lists = MysqlOperator.I.query(sql, new RowMapper()
			{
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException
				{
					Map<Integer, Integer> b = new HashedMap();
					Integer state = 0;
					Integer f = 0;

					state = rs.getInt("state");

					if ( rs.getObject("f") != null )
					{
						if ( rs.getObject("f") instanceof Long )
						{
							f = ((Long) rs.getLong("f")).intValue();
						} else
						{
							f = ((BigDecimal) rs.getBigDecimal("f")).intValue();
						}
					}

					b.put(state, f);
					return b;
				}
			});

			for ( int i = 0; i < lists.size(); i++ )
			{
				for ( Integer state : lists.get(i).keySet() )
				{
					re.put(state, lists.get(i).get(state));
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return re;
	}

	/**
	 * 更新任务状态
	 */
	public void updateTaskReturnState(Long taskId, int fee)
	{
		String sql = " update sms_send_task set return_state=1, state=101,  actual_num=" + fee
		        + ",update_time = NOW() where id = " + taskId + 
		        " AND create_time < CAST(CAST(SYSDATE()AS DATE)AS DATETIME) ";
		try
		{
			System.out.println(sql);
			MysqlOperator.I.update(sql);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新任务状态
	 */
	public void updateTaskPhoneReturnState(Long taskId)
	{
		String sql = "UPDATE sms_send_task_phone p, sms_send_log l " +
                " SET p.state = 100 " +
                " , p.send_time=now(), p.aid=l.aid "+
                " WHERE p.sms_send_task_id="+ taskId +
                " AND p.state=-1 " +
                " AND p.sms_send_task_id=l.sms_send_task_id " +
                " AND p.phone=l.receive_phone " +
                " AND p.create_time < CAST(CAST(SYSDATE()AS DATE)AS DATETIME) ";
		try
		{
			System.out.println(sql);
			MysqlOperator.I.update(sql);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
