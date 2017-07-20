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
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONException;
import com.dzd.base.page.Pager;
import com.dzd.base.util.HtmlUtil;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.ExcelUtil;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.SmsReceiveReplyPush;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.page.MobileCheckUtil;
import com.dzd.phonebook.service.Instruct;
import com.dzd.phonebook.service.SysMenuBtnService;
import com.dzd.phonebook.service.SysRoleRelService;
import com.dzd.phonebook.service.SysUserService;
import com.dzd.phonebook.service.UserMessageService;
import com.dzd.phonebook.service.UserMsmSendService;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.InstructState;
import com.dzd.phonebook.util.RedisUtil;
import com.dzd.phonebook.util.ServiceUrl;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsSendLog;
import com.dzd.phonebook.util.SmsSendTask;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.phonebook.util.WebRequestParameters;
import com.dzd.sms.application.Define;
import com.github.pagehelper.Page;
import com.google.common.base.Joiner;

import net.sf.json.JSONObject;

/**
 * 用户发送消息controller
 *
 * @author oygy
 * @date 2016-6-24 16:11:00
 */
@Controller
@RequestMapping("/userMsmSend")
public class UserMsmSendController extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(UserMsmSendController.class);

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private SysMenuBtnService sysMenuBtnService;

    @Autowired
    private SysRoleRelService sysRoleRelService;


    @Autowired
    private SysUserService<SysUser> sysUserService;

    @Autowired
    private UserMsmSendService userMsmSendService;


    /**
     * 访问发送消息页面
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/listview")
    public String list(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        return "userSmsManage/msmSend";
    }


    /**
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
            Object ids = data.get("ids");
            Object state = data.get("state");


            SysUser user = (SysUser) request.getSession().getAttribute("session_user");


            if (menuId == null || user == null || user.getId() == null) {
                return dzdPageResponse;
            }
            Integer sysUserId = user.getId();
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

            if (!StringUtil.isEmpty(sysUserId.toString())) {
                sortMap.put("sysUserId", sysUserId);
            }

            if (!StringUtil.isEmpty(ids.toString())) {
                sortMap.put("ids", ids);
            }

            if (content != null && !StringUtil.isEmpty(content.toString())) {
                sortMap.put("content", content.toString());
            }

            if (state != null && !StringUtil.isEmpty(state.toString())) {
                sortMap.put("state", Integer.parseInt(state.toString()));
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

            Page<SmsSendTask> dataList = userMsmSendService.queryMsmSendList(dzdPageParam);
            SmsSendTask dataNumCount = userMsmSendService.queryMsmSendCount(dzdPageParam);
            if (dataList.getResult().size() > 0) {
                dataList.getResult().get(0).setBillingNumCount(dataNumCount.getBillingNumCount());
                dataList.getResult().get(0).setSendNumCount(dataNumCount.getSendNumCount());
                dataList.getResult().get(0).setActualNumCount(dataNumCount.getActualNumCount());
            }

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

    @RequestMapping(value = "/export/csv", method = {RequestMethod.GET})
    @ResponseBody
    public void exportCsv(HttpServletRequest request, HttpServletResponse response) {

        List<String> projects = new ArrayList<String>();
        Object id = request.getParameter("id");
        if (id == null || "".equals(id)) {
            return;
        }
        projects = userMsmSendService.queryMsmSendPhoneByid(Integer.parseInt(id.toString()));
        String fileName = id + "批次发送号码";
        //填充projects数据
        // List<String> projects=new ArrayList<String>();
        List<Map<String, Object>> list = createExcelRecord(projects);
        String columnNames[] = {"电话号码"};//列名
        String keys[] = {"id"};//map中的key
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ExcelUtil.createWorkBook(list, keys, columnNames).write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
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
    @RequestMapping(value = "/smsSendDetails", method = {RequestMethod.POST})
    public String smsSendDetails(HttpServletRequest request, HttpServletResponse response) {

        Object ids = request.getParameter("ids");
        Object menuId = request.getParameter("menuId");
        try {
            if (ids == null || "".equals(ids)) {
                return "redirect:/userSmsManage/listview.do?id=" + menuId;
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/userSmsManage/listview.do?id=" + menuId;
        }
        //return "redirect:/msmSend/listview.do?id="+ menuId;
        return "redirect:/userMsmSend/detailsListview.do?id=" + menuId + "&ids=" + ids;
    }


    @RequestMapping("/detailsListview")
    public String detailsListview(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        Object ids = request.getParameter("ids");
        model.addAttribute("menuId", menuId);
        model.addAttribute("ids", ids);
        return "userSmsManage/smsSendDetails";
    }





    /**
     * 终止发送
     *
     * @Description:
     * @author:oygy
     * @time:2017年1月13日 下午6:29:46
     */
    @RequestMapping(value = "/stopSmsSend", method = {RequestMethod.GET})
    @ResponseBody
    public DzdResponse stopSmsSend(HttpServletRequest request, HttpServletResponse response) {
        DzdResponse dzdPageResponse = new DzdResponse();
        Object id = request.getParameter("id");

        try {
            if (id == null || "".equals(id)) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdPageResponse;
            }

            Integer stete = userMsmSendService.querySmsSendById(Integer.parseInt(id.toString()));
            if (stete != -1) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdPageResponse;
            }

            userMsmSendService.updateSmsSendById(Integer.parseInt(id.toString()));
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);

            Instruct instruct = new Instruct();
            instruct.setKey(InstructState.STOPSMSSEND_SUCESS);
            instruct.setSmsSendId(id.toString());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(instruct);
            RedisUtil.publish(InstructState.AB, jsonStr);
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        }
        return dzdPageResponse;
    }


    /**
     * 查询发送消息详情列表
     *
     * @Description:
     * @author:oygy
     * @time:2016年12月31日 下午2:01:34
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/puserDetailsList")
    @ResponseBody
    public void list(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> data) throws Exception {
        try {
            Object menuId = request.getParameter("menuId");
            String offset = request.getParameter("offset");
            String pageSize = request.getParameter("limit");
            Object pagenum = data.get("pagenum");
            Object pagesize = data.get("pagesize");
            SmsSendLog smsSendLog = new SmsSendLog();
            org.json.JSONObject jsonObject = new org.json.JSONObject();
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                HtmlUtil.writerJson(response, jsonObject);
            }
            Object content = data.get("content");
            Object state = data.get("state");
            Object ids = data.get("ids");
            SysUser user = (SysUser) request.getSession().getAttribute("session_user");

            if (menuId == null || user == null || user.getId() == null) {
                HtmlUtil.writerJson(response, jsonObject);
            }
            Integer sysUserId = user.getId();

            if (!StringUtil.isEmpty(parameters.getStartInput())) {
                smsSendLog.setStartInput(parameters.getStartInput());
                smsSendLog.setEndInput(parameters.getEndInput());
            }

            if (state != null && !StringUtil.isEmpty(state.toString())) {
                if (state.toString().equals("2")) {  //如果发送状态为发送失败则区间查询
                    smsSendLog.setStateBs(1);
                } else {
                    smsSendLog.setState(Integer.parseInt(state.toString()));
                }
            }

            if (content != null && !StringUtil.isEmpty(content.toString())) {
                smsSendLog.setContent(content.toString());
            }

            if (ids != null && !StringUtil.isEmpty(ids.toString())) {
                smsSendLog.setId(Integer.parseInt(ids.toString()));
            }

            if (sysUserId != null && !StringUtil.isEmpty(sysUserId.toString())) {
                smsSendLog.setSysUserId(Integer.parseInt(sysUserId.toString()));
            }

            Pager pager = new Pager();
            if (pagenum != null && !pagenum.equals("")) {
                Integer pagenums = 0;
                if (pagesize != null && !pagesize.equals("")) {
                    pagenums = (Integer.parseInt(pagenum.toString()) - 1) * Integer.parseInt(pagesize.toString());
                }

                pager.setPageOffset(pagenums);
            }
            //role.setPager(pager);

            smsSendLog.setPager(pager);
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
            }
            //List<SysRole> dataList = sysRoleService.queryByList(role);

            List<SmsSendLog> dataList = userMsmSendService.queryByList(smsSendLog);

            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsSendLog sysRole : dataList) {
                    sysRole.setSysMenuBtns(sysMenuBtns);
                }
                // 设置页面数据

                jsonObject.put("total", smsSendLog.getPager().getRowCount());
                jsonObject.put("rows", dataList);
                HtmlUtil.writerJson(response, jsonObject);
            }
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }

    }


    @RequestMapping("/userPushListview")
    public String loading(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        return "userSmsManage/smsReceiveReplyPush";
    }

    /**
     * 查询用户短信回复列表
     *
     * @param request
     * @param response
     * @param data
     * @throws Exception
     */
    @RequestMapping(value = "/pushlist", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse blacklist(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");
            Object emailInput = data.get("emailInput");
            Object contentInput = data.get("contentInput");
            Object nameInput = data.get("nameInput");

            SysUser user = (SysUser) request.getSession().getAttribute("session_user");

            if (menuId == null || user == null || user.getId() == null) {
                return dzdPageResponse;
            }
            Integer sysUserId = user.getId();

            DzdPageParam dzdPageParam = new DzdPageParam();

            Map<String, Object> sortMap = new HashMap<String, Object>();

            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }
            if (!StringUtil.isEmpty(parameters.getStartInput())) {
                sortMap.put("start", parameters.getStartInput());
                sortMap.put("end", parameters.getEndInput());
            }
            if (!StringUtil.isEmpty(parameters.getSysUserName())) {
                // 根据名称筛选
                sortMap.put("SmsBlacklistHone", parameters.getSysUserName().trim());
            }
            if (emailInput != null && !StringUtil.isEmpty(emailInput.toString())) {
                sortMap.put("emailInput", emailInput.toString());
            }
            if (contentInput != null && !StringUtil.isEmpty(contentInput.toString())) {
                sortMap.put("contentInput", contentInput.toString());
            }
            if (nameInput != null && !StringUtil.isEmpty(nameInput.toString())) {
                sortMap.put("nameInput", nameInput.toString());
            }
            if (sysUserId != null && !StringUtil.isEmpty(sysUserId.toString())) {
                sortMap.put("sysUserId", sysUserId);
            }


            // 排序
            if (parameters.getSort() != null && parameters.getOrder() != null) {
                sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
            } else {
                sortMap.put("sortVal", "order by createTime desc");
            }

            dzdPageParam.setCondition(sortMap);
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
            }
            SmsReceiveReplyPush smsReceiveReplyPush = new SmsReceiveReplyPush();
            Page<SmsReceiveReplyPush> dataList = userMsmSendService.queryUserList(dzdPageParam);

            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsReceiveReplyPush smsBlacklist : dataList.getResult()) {
                    smsBlacklist.setSysMenuBtns(sysMenuBtns);
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



    @RequestMapping("/fileUpLoad")
    //btnFile对应页面的name属性
    @ResponseBody
    public DzdResponse fileUpLoad(@RequestParam MultipartFile[] btnFile, HttpServletRequest request, HttpServletResponse response) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            //文件类型:btnFile[0].getContentType()
            //文件名称:btnFile[0].getName()
            if (btnFile[0].getSize() > Integer.MAX_VALUE) {//文件长度
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }
            File f = null;
            f = File.createTempFile("tmp", null);
            btnFile[0].transferTo(f);
            f.deleteOnExit();

            List<String> phoneList = new ArrayList<String>();                            //记录上传号码
            Map<String, List<String>> smsInfoMap = new HashMap<String, List<String>>(); //处理后的号码信息
            InputStreamReader reader = new InputStreamReader(new FileInputStream(f));    // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader);                            // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            line = br.readLine();
            while (line != null) {
                phoneList.add(line.trim());
                line = br.readLine(); // 一次读入一行数据

            }
            smsInfoMap = MobileCheckUtil.mobileAssort(phoneList);

            Integer invalidNum = smsInfoMap.get(Define.PHONEKEY.INVALID).size();    //无效号码个数

            Integer duplicateNum = smsInfoMap.get(Define.PHONEKEY.DUPLICATE).size(); //重复号码个数

            Integer validNum = smsInfoMap.get(Define.PHONEKEY.VALID).size();         //有效号码个数

            List<String> validLsit = smsInfoMap.get(Define.PHONEKEY.VALID);            //有效号码

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("invalidNum", invalidNum);
            map.put("duplicateNum", duplicateNum);
            map.put("validNum", validNum);
            map.put("validLsit", validLsit);
				
				
		        
		       /* 保存文件
		        InputStream is = btnFile[0].getInputStream();//多文件也适用,我这里就一个文件  
		        //String fileName = request.getParameter("fileName");  
		        String guid = request.getParameter("guid");  
		        byte[] b = new byte[(int)btnFile[0].getSize()];  
		        int read = 0;  
		        int i = 0;  
		        while((read=is.read())!=-1){  
		            b[i] = (byte) read;  
		            i++;  
		        }  
		        is.close();  
		        OutputStream os = new FileOutputStream(new File("D://"+guid+"."+btnFile[0].getOriginalFilename()));//文件原名,如a.txt  
		        os.write(b);  
		        os.flush();  
		        os.close();  */
            dzdResponse.setData(map);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdResponse;
    }


}