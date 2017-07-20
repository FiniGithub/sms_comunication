package com.dzd.sms.api.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.SmsSpecificSymbol;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.ChannelService;
import com.dzd.phonebook.service.MsmSendService;
import com.dzd.phonebook.service.SmsOrderExportService;
import com.dzd.phonebook.service.SmsSpecificSymbolService;
import com.dzd.phonebook.service.SysMenuBtnService;
import com.dzd.phonebook.service.UserMessageService;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.SmsAisle;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsSendLog;
import com.dzd.phonebook.util.SmsSendTask;
import com.dzd.phonebook.util.WebRequestParameters;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsRequestParameter;
import com.dzd.sms.service.data.SmsTaskData;
import com.dzd.sms.service.data.SmsUser;
import com.dzd.sms.task.quartz.QuartzJobManager;
import com.github.pagehelper.Page;

/**
 * 
 * @ClassName:     SmsTimedTaskController
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author:    hz-liang
 * @date:        2017年5月23日 下午2:25:17
 */
@Controller
@RequestMapping("/sms/task")
public class SmsTimedTaskController extends WebBaseController
{
	static Logger logger = Logger.getLogger(SmsTimedTaskController.class);
	// 服务操作类

	@SuppressWarnings("rawtypes")
	@Autowired
	private MsmSendService msmSendService;

	@SuppressWarnings("rawtypes")
	@Autowired
	private UserMessageService userMessageService;

	@SuppressWarnings("rawtypes")
	@Autowired
	private SysMenuBtnService sysMenuBtnService;

	@Autowired
	private SmsSpecificSymbolService smsSpecificSymbolService;

	@SuppressWarnings("rawtypes")
	@Autowired
	private ChannelService channelService;

	private static final String SMS_FLAG_BOOLEAN = "smsFlag";

	private List<List<String>> dataList;

	public List<List<String>> getDataList()
	{
		return dataList;
	}

	public void setDataList(List<List<String>> dataList)
	{
		this.dataList = dataList;
	}

