package com.dzd.phonebook.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.entity.SmsWordShielding;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.service.Instruct;
import com.dzd.phonebook.service.SmsWordShieldingService;
import com.dzd.phonebook.service.SysMenuBtnService;
import com.dzd.phonebook.util.AbstractController;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.InstructState;
import com.dzd.phonebook.util.RedisUtil;
import com.dzd.phonebook.util.JspResponseBean;
import com.dzd.phonebook.util.SmsShieldWord;
import com.dzd.phonebook.util.WebRequestParameters;
import com.github.pagehelper.Page;

/**
 * controller屏蔽词管理
 *
 * @author lq
 * @date 2017-1-3
 */
@Controller
@RequestMapping("/wordShielding")
public class SmsWordShieldingController extends AbstractController {
    public static final Logger log = LoggerFactory.getLogger(SmsWordShieldingController.class);

    @Autowired
    private SmsWordShieldingService smsWordShieldingService;

    @Autowired
    private SysMenuBtnService sysMenuBtnService;

    /**
     * @Description:总屏蔽词管理
     * @author:oygy
     * @time:2017年2月24日 下午5:05:08
     */

    @RequestMapping("/shieldWordview")
    public String shieldWordview(HttpServletRequest request, Model model) {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        return "systemUser/smsShieldWord";
    }

    /**
     * @Description:查询总屏蔽词信息列表
     * @author:CHENCHAO
     * @time:2017年06月09日 上午10:50:34
     */
    @RequestMapping(value = "/shieldWordList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse shieldWordList(HttpServletRequest request, HttpServletResponse response,
                                      @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");
            Object name = data.get("name");
            Object typeId = data.get("typeId");
            Object level = data.get("level");

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

            if (name != null && !StringUtil.isEmpty(name.toString())) {
                sortMap.put("name", name.toString());
            }
            if (typeId != null && !StringUtil.isEmpty(typeId.toString())) {
                sortMap.put("type", typeId);
            }

            if (level != null && !StringUtil.isEmpty(level.toString())) {
                sortMap.put("level", level);
            }


            // 排序
            if (parameters.getSort() != null && parameters.getOrder() != null) {
                sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
            }
            dzdPageParam.setCondition(sortMap);
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
            }
            Page<SmsShieldWord> dataList = smsWordShieldingService.querShieldWordList(dzdPageParam);

            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsShieldWord smsAisles : dataList.getResult()) {
                    smsAisles.setSysMenuBtns(sysMenuBtns);
                }
            }
            dzdPageResponse.setRows(dataList.getResult());
            dzdPageResponse.setTotal(dataList.getTotal());
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    /**
     * 总屏蔽词新增或修改
     *
     * @param request
     * @return
     */
    @MethodDescription("屏蔽词新增或修改操作")
    @RequestMapping(value = "/from/addShieldWord", method = RequestMethod.POST)
    public String addShieldWord(HttpServletRequest request, HttpServletResponse response, Model model) {
        Object menuId = request.getParameter("menuId");
        try {
            SmsShieldWord smsShieldWord = new SmsShieldWord(); // 屏蔽词

            Object id = request.getParameter("id");
            Object type = request.getParameter("type"); // 属性
            Object wordName = request.getParameter("wordName"); // 屏蔽词
            Object level = request.getParameter("level");// 等级

            if (id != null && !StringUtil.isEmpty(id.toString())) {
                smsShieldWord.setId(Integer.parseInt(id.toString()));
            }

            if (type != null && !StringUtil.isEmpty(type.toString())) {
                smsShieldWord.setType(Integer.parseInt(type.toString()));
            }
            if (wordName != null && !StringUtil.isEmpty(wordName.toString())) {
                smsShieldWord.setWordName(wordName.toString());
            }
            if (level != null && !StringUtil.isEmpty(level.toString())) {
                smsShieldWord.setLevel(Integer.parseInt(level.toString()));
            }

            if (smsShieldWord.getId() == null) {
                log.info("-----------------》新增屏蔽词");
                smsWordShieldingService.saveShieldWord(smsShieldWord);
                instructSend();// 发送动作指令
            } else {
                log.info("-----------------》修改屏蔽词");
                smsWordShieldingService.updateShieldWord(smsShieldWord);
                instructSend();// 发送动作指令
            }

        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return "redirect:/wordShielding/shieldWordview.do?id=" + menuId;
    }

    /**
     * @Description:根据ID查询屏蔽词信息
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
            SmsShieldWord smsShieldWord = smsWordShieldingService
                    .queryShieldWordById(Integer.parseInt(id.toString()));


            JspResponseBean jspResponseBean = new JspResponseBean();
            jspResponseBean.setData(smsShieldWord);
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
     * @Description:删除屏蔽词信息
     * @author:liuyc
     * @time:2017年1月4日 下午4:39:53
     */
    @MethodDescription("删除屏蔽词")
    @RequestMapping(value = "/from/delete", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public DzdResponse delete(HttpServletRequest request, Model model) {
        DzdResponse response = new DzdResponse();
        try {
            String id = request.getParameter("id");
            log.info("--------->根据ID" + id + "删除通道信息");
            smsWordShieldingService.deleteShieldWord(Integer.parseInt(id));
            instructSend();// 发送动作指令
            response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            log.info("--------->删除通道信息失败");
            response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return response;
    }


    /**
     * 批量删除屏蔽词
     *
     * @param request
     * @param data
     * @return
     */
    @MethodDescription("批量删除屏蔽词")
    @RequestMapping(value = "formDelete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DzdResponse userDelete(HttpServletRequest request, @RequestBody Map<String, Object> data) {
        DzdResponse response = new DzdResponse();
        try {
            Object ids = data.get("ids");
            if (ids == null) {
                response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
                return response;
            }
            List<Integer> idList = (List<Integer>) ids;
            if (idList == null || idList.size() == 0) {
                response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            } else {
                smsWordShieldingService.deleteShieldWordList(idList);
                response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
                instructSend();// 发送动作指令
            }
        } catch (Exception e) {
            log.error(null, e);
            response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return response;
    }


    /**
     * @Description:代理数据处理动作发送
     * @author:oygy
     * @time:2017年1月11日 下午2:45:22
     */
    private void instructSend() {
        try {
            Instruct instruct = new Instruct();
            instruct.setKey(InstructState.SMSWORDSHIELDING_SUCESS);
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(instruct);
            RedisUtil.publish(InstructState.AB, jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
