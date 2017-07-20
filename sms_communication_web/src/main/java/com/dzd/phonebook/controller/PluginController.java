package com.dzd.phonebook.controller;



import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONException;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.Plugin;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.entity.UploadFile;
import com.dzd.phonebook.service.FileUploadService;
import com.dzd.phonebook.service.Instruct;
import com.dzd.phonebook.service.PluginService;
import com.dzd.phonebook.service.SysMenuBtnService;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.GsonUtil;
import com.dzd.phonebook.util.InstructState;
import com.dzd.phonebook.util.RedisUtil;
import com.dzd.phonebook.util.WebRequestParameters;
import com.github.pagehelper.Page;

import net.sf.json.JSONObject;

/**
 * 
 * @Description:插件上传
 * @author:oygy
 * @time:2017年3月3日 下午2:41:54
 */
@Controller
@RequestMapping("/plugin")
public class PluginController  extends WebBaseController{
	public static final Logger log = LoggerFactory.getLogger(PluginController.class);
	
	
    @Autowired
    private SysMenuBtnService sysMenuBtnService;
	
    @Autowired
    private PluginService pluginService;
	
    @Autowired
    private FileUploadService fileService;
    
	@RequestMapping("/listview")
    public String list(HttpServletRequest request, Model model) throws Exception {
       Object menuId = request.getParameter("id");
       model.addAttribute("menuId", menuId);
       return "plugin/pluginlist";
    }
	
	
	 /**
	  * 
	  * @Description:查询插件列表
	  * @author:oygy
	  * @time:2016年12月31日 下午2:01:34
	  */
	 @RequestMapping(value = "/pluginList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse pluginList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> data) throws Exception {
       DzdResponse dzdPageResponse = new DzdResponse();
       try {
           WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
           if (parameters == null) {
               return dzdPageResponse;
           }
           Object menuId = request.getParameter("menuId");
           Object pname = data.get("pname");
           
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
          
           if (pname!=null && !StringUtil.isEmpty(pname.toString())) {
               sortMap.put("pname", pname.toString());
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
           
           Page<Plugin> dataList = pluginService.queryPluginlist(dzdPageParam);
           
           
           
           
           if (!CollectionUtils.isEmpty(dataList)) {
               for (Plugin instruct : dataList.getResult()) {
            	   System.out.println("--------------------->"+instruct.getCreateTime());
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
	 
	 	@MethodDescription("上传jar插件")
	    @RequestMapping("/from/merge")
	    public String merge(HttpServletRequest request, HttpServletResponse response) {
	        logger.info("merge -- >>");
	        //菜单id
	        Object menuId = request.getParameter("menuId");
	        Object id = request.getParameter("id_name");
	        Object name = request.getParameter("apk_name");
	        Object description = request.getParameter("apk_description_name");
	        try {
	        	
	            UploadFile uploadFiles = fileService.saveFile(request, "CALIM_TYPE");
	            /*应用信息*/
	            Plugin claim = new Plugin();   
	            /*应用信息版本记录*/
	            claim.setName(name + "");
	            String quanurl = "";
	            if (uploadFiles != null && !StringUtil.isEmpty(uploadFiles.getPrePath())) {
	                claim.setPath(uploadFiles.getPrePath());
	                logger.info("uploadFiles -- >>" + GsonUtil.toStr(uploadFiles));
	                String url = uploadFiles.getSaveDirectory()+uploadFiles.getFileName();
	                logger.info("得到上传后的jar路径--->>"+url);
	                
	                File file1=new File(url);
	                InputStream isjar=new FileInputStream(file1);
	                byte[] bjar = FileCopyUtils.copyToByteArray(isjar);    //FileCopyUtils   为spring下的一个工具类。  
	                claim.setJar(bjar);
	                
	                 //http://202.104.150.120:60000/img_path/calim/app-debug.apk");
	                String jarPath = "";
	                if( url.substring(0,1).equals("/")){
	                	jarPath = "jar:file:"+url+"!/config.json";
	                }else{
	                	jarPath = "jar:file:/"+url+"!/config.json";
	                }
	                URL urls=new URL(jarPath); 
		            System.out.println(urls); 
		            InputStream is=urls.openStream(); 
		            byte b[]=new byte[2000]; 
		            is.read(b); 
		            String jsonName =new String(b).trim();
		            JSONObject obj =jsonObject(jsonName);
		            //通道名称 插件中json里面的name
		        	String aisleName = obj.getString("name"); 	
		        	//通道标识如（ShenZhenAisle）
		    		String className = obj.getString("className");     
		    		claim.setAisleName(aisleName);
		    		claim.setClassName(className);
		    		claim.setConfig(obj.getString("option"));
		            System.out.println("------------------json:>"+new String(b).trim()); 
		            quanurl = url;
	            }
	            
	           if(name!=null && !"".equals(name.toString())){
	        	   claim.setName(name.toString());
	           }
	           if(description!=null && !"".equals(description.toString())){
	        	   claim.setIntro(description.toString());
	           }
	           SysUser user = (SysUser)request.getSession().getAttribute("session_user");
	           if(user.getId()!=null && !"".equals(user.getId().toString())){
	        	   claim.setCreateBy(user.getId());
	           }
	           
	           Integer pid = pluginService.queryPluginId(claim.getPath());
	          
	           if(pid!=null){
	        	   pluginService.updatePlugin(claim);
	        	   instructSend(InstructState.SAVEPLUGIN_SUCESS,pid,quanurl);
	           }else{
	        	   pluginService.savePlugin(claim); 
	        	   instructSend(InstructState.SAVEPLUGIN_SUCESS,claim.getId(),quanurl);
	           }
	           
	        } catch (Exception e) {
	            logger.error(e.getMessage());
	            e.printStackTrace();
	        }
	        return "redirect:/plugin/listview.do?id=" + menuId;
	    }
	 	
	 	/**
		 * 删除上传插件
		 * @param request
		 * @param model
		 * @return
		 */
	 	@MethodDescription("删除上传插件")
		@RequestMapping(value = "/from/delete", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public DzdResponse postsDelete(HttpServletRequest request, Model model) {
			DzdResponse response = new DzdResponse();
			try {
				String pid = request.getParameter("id");
				if(pid==null || "".equals(pid)){
					response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
					return response;
				}
				
				Integer num =pluginService.querySmsAislePid(Integer.parseInt(pid));
				if(num>0){
					response.setRetCode(ErrorCodeTemplate.CODE_PARAMETER_ERROR);  //存在引用改通道源的通道 不可删除
					return response;
				}
				pluginService.deletePlugin(Integer.parseInt(pid));
				instructSend(InstructState.DELETEPLUGIN_SUCESS,Integer.parseInt(pid),null);
				response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
			} catch (Exception e) {
				response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
				e.printStackTrace();
			}
			return response;
		}
	 	
	    /**
	     * json转换成Object
	     * @Description:
	     * @author:oygy
	     * @time:2016年9月2日 下午3:34:50
	     */
	   private JSONObject jsonObject(String json){
		   String jsonMessage = json;
		   JSONObject myJsonObject = new JSONObject();
		   try
		   {
		    //将字符串转换成jsonObject对象
		    myJsonObject = JSONObject.fromObject(jsonMessage);
		   }catch (JSONException e){
			   e.getMessage();
		   }
		   return myJsonObject;
	   } 
	
	   
	    /**
	     * @Description:代理数据处理动作发送
	     * @author:oygy
	     * @time:2017年1月11日 下午2:45:22
	     */
	    private void instructSend(String keys,Integer pid,String pluginUrl){
	    	Instruct instruct= new Instruct();
	    	instruct.setKey(keys);
	    	instruct.setPid(pid);
	    	instruct.setPluginUrl(pluginUrl);
           ObjectMapper mapper = new ObjectMapper(); 
			try {
				 String jsonStr = mapper.writeValueAsString(instruct);
				 RedisUtil.publish(InstructState.AB, jsonStr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
           
	    }
	   
	   
}