	/**
	 * 定时任务
	 *
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	@MethodDescription("定时任务")
	@RequestMapping(value = "/timedtask")
	public String timedTask(HttpServletRequest request, Model model)
	{
		List<SmsAisle> smsAisleGroup = msmSendService.querySmsAisleGroup(null);// 查询所有的通道名称

		Map<String, Object> param = new HashMap<String, Object>();
		SysUser user = SessionUtils.getUser(request);
		if ( user.getId() != 1 )
		{
			SmsUser smsUser = SmsServerManager.I.getUserBySysId(Long.valueOf(user.getId()));
			param.put(Define.STATICAL.SMSID, smsUser.getId());
		}
		List<String> querySmsUserSignList = userMessageService.querySmsUserSignList(param);

		List<String> shieldList = SmsServerManager.I.getShieldList();
		StringBuffer strb = new StringBuffer();
		for ( String shile : shieldList )
		{
			strb.append(shile + ",");
		}
		model.addAttribute(Define.REQUESTPARAMETER.AISLENAMES, smsAisleGroup);
		model.addAttribute(Define.REQUESTPARAMETER.SIGNATURES, querySmsUserSignList);
		model.addAttribute(Define.REQUESTPARAMETER.SHIELDWORD,
		        strb.substring(0, strb.length() - 1));

		Object menuId = request.getParameter(Define.REQUESTPARAMETER.ID);
		model.addAttribute(Define.REQUESTPARAMETER.MENUID, menuId);

		return "smsManage/timedTask";
	}

	/**
	 * 查询定时任务
	 *
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	@MethodDescription("查询定时任务")
	@RequestMapping(value = "/querytimedtask")
	@ResponseBody
	public DzdResponse queryTimedTask(HttpServletRequest request, HttpServletResponse response,
	        @RequestBody Map<String, Object> data)
	{
		DzdResponse dzdPageResponse = new DzdResponse();
		try
		{
			WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class,
			        data);
			if ( parameters == null )
			{
				return dzdPageResponse;
			}
			// 查询入参
			DzdPageParam dzdPageParam = preParameter(data, parameters, request);

			String menuId = request.getParameter(Define.REQUESTPARAMETER.MENUID);
			// 2. 查询菜单按钮
			SysUser user = SessionUtils.getUser(request);
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                if (user.getSuperAdmin() == 1) {// 管理员查询所有菜单按钮
                    sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId));
                } else {// 其余角色查询配置好的按钮
                    sysMenuBtns = sysMenuBtnService.queryMenuListByRole(Integer.parseInt(menuId), user.getId());
                }
            }
            
			Page<SmsSendLog> dataList = msmSendService.querySmsTimedTask(dzdPageParam);

			dzdPageResponse.setData(sysMenuBtns);
			if ( !CollectionUtils.isEmpty(dataList) )
			{
				for (SmsSendLog smsSendLog : dataList.getResult()) {
					smsSendLog.setSysMenuBtns(sysMenuBtns);
                }
				dzdPageResponse.setRows(dataList.getResult());
				dzdPageResponse.setTotal(dataList.getTotal());
			}
		} catch (Exception e)
		{
			logger.error(null, e);
			e.printStackTrace();
		}
		return dzdPageResponse;
	}

	/**
	 * 更新定时任务
	 *
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@MethodDescription("更新定时任务")
	@RequestMapping(value = "/updatetimedtask", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateTimedTask(HttpServletRequest request,
	        HttpServletResponse response)
	{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try
		{
			List<SmsSendTask> smsSendTasks = preParameterForSmsSendTask(request, resultMap);

			if ( !CollectionUtils.isEmpty(smsSendTasks) )
			{
				RedisClient redisClient = RedisManager.I.getRedisClient();
				SmsTaskData smsTaskData = null;
				String taskId = null;
				for ( SmsSendTask smsSendTask : smsSendTasks )
				{
					msmSendService.updateTimedTask(smsSendTask);

					// 更新缓存数据
					taskId = smsSendTask.getId().toString();
					smsTaskData = (SmsTaskData) redisClient.hgetObject(Define.KEY_SMS_TASK_CACHE,
					        new String(taskId));
					if ( smsTaskData != null )
					{
						if ( smsSendTask.getSendType() != null )
						{
							smsTaskData.setTaskType(smsSendTask.getSendType());
						}
						if ( smsSendTask.getSendType() != null && 1 == smsSendTask.getSendType() )
						{
							smsTaskData.setFree(true);
							smsTaskData.setAuditState(true);
						} else if ( smsSendTask.getSendType() != null
						        && 2 == smsSendTask.getSendType() )
						{
							smsTaskData.setFree(false);
							smsTaskData.setAuditState(false);
						}
						if ( smsSendTask.getTimingTime() != null )
						{
							smsTaskData.setTiming(smsSendTask.getTimingTime());
							QuartzJobManager.I.addTimeTask(smsTaskData.getTiming(),
							        smsTaskData.getTaskId());
						}
						if ( smsSendTask.getBillingNum() != null )
						{
							if ( (smsSendTask.getBillingNum() - smsTaskData.getBilling_num()) != 0 )
							{
								SmsServerManager.I.addUserBalance(smsTaskData.getUserId(),
								        smsSendTask.getBillingNum() - smsTaskData.getBilling_num(),
								        "任务ID：" + smsTaskData.getTaskId());
								smsTaskData.setBilling_num(smsSendTask.getBillingNum());
							}
						}
						redisClient.hsetObject(Define.KEY_SMS_TASK_CACHE, new String(taskId),
						        smsTaskData);
					}
				}

			}
		} catch (Exception e)
		{
			logger.error(null, e);
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 修改定时任务
	 *
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	@MethodDescription("修改定时任务")
	@RequestMapping(value = "/querytimedtaskformodify")
	@ResponseBody
	public SmsSendLog queryTimedTaskForModify(HttpServletRequest request,
	        HttpServletResponse response)
	{
		SmsSendLog smsSendLog = null;
		try
		{
			Map<String, Object> param = new HashMap<String, Object>();

			String taskId = request.getParameter(Define.REQUESTPARAMETER.TASKID);

			if ( !StringUtils.isEmpty(taskId) )
			{
				param.put(Define.REQUESTPARAMETER.TASKID, taskId);
			}

			smsSendLog = msmSendService.queryTimedTaskForModify(param);

			if ( smsSendLog == null )
			{
				return null;
			}

			fillSmsSendLog(smsSendLog);

		} catch (Exception e)
		{
			logger.error(null, e);
			e.printStackTrace();
		}
		return smsSendLog;
	}

	/**
	 * 删除定时任务
	 *
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	@MethodDescription("删除定时任务")
	@RequestMapping(value = "/deletetimedtask")
	@ResponseBody
	public void deleteTimedTask(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			Map<String, Object> param = new HashMap<String, Object>();

			String taskId = request.getParameter(Define.REQUESTPARAMETER.TASKIDS);

			String[] taskIdStr = taskId.split(",");
			List<String> taskIds = Arrays.asList(taskIdStr);

			if ( !CollectionUtils.isEmpty(taskIds) )
			{
				param.put(Define.REQUESTPARAMETER.TASKIDS, taskIds);
			}

			msmSendService.deleteTimedTask(param);

		} catch (Exception e)
		{
			logger.error(null, e);
			e.printStackTrace();
		}
	}

	/**
	 * 查询定时任务
	 *
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	@MethodDescription("查询定时任务相关号码信息")
	@RequestMapping(value = "/querytimedtaskfororder")
	@ResponseBody
	public DzdResponse queryTimedTaskForOrder(HttpServletRequest request,
	        HttpServletResponse response)
	{
		DzdResponse dzdPageResponse = new DzdResponse();
		try
		{
			// 查询入参
			String taskIds = request.getParameter(Define.REQUESTPARAMETER.TASKIDS);
			List<String> taskIdList = Arrays.asList(taskIds.split(","));

			List<List<String>> dataList = new ArrayList<List<String>>();
			List<String> phonelist = null;
			for(String taskId : taskIdList){
				phonelist = msmSendService.querySmsTaskPhone(taskId);
				dataList.add(phonelist);
			}

			if ( CollectionUtils.isEmpty(dataList) )
			{
				dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
				return dzdPageResponse;
			}
			this.setDataList(dataList);
			dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
		} catch (Exception e)
		{
			logger.error(null, e);
			e.printStackTrace();
		}
		return dzdPageResponse;
	}

	@MethodDescription("批量打包导出数据")
	@RequestMapping(value = "/orderExport")
	@ResponseBody
	public void orderExport(HttpServletRequest request, HttpServletResponse response)
	{
		List<List<String>> dataLists = this.getDataList();

		SmsOrderExportService orderExportService = new SmsOrderExportService();
		orderExportService.taskOrderExport(request, response, dataLists);
	}

	private void fillSmsSendLog(SmsSendLog smsSendLog)
	{
		String content = smsSendLog.getContent();
		int signEndPos = content.indexOf("】");
		smsSendLog.setSignature(content.substring(0, signEndPos + 1));
		smsSendLog.setContent(content.substring(signEndPos + 1, content.length()));
	}

	private List<SmsSendTask> preParameterForSmsSendTask(HttpServletRequest request,
	        Map<String, Object> resultMap) throws ParseException
	{
		List<SmsSendTask> smsSendTasks = new ArrayList<SmsSendTask>();

		String taskId = request.getParameter(Define.REQUESTPARAMETER.TASKID);
		String sendType = request.getParameter(Define.REQUESTPARAMETER.SENDTYPE);
		String taskIds = request.getParameter(Define.REQUESTPARAMETER.TASKIDS);
		String sendTypes = request.getParameter(Define.REQUESTPARAMETER.SENDTYPES);
		String content = request.getParameter(Define.MANAGEMENTUSER.SIGNATURE)
		        + request.getParameter(Define.STATICAL.CONTENT);
		String timing = request.getParameter(Define.REQUESTPARAMETER.TIMING);
		String billingNnum = request.getParameter(Define.REQUESTPARAMETER.BILLINGNNUM);

		if ( !StringUtils.isEmpty(content) )
		{
			SmsRequestParameter parameter = new SmsRequestParameter();
			parameter.setText(content);

			SysUser user = SessionUtils.getUser(request);
			com.dzd.phonebook.util.SmsUser smsUsers = userMessageService
			        .querySmsUserById(user.getId());
			SmsAisleGroup smsAisleGroup = channelService
			        .querySmsAisleGroupById(smsUsers.getAisleGroupId());
			parameter.setUnregTypeId(smsAisleGroup.getUnregTypeId());// 退订格式
			                                                         // 0-关，1-开

			// 1. 验证签名格式
			smsSignatureIsNullValid(content, resultMap);
			if ( !jsonReturnBoolean(resultMap) )
			{
				return smsSendTasks;
			}
			// 3. 验证是否包含特殊符号
			specialSymbolValid(parameter, resultMap);
			if ( !jsonReturnBoolean(resultMap) )
			{
				return smsSendTasks;
			}

			// 4. 验证通道组内容是否需要加退订回T
			unsubscribeValid(parameter, resultMap);
			if ( !jsonReturnBoolean(resultMap) )
			{
				return smsSendTasks;
			}
		}

		String[] taskIdStr = null;
		String[] sendTypeStr = null;
		String[] contentStr = null;
		Date[] timingStr = null;
		int[] billingNnumInt = null;

		if ( taskIds != null && taskIds != "" )
		{
			taskIdStr = taskIds.toString().split(",");
			sendTypeStr = sendTypes.toString().split(",");
		} else
		{
			taskIdStr = new String[1];
			sendTypeStr = new String[1];
			contentStr = new String[1];
			timingStr = new Date[1];
			billingNnumInt = new int[1];

			if ( !StringUtils.isEmpty(taskId) )
			{
				taskIdStr[0] = taskId;
			}
			if ( !StringUtils.isEmpty(sendType) )
			{
				sendTypeStr[0] = sendType;
			}
			if ( !StringUtils.isEmpty(content) )
			{
				contentStr[0] = content;
			}
			if ( !StringUtils.isEmpty(billingNnum) )
			{
				billingNnumInt[0] = Integer.valueOf(billingNnum);
			}
			if ( !StringUtils.isEmpty(timing) )
			{
				timingStr[0] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timing + ":00");
			}
		}

		SmsSendTask smsSendTask = null;
		for ( int i = 0; i < taskIdStr.length; i++ )
		{
			smsSendTask = new SmsSendTask();
			if ( taskIdStr != null && taskIdStr.length != 0 && taskIdStr[0] != null )
			{
				smsSendTask.setId(Integer.valueOf(taskIdStr[i]));
			}
			if ( sendTypeStr != null && sendTypeStr.length != 0 && sendTypeStr[0] != null )
			{
				smsSendTask.setSendType(Integer.valueOf(sendTypeStr[i]));
			}
			if ( contentStr != null && contentStr.length != 0 && contentStr[0] != null )
			{
				smsSendTask.setContent(contentStr[i]);
			}
			if ( timingStr != null && timingStr.length != 0 && timingStr[0] != null )
			{
				smsSendTask.setTimingTime(timingStr[i]);
			}
			if ( billingNnumInt != null && billingNnumInt.length != 0 && billingNnumInt[0] != 0 )
			{
				smsSendTask.setBillingNum(billingNnumInt[i]);
			}

			smsSendTasks.add(smsSendTask);
		}
		return smsSendTasks;
	}

	/**
	 * 
	 * @Title: preParameter
	 * @Description: 查询入参
	 * @author:    hz-liang
	 * @param data
	 * @param parameters
	 * @return  
	 * @return: DzdPageParam   
	 * @throws
	 */
	private DzdPageParam preParameter(Map<String, Object> data, WebRequestParameters parameters,
	        HttpServletRequest request)
	{
		Object email = null;
		Object sendType = null;
		Object aisleName = null;

		if ( !CollectionUtils.isEmpty(data) )
		{
			email = data.get(Define.REQUESTPARAMETER.EMAIL);
			sendType = data.get(Define.REQUESTPARAMETER.SENDTYPE);
			aisleName = data.get(Define.REQUESTPARAMETER.AISLENAME);
		}

		DzdPageParam dzdPageParam = new DzdPageParam();
		Map<String, Object> sortMap = new HashMap<String, Object>();
		if ( parameters.getPagenum() != 0 && parameters.getPagesize() != 0 )
		{
			dzdPageParam.setStart(parameters.getPagenum());
			dzdPageParam.setLimit(parameters.getPagesize());
		}

		if ( !StringUtil.isEmpty(parameters.getStartInput()) )
		{
			sortMap.put(Define.REQUESTPARAMETER.STARTINPUT, parameters.getStartInput());
			sortMap.put(Define.REQUESTPARAMETER.ENDINPUT, parameters.getEndInput());
		}

		if ( email != null && !StringUtil.isEmpty(email.toString()) )
		{
			sortMap.put(Define.REQUESTPARAMETER.EMAIL, email.toString());
		}

		if ( aisleName != null && !StringUtil.isEmpty(aisleName.toString()) )
		{
			sortMap.put(Define.REQUESTPARAMETER.AISLENAME, aisleName.toString());
		}

		if ( sendType != null && !StringUtil.isEmpty(sendType.toString()) )
		{
			sortMap.put(Define.REQUESTPARAMETER.SENDTYPE, Integer.parseInt(sendType.toString()));
		}

		SysUser user = SessionUtils.getUser(request);
		if ( user.getId() != 1 )
		{
			SmsUser smsUser = SmsServerManager.I.getUserBySysId(Long.valueOf(user.getId()));
			sortMap.put(Define.STATICAL.SMSID, smsUser.getId());
			sortMap.put(Define.STATICAL.SYSID, smsUser.getSysId());
		}

		dzdPageParam.setCondition(sortMap);
		return dzdPageParam;
	}

