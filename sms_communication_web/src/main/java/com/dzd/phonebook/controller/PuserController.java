package com.dzd.phonebook.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.poifs.storage.ListManagedBlock;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.http.HttpRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.pool.GetConnectionTimeoutException;
import com.dzd.base.util.Constant;
import com.dzd.base.util.MethodUtil;
import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.SmsUserMessage;
import com.dzd.phonebook.entity.SysMenu;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.page.MD5;
import com.dzd.phonebook.service.ChannelService;
import com.dzd.phonebook.service.Instruct;
import com.dzd.phonebook.service.MsmSendService;
import com.dzd.phonebook.service.SmsOrderExportService;
import com.dzd.phonebook.service.SmsUserService;
import com.dzd.phonebook.service.SysMenuBtnService;
import com.dzd.phonebook.service.SysMenuService;
import com.dzd.phonebook.service.SysRoleRelService;
import com.dzd.phonebook.service.SysUserService;
import com.dzd.phonebook.service.UserMessageService;
import com.dzd.phonebook.util.ComparatorSmsUser;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdParameters;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.InstructState;
import com.dzd.phonebook.util.JspResponseBean;
import com.dzd.phonebook.util.RedisUtil;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsAisleGroupType;
import com.dzd.phonebook.util.SmsRechargeUser;
import com.dzd.phonebook.util.SmsSendLog;
import com.dzd.phonebook.util.SmsSendTask;
import com.dzd.phonebook.util.SmsSendTaskPhone;
import com.dzd.phonebook.util.SmsShieldWord;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.phonebook.util.SmsUserBlank;
import com.dzd.phonebook.util.SmsUserMoneyRunning;
import com.dzd.phonebook.util.TestBean;
import com.dzd.phonebook.util.WebRequestParameters;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.github.pagehelper.Page;

/**
 * 会员controller
 *
 * @author oygy
 * @date 2016-6-24 16:11:00
 */
