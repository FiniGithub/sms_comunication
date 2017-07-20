package com.dzd.phonebook.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;

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

import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.Instruct;
import com.dzd.phonebook.service.MsmSendService;
import com.dzd.phonebook.service.SmsAuditService;
import com.dzd.phonebook.service.SmsUserService;
import com.dzd.phonebook.service.SysMenuBtnService;
import com.dzd.phonebook.service.SysRoleRelService;
import com.dzd.phonebook.service.SysUserService;
import com.dzd.phonebook.service.UserFreeTrialService;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.InstructState;
import com.dzd.phonebook.util.RedisUtil;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsSendTask;
import com.dzd.phonebook.util.SmsUser;
import com.dzd.phonebook.util.UserFreeTria;
import com.dzd.phonebook.util.WebRequestParameters;
import com.github.pagehelper.Page;

/**
 * 审核消息controller
 *
 * @author oygy
 * @date 2016-6-24 16:11:00
 */
@Controller
@RequestMapping("/smsAudit")
public class SmsSendAuditController extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(PuserController.class);

    @Autowired
    private SysMenuBtnService sysMenuBtnService;

    @Autowired
    private SysRoleRelService sysRoleRelService;


    @Autowired
    private SysUserService<SysUser> sysUserService;

    @Autowired
    private MsmSendService msmSendService;

    @Autowired
    private SmsAuditService smsAuditService;

    @Autowired
    private SmsUserService smsUserService;

    @Autowired
    private UserFreeTrialService userFreeTrialService;

    @RequestMapping("/listview")
    public String list(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        return "smsManage/smsSendAudit";
    }


    /**
     * @Description:查询审核消息列表
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

            if (state != null && !StringUtil.isEmpty(state.toString())) {
                sortMap.put("state", Integer.parseInt(state.toString()));
            }

            if (content != null && !StringUtil.isEmpty(content.toString())) {
                sortMap.put("content", content.toString());
            }

            if (ids != null && !StringUtil.isEmpty(ids.toString())) {
                sortMap.put("ids", Integer.parseInt(ids.toString()));
            }

            if (smsUser != null && !StringUtil.isEmpty(smsUser.toString())) {
                sortMap.put("smsUser", smsUser.toString());
            }

            if (sendType != null && !StringUtil.isEmpty(sendType.toString())) {
                sortMap.put("sendType", Integer.parseInt(sendType.toString()));
            }
            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
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
                sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
            }


            Page<SmsSendTask> dataList = smsAuditService.querySmsAuditList(dzdPageParam);

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
    public DzdResponse exportCsv(HttpServletRequest request, HttpServletResponse response) {
        DzdResponse dzdPageResponse = new DzdResponse();
        List<String> dataSource = new ArrayList<String>();
        Object id = request.getParameter("id");
        FileWriter fw = null;
        StringBuffer row = null;
        try {
            if (id == null || "".equals(id)) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdPageResponse;
            }
            dataSource = msmSendService.queryMsmSendPhoneByid(Integer.parseInt(id.toString()));
            File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
            String desktopPath = desktopDir.getAbsolutePath();
            String file = desktopPath + "/" + id + "批次发送号码.txt";
            fw = new FileWriter(file);
            row = new StringBuffer();
            //row.append("city\t\t").append("area_code\t\t").append("start_gt\t\t").append("hlr_name"+"\n");
            fw.write(row.toString());
            for (int i = 0; i < dataSource.size(); i++) {
                row = new StringBuffer();
                String myBean = dataSource.get(i);
                row.append(myBean + ",");
                fw.write(row.toString());
            }
            fw.flush();
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (IOException e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        } finally {
            if (fw != null)
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return dzdPageResponse;
    }


    /**
     * @Description:短信审核
     * @author:oygy
     * @time:2017年1月12日 上午10:38:44
     */
    @RequestMapping(value = "/auditSms", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DzdResponse auditSms(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> data) {
        DzdResponse dzdPageResponse = new DzdResponse();
        Object id = data.get("id");
        Object type = data.get("type");

        Object sCount = data.get("sCount");
        Object iAisleGroup = data.get("iAisleGroup");
        Object qmbb = data.get("qmbb");
        Object qmnr = data.get("qmnr");
        Object smsUserId = data.get("smsUserId");

        try {

            if (id == null || "".equals(id) || type == null || "".equals(type)) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdPageResponse;
            }
            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            List<SmsSendTask> list = new ArrayList<SmsSendTask>();
            if (id.toString().indexOf(",") != -1) {
                String[] ids = id.toString().split(",");
                for (int i = 0; i < ids.length; i++) {
                    SmsSendTask smsSendTask = new SmsSendTask();
                    smsSendTask.setId(Integer.parseInt(ids[i].toString()));

                    list.add(smsSendTask);
                }
            } else {
                SmsSendTask smsSendTask = new SmsSendTask();
                smsSendTask.setId(Integer.parseInt(id.toString()));

                if (Integer.parseInt(type.toString()) == 1) {  //通过
                    if (sCount != null && !"".equals(sCount)) {
                        smsSendTask.setContent(sCount.toString());
                    }

                    if (iAisleGroup != null && !"".equals(iAisleGroup)) {
                        smsSendTask.setGroupId(Integer.parseInt(iAisleGroup.toString()));
                    }

                    if (smsUserId != null && !"".equals(smsUserId)) {
                        SmsUser smsUser = (SmsUser) smsUserService.queryById(Integer.parseInt(smsUserId.toString()));
                        if (qmnr != null && !"".equals(qmnr) && smsUser != null) {//添加内容报备
                            //1.查询是否有相同的内容报备
                            UserFreeTria userFreeTria = new UserFreeTria();
                            userFreeTria.setName(qmnr.toString());
                            userFreeTria.setFreeTrialType(smsUser.getGroupTypeId());
                            userFreeTria.setSmsUserId(smsUser.getId());
                            userFreeTria.setFreeTrialState(1);
                            userFreeTria.setContent(qmnr.toString());
                            log.info("-----------------》新增用户免审模板");
                            Integer num = userFreeTrialService.queryCountUserFreeTria(userFreeTria);
                            if (num <= 0) {
                                userFreeTrialService.sysSaveUserFreeTria(userFreeTria);
                                instructSend(InstructState.ADDUSERFREETRIAL_SUCESS, smsUser.getId());   //发送动作指令到redis
                            }
                        }

                        if (qmbb != null && !"".equals(qmbb) && smsUser != null) { //添加签名报备
                            //1.查询是否有相同的签名报备
                            //1.查询是否有相同的内容报备
                            UserFreeTria userFreeTria = new UserFreeTria();
                            userFreeTria.setName(qmbb.toString());
                            userFreeTria.setSmsUserId(smsUser.getId());
                            userFreeTria.setFreeTrialState(1);
                            userFreeTria.setContent(qmbb.toString());
                            log.info("-----------------》新增用户免审模板");
                            Integer num = userFreeTrialService.queryCountUserSignature(userFreeTria);
                            if (num <= 0) {
                                userFreeTrialService.sysSaveUserSignature(userFreeTria);
                                instructSend(InstructState.ADDUSERFREETRIAL_SUCESS, smsUser.getId());   //发送动作指令到redis
                            }
                        }
                    }
                }
                list.add(smsSendTask);
            }

            String auditState = "1";

            if (Integer.parseInt(type.toString()) == 0) {   //拒绝通过
                for (SmsSendTask smsSendTask2 : list) {
                    smsSendTask2.setAudiId(user.getId());
                    smsSendTask2.setAuditState(3);
                    smsSendTask2.setState(9);  //修改发送状态
                    smsAuditService.updateSmsAudit(smsSendTask2);
                    //返还号码数量
                    //根据任务ID查询任务信息
                    SmsSendTask smsSend = smsAuditService.querySmsSeng(smsSendTask2.getId());
                    if (smsSend != null) {
                        smsAuditService.rollbackNum(smsSend, user.getId());
                    }
                }
                auditState = "0";
            }
            if (Integer.parseInt(type.toString()) == 1) {   //通过
                for (SmsSendTask smsSendTask2 : list) {
                    smsSendTask2.setAudiId(user.getId());
                    smsSendTask2.setAuditState(2);
                    smsSendTask2.setState(0);  //修改发送状态
                    smsSendTask2.setSendTime(new Date());  //发送时间
                    smsAuditService.updateSmsAudit(smsSendTask2);
                }
            }

            Instruct instruct = new Instruct();
            instruct.setKey(InstructState.AUDITSMS_SUCESS);
            instruct.setSmsSendId(id + "");
            instruct.setAuditState(auditState);
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(instruct);//将java对象转换为json对象
            RedisUtil.publish(InstructState.AB, jsonStr);

            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        }

        return dzdPageResponse;
    }

    /**
     * @Description:免审模板数据处理动作发送
     * @author:oygy
     * @time:2017年1月11日 下午2:45:22
     */
    private void instructSend(String keys, Integer id) {
        Instruct instruct = new Instruct();
        instruct.setKey(keys);
        instruct.setSmsUserId(id + "");
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