	/**
	* 检验签名
	*
	* @param content
	* @return
	*/
	private void smsSignatureIsNullValid(String content, Map<String, Object> resultMap)
	{
		boolean signFlag = true;
		int code = 0;
		String msg = "";

		// 注意判断签名在前，或在后，中间的不算
		String smsContent = content;
		int signStartPos = smsContent.indexOf("【");
		int signEndPos = smsContent.indexOf("】");

		// 判断是否包含【】符号
		if ( signStartPos == 0 && signEndPos > 0 )
		{
			String sign = smsContent.substring(signStartPos + 1, signEndPos);
			// 判断签名字数3~8个字
			if ( sign.length() >= 3 && sign.length() <= 8 )
			{
				// 判断是否存在双签名
				String twoContent = smsContent.substring(signEndPos + 1, smsContent.length());
				int twoStartPos = twoContent.indexOf("【");
				int twoEndPos = twoContent.indexOf("】");
				if ( twoStartPos != -1 && twoEndPos > 0 )
				{// 存在返回双签名提示
					signFlag = false;
					code = -107;
					msg = Define.USERSENDSMS.SMS_SEND_SIGNTRUE_DOUBLE_MSG;
				} else
				{
					signFlag = true;
				}
			} else
			{// 返回字数错误提示
				signFlag = false;
				code = -107;
				msg = Define.USERSENDSMS.SMS_SEND_SIGNTURE_LENGTH;
			}
		} else
		{// 返回没有【】签名符号
			signFlag = false;
			code = -107;
			msg = Define.USERSENDSMS.SMS_SEND_SIGNATURE;
		}
		getResultBooleanJson(signFlag, code, msg, resultMap);
	}

