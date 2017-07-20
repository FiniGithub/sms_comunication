
package com.dzd.phonebook.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.*;
import com.dzd.phonebook.service.UserMessageService;
import com.dzd.phonebook.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.SmsOrderExportService;
import com.dzd.phonebook.service.SmsUserService;
import com.dzd.sms.application.Define;
import com.github.pagehelper.Page;

/**
 * 用户群发短信Controller
 *
 * @Author CHENCHAO
 * @Date 2017-03-25 15:38:00
 */
@Controller
@RequestMapping("/export")
public class SmsOrderExportController extends WebBaseController {
    @SuppressWarnings("rawtypes")
    @Autowired
    private SmsUserService smsUserService;
    @Autowired
    private UserMessageService userMessageService;

    private SmsOrderExportService orderExportService = new SmsOrderExportService();

    private Page<SmsUser> dataList;

    public Page<SmsUser> getDataList() {
        return dataList;
    }

    public void setDataList(Page<SmsUser> dataList) {
        this.dataList = dataList;
    }

    @RequestMapping(value = "/orderExport", method =
            {RequestMethod.GET})
    @ResponseBody
    public void orderExport(HttpServletRequest request, HttpServletResponse response) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        orderExportService.orderExport(request, response, dataList);
    }

    @RequestMapping(value = "/customerOrderExport", method =
            {RequestMethod.GET})
    @ResponseBody
    public void customerOrderExport(HttpServletRequest request, HttpServletResponse response) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        orderExportService.customerOrderExport(request, response, dataList);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/querySmsData", method = RequestMethod.GET)
    @ResponseBody
    public DzdResponse querySmsData(HttpServletRequest request) {
        DzdResponse response = new DzdResponse();

        DzdPageParam dzdPageParam = preParameter(request);

        Page<SmsUser> dataList = smsUserService.querySmsUserStatisticalList(dzdPageParam);

        if (dataList == null || CollectionUtils.isEmpty(dataList.getResult())) {
            response.setRetCode(ErrorCodeTemplate.CODE_PARAMETER_ERROR);
            logger.error(
                    "querySmsUserStatisticalList dataList is empty, Last month no hava statistics data");
            return response;
        }

        if (Boolean.parseBoolean(request.getParameter(Define.FILENAME.CUSTOMER_ORDER))) {
            this.setDataList(dataList);

            response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            return response;
        }

        List<String> emails = new ArrayList<String>();
        for (SmsUser date : dataList.getResult()) {
            emails.add(date.getEmail());
        }
        DzdPageParam filldzdPageParam = new DzdPageParam();
        filldzdPageParam.setCondition(emails);
        Map<String, String> fillSmsUserStatisticalMap = smsUserService
                .fillSmsUserStatisticalListPage(filldzdPageParam);
        for (SmsUser smsUser : dataList.getResult()) {
            smsUser.setNickName(fillSmsUserStatisticalMap.get(smsUser.getEmail()));
        }

        this.setDataList(dataList);

        response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        return response;
    }

    private DzdPageParam preParameter(HttpServletRequest request) {
        DzdPageParam dzdPageParam = new DzdPageParam();
        Map<String, Object> sortMap = new HashMap<String, Object>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String firstDay;
        String lastDay;
        // 获取前月的第一天
        Calendar cal_1 = Calendar.getInstance();// 获取当前日期
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        firstDay = format.format(cal_1.getTime());

        // 获取前月的最后一天
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH, 0);// 设置为1号,当前日期既为本月第一天
        lastDay = format.format(cale.getTime());

        sortMap.put("startInput", firstDay);
        sortMap.put("endInput", lastDay);

        Object email = request.getParameter("email");
        if (email == null || "".equals(email)) {
            SysUser user = (SysUser) request.getSession().getAttribute("session_user");
            if (user.getSuperAdmin() != 1) {
                email = user.getEmail();
            }
        }
        sortMap.put("email", email);

        dzdPageParam.setCondition(sortMap);
        return dzdPageParam;
    }


    /**
     * 导出充值记录
     */
    @SuppressWarnings("unchecked")
    @MethodDescription("导出充值记录")
    @RequestMapping(value = "/exportRecharge")
    @ResponseBody
    public void exportCsv(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 1.接收参数
            Object email = request.getParameter("email");
            Object type = request.getParameter("type");
            Object startInput = request.getParameter("startInput");
            Object endInput = request.getParameter("endInput");
            SysUser sysUser = SessionUtils.getUser(request);
            SmsUser smsUser = userMessageService.querySmsUserById(sysUser.getId());

            // 2. 参数赋值
            SmsUserMoneyRunning running = new SmsUserMoneyRunning();
            running.setSuperAdmin(sysUser.getSuperAdmin());

            if (!StringUtil.isEmpty(startInput.toString())) {
                running.setStartInput(startInput.toString());
                running.setEndInput(endInput.toString());
            }

            if (email != null && !StringUtil.isEmpty(email.toString())) {
                running.setEmail(email.toString());
            }
            if (type != null && !StringUtil.isEmpty(type.toString())) {
                running.setType(Integer.parseInt(type.toString()));
            }
            if (smsUser.getId() != null && !StringUtil.isEmpty(smsUser.getId().toString())) {
                running.setSmsUserId(smsUser.getId());
            }

            // 3. 查询消费记录
            List<SmsUserMoneyRunning> projects = smsUserService.querySmsUserRechargeBillList(running);
            if (projects == null || projects.size() == 0) {
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert(\"没有可以导出的数据!\");");
                out.println("window.history.back();");
                out.println("</script>");
                return;
            }

            String fileName = "全网信通短信充值记录";

            // 4.填充projects数据
            List<Map<String, Object>> list = FileUploadUtil.createExcelConsumed(projects);
            String columnNames[] = {"操作者", "账号", "时间", "充值金额", "数量", "充值前", "充值后", "类型", "归属"};// 列名
            String keys[] = {"uaccount", "email", "createTime", "money", "operateNum", "beforeNum", "afterNum", "type", "bname"};// map中的key
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                com.dzd.phonebook.controller.base.ExcelUtil.createWorkBook(list, keys, columnNames).write(os);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            try {
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
