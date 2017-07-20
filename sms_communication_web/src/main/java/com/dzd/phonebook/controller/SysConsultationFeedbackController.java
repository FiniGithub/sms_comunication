package com.dzd.phonebook.controller;

import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.dao.VertifyCodeDao;
import com.dzd.phonebook.entity.SysConsultationFeedback;
import com.dzd.phonebook.entity.SysLog;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.entity.VertifyCode;
import com.dzd.phonebook.service.SmsUserService;
import com.dzd.phonebook.service.SysConsultationFeedbackService;
import com.dzd.phonebook.service.VertifyCodeService;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.WebRequestParameters;
import com.github.pagehelper.Page;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 咨询与反馈
 * Created by wangran on 2017/6/02.
 */
@Controller
@RequestMapping("/zixun")
public class SysConsultationFeedbackController extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(SysConsultationFeedbackController.class);

    @Autowired
    private SysConsultationFeedbackService sysConsultationFeedbackService;

    @Autowired
    private SmsUserService smsUserService;

    @Autowired
    private VertifyCodeService vertifyCodeService;


    @Autowired
    private VertifyCodeDao<T> vertifyCodeDao;

    public VertifyCodeDao<T> getDao() {
        return vertifyCodeDao;
    }


    @RequestMapping("/listview")
    public String list(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        return "zx/zxfk";
    }

    /**
     * @Description:咨询与反馈列表
     * @author:wangran
     * @time:2017年6月2日
     */
    @RequestMapping(value = "/zixunList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse zixunList(@RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        DzdPageParam dzdPageParam = new DzdPageParam();
        Map<String, Object> sortMap = new HashMap<String, Object>();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }

            sortMap.put("sortVal", "order by createTime desc");
            dzdPageParam.setCondition(sortMap);

            Page<SysLog> dataList = sysConsultationFeedbackService.querySysListPage(dzdPageParam);
            if (!CollectionUtils.isEmpty(dataList)) {
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
     * @Description:咨询与反馈列表
     * @author:wangran
     * @time:2017年6月2日
     */
    @MethodDescription("新增咨询与反馈")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse insertData(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            HttpSession session = request.getSession();
            //session中图片验证码
            Object randomString = session.getAttribute("randomString");
            //前台输入的图片验证码
            String imgCode = request.getParameter("imgCode");
            //短信验证码
            String verifyCode = request.getParameter("verifyCode");
            //手机号码
            String phone = request.getParameter("phone");
            //账户名称
            String email = request.getParameter("email");
            //咨询与反馈内容
            String content = request.getParameter("content");
            //联系人
            String contact = request.getParameter("contact");

            //查询手机号码是否已经存在
            Integer phoneIsHad = smsUserService.queryPhone(phone, null);

            if (phoneIsHad < 0) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                dzdResponse.setRetMsg("手机号码不存在，请重新输入!");
                return dzdResponse;
            }


            //查询手机验证码当天有效
            VertifyCode code2 = vertifyCodeDao.queryCodeByPhoneAndCode(phone, verifyCode);

            if (code2 == null) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                dzdResponse.setRetMsg("短信验证码错误!");
                return dzdResponse;
            }

            if (imgCode != null && !"".equals(imgCode) && randomString != null
                    && !"".equals(randomString.toString())
                    && imgCode.trim().toUpperCase().equals(randomString.toString().trim().toUpperCase())) {
            } else {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                dzdResponse.setRetMsg("图形验证码错误!");
                return dzdResponse;
            }

            SysConsultationFeedback scf = new SysConsultationFeedback();
            scf.setEmail(email);
            scf.setContact(contact);
            scf.setContent(content);
            scf.setPhone(phone);
            //保存咨询与反馈信息
            sysConsultationFeedbackService.saveData(scf);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            dzdResponse.setRetMsg("操作成功!");
        } catch (Exception ex) {
            ex.printStackTrace();
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            dzdResponse.setRetMsg("发生异常,请稍后再试!");
        }
        return dzdResponse;
    }

    /**
     * @Description:删除咨询与反馈信息
     * @author:wangran
     * @time:2017年6月02日 下午16:35:25
     */
    @MethodDescription("删除咨询与反馈信息")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse apply_delete(HttpServletRequest request, @RequestParam("ids[]") List<Integer> ids) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            sysConsultationFeedbackService.delete(ids);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            log.error(null, e);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }
}