	public void getResultBooleanJson(boolean flag, Integer code, String msg,
	        Map<String, Object> resultMap)
	{
		resultMap.put(SMS_FLAG_BOOLEAN, flag);
		resultMap.put("code", code);
		resultMap.put("msg", msg);
	}

	private boolean jsonReturnBoolean(Map<String, Object> resultMap)
	{
		if ( !Boolean.parseBoolean(resultMap.get(SMS_FLAG_BOOLEAN).toString()) )
		{
			return false;
		}
		return true;
	}

	/**
	 * 校验是否包含特殊符号
	 *
	 * @param parameter
	 * @return
	 */
	private void specialSymbolValid(SmsRequestParameter parameter, Map<String, Object> resultMap)
	{
		StringBuffer sb = new StringBuffer();
		boolean sysmolFlag = true;
		int code = 0;
		String msg = "";

		List<SmsSpecificSymbol> smsSpecificSymbolList = smsSpecificSymbolService
		        .querySpecificSymbolList();
		for ( SmsSpecificSymbol smsSpecificSymbol : smsSpecificSymbolList )
		{
			String[] sysmolStr = smsSpecificSymbol.getName().split(",");

			for ( String sysmol : sysmolStr )
			{
				if ( parameter.getText().indexOf(sysmol) != -1 )
				{
					sysmolFlag = false;
					sb.append(sysmol + ",");
				}
			}
		}
		if ( !sysmolFlag )
		{
			String sysmol = sb.toString();
			if ( sysmol != null && !sysmol.equals("") )
			{// 移除最后一个逗号
				sysmol = sysmol.substring(0, sysmol.length() - 1);
			}
			msg = Define.USERSENDSMS.SMS_SEND_SPECIALSYMBOLMSG + " " + sysmol + " " + ",请返回修改。";
			code = -100;
		}

		getResultBooleanJson(sysmolFlag, code, msg, resultMap);
	}

	/**
	 * 验证内容是否有加退订回T
	 *
	 * @param parameter
	 * @return
	 */
	private void unsubscribeValid(SmsRequestParameter parameter, Map<String, Object> resultMap)
	{
		boolean unsubFlag = true;
		int code = 0;
		String msg = "";

		Integer unregTypeId = parameter.getUnregTypeId(); // 退订格式 0-关，1-开
		if ( unregTypeId == 1 )
		{
			// 判断结尾是否存在退订回X、回X退订
			if ( !parameter.getText().contains(Define.USERSENDSMS.SMS_SEND_CONTENT_UNREG) )
			{
				code = -104;
				msg = Define.USERSENDSMS.SMS_SEND_AISLE_FORMAT;
				unsubFlag = false;
			}
		}
		getResultBooleanJson(unsubFlag, code, msg, resultMap);
	}

}
