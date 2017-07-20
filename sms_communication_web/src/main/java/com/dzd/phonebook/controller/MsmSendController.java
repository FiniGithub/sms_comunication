package com.dzd.phonebook.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dzd.base.page.Pager;
import com.dzd.base.util.HtmlUtil;
import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.business.MsmSendBusiness;
import com.dzd.phonebook.controller.base.ExcelUtil;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.SmsUserMessage;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.MsmSendService;
import com.dzd.phonebook.service.SmsOrderExportService;
import com.dzd.phonebook.service.SysMenuBtnService;
import com.dzd.phonebook.service.SysRoleRelService;
import com.dzd.phonebook.service.SysUserService;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.JspResponseBean;
import com.dzd.phonebook.util.SmsAisle;
import com.dzd.phonebook.util.SmsSendLog;
import com.dzd.phonebook.util.SmsSendPackageLog;
import com.dzd.phonebook.util.SmsSendTask;
import com.dzd.phonebook.util.TempParameter;
import com.dzd.phonebook.util.WebRequestParameters;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsTaskData;
import com.dzd.sms.service.data.SmsUser;
import com.github.pagehelper.Page;

/**
 * 发送消息controller
 * 
 * @author oygy
 * @date 2016-6-24 16:11:00
 *
 */
@Controller
@RequestMapping("/msmSend")
public class MsmSendController  extends WebBaseController{
	public static final Logger log = LoggerFactory.getLogger(MsmSendController.class);
	
    @Autowired
    private SysMenuBtnService sysMenuBtnService;
    
    @Autowired
    private SysRoleRelService sysRoleRelService;
    
	
	@Autowired
	private SysUserService<SysUser> sysUserService;

	@Autowired
	private MsmSendService msmSendService;
	
	private MsmSendBusiness msmSendBusiness = new MsmSendBusiness();
	
	private List<SmsSendLog> dataList;
	
	public List<SmsSendLog> getDataList()
	{
		return dataList;
	}

	public void setDataList(List<SmsSendLog> dataList)
	{
		this.dataList = dataList;
	}