@Controller
@RequestMapping("/puser")
public class PuserController extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(PuserController.class);

    @Autowired
    private SysMenuBtnService sysMenuBtnService;

    @Autowired
    private SysRoleRelService sysRoleRelService;

 
    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private SysUserService<SysUser> sysUserService;

    @Autowired
    private SmsUserService<?> smsUserService;

    @Autowired
    private ChannelService channelService;

    @SuppressWarnings("rawtypes")
    @Autowired
    private MsmSendService msmSendService;

    @Autowired
    private SysMenuService<SysMenu> sysMenuService;

    private Page<SmsUser> dataList = new Page<SmsUser>();

    public Page<SmsUser> getDataList() {
        return dataList;
    }

	public void setDataList(Page<SmsUser> dataList) {
		this.dataList = dataList;
	}
	
	/**
	 * @Description:新增屏蔽词
	 * @author:Fini
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/insert/shieldingword")
	@ResponseBody
	public Map<String, Object> insertShieldingWord(HttpServletRequest request) throws UnsupportedEncodingException {
		//获取入参条件
		String word = request.getParameter(Define.SHIELDINGWORD.WORD);
		
		//创建用于存储返回信息的map集合
		Map<String, Object> hintMsg = new HashMap<String, Object>();
		
		//对入参进行判空
		if (StringUtil.isEmpty(word)) {
			hintMsg.put(Define.SMSSENDTASK.MSG, Define.INTERFACE_STATE_SHIELDING_EMPTY.message);
			hintMsg.put(Define.SMSSENDTASK.CODE, Define.INTERFACE_STATE_SHIELDING_EMPTY.code);
			
			return hintMsg;
		}
		
		//转换入参字符集格式
		String decode = java.net.URLDecoder.decode(new String(word.getBytes("iso-8859-1")), "utf-8");
		
		//屏蔽词入库
		smsUserService.insertShieldingWord(decode);
		hintMsg.put(Define.SMSSENDTASK.MSG, Define.INTERFACE_STATE_SHIELDING_SUCCESS.message);
		hintMsg.put(Define.SMSSENDTASK.CODE, Define.INTERFACE_STATE_SHIELDING_SUCCESS.code);
		
		return hintMsg;
	}
	
	/**
	 * @Description:删除屏蔽词
	 * @author:Fini
	 * @throws UnsupportedEncodingException 
	 */
	/**
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/delete/shieldingword")
	@ResponseBody
	public Map<String, Object> deleteShieldingWord(HttpServletRequest request) throws UnsupportedEncodingException {
		//获取入参条件
		String deleteModel = request.getParameter(Define.SHIELDINGWORD.DELETEMODEL); 
		String word = request.getParameter(Define.SHIELDINGWORD.WORD);
		
		//创建用于存储返回信息的map集合
		Map<String, Object> hintMsg = new LinkedHashMap<String, Object>();
		
		//对入参条件进行判空
		if (StringUtil.isEmpty(word)) {
			hintMsg.put(Define.SMSSENDTASK.MSG, Define.INTERFACE_STATE_SHIELDING_EMPTY.message);
			hintMsg.put(Define.SMSSENDTASK.CODE, Define.INTERFACE_STATE_SHIELDING_EMPTY.code);
			
			return hintMsg;
		}
		
		//转换入参字符集格式
		String decode = java.net.URLDecoder.decode(new String(word.getBytes("iso-8859-1")), "utf-8");
		
		
		//判断模糊删除或精确删除，默认精确删除，当且仅当deleteModel是"0"时执行模糊删除
		if (!(StringUtil.isEmpty(deleteModel)) && deleteModel.equals("0")) {
			//返回执行模糊删除结果信息
			return deleteFuzzy(hintMsg, decode);
		}
		
		//返回执行精确删除结果信息
		return deletePrecise(hintMsg, decode);
	}
	
	//封装精确删除方法
	private Map<String, Object> deletePrecise(Map<String, Object> hintMsg, String decode) {
		//调用精确查询获取list数据
		List<SmsShieldWord> preciseQueryList = smsUserService.queryShieldingWordPrecise(decode);
		
		//判断是否存可以精确删除的记录
		if (CollectionUtils.isEmpty(preciseQueryList)) {
			hintMsg.put(Define.SMSSENDTASK.MSG, Define.INTERFACE_STATE_SHIELDING_WORD_EMPTY.message);
			hintMsg.put(Define.SMSSENDTASK.CODE, Define.INTERFACE_STATE_SHIELDING_WORD_EMPTY.code);
			
			return hintMsg;
		}
		
		//获取对象，设置屏蔽词字段的值
		smsUserService.deleteShieldingWordPrecise(decode);	
		
		hintMsg.put(Define.SMSSENDTASK.MSG, Define.INTERFACE_STATE_SHIELDING_WORD_SUCCESS.message);
		hintMsg.put(Define.SMSSENDTASK.CODE, Define.INTERFACE_STATE_SHIELDING_WORD_SUCCESS.code);
		
		return hintMsg;
	}

	//封装模糊删除方法
	private Map<String, Object> deleteFuzzy(Map<String, Object> hintMsg, String decode) {
		//调用模糊查询获取list数据
		List<SmsShieldWord> fuzzyQueryList = smsUserService.queryShieldingWordFuzzy(decode);
		
		//判断是否存可以模糊删除的记录
		if (CollectionUtils.isEmpty(fuzzyQueryList)) {
			hintMsg.put(Define.SMSSENDTASK.MSG, Define.INTERFACE_STATE_SHIELDING_WORD_EMPTY.message);
			hintMsg.put(Define.SMSSENDTASK.CODE, Define.INTERFACE_STATE_SHIELDING_WORD_EMPTY.code);
			
			return hintMsg;
		}
		
		//获取对象，设置屏蔽词字段的值
		smsUserService.deleteShieldingWordFuzzy(decode);	
		
		hintMsg.put(Define.SMSSENDTASK.MSG, Define.INTERFACE_STATE_SHIELDING_WORD_SUCCESS.message);
		hintMsg.put(Define.SMSSENDTASK.CODE, Define.INTERFACE_STATE_SHIELDING_WORD_SUCCESS.code);
		
		return hintMsg;
	}
	
	/**
	 * @Description:根据id更新短信内容
	 * @author:Fini
	 * @throws UnsupportedEncodingException
	 * @time:2017年7月4日 上午16:12:53
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/update/updatecontent")
	@ResponseBody
	public Map<String, Object> updateSmsContentById(HttpServletRequest request) throws UnsupportedEncodingException {

		// 获取入参条件
		String id = request.getParameter(Define.SMSSENDTASK.ID);
		String content = request.getParameter(Define.SMSSENDTASK.CONTENT);

		// 创建map集合存储状态码与状态信息
		Map<String, Object> hintMsg = new HashMap<String, Object>();

		// 对入参条件进行判空
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(content)) {
			hintMsg.put(Define.SMSSENDTASK.MSG, Define.INTERFACE_STATE_PARAM_EMPTY.message);
			hintMsg.put(Define.SMSSENDTASK.CODE, Define.INTERFACE_STATE_PARAM_EMPTY.code);
			return hintMsg;
		}

		// 将request接收到的数据字符集改为utf-8
		String decode = java.net.URLDecoder.decode(new String(content.getBytes("iso-8859-1")), "utf-8");

		// 对屏蔽词进行判断
		String str1 = "毒品";
		String str2 = "枪支";
		if (decode.contains(str1) || decode.contains(str2)) {
			hintMsg.put(Define.SMSSENDTASK.MSG, Define.INTERFACE_STATE_SHIELDING.message);
			hintMsg.put(Define.SMSSENDTASK.CODE, Define.INTERFACE_STATE_SHIELDING.code);
			return hintMsg;
		}
		
		// 调用SERVICE的方法获取sms_send_task表的数据
		List<SmsSendTask> smsSendTaskList = smsUserService.querySmsSendTaskListById(id);

		// 对获取的数据进行判空
		if (CollectionUtils.isEmpty(smsSendTaskList)) {
			hintMsg.put(Define.SMSSENDTASK.MSG, Define.INTERFACE_STATE_ID_EMPTY.message);
			hintMsg.put(Define.SMSSENDTASK.CODE, Define.INTERFACE_STATE_ID_EMPTY.code);
			return hintMsg;
		}

		// 更新任务内容
		for (SmsSendTask smsSendTask : smsSendTaskList) {
				smsSendTask.setContent(decode);
				smsUserService.updateSmsContent(smsSendTask);
				hintMsg.put(Define.SMSSENDTASK.MSG, Define.INTERFACE_STATE_UPDATE_SUCCESS.message);
				hintMsg.put(Define.SMSSENDTASK.CODE, Define.INTERFACE_STATE_UPDATE_SUCCESS.code);
		}

		return hintMsg;
	}
	
	/**
	 * @Description:根据用户或任务或 发送时间查询短信详细信息
	 * @author:Fini
	 * @time:2017年6月30日 上午16:12:53
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/query/smsdetail")
	@ResponseBody
	public List<SmsSendTask> querySmsDataConstructTree(HttpServletRequest request) {
		//获取入参条件
		String smsUserId = request.getParameter(Define.QUERYINFO.SMSUSERID);
		String sendTaskId = request.getParameter(Define.QUERYINFO.SENDTASKID);
		String sendTime = request.getParameter(Define.QUERYINFO.SENDTIME);
		
		//调用SERVICE的方法获取查询数据
		List<SmsSendTask> smsSendTaskList = smsUserService.querySmsSendTaskList();
		List<SmsSendTaskPhone> smsSendTaskPhoneList = smsUserService.querySmsSendTaskPhoneList();
		
		//定义存储过滤后数据的集合
		List<SmsSendTask> filterQueryList = new ArrayList<SmsSendTask>();

		//对查询的数据进行判空
		if(CollectionUtils.isEmpty(smsSendTaskList) || CollectionUtils.isEmpty(smsSendTaskPhoneList)){
			return smsSendTaskList;
		}
		
		//将SmsSendTaskPhone按smsSendTaskId分别存入Map<Integer, List<SmsSendTaskPhone>>集合中
		Map<Integer, List<SmsSendTaskPhone>> taskPhoneMap = new HashMap<Integer, List<SmsSendTaskPhone>>();
		List<SmsSendTaskPhone> taskPhoneList = null;
		for (SmsSendTaskPhone sendPhone : smsSendTaskPhoneList) {
			
			if (CollectionUtils.isEmpty(taskPhoneMap) || !taskPhoneMap.containsKey(sendPhone.getSmsSendTaskId())) {
				taskPhoneList = new ArrayList<SmsSendTaskPhone>();
			}
			if(taskPhoneMap.containsKey(sendPhone.getSmsSendTaskId())){
				taskPhoneList = taskPhoneMap.get(sendPhone.getSmsSendTaskId());
			}
			taskPhoneList.add(sendPhone);
			taskPhoneMap.put(sendPhone.getSmsSendTaskId(), taskPhoneList);
		}
		
		//根据任务id将phone表中的数据一一对应存入
		for (SmsSendTask sendTask : smsSendTaskList) {
			sendTask.setSmsSendTaskPhones(taskPhoneMap.get(sendTask.getId()));
		}
		
		//嵌套for循环存储树状结构数据
	/*	for (SmsSendTask sendTask : smsSendTaskList) {
			
			//根据任务ID查询对应的SmsSendTaskPhone数据
			smsSendTaskPhones = new ArrayList<SmsSendTaskPhone>();
			for (SmsSendTaskPhone sendPhone : smsSendTaskPhoneList) {
				if (sendTask.getId().equals(sendPhone.getSmsSendTaskId())) {
					smsSendTaskPhones.add(sendPhone);
				}
				
			sendTask.setSmsSendTaskPhones(smsSendTaskPhones);
			
			}
		}*/
		
		// 无条件过滤查询
		if (smsUserId == null && sendTaskId == null && sendTime == null) {
			return smsSendTaskList;
		}

		// 根据过滤条件查询数据
		for (SmsSendTask smsSendTask : smsSendTaskList) {
			// 获取过滤条件参数的字符串表现形式
			String smsUserIdString = String.valueOf(smsSendTask.getSmsUserId());
			String sendTaskIdString = String.valueOf(smsSendTask.getId());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date getSentTime = smsSendTask.getSendTime();
			String sendTimeString = sdf.format(getSentTime);

			// 当有一个以上过滤条件不存在时
			if (!(smsUserId == null || smsUserIdString.equals(smsUserId))
					|| !(sendTaskId == null || sendTaskIdString.equals(sendTaskId))
					|| !(sendTime == null || sendTimeString.equals(sendTime))) {

				continue;
			} else if (smsUserIdString.equals(smsUserId) && sendTaskId == null && sendTime == null) {
				// smsUserId过滤条件查询
				filterQueryList.add(smsSendTask);
			} else if (sendTaskIdString.equals(sendTaskId) && smsUserId == null && sendTime == null) {
				// sendTaskId过滤条件查询
				filterQueryList.add(smsSendTask);
			} else if (sendTimeString.equals(sendTime) && smsUserId == null && smsUserId == null) {
				// sendTime过滤条件查询
				filterQueryList.add(smsSendTask);
			} else {
				// 其他过滤条件查询
				filterQueryList.add(smsSendTask);
			}
		}

		return filterQueryList;
	}

	/**
	 * @Description:根据用户name smsAisleName查询短信平台统计信息
	 * @author:Fini
	 * @time:2017年6月29日 上午11:12:53
	 */
	@RequestMapping("/query/statisticslist")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryStattisticsList(HttpServletRequest request) {
		// 获取接口入参条件
		Object name = request.getParameter("name");
		Object smsAisleName = request.getParameter("smsAisleName");

		List<SmsUser> smsUsersList = smsUserService.querySmsAgentStatisticsList();

		List<Map<String, Object>> smsListMap = new ArrayList<Map<String, Object>>();

		for (SmsUser smsUser : smsUsersList) {
			// 判断过滤条件
			if (smsAisleName==null && name == null) {
				getSmsListMapBySmsUser(smsListMap, smsUser);
				continue;
			} 
			if (smsUser.getName().equals(name) && smsAisleName == null) {
				getSmsListMapBySmsUser(smsListMap, smsUser);
				continue;
			} 
			
			if (smsUser.getSmsAisleName().equals(smsAisleName) && name == null) {
				getSmsListMapBySmsUser(smsListMap, smsUser);
				continue;
			} 
			
			if (smsUser.getName().equals(name) && smsUser.getSmsAisleName().equals(smsAisleName)) {
				getSmsListMapBySmsUser(smsListMap, smsUser);
				continue;
			} 
		}
		
		
		//判断入参条件是否存在
		if (smsListMap.isEmpty()) {
			//给用户反馈过滤条件异常
			Map<String, Object> smsMap = new HashMap<String, Object>();
			smsMap.put("msg", "用户或通道名称不存在,请确认后重新发送!!!");
			smsMap.put("code", "1001");
			smsListMap.add(smsMap);
		}

		return smsListMap;
	}

	//抽取数据封装逻辑
	private void getSmsListMapBySmsUser(List<Map<String, Object>> smsListMap, SmsUser smsUser) {
		Map<String, Object> smsMap = new LinkedHashMap<String, Object>();

		smsMap.put("name", smsUser.getName());
		smsMap.put("email", smsUser.getEmail());
		smsMap.put("smsAisleName", smsUser.getSmsAisleName());
		smsMap.put("smsAisleTypeId", smsUser.getSmsAisleTypeId());
		smsMap.put("sendNum", smsUser.getSendNum());
		smsMap.put("billingNum", smsUser.getBillingNum());
		smsMap.put("succeedNumUs", smsUser.getSucceedNumUs());
		smsMap.put("failureNumUs", smsUser.getFailureNumUs());
		smsMap.put("unknownNumUs", smsUser.getUnknownNumUs());
		smsMap.put("succeedNumMs", smsUser.getSucceedNumMs());
		smsMap.put("failureNumMs", smsUser.getSucceedNumMs());
		smsMap.put("unknownNumMs", smsUser.getUnknownNumMs());
		smsMap.put("succeedNumTs", smsUser.getSucceedNumTs());
		smsMap.put("failureNumTs", smsUser.getFailureNumTs());
		smsMap.put("unknownNumTs", smsUser.getUnknownNumTs());

		smsListMap.add(smsMap);
	}

    @RequestMapping("/listview")
    public String list(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        Object ptype = request.getParameter("ptype");
        if (ptype != null && !ptype.equals("") && ptype.toString().equals("1")) {  //判断时候首页过来
            Date lastTime = smsUserService.queryLastSmsUserTime(); //得到最后查询的时间
            Date newTime = new Date();
            if (lastTime != null) {
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String lastsString = sd.format(lastTime);
                String newString = sd.format(newTime);
                model.addAttribute("lastTime", lastsString);
                model.addAttribute("newTime", newString);
                smsUserService.updateLastSmsUserTime(newTime);
            } else {
                smsUserService.addLastSmsUserTime(newTime);
            }

        }
        model.addAttribute("menuId", menuId);
        SysUser user = (SysUser) request.getSession().getAttribute("session_user");
        List<SysRoleRel> sysRoleRels = queryRoleByUserId(user.getId());
        List<SysUser> sysUserList = new ArrayList<SysUser>();
        int num = 0;

        for (SysRoleRel sysRoleRel : sysRoleRels) {
            if (sysRoleRel.getRoleId() == 51) {  //如果当前登录用户为经理
                sysUserList = sysUserService.queryYwSysUserList(user.getId()); //查询出经理下面的所有业务员
                //model.addAttribute("sysUsertList",sysUsertList);
                break;
            }
        }
        for (SysRoleRel sysRoleRel : sysRoleRels) {
            if (48 == sysRoleRel.getRoleId()) {        //判断是否业务员
                num = 1;
                break;
            }
        }


        List<SmsAisleGroupType> list = channelService.querySmsAisleGroupType();

        model.addAttribute("sysUser", user);
        model.addAttribute("sysUserList", sysUserList);
        model.addAttribute("num", num);
        model.addAttribute("typeList", list);
        return "puser/puserlist";
    }


    /**
     * @Description:根据用户ID查询拥有角色
     * @author:oygy
     * @time:2016年12月31日 上午11:12:53
     */
    private List<SysRoleRel> queryRoleByUserId(Integer uid) {
        DzdPageParam dzdPageParam = new DzdPageParam();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("sysUserId", uid);
        dzdPageParam.setCondition(condition);
        List<SysRoleRel> sysRoleRels = sysRoleRelService.queryRoleByUserId(dzdPageParam);
        return sysRoleRels;
    }


    /**
     * @Description:查询代理信息列表
     * @author:oygy
     * @time:2016年12月31日 下午2:01:34
     */
    @RequestMapping(value = "/puserList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse puserList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");
            Object state = data.get("state");
            Object tdSelect = data.get("tdState");
            Object email = data.get("email");
            Object bid = data.get("bid");

            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            List<SysRoleRel> sysRoleRels = queryRoleByUserId(user.getId());
            List<Integer> rolist = minRoid(sysRoleRels);

            if (menuId == null || user == null || sysRoleRels == null) {
                return dzdPageResponse;
            }
            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }

            if (!StringUtil.isEmpty(parameters.getStartInput())) {
                sortMap.put("startInput", parameters.getStartInput());
                sortMap.put("endInput", parameters.getEndInput());
            }

            if (state != null && !StringUtil.isEmpty(state.toString())) {
                sortMap.put("state", state.toString());
            }

            if (email != null && !StringUtil.isEmpty(email.toString())) {
                sortMap.put("email", email.toString());
            }

            if (bid != null && !StringUtil.isEmpty(bid.toString())) {
                sortMap.put("bid", bid.toString());
            }

            if (tdSelect != null && !StringUtil.isEmpty(tdSelect.toString())) {
                sortMap.put("teamId", Integer.parseInt(tdSelect.toString()));
            }

            //
            // SysUser user = (SysUser)request.getSession().getAttribute("session_user");
            if (user != null && user.getId() != 1) {
                sortMap.put("smsUserVal", "select id from sys_user where id=" + user.getId() + " or superiorId = " + user.getId() + "");
            }


            //排序
            if (parameters.getSort() != null && parameters.getOrder() != null) {
                sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
            } else {
                sortMap.put("sortVal", "order by createTime desc");
            }
            dzdPageParam.setCondition(sortMap);
            List<SysMenuBtn> sysMenuBtns = null;

            if (menuId != null) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("userId", user.getId());
                param.put("rolist", rolist);
                param.put("menuId", Integer.parseInt(menuId.toString()));
                sysMenuBtns = sysMenuBtnService.queryByMenuid2(param);
                // sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
            }

            Page<SmsUser> dataList = smsUserService.querySmsUserList(dzdPageParam);
            Integer statistical = smsUserService.querySmsStatistical(dzdPageParam);

            if (dataList.size() > 0) {
                dataList.getResult().get(0).setStatistical(statistical);
            }
            dzdPageResponse.setData(sysMenuBtns);
            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsUser instruct : dataList.getResult()) {
                    instruct.setSysMenuBtns(sysMenuBtns);
                }
                dzdPageResponse.setRows(dataList.getResult());
                dzdPageResponse.setTotal(dataList.getTotal());
            }
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }


    private List<Integer> minRoid(List<SysRoleRel> sysRoleRels) {

        List<Integer> list = new ArrayList<Integer>();
        for (SysRoleRel sysRoleRel : sysRoleRels) {
            list.add(sysRoleRel.getRoleId());
        }
        return list;
    }


    /**
     * 代理新增或修改
     *
     * @param request
     * @return
     */
    @MethodDescription("新增代理或修改代理")
    @RequestMapping(value = "/from/merge", method = RequestMethod.POST)
    public String merge(HttpServletRequest request, HttpServletResponse response, Model model) {
        Object menuId = request.getParameter("menuId");
        try {
            SmsUser smsUser = new SmsUser();                  //代理账户
            SmsUserBlank smsUserBlank = new SmsUserBlank();  //代理账户的“钱包”
            SysUser sysUser = new SysUser();                  //分配给代理账户的系统账号

            Object id = request.getParameter("id");
            Object bid = request.getParameter("bid");                //业务员ID
            Object sysUserId = request.getParameter("sysUserId");   //系统用户ID
            Object smsUserBlankId = request.getParameter("smsUserBlankId");   //代理账户钱包ID


            Object aisleType = request.getParameter("aisleType");            //客户姓名
            Object name = request.getParameter("name");            //客户姓名
            Object email = request.getParameter("userEmail");            //账号
            //Object pwd = request.getParameter("userPwd");        		//密码
            Object state = request.getParameter("state");            //状态
            //Object money = request.getParameter("money");      		//预存金额
            //Object awardMoney = request.getParameter("awardMoney");	//授信额度
            //Object contact = request.getParameter("contact");   	//联系人
            Object phone = request.getParameter("phone");            //联系电话
            Object describes = request.getParameter("describes");    //描述
            //Object signature = request.getParameter("signature");	//签名
            Object replyUrl = request.getParameter("replyUrl");        //上行推送地址
            Object reportUrl = request.getParameter("reportUrl");    //报告推送地址
            Object checkboxs = request.getParameter("checkboxs");    //报告推送地址
            //Object level = request.getParameter("level");	        //级别

            Object aisleGroupId = request.getParameter("aisleGroup");    //选择通道组


            Object joinupCoding = request.getParameter("joinupCoding");    //通道接入码
            Object firmName = request.getParameter("firmName");            //企业名称
            Object firmPwd = request.getParameter("firmPwd");            //企业密码
            Object joinuoMax = request.getParameter("joinuoMax");        //最大链接量
            Object firmIp = request.getParameter("firmIp");                //企业IP
            Object httpProtocol = request.getParameter("httpProtocol"); //http协议支持
            Object cmppProtocol = request.getParameter("cmppProtocol"); //cmpp协议支持
            Object defaultAgid = request.getParameter("defaultAgid");    //默认通道组ID

            log.info("bid:" + bid);

            if (id != null && !id.toString().isEmpty()) {
                smsUser.setId(Integer.parseInt(id.toString()));
            }

            if (smsUserBlankId != null && !smsUserBlankId.toString().isEmpty()) {
                smsUserBlank.setId(Integer.parseInt(smsUserBlankId.toString()));  //钱包ID
            }

            if (sysUserId != null && !sysUserId.toString().isEmpty()) {
                sysUser.setId(Integer.parseInt(sysUserId.toString()));  //分配系统用户  系统用户ID
            }
            if (name != null && !name.toString().isEmpty()) {
                smsUser.setName(name.toString());
                sysUser.setNickName(name.toString());         //分配系统用户 记录昵称
            }
            Integer tdId = -1;
            if (bid != null && !bid.toString().isEmpty()) {
                smsUser.setBid(Integer.parseInt(bid.toString()));
                sysUser.setSuperiorId(Integer.parseInt(bid.toString()));
                //根据上级业务员ID查询团队ID
                tdId = sysUserService.queryTdUser(Integer.parseInt(bid.toString()));
            }
            if (tdId != null && tdId != -1) {
                smsUser.setTeamId(tdId);
            }
            if (email != null && !email.toString().isEmpty()) {
                smsUser.setEmail(email.toString());
                sysUser.setEmail(email.toString());                 //分配系统用户 记录账号
            }
            String pwd2 = MethodUtil.MD5("123456");     //分配系统用户  记录密码
            sysUser.setPwd(pwd2);
            if (state != null && !state.toString().isEmpty()) {
                smsUser.setState(Integer.parseInt(state.toString()));
            }
            if (describes != null && !describes.toString().isEmpty()) {
                smsUser.setDescribes(describes.toString());
            }
            if (replyUrl != null && !replyUrl.toString().isEmpty()) {
                smsUser.setReplyUrl(replyUrl.toString());
            }
            if (reportUrl != null && !reportUrl.toString().isEmpty()) {
                smsUser.setReportUrl(reportUrl.toString());
            }
            if (checkboxs != null && !checkboxs.toString().isEmpty()) {
                smsUser.setSignatureCheck(1);
            } else {
                smsUser.setSignatureCheck(0);
            }
            if (httpProtocol != null && !httpProtocol.toString().isEmpty()) {
                smsUser.setHttpProtocol(1);
            } else {
                smsUser.setHttpProtocol(0);
            }

            if (cmppProtocol != null && !cmppProtocol.toString().isEmpty()) {
                smsUser.setCmppProtocol(1);
            } else {
                smsUser.setCmppProtocol(0);
            }

            if (phone != null && !phone.toString().isEmpty()) {
                smsUser.setPhone(phone.toString());
            }

            if (joinupCoding != null && !joinupCoding.toString().isEmpty()) {
                smsUser.setJoinupCoding(joinupCoding.toString());
            }

            if (firmName != null && !firmName.toString().isEmpty()) {
                smsUser.setFirmName(firmName.toString());
            }

            if (firmPwd != null && !firmPwd.toString().isEmpty()) {
                smsUser.setFirmPwd(firmPwd.toString());
            }

            if (firmIp != null && !firmIp.toString().isEmpty()) {
                smsUser.setFirmIp(firmIp.toString());
            }

            if (joinuoMax != null && !joinuoMax.toString().isEmpty()) {
                smsUser.setJoinuoMax(Integer.parseInt(joinuoMax.toString()));
            }
            if (defaultAgid != null && !defaultAgid.toString().isEmpty()) {
                smsUser.setDefaultAgid(Integer.parseInt(defaultAgid.toString()));
            }

            if (aisleType != null && !aisleType.toString().isEmpty()) {
                smsUser.setGroupTypeId(Integer.parseInt(aisleType.toString()));
            }

            if (aisleGroupId != null && !aisleGroupId.toString().isEmpty()) {
                smsUser.setAisleGroupId(Integer.parseInt(aisleGroupId.toString()));
            }


            if (smsUser.getId() == null) {
                log.info("-----------------》新增代理");
                //1.分配系统账号
                sysUser.setState(1);
                sysUser.setDeleted(0);
                sysUser.setUserType(2);
                sysUser.setSuperAdmin(0);
                sysUserService.saveUser(sysUser, null);
                //2.分配代理权限
                sysUserService.sevaSysRoleRels(sysUser.getId());
                //2.添加代理信息
                smsUser.setSysUserId(sysUser.getId());
                smsUser.setKey(queryKey());
                //smsUser.setSmsUserBlankId(smsUserBlank.getId());
                smsUserService.saveSmsUser(smsUser);
                //3.代理用户钱包
                smsUserBlank.setUserId(smsUser.getId());
                smsUserService.addSmsUserBlank(smsUserBlank);
                instructSend(InstructState.ADDSMSUSER_SUCESS, smsUser.getKey(), smsUser.getId());   //发送动作指令到redis
            } else {
                log.info("-----------------》修改代理");
                sysUserService.update(sysUser);
                //smsUserService.updateSmsUserBlank(smsUserBlank);
                smsUserService.updateSmsUser(smsUser);
                String keys = smsUserService.querySmsUserKey(smsUser.getId());
                instructSend(InstructState.UPDATESMSUSER_SUCESS, keys, smsUser.getId());   //发送动作指令到redis
            }
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return "redirect:/puser/listview.do?id=" + menuId;
    }


    /**
     * @Description:MD5加密生成Key
     * @author:oygy
     * @time:2017年1月11日 下午3:12:52
     */
    public String queryKey() {
             /* 得到当前时间戳*/
        Date dt = new Date();
        Long currentTime = dt.getTime();

        Random rand = new Random();
        int randNum = rand.nextInt(10000) + 1;

        MD5 getMD5 = new MD5();
        String md5Token = getMD5.GetMD5Code(randNum + "" + currentTime);//得到MD5加密后的MD5串
        return md5Token;
    }
	    
	    
	 /*   public static void main(String[] args) {
	    	String num =queryKey();
	    	System.out.println(num);
		}*/

    /**
     * @Description:代理数据处理动作发送
     * @author:oygy
     * @time:2017年1月11日 下午2:45:22
     */
    private void instructSend(String keys, String smsUserKey, Integer smsUserId) {
        Instruct instruct = new Instruct();
        instruct.setKey(keys);
        instruct.setSmsUserKey(smsUserKey);
        instruct.setSmsUserId(smsUserId + "");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonStr = mapper.writeValueAsString(instruct);
            RedisUtil.publish(InstructState.AB, jsonStr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /**
     * @Description:根据新增账户信息查询该账号是否已分配
     * @author:oygy
     * @time:2017年1月3日 上午11:56:29
     */
    @RequestMapping(value = "/querySysUserbyMsmuser", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DzdResponse merge(HttpServletRequest request, @RequestBody Map<String, Object> data) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            Object id = request.getParameter("id");
            Object userEmal = data.get("userEmal");      //获取分配的账户名，
            Object fname = data.get("fname");
            if (userEmal == null || "".equals(userEmal)) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdPageResponse;
            }
            SmsUser smsUser = new SmsUser();
            smsUser.setEmail(userEmal.toString());
            if (id != null && !"".equals(id)) {
                smsUser.setId(Integer.parseInt(id.toString()));
            }

            Integer num = sysUserService.querySysUserByuserEmal(smsUser);
            if (num > 0) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            } else {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            }

            if (fname != null && !"".equals(userEmal)) {
                smsUser.setFirmName(fname.toString());
                Integer num2 = sysUserService.queryfirmName(smsUser);
                if (num2 > 0) {
                    dzdPageResponse.setRetCode("000002");
                }
            }

        } catch (Exception e) {
            log.error(null, e);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    /**
     * @Description:根据ID查询代理信息
     * @author:oygy
     * @time:2017年1月3日 上午11:55:53
     */
    @RequestMapping(value = "/formEdit")
    @ResponseBody
    public DzdResponse edit(HttpServletRequest request) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            Object id = request.getParameter("id");
            if (id == null) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdPageResponse;
            }
            SmsUser smsUser = (SmsUser) smsUserService.queryById(Integer.parseInt(id.toString()));
            JspResponseBean jspResponseBean = new JspResponseBean();
            jspResponseBean.setData(smsUser);
            dzdPageResponse.setData(jspResponseBean);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            log.error(null, e);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }


    @RequestMapping(value = "/from/indexdl", method = RequestMethod.POST)
    public String indexdl(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("menuId");
        SysUser users = (SysUser) request.getSession().getAttribute("session_user");
        Object indexUid = request.getParameter("indexUid");
        if (menuId == null || indexUid == null || users == null) {
            return "redirect:/puser/listview.do?id=" + menuId;
        }
        List<SysRoleRel> sysRoleRels = queryRoleByUserId(users.getId());
        try {
            int state = -1;
            if (Constant.SuperAdmin.YES.key == users.getSuperAdmin()) {  //如果当前登录用户为超级管理员
                state = 1;
            }
            for (SysRoleRel sysRoleRel : sysRoleRels) {
                if (sysRoleRel.getRoleId() == 51) {  //如果当前登录用户为经理
                    state = 1;
                }
            }
            if (state == 1) {
                HttpSession session = request.getSession();
                SmsUser smsUser = (SmsUser) smsUserService.queryById(Integer.parseInt(indexUid.toString()));
                SysUser u = sysUserService.queryById(smsUser.getSysUserId());
                // 清空session
                //session.invalidate();

                // 设置User到Session
                SessionUtils.setUser(request, u);
                SysUser user = SessionUtils.getUser(request);
                List<SysMenu> rootMenus = null;
                // List<SysMenuBtn> childBtns = null;
                // 超级管理员
                if (user != null && Constant.SuperAdmin.YES.key == user.getSuperAdmin()) {
                    rootMenus = sysMenuService.queryRootSysMenuList();
                } else {
                    // 根据用户查询角色再查询菜单
                    DzdParameters dzdParameters = new DzdParameters();
                    dzdParameters.setUserId(user.getId());
                    rootMenus = sysMenuService.queryMenusByUserId(dzdParameters);
                    // 根据用户id查找菜单 子父同一层级
                    List<String> allUrls = sysMenuService.getActionUrls(dzdParameters);
                    // 将用户对应的菜单放入session中
                    SessionUtils.setAccessUrl(request, allUrls);// 设置可访问的URL
                }
                //response.put("userType", user.getUserType());// 用户类型(1-代理管理,2-代理商)
                session.setAttribute("user", u);
                session.setAttribute("menuList", rootMenus);
            }
        } catch (Exception e) {
            // TODO: handle exception
            return "redirect:/puser/listview.do?id=" + menuId;
        }
        return "redirect:/smsUser/index.do";
    }

    /**
     * @Description:根据用户ID查询所剩条数
     * @author:oygy
     * @time:2017年1月3日 上午11:55:53
     */
    @RequestMapping(value = "/from/querySurplusNum")
    @ResponseBody
    public DzdResponse querySymoney(HttpServletRequest request) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            Object id = request.getParameter("id");
            if (id == null) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdPageResponse;
            }

            Integer saList = smsUserService.querySurplusNum(Integer.parseInt(id.toString()));
            JspResponseBean jspResponseBean = new JspResponseBean();
            jspResponseBean.setData(saList);
            dzdPageResponse.setData(jspResponseBean);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            log.error(null, e);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }


    /**
     * 用户添加短信条数
     *
     * @param request
     * @param data
     * @return
     */
    @MethodDescription("用户添加短信条数")
    @RequestMapping(value = "/from/moneyMerge", method = RequestMethod.POST)
    public String moneyMerge(HttpServletRequest request, HttpServletResponse response, Model model) {
        Object menuId = request.getParameter("menuId");
        try {

            Object id = request.getParameter("czid");                          //获得用户ID
            Object czmoney = request.getParameter("czmoney");

            if (id == null || id.toString().isEmpty()) {
                return "redirect:/puser/listview.do?id=" + menuId;
            }
            if (czmoney == null || czmoney.toString().isEmpty()) {
                return "redirect:/puser/listview.do?id=" + menuId;
            }
            SmsUser smsUser = new SmsUser();
            Integer saList = smsUserService.querySurplusNum(Integer.parseInt(id.toString()));
            Integer czmoneyValue = Integer.parseInt(czmoney.toString());
            Date createTime = new Date();
            smsUser.setId(Integer.parseInt(id.toString()));
            smsUser.setSurplusNum(czmoneyValue);
            smsUser.setCreateTime(createTime);
            smsUserService.updateSmsUserBlankMoney(smsUser);

            log.info("-----------------》用户添加短信条数");

            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            SmsUserMoneyRunning smr = new SmsUserMoneyRunning();
            smr.setSmsUserId(Integer.parseInt(id.toString()));
            smr.setUid(user.getId());
            smr.setBeforeNum(saList);   //操作前条数
            smr.setType(0);
            smr.setAfterNum(saList + czmoneyValue);   //操作后条数
            smr.setOperateNum(czmoneyValue);                //操作条数
            smr.setCreateTime(createTime);          //操作时间
            smr.setComment("手动添加短信数量！");

            smsUserService.saveSmsUserMoneyRunning(smr);


            String keys = smsUserService.querySmsUserKey(smsUser.getId());
            instructSend(InstructState.USERTOPUP_SUCESS, keys, smsUser.getId());   //发送动作指令到redis
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return "redirect:/puser/listview.do?id=" + menuId;
    }


    @RequestMapping("/statistical")
    public String statistical(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        return "puser/puserStatistical";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/channelstataical")
    public String channelStataical(HttpServletRequest request, Model model) throws Exception {
        List<String> aisleNames = msmSendService.queryAisleNames();// 查询所有的通道名称
        model.addAttribute(Define.REQUESTPARAMETER.AISLENAMES, aisleNames);

        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);

        return "puser/channelStataical";
    }


    /**
     * @Description:查询代理统计信息列表
     * @author:oygy
     * @time:2016年12月31日 下午2:01:34
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/puserStatisticalList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse puserStatisticalList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");
            Object email = data.get("email");
            Object aisleName = data.get("aisleName");
            Object date_export = data.get("date_export");


            if (menuId == null) {
                return dzdPageResponse;
            }
            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }

            if (!StringUtil.isEmpty(parameters.getStartInput())) {
                sortMap.put("startInput", parameters.getStartInput());
                sortMap.put("endInput", parameters.getEndInput());
            }

            if (email != null && !StringUtil.isEmpty(email.toString())) {
                sortMap.put("email", email.toString());
            }

            if (aisleName != null && !StringUtil.isEmpty(aisleName.toString())) {
                sortMap.put("aisleName", aisleName.toString());
            }

            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            if (user.getId() != 1) {
                com.dzd.sms.service.data.SmsUser smsUserDate = SmsServerManager.I
                        .getUserBySysId(Long.valueOf(user.getId()));
                String uid = user.getId().toString();
                if (smsUserDate != null) {
                    uid = smsUserDate.getId().toString();// 用户id
                }
                if (!StringUtil.isEmpty(uid)) {
                    sortMap.put("uid", uid);
                }
            }

            //排序
            if (parameters.getSort() != null && parameters.getOrder() != null) {
                sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
            }
            dzdPageParam.setCondition(sortMap);
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
            }

            if (user != null && user.getId() != 1) {
                sortMap.put("smsUserVal", "select id from sys_user where id=" + user.getId() + " or superiorId = " + user.getId() + "");
            }

            // 查询代理统计信息列表
            Page<SmsUser> dataList = smsUserService.querySmsUserStatisticalList(dzdPageParam);

            if (CollectionUtils.isEmpty(dataList)) {
                return dzdPageResponse;
            }

            // 查询列表头信息
            SmsUser smsUser = smsUserService.querySmsUserStatisticalZong(dzdPageParam);
            // 设置列表头信息，使用同一个对象返回

            dzdPageResponse.setData(smsUser);

            Map<String, Integer> totalNumMap = new HashMap<String, Integer>();
            for (SmsUser smsUserInfo : dataList) {
                smsUserInfo.setUnknownFailureNum(smsUserInfo.getUnknownNumMs()
                        + smsUserInfo.getUnknownNumTs() + smsUserInfo.getUnknownNumUs());

                smsUserInfo.setUnknownSucceedNum(0);

                smsUserInfo.setSucceedNum(smsUserInfo.getSucceedNumMs()
                        + smsUserInfo.getSucceedNumTs() + smsUserInfo.getSucceedNumUs());

                smsUserInfo.setFailureNum(smsUserInfo.getFailureNumMs()
                        + smsUserInfo.getFailureNumTs() + smsUserInfo.getFailureNumUs());

                setTotalNumMap(totalNumMap, smsUserInfo);
            }

            setTotalSmsUser(dataList, totalNumMap);

            ComparatorSmsUser.comparatorListBySucceedNum(dataList);
            ComparatorSmsUser.comparatorListByAuditTime(dataList);
				
/*			if ( dataList.size() > 0 )
			{
				dataList.get(0).setSumSendNum(smsUser.getSumSendNum());
				dataList.get(0).setSumBillingNum(smsUser.getSumBillingNum());
				dataList.get(0).setSumActualNum(smsUser.getSumActualNum());

				dataList.get(0).setSumFailureNumUs(smsUser.getSumFailureNumUs());
				dataList.get(0).setSumSucceedNumUs(smsUser.getSumSucceedNumUs());
				dataList.get(0).setSumUnknownNumUs(smsUser.getSumUnknownNumUs());

				dataList.get(0).setSumFailureNumMs(smsUser.getSumFailureNumMs());
				dataList.get(0).setSumSucceedNumMs(smsUser.getSumSucceedNumMs());
				dataList.get(0).setSumUnknownNumMs(smsUser.getSumUnknownNumMs());

				dataList.get(0).setSumFailureNumTs(smsUser.getSumFailureNumTs());
				dataList.get(0).setSumSucceedNumTs(smsUser.getSumSucceedNumTs());
				dataList.get(0).setSumUnknownNumTs(smsUser.getSumUnknownNumTs());
			}*/


            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsUser instruct : dataList.getResult()) {
                    instruct.setSysMenuBtns(sysMenuBtns);
                }
                dzdPageResponse.setRows(dataList.getResult());
                dzdPageResponse.setTotal(dataList.getTotal());

                if (date_export.equals("date_export")) {
                    this.setDataList(dataList);
                }
            }
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    /**
     * @param request
     * @param response
     * @throws
     * @Title: orderExport
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @author: hz-liang
     * @return: void
     */
    @MethodDescription("通道统计导出")
    @RequestMapping(value = "/orderExport")
    @ResponseBody
    public void orderExport(HttpServletRequest request, HttpServletResponse response) {
        if (CollectionUtils.isEmpty(this.getDataList())) {
            return;
        }
        SmsOrderExportService orderExportService = new SmsOrderExportService();
        orderExportService.channlOrderExport(request, response, this.getDataList());
    }


    private void setTotalSmsUser(Page<SmsUser> dataList, Map<String, Integer> totalNumMap) {
        SmsUser totalSmsUser = new SmsUser();
        totalSmsUser.setSendNum(totalNumMap.get(Define.STATICAL.SENDNUM).intValue());
        totalSmsUser.setSucceedNum(totalNumMap.get(Define.STATICAL.SUCCEEDNUM).intValue());
        totalSmsUser.setFailureNum(totalNumMap.get(Define.STATICAL.FAILURENUM).intValue());
        totalSmsUser.setSucceedNumUs(totalNumMap.get(Define.STATICAL.SUMSUCCEEDNUMUS).intValue());
        totalSmsUser.setSucceedNumMs(totalNumMap.get(Define.STATICAL.SUMSUCCEEDNUMMS).intValue());
        totalSmsUser.setSucceedNumTs(totalNumMap.get(Define.STATICAL.SUMSUCCEEDNUMTS).intValue());
        totalSmsUser.setUnknownSucceedNum(totalNumMap.get(Define.STATICAL.UNKNOWNSUCCEEDNUM).intValue());
        totalSmsUser.setFailureNumUs(totalNumMap.get(Define.STATICAL.SUMFAILURENUMUS).intValue());
        totalSmsUser.setFailureNumMs(totalNumMap.get(Define.STATICAL.SUMFAILURENUMMS).intValue());
        totalSmsUser.setFailureNumTs(totalNumMap.get(Define.STATICAL.SUMFAILURENUMTS).intValue());
        totalSmsUser.setUnknownFailureNum(totalNumMap.get(Define.STATICAL.UNKNOWNFAILURENUM));
        totalSmsUser.setAuditTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        dataList.getResult().add(0, totalSmsUser);
    }


    private void setTotalNumMap(Map<String, Integer> totalNumMap, SmsUser smsUserInfo) {
        totalNumMap.put(Define.STATICAL.SENDNUM,
                setDefultValue(totalNumMap.get(Define.STATICAL.SENDNUM))
                        + smsUserInfo.getSendNum());
        totalNumMap.put(Define.STATICAL.SUCCEEDNUM,
                setDefultValue(totalNumMap.get(Define.STATICAL.SUCCEEDNUM))
                        + smsUserInfo.getSucceedNum());
        totalNumMap.put(Define.STATICAL.FAILURENUM,
                setDefultValue(totalNumMap.get(Define.STATICAL.FAILURENUM))
                        + smsUserInfo.getFailureNum());
        totalNumMap.put(Define.STATICAL.SUMSUCCEEDNUMUS,
                setDefultValue(totalNumMap.get(Define.STATICAL.SUMSUCCEEDNUMUS))
                        + smsUserInfo.getSucceedNumUs());
        totalNumMap.put(Define.STATICAL.SUMSUCCEEDNUMMS,
                setDefultValue(totalNumMap.get(Define.STATICAL.SUMSUCCEEDNUMMS))
                        + smsUserInfo.getSucceedNumMs());
        totalNumMap.put(Define.STATICAL.SUMSUCCEEDNUMTS,
                setDefultValue(totalNumMap.get(Define.STATICAL.SUMSUCCEEDNUMTS))
                        + smsUserInfo.getSucceedNumTs());
        totalNumMap.put(Define.STATICAL.UNKNOWNSUCCEEDNUM,
                setDefultValue(totalNumMap.get(Define.STATICAL.UNKNOWNSUCCEEDNUM))
                        + smsUserInfo.getUnknownSucceedNum());
        totalNumMap.put(Define.STATICAL.SUMFAILURENUMUS,
                setDefultValue(totalNumMap.get(Define.STATICAL.SUMFAILURENUMUS))
                        + smsUserInfo.getFailureNumUs());
        totalNumMap.put(Define.STATICAL.SUMFAILURENUMMS,
                setDefultValue(totalNumMap.get(Define.STATICAL.SUMFAILURENUMMS))
                        + smsUserInfo.getFailureNumMs());
        totalNumMap.put(Define.STATICAL.SUMFAILURENUMTS,
                setDefultValue(totalNumMap.get(Define.STATICAL.SUMFAILURENUMTS))
                        + smsUserInfo.getFailureNumTs());
        totalNumMap.put(Define.STATICAL.UNKNOWNFAILURENUM,
                setDefultValue(totalNumMap.get(Define.STATICAL.UNKNOWNFAILURENUM))
                        + smsUserInfo.getUnknownFailureNum());
    }

    private int setDefultValue(Object obj) {
        obj = obj == null ? 0 : obj;
        return Integer.parseInt(obj.toString());
    }


    @RequestMapping("/puserBill")
    public String puserBill(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        Object type = request.getParameter("type");
        if (type != null) {
            model.addAttribute("type", type);
        }
        model.addAttribute("menuId", menuId);

        return "puser/puserBill";
    }


    /**
     * @Description:查询代理账单流水信息列表
     * @author:oygy
     * @time:2016年12月31日 下午2:01:34
     */
    @RequestMapping(value = "/puserBillList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse puserBillList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");
            Object email = data.get("email");
            Object type = data.get("type");


            if (menuId == null) {
                return dzdPageResponse;
            }
            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }

            if (!StringUtil.isEmpty(parameters.getStartInput())) {
                sortMap.put("startInput", parameters.getStartInput());
                sortMap.put("endInput", parameters.getEndInput());
            }

            if (email != null && !StringUtil.isEmpty(email.toString())) {
                sortMap.put("email", email.toString());
            }
            if (type != null && !StringUtil.isEmpty(type.toString())) {
                sortMap.put("type", Integer.parseInt(type.toString()));
            }

            //排序
            if (parameters.getSort() != null && parameters.getOrder() != null) {
                sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
            } else {
                sortMap.put("sortVal", "order by createTime desc");
            }

            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            if (user != null && user.getId() != 1) {
                sortMap.put("smsUserVal", "select id from sys_user where id=" + user.getId() + " or superiorId = " + user.getId() + "");
            }

            dzdPageParam.setCondition(sortMap);
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
            }

            Page<SmsUserMoneyRunning> dataList = smsUserService.querySmsUserPuserBillList(dzdPageParam);
            SmsUserMoneyRunning smsUser = smsUserService.querySmsUserPuserBillZong(dzdPageParam);
            if (dataList.size() > 0) {
                dataList.get(0).setTopUpNum(smsUser.getTopUpNum());
                dataList.get(0).setConsumeNum(-smsUser.getConsumeNum());
                dataList.get(0).setRefundNum(smsUser.getRefundNum());
            }


            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsUserMoneyRunning instruct : dataList.getResult()) {
                    instruct.setSysMenuBtns(sysMenuBtns);
                }
                dzdPageResponse.setRows(dataList.getResult());
                dzdPageResponse.setTotal(dataList.getTotal());
            }
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }


    /**
     * @Description:根据通道组ID查询所有通道组
     * @author:oygy
     * @time:2017年1月3日 上午11:56:29
     */
    @RequestMapping(value = "/querySmsGroup", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DzdResponse querySmsGroup(HttpServletRequest request, @RequestBody Map<String, Object> data) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            Object gtype = request.getParameter("gid");
            if (gtype == null || "".equals(gtype)) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdPageResponse;
            }
            List<SmsAisleGroup> list = smsUserService.querySmsGroupById(Integer.parseInt(gtype.toString()));
            dzdPageResponse.setData(list);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            log.error(null, e);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    @RequestMapping("/pushview")
    public String pushview(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        List<SmsAisleGroupType> list = channelService.querySmsAisleGroupType();
        model.addAttribute("typeList", list);
        model.addAttribute("menuId", menuId);
        return "puser/pushManage";
    }

    /**
     * @Description:查询消息推列表
     * @author:oygy
     * @time:2016年12月31日 下午2:01:34
     */
    @RequestMapping(value = "/pushManageList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse pushManageList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");


            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            List<SysRoleRel> sysRoleRels = queryRoleByUserId(user.getId());
            List<Integer> rolist = minRoid(sysRoleRels);

            if (menuId == null || user == null || sysRoleRels == null) {
                return dzdPageResponse;
            }
            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }

            if (!StringUtil.isEmpty(parameters.getStartInput())) {
                sortMap.put("startInput", parameters.getStartInput());
                sortMap.put("endInput", parameters.getEndInput());
            }

            if (user != null && user.getId() != 1) {
                sortMap.put("smsUserVal", "select id from sys_user where id=" + user.getId() + " or superiorId = " + user.getId() + "");
            }


            //排序
            if (parameters.getSort() != null && parameters.getOrder() != null) {
                sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
            } else {
                sortMap.put("sortVal", "order by created desc");
            }
            dzdPageParam.setCondition(sortMap);
            List<SysMenuBtn> sysMenuBtns = null;

            if (menuId != null) {
/*	            	Map<String, Object> param = new HashMap<String, Object>();
	        		param.put("userId", user.getId());
	        		param.put("rolist", rolist);
	        		param.put("menuId", Integer.parseInt(menuId.toString()));
	        		sysMenuBtns = sysMenuBtnService.queryByMenuid2(param); */
                sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
            }

            Page<SmsUserMessage> dataList = smsUserService.pushManageList(dzdPageParam);

            dzdPageResponse.setData(sysMenuBtns);
            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsUserMessage instruct : dataList.getResult()) {
                    instruct.setSysMenuBtns(sysMenuBtns);
                }
                dzdPageResponse.setRows(dataList.getResult());
                dzdPageResponse.setTotal(dataList.getTotal());
            }
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    /**
     * @Description:新增消息推送
     * @author:oygy
     * @time:2017年1月3日 上午11:56:29
     */
    @MethodDescription("新增消息推送")
    @RequestMapping(value = "/addPushManage", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DzdResponse addPushManage(HttpServletRequest request, @RequestBody Map<String, Object> data) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            Object id = request.getParameter("menuId");
            Object title = data.get("title");       //推送消息标题
            Object content = data.get("content");   //推送内容
            Object types = data.get("types");       //推送类型
            Object pushIndex = data.get("pushIndex"); //是否推送首页、
            Object smsUserType = data.get("smsUserType"); //推送范围
            Object smsUserEmail = data.get("smsUserEmail"); //推送有客户账号

            SmsUserMessage smsUserMessage = new SmsUserMessage();
            if (id == null || "".equals(id)) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdPageResponse;
            }
            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            smsUserMessage.setSysUserId(user.getId());
            if (title != null && !"".equals(title)) {
                smsUserMessage.setTitle(title.toString());
            }
            if (content != null && !"".equals(content)) {
                smsUserMessage.setContent(content.toString());
            }
            if (types != null && !"".equals(types)) {
                smsUserMessage.setType(Integer.parseInt(types.toString()));
            }
            if (pushIndex != null && !"".equals(pushIndex)) {
                smsUserMessage.setPushIndex(1);
            } else {
                smsUserMessage.setPushIndex(0);
            }
            if (smsUserType != null && !"".equals(smsUserType)) {
                smsUserMessage.setSmsUserTypeId(smsUserType.toString());
            }
            if (smsUserEmail != null && !"".equals(smsUserEmail)) {
                smsUserMessage.setSmsUserEmail(smsUserEmail.toString());
            }

            smsUserService.addPushManage(smsUserMessage);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            log.error(null, e);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/rechargerecord", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse getRechargeRecordList(HttpServletRequest request,
                                             HttpServletResponse response, @RequestBody Map<String, Object> data) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class,
                    data);
            if (parameters == null) {
                return dzdResponse;
            }
            Object filterByTdSelect = data.get("tdState");
            Object filterByEmail = data.get("email");

            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            Integer userId = user.getId();

            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }

            if (!StringUtil.isEmpty(parameters.getStartInput())) {
                sortMap.put("startInput", parameters.getStartInput());
                sortMap.put("endInput", parameters.getEndInput());
            }

            if (filterByEmail != null && !StringUtil.isEmpty(filterByEmail.toString())) {
                sortMap.put("email", filterByEmail.toString());
            }

            if (filterByTdSelect != null && !StringUtil.isEmpty(filterByTdSelect.toString())) {
                sortMap.put("teamId", Integer.parseInt(filterByTdSelect.toString()));
            }

            if (userId != null) {
                sortMap.put("userId", userId);
            }

            if ("admin@qq.com".equals(user.getEmail())) {
                sortMap.put("admin", 1);
            }

            // 排序
            sortMap.put("sortVal", "order by createTime desc");
            dzdPageParam.setCondition(sortMap);

            Page<SmsRechargeUser> rechargeRecordList = smsUserService
                    .rechargeRecordList(dzdPageParam);

            if (CollectionUtils.isEmpty(rechargeRecordList)) {
                return dzdResponse;
            }

//			Map<String,String> teamMap = new HashMap<String, String>();
            Float sumRechargeMoney = 0F;
            int sumRechargeNum = 0;
            for (SmsRechargeUser smsRechargeUser : rechargeRecordList) {
                sumRechargeMoney += smsRechargeUser.getRechargeMoney();
                sumRechargeNum += smsRechargeUser.getRechargeNum();

//				if ( smsRechargeUser.getTeamId() != null )
//				{
//					teamMap.put(smsRechargeUser.getTeamId().toString(), smsRechargeUser.getTeamName());
//				}
            }
            rechargeRecordList.get(0).setSumRechargeMoney(sumRechargeMoney);
            rechargeRecordList.get(0).setSumRechargeNum(sumRechargeNum);
//			rechargeRecordList.get(0).setTeamMap(teamMap);

            if (!CollectionUtils.isEmpty(rechargeRecordList)) {
                dzdResponse.setRows(rechargeRecordList.getResult());
                dzdResponse.setTotal(rechargeRecordList.getTotal());
            }
        } catch (Exception e) {
            logger.error(null, e);
            e.printStackTrace();
        }

        return dzdResponse;
    }

}