	@RequestMapping("/listview")
    public String list(HttpServletRequest request, Model model) throws Exception {
       Object menuId = request.getParameter("id");
       model.addAttribute("menuId", menuId);
       return "smsManage/msmSend";
    }
	
	 
	 /**
	  * 
	  * @Description:查询发送消息列表
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
            Object content = data.get("content");
            Object state = data.get("state");
            Object ids = data.get("ids");
            Object smsUser = data.get("smsUser");
            Object sendType = data.get("sendType");
            
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
            
            if (state!=null && !StringUtil.isEmpty(state.toString())) {
                sortMap.put("state", Integer.parseInt(state.toString()));
            }
            
            if (content!=null && !StringUtil.isEmpty(content.toString())) {
                sortMap.put("content", content.toString());
            }
            
            if (ids!=null && !StringUtil.isEmpty(ids.toString())) {
                sortMap.put("ids", Integer.parseInt(ids.toString()));
            }
            
            if (smsUser!=null && !StringUtil.isEmpty(smsUser.toString())) {
                sortMap.put("smsUser", smsUser.toString());
            }
            
            if (sendType!=null && !StringUtil.isEmpty(sendType.toString())) {
                sortMap.put("sendType", Integer.parseInt(sendType.toString()));
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
            SysUser user = (SysUser)request.getSession().getAttribute("session_user");
            if(user !=null && user.getId()!=1){
            	sortMap.put("smsUserVal", "select id from sys_user where id="+user.getId()+" or superiorId = "+user.getId()+""); 
            }
            Page<SmsSendTask> dataList = msmSendService.queryMsmSendList(dzdPageParam);
            
            SmsSendTask dataNumCount = msmSendService.queryMsmSendCount(dzdPageParam);
            
            dzdPageResponse.setData(dataNumCount);
           /* if(dataList.getResult().size()>0){
            	dataList.getResult().get(0).setBillingNumCount(dataNumCount.getBillingNumCount());
            	dataList.getResult().get(0).setSendNumCount(dataNumCount.getSendNumCount());
            	dataList.getResult().get(0).setActualNumCount(dataNumCount.getActualNumCount());
            }*/
            
            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsSendTask instruct : dataList.getResult()) {
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
	 * export导出文件 
	 */
	 @MethodDescription("导出任务发送号码")
	 @RequestMapping(value="/export/csv",method={RequestMethod.GET}) 
     @ResponseBody
	 public void exportCsv(HttpServletRequest request,HttpServletResponse response){
		 
		 List<SmsSendLog> projects=new ArrayList<SmsSendLog>();
		 Object id= request.getParameter("id");
		 if(id==null || "".equals(id)){
     		return;
     	}
		 try {
			projects=msmSendService.queryMsmSendPhoneByid(Integer.parseInt(id.toString()));
		} catch (NumberFormatException e2) {
			e2.printStackTrace();
			return;
		}
	 	 String fileName =id+"批次发送号码";
        //填充projects数据
       // List<String> projects=new ArrayList<String>();
        List<Map<String,Object>> list=createExcelRecordObj(projects);
        String columnNames[]={"电话号码","通道","状态"};//列名
        String keys[]    =     {"phone","aisleName","state"};//map中的key
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ExcelUtil.createWorkBook(list,keys,columnNames).write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try {
			response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        } catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
        	 ServletOutputStream out = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            try {
				throw e;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        } finally {
            if (bis != null)
				try {
					bis.close();
					 if (bos != null)
			         bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           
        }
		 

	 }
	 
	   private List<Map<String, Object>> createExcelRecordObj(List<SmsSendLog> projects) {
	        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("sheetName", "sheet1");
	        listmap.add(map);
	        for (SmsSendLog smsSendLog:projects) {
	        	 Map<String, Object> maps = new HashMap<String, Object>();
	        	 maps.put("phone", smsSendLog.getSendPhone());
	        	 maps.put("aisleName", smsSendLog.getAisleName());
	        	 String stateName = "";
    		 if(smsSendLog.getState()==null){
    			 	stateName="";
            	}else if (smsSendLog.getState()==-1) {
            		stateName= "待发送";
                }else if(smsSendLog.getState()==0){
                	stateName= "已发送";
                }else if(smsSendLog.getState()==2 || smsSendLog.getState()==26){
                	stateName= "发送失败";
                }else if(smsSendLog.getState()==3){
                	stateName= "未知";
                }else if(smsSendLog.getState()==21){
                	stateName= "网络错误";
                }else if(smsSendLog.getState()==22){
                	stateName= "空号错误";
                }else if(smsSendLog.getState()==23){
                	stateName= "发送上限";
                }else if(smsSendLog.getState()==24){
                	stateName= "黑名单错误";
                }else if(smsSendLog.getState()==25){
                	stateName= "审核失败";
                }else if(smsSendLog.getState()==27){
                	stateName= "通道拒绝接收";
                }else if(smsSendLog.getState()==31){
                	stateName= "通道返回失败";
                }else if(smsSendLog.getState()==99){
                	stateName= "状态报告为未知";
                }else if(smsSendLog.getState()==100){
                	stateName= "发送成功";
                }
	        	 maps.put("state", stateName);
	            listmap.add(maps);
	        }
	        return listmap;
	    }
	 
	 
	 
	   private List<Map<String, Object>> createExcelRecord(List<String> projects) {
	        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("sheetName", "sheet1");
	        listmap.add(map);
	        for (int j = 0; j < projects.size(); j++) {
	        	 Map<String, Object> maps = new HashMap<String, Object>();
	        	 maps.put("id", projects.get(j));
	            listmap.add(maps);
	        }
	        return listmap;
	    }
	 
	 
	 
	 
	 
	 
	 /**
	  * @Description:点击详情
	  * @author:liuyc
	  * @time:2017年1月9日 下午6:04:09
	  */
	 @RequestMapping(value="/smsSendDetails",method={RequestMethod.POST})
	 public String smsSendDetails(HttpServletRequest request,HttpServletResponse response){
		 
		 Object ids= request.getParameter("ids");
		 Object menuId= request.getParameter("menuId");
	        try{
	        	if(ids==null || "".equals(ids)){
	        		return "redirect:/msmSend/listview.do?id="+ menuId;
	        	}
	        	
	        }catch(Exception e){
	        	log.error(e.getMessage());
	        	System.out.println(e.getMessage());
	        	return "redirect:/msmSend/listview.do?id="+ menuId;
	        }
	        //return "redirect:/msmSend/listview.do?id="+ menuId;
			return "redirect:/msmSend/detailsListview.do?id="+ menuId+"&ids="+ids;
	 	}
	 
	  
		@RequestMapping("/detailsListview")
	    public String detailsListview(HttpServletRequest request, Model model) throws Exception {
	       Object menuId = request.getParameter("id");
	       Object ids = request.getParameter("ids");
	       model.addAttribute("menuId", menuId);
	       model.addAttribute("ids", ids);
	       return "smsManage/smsSendDetails";
	    }
	 
	 

	 
	/**
	 * 
	 * @Description:查询发送消息详情列表
	 * @author:oygy
	 * @time:2016年12月31日 下午2:01:34
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/puserDetailsList")
	@ResponseBody
	public void list(HttpServletRequest request, HttpServletResponse response,
	        @RequestBody Map<String, Object> data) throws Exception
	{
		try
		{
			SmsSendLog smsSendLog = new SmsSendLog();
			JSONObject jsonObject = new JSONObject();

			WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class,
			        data);
			if ( parameters == null )
			{
				HtmlUtil.writerJson(response, jsonObject);
			}

			// URL入参数据构造
			TempParameter tempParameter = msmSendBusiness.preTemParameter(request, data);

			if ( tempParameter.getMenuId() == null )
			{
				HtmlUtil.writerJson(response, jsonObject);
			}

			// 构建查询入参数据
			msmSendBusiness.preParameter(request, tempParameter, smsSendLog, parameters);
			// 构建页码入参数据
			msmSendBusiness.prePageParameter(tempParameter, smsSendLog);

			// 相关按钮数据
			List<SysMenuBtn> sysMenuBtns = null;
			if ( tempParameter.getMenuId() != null )
			{
				sysMenuBtns = sysMenuBtnService
				        .queryByMenuid(Integer.parseInt(tempParameter.getMenuId().toString()));
			}

			// 查询发送日志信息
			List<SmsSendLog> dataList = msmSendService.queryByList(smsSendLog);
			
			jsonObject.put(Define.RESULTSTATE.BTN, sysMenuBtns);
			
			if ( CollectionUtils.isEmpty(dataList) )
			{
				HtmlUtil.writerJson(response, jsonObject);
			}
			
			// 构造出参数据
			msmSendBusiness.constructResult(msmSendService,response, tempParameter.getLogTime(), smsSendLog, jsonObject,
			        sysMenuBtns, dataList);

		} catch (Exception e)
		{
			log.error(null, e);
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 * @Title: orderExport
	 * @Description: 导出数据
	 * @author:    hz-liang
	 * @param request
	 * @param response
	 * @param data
	 * @throws Exception  
	 * @return: void   
	 * @throws
	 */
	@MethodDescription("导出数据")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryOrderExport")
	@ResponseBody
	public DzdResponse queryOrderExport(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		DzdResponse dzdPageResponse = new DzdResponse();
		try
		{
			SmsSendLog smsSendLog = preParameterForSmsSendLog(request);

			// 查询发送日志信息
			List<SmsSendLog> dataList = msmSendService.queryByList(smsSendLog);

			if ( CollectionUtils.isEmpty(dataList) )
			{
				dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
				return dzdPageResponse;
			}
			
			this.setDataList(dataList);

			dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);

		} catch (Exception e)
		{
			log.error(null, e);
			e.printStackTrace();
		}
		return dzdPageResponse;
	}


	private SmsSendLog preParameterForSmsSendLog(HttpServletRequest request)
	{
		SmsSendLog smsSendLog = new SmsSendLog();

		smsSendLog.setNeedPage("true");
		if ( !StringUtils.isEmpty(request.getParameter(Define.REQUESTPARAMETER.STARTINPUT)) )
		{
			smsSendLog.setStartInput(request.getParameter(Define.REQUESTPARAMETER.STARTINPUT));
		}
		if ( !StringUtils.isEmpty(request.getParameter(Define.REQUESTPARAMETER.ENDINPUT)) )
		{
			smsSendLog.setEndInput(request.getParameter(Define.REQUESTPARAMETER.ENDINPUT));
		}
		if ( "2".equals(request.getParameter(Define.STATICAL.STATE)) )
		{ // 如果发送状态为发送失败则区间查询
			smsSendLog.setStateBs(1);
		} else if ( !StringUtils.isEmpty(request.getParameter(Define.STATICAL.STATE)) )
		{
			smsSendLog.setState(Integer.parseInt(request.getParameter(Define.STATICAL.STATE)));
		}
		if ( !StringUtils.isEmpty(request.getParameter(Define.STATICAL.CONTENT)) )
		{
			smsSendLog.setContent(request.getParameter(Define.STATICAL.CONTENT));
		}
		if ( !StringUtils.isEmpty(request.getParameter(Define.REQUESTPARAMETER.SMSUSER)) )
		{
			smsSendLog.setSmsUserName(request.getParameter(Define.REQUESTPARAMETER.SMSUSER));
		}
		if ( !StringUtils.isEmpty(request.getParameter(Define.REQUESTPARAMETER.PHONE)) )
		{
			smsSendLog.setReceivePhone(request.getParameter(Define.REQUESTPARAMETER.PHONE));
		}
		if ( Define.REQUESTPARAMETER.TODAY.equals(request.getParameter(Define.REQUESTPARAMETER.LOGTIME)) )
		{
			smsSendLog.setLogTime(request.getParameter(Define.REQUESTPARAMETER.LOGTIME));
		}
		return smsSendLog;
	}

	@MethodDescription("导出数据")
	@RequestMapping(value = "/orderExport")
	@ResponseBody
	public void orderExport(HttpServletRequest request, HttpServletResponse response)
	{
		List<SmsSendLog> dataList = this.getDataList();
		String sheetName = null;
		if ( Define.REQUESTPARAMETER.TODAY
		        .equals(request.getParameter(Define.REQUESTPARAMETER.LOGTIME)) )
		{
			sheetName = "今日日志";
		} else if ( Define.REQUESTPARAMETER.HISTORY
		        .equals(request.getParameter(Define.REQUESTPARAMETER.LOGTIME)) )
		{
			sheetName = "历史日志";
		}

		SmsOrderExportService orderExportService = new SmsOrderExportService();
		orderExportService.logrOrderExport(request, response, dataList, sheetName);
	}

		 /**
		  * 
		  * @Description:查询子任务消息列表
		  * @author:oygy
		  * @time:2016年12月31日 下午2:01:34
		  */
		 @RequestMapping(value = "/sendPackageList", method = RequestMethod.POST)
	     @ResponseBody
	     public DzdResponse sendPackageList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> data) throws Exception {
	        DzdResponse dzdPageResponse = new DzdResponse();
	        try {
	            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
	            if (parameters == null) {
	                return dzdPageResponse;
	            }
	            Object menuId = request.getParameter("menuId");
	            Object skid = request.getParameter("skid");
	            Object content = data.get("smsAisleName");
	            //Object content = request.getParameter("smsAisleName");
	            
	            if (menuId == null) {
	                return dzdPageResponse;
	            }
	            DzdPageParam dzdPageParam = new DzdPageParam();
	            Map<String, Object> sortMap = new HashMap<String, Object>();
	            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
	                dzdPageParam.setStart(parameters.getPagenum());
	                dzdPageParam.setLimit(parameters.getPagesize());
	            }
	            
	            if (content!=null && !StringUtil.isEmpty(content.toString())) {
	                sortMap.put("content", content.toString());
	            }
	            
	            if (skid!=null && !StringUtil.isEmpty(skid.toString())) {
	                sortMap.put("skid", Integer.parseInt(skid.toString()));
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
	            Page<SmsSendPackageLog> dataList = msmSendService.queryMsmSendPackageList(dzdPageParam);
	            
	            if (!CollectionUtils.isEmpty(dataList)) {
	                for (SmsSendPackageLog instruct : dataList.getResult()) {
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
		     * @Description:查询当前登录用户下有多少待审核任务
		     * @author:oygy
		     * @time:2017年1月3日 上午11:55:53
		     */
		    @RequestMapping(value = "/from/querySmsToAudit")
		    @ResponseBody
		    public DzdResponse querySmsToAudit(HttpServletRequest request) {
		        DzdResponse dzdPageResponse = new DzdResponse();
		        SysUser user = (SysUser)request.getSession().getAttribute("session_user");
		        if(user==null){
		        	dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
		        	 return dzdPageResponse;
		        }
		        try {
		        	  int nums = 0;
		    	       List<SysRoleRel> sysRoleRels = queryRoleByUserId(user.getId());
		    	       int num =0;
		    		   for (SysRoleRel sysRoleRel : sysRoleRels) {
		    	    	   if(sysRoleRel.getRoleId() == 3){  //如果当前登录用户为管理员
		    	    		   nums =1;
		    	    		   break;
		    	    	   }
		    	       }
		    		 Map<String, Object> sortMap = new HashMap<String, Object>();
		    		 Integer toAuditSize = 0;
		    		 if(user.getId()==1 || nums==1){   //判断当前登录是否为管理员
		    			  toAuditSize = msmSendService.querySmsToAudit(sortMap);
		    		 }else{
		    			 sortMap.put("smsUserVal", "select id from sys_user where id="+user.getId()+" or superiorId = "+user.getId()+""); 
		    			  toAuditSize = msmSendService.querySmsToAudit(sortMap);
		    		 }
		            JspResponseBean jspResponseBean = new JspResponseBean(); 
		            jspResponseBean.setData(toAuditSize);
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
			  * 
			  * @Description:根据用户ID查询拥有角色
			  * @author:oygy
			  * @time:2016年12月31日 上午11:12:53
			  */
			 private List<SysRoleRel> queryRoleByUserId(Integer uid){
			    DzdPageParam dzdPageParam = new DzdPageParam();
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("sysUserId", uid);
				dzdPageParam.setCondition(condition);
				List<SysRoleRel> sysRoleRels = sysRoleRelService.queryRoleByUserId(dzdPageParam);
				return sysRoleRels;
			 }
			 
			 
			 
			 
		/**
	     * @Description:根据通道类型ID查询通道
	     * @author:oygy
	     * @time:2017年1月3日 上午11:56:29
	     */
		@RequestMapping(value = "/querySmsAisle", method = RequestMethod.POST, produces = "application/json")
		@ResponseBody
		public DzdResponse addPushManage(HttpServletRequest request, @RequestBody Map<String, Object> data) {
			DzdResponse dzdPageResponse = new DzdResponse();
			try {
				Object tid = request.getParameter("tid");
				
				if (tid == null || "".equals(tid)) {
					dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
					return dzdPageResponse;
				}
				
				List<SmsAisle> list =msmSendService.querySmsAisleGroup(Integer.parseInt(tid.toString()));
				
				dzdPageResponse.setData(list);
				dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
			} catch (Exception e) {
				log.error(null, e);
				dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
				e.printStackTrace();
			}
			return dzdPageResponse;
		}		 
			 
		
		@RequestMapping("/smsSendResend")
		@ResponseBody
		public DzdResponse fileUpload(@RequestParam final MultipartFile[] uploadFile, final HttpServletRequest request,
				HttpServletResponse response) {
	    	DzdResponse dzdResponse = new DzdResponse();
			try{  
		        //文件类型:btnFile[0].getContentType()  
		        //文件名称:btnFile[0].getName()  
				
				///Object names  = data.get("name");
				
		        if(uploadFile[0].getSize()>Integer.MAX_VALUE){//文件长度  
		        	dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
					return dzdResponse;
		        }  
		        File f = null;
		        f=File.createTempFile("tmp", null);
		        uploadFile[0].transferTo(f);
		        f.deleteOnExit();
				
		        List<String> phoneList = new ArrayList<String>();  							//记录上传号码
		        InputStreamReader reader = new InputStreamReader(new FileInputStream(f)); 	// 建立一个输入流对象reader
				BufferedReader br = new BufferedReader(reader); 							// 建立一个对象，它把文件内容转成计算机能读懂的语言
				String line = "";
				line = br.readLine();
				while (line != null) {
					phoneList.add(line.trim());
					line = br.readLine(); // 一次读入一行数据
				}
				if(phoneList.size()>0){
					String phoneString = listToString(phoneList,',');
					request.getSession().removeAttribute("phoneString");
					request.getSession().setAttribute("phoneString", phoneString);
					dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
				}else{
					dzdResponse.setRetCode("000002");
				}
		    }catch (Exception e) {  
		    	dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
				e.printStackTrace();  
		    }  
		    return dzdResponse;
		} 
	    
	    public String listToString(List list, char separator) {
	    	return org.apache.commons.lang.StringUtils.join(list.toArray(),separator);    
	    }
	    
	    
	    /**
	     * @Description:重发调用接口
	     * @author:oygy
	     * @time:2017年1月3日 上午11:56:29
	     */
	    @MethodDescription("重发调用接口")
		@RequestMapping(value = "/saveSendResend", method = RequestMethod.POST, produces = "application/json")
		@ResponseBody
		public DzdResponse saveSendResend(HttpServletRequest request, @RequestBody Map<String, Object> data) {
			DzdResponse dzdPageResponse = new DzdResponse();
			try {
				Object id = request.getParameter("menuId");
				Object type  = data.get("type");       	   //重发类型(0:状态重发，1：号码重发)
				Object smsAisle  = data.get("smsAisle");   //通道ID
				Object sendId  = data.get("sendId");       //任务ID
				Object smsState  = data.get("smsState");   //重发转态 (0:待发送,1:发送失败)
				
				SmsUserMessage smsUserMessage = new SmsUserMessage();
				if (id == null || "".equals(id)) {
					dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
					return dzdPageResponse;
				}
				
				Integer state = msmSendService.querySendResendState(Integer.parseInt(sendId.toString()));
				
				if(state==1){
					dzdPageResponse.setRetCode("000002");
					return dzdPageResponse;
				}
				//SysUser user = (SysUser)request.getSession().getAttribute("session_user");
				Long tid = Long.parseLong(sendId.toString());
				SmsTaskData smsTaskData= new SmsTaskData(tid,0,null);   //ID设置
				smsTaskData.setAisleGroupId(Long.parseLong(smsAisle.toString()));
				if(type!=null && Integer.parseInt(type.toString())==1){  //如果重发类型为1（号码重发）
					Object phoneString =request.getSession().getAttribute("phoneString"); //获取重发号码
					request.getSession().removeAttribute("phoneString");
					//调用接口
					
					smsTaskData.setPhongText(phoneString.toString());       //发送号码设置
					smsTaskData.setResendType(1);
					SmsServerManager.I.putSmsTaskResend(smsTaskData);
					
					//参数 sendId，smsAisle，phoneString
				}else{  //否则为类型重发
					//调用接口
					//参数 sendId，smsAisle，smsState
					int resendState = Integer.parseInt(smsState.toString());
					smsTaskData.setResendType(0);
					smsTaskData.setResendState(resendState);
					SmsServerManager.I.putSmsTaskResend(smsTaskData);
					
				}
				//修改重发状态为已经重发
				msmSendService.updateSendResendState(Integer.parseInt(sendId.toString()));
				dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
			} catch (Exception e) {
				log.error(null, e);
				dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
				e.printStackTrace();
			}
			return dzdPageResponse;
		}
	    
	    
	    
	    /** 
		 * 导出子任务的发送号码
		 */
		 @MethodDescription("导出子任务的发送号码")
		 @RequestMapping(value="/export/phoneAllLoad",method={RequestMethod.GET})
	     @ResponseBody
		 public void phoneAllLoad(HttpServletRequest request,HttpServletResponse response){
			 
			 List<String> projects=new ArrayList<String>();
			 Object id= request.getParameter("id");
			 if(id==null || "".equals(id)){
	     		return;
	     	}
			 try {
				String phones =msmSendService.querySmsSendPackageLogByid(Integer.parseInt(id.toString()));
				if(phones.indexOf(",")==-1){
					projects.add(phones);
				}else{
					String [] s = phones.split(",");
					projects=Arrays.asList(s);
				}
			} catch (NumberFormatException e2) {
				e2.printStackTrace();
				return;
			}
		 	 String fileName =id+"子任务";
	        //填充projects数据
	       // List<String> projects=new ArrayList<String>();
	        List<Map<String,Object>> list=createExcelRecord(projects);
	        String columnNames[]={"电话号码"};//列名
	        String keys[]    =     {"id"};//map中的key
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        try {
	            ExcelUtil.createWorkBook(list,keys,columnNames).write(os);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        byte[] content = os.toByteArray();
	        InputStream is = new ByteArrayInputStream(content);
	        // 设置response参数，可以打开下载页面
	        response.reset();
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
	        try {
				response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
	        } catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	       
	        BufferedInputStream bis = null;
	        BufferedOutputStream bos = null;
	        try {
	        	 ServletOutputStream out = response.getOutputStream();
	            bis = new BufferedInputStream(is);
	            bos = new BufferedOutputStream(out);
	            byte[] buff = new byte[2048];
	            int bytesRead;
	            // Simple read/write loop.
	            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	                bos.write(buff, 0, bytesRead);
	            }
	        } catch (final IOException e) {
	            try {
					throw e;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        } finally {
	            if (bis != null)
					try {
						bis.close();
						 if (bos != null)
				         bos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        }
		 }
	    
}