package com.dzd.phonebook.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dzd.base.util.SessionUtils;
import com.dzd.phonebook.entity.SysUser;
import com.google.common.collect.Lists;
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

import com.alibaba.fastjson.JSONException;
import com.dzd.base.util.MethodUtil;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.District;
import com.dzd.phonebook.entity.SysMenuBtn;
import com.dzd.phonebook.service.ChannelService;
import com.dzd.phonebook.service.Instruct;
import com.dzd.phonebook.service.SysMenuBtnService;
import com.dzd.phonebook.service.SysRoleRelService;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.InstructState;
import com.dzd.phonebook.util.JspResponseBean;
import com.dzd.phonebook.util.RedisUtil;
import com.dzd.phonebook.util.SmsAisle;
import com.dzd.phonebook.util.SmsAisleGroup;
import com.dzd.phonebook.util.SmsAisleGroupHasSmsAisle;
import com.dzd.phonebook.util.SmsAisleGroupType;
import com.dzd.phonebook.util.SmsAisleSource;
import com.dzd.phonebook.util.SmsShieldWord;
import com.dzd.phonebook.util.WebRequestParameters;
import com.github.pagehelper.Page;

import net.sf.json.JSONObject;

/**
 * @Description:通道Controller
 * @author:oygy
 * @time:2017年1月3日 下午3:06:04
 */
@Controller
@RequestMapping("/channel")
public class ChannelController extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(ChannelController.class);

    @Autowired
    private SysMenuBtnService sysMenuBtnService;

    @Autowired
    private SysRoleRelService sysRoleRelService;

    @Autowired
    private ChannelService channelService;

    /**
     * 访问通道页面
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
        List<SmsAisleGroupType> list = channelService.querySmsAisleGroupType();
        List<District> smsRegionList = channelService.queryDistrict();

        List<SmsAisleSource> sasList = channelService.queryAisleSourceList();

        List<SmsShieldWord> swList = channelService.querySmsShieldWordByType(2); // 所属类型（1：通道组，
        // 2：通道
        // ，3：平台）

        model.addAttribute("sasList", sasList);
        model.addAttribute("swList", swList);
        model.addAttribute("typeList", list);
        model.addAttribute("smsRegionList", smsRegionList);
        return "channel/channellist";
    }

    /**
     * @Description:查询通道信息列表
     * @author:oygy
     * @time:2016年12月31日 下午2:01:34
     */
    @RequestMapping(value = "/channelList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse channelList(HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");
            Object state = data.get("state");
            Object email = data.get("name");

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
                sortMap.put("state", state.toString());
            }

            if (email != null && !StringUtil.isEmpty(email.toString())) {
                sortMap.put("name", email.toString());
            }

            // 排序
            if (parameters.getSort() != null && parameters.getOrder() != null) {
                sortMap.put("sortVal", "order by " + parameters.getSort() + " " + parameters.getOrder());
            } else {
                sortMap.put("sortVal", "order by createTime desc");
            }
            dzdPageParam.setCondition(sortMap);
            SysUser sysUser = SessionUtils.getUser(request);
            List<SysMenuBtn> sysMenuBtns = null;
            if (menuId != null) {
                if (sysUser.getSuperAdmin() == 1) {// 管理员查询所有菜单按钮
                    sysMenuBtns = sysMenuBtnService.queryByMenuid(Integer.parseInt(menuId.toString()));
                } else {// 其余角色查询配置好的按钮
                    sysMenuBtns = sysMenuBtnService.queryMenuListByRole(Integer.parseInt(menuId.toString()), sysUser.getId());
                }
            }
            SmsAisle smsAisle = new SmsAisle();

            Page<SmsAisle> dataList = channelService.querySmsAisleList(dzdPageParam);
            dzdPageResponse.setData(sysMenuBtns);
            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsAisle smsAisles : dataList.getResult()) {
                    smsAisles.setSysMenuBtns(sysMenuBtns);
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
     * 查询所有通道
     *
     * @param request
     * @return
     */
    @RequestMapping("queryAisleList")
    @ResponseBody
    public DzdResponse queryAisleList(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            List<SmsAisle> smsAisleList = channelService.querySmsAisleList();
            dzdResponse.setData(smsAisleList);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dzdResponse;
    }


    /**
     * 通道新增或修改
     *
     * @param request
     * @return
     */
    @MethodDescription("新增通道或修改通道")
    @RequestMapping(value = "/from/merge", method = RequestMethod.POST)
    public String merge(HttpServletRequest request, HttpServletResponse response, Model model) {
        Object menuId = request.getParameter("menuId");
        try {
            SmsAisle smsAisle = new SmsAisle(); // 通道

            Object id = request.getParameter("id");
            Object name = request.getParameter("name"); // 客户姓名
            Object state = request.getParameter("state"); // 状态
            Object aisleType = request.getParameter("aisleType"); // 通道类型
            Object signatureState = request.getParameter("signatureState"); // 签名类型（前置OR后置）
            Object maxNum = request.getParameter("maxNum"); // 每日上限数
            Object startCount = request.getParameter("startCount"); // 起发量
            Object singleNum = request.getParameter("singleNum"); // 单包上限
            Object shieldingField = request.getParameter("shieldingField"); // 屏蔽词
            Object smsAisleKey = request.getParameter("smsAisleKey"); // 屏蔽词

            Object comment = request.getParameter("awardMoney"); // 描述
            Object smsRegion = request.getParameter("smsRegion"); // 可选省份

            Object money = request.getParameter("money");


            Object mobileSate = "1"; // 移动是否1支持
            Object unicomSate = "1"; // 联通是否1支持
            Object telecomState = "1"; // 电信是否1支持


            Object succeedBilling = request.getParameter("succeedBilling"); // 成功计费（计费模式）0：不支持，1：支持
            Object failureBilling = request.getParameter("failureBilling"); // 失败计费（计费模式）0：不支持，1：支持
            Object unknownBilling = request.getParameter("unknownBilling"); // 无知计费（计费模式）0：不支持，1：支持

            Object optionsKey = request.getParameterValues("optionsKey"); // optionsKey
            // json参数名称
            Object options = request.getParameterValues("options"); // options
            // json参数值
            Object extra = request.getParameter("extra"); // 得到拼接后的json参数

            String optionJeson = "";
            if (optionsKey != null && options != null) {
                String[] optionsKeyArr = (String[]) optionsKey;
                String[] optionsArr = (String[]) options;
                for (int i = 0; i < optionsKeyArr.length; i++) {
                    if (i == 0) {
                        optionJeson += "{";
                    }
                    if (optionsKeyArr[i] != null && !"".equals(optionsKeyArr[i])) {
                        optionJeson += "\"" + optionsKeyArr[i] + "\":\"" + optionsArr[i] + "\"";
                    }
                    if (i < optionsKeyArr.length - 1) {
                        optionJeson += ",";
                    }
                    if (i == optionsKeyArr.length - 1) {
                        optionJeson += "}";
                    }
                }

            }
            System.out.println("-------------------------optionJeson>" + optionJeson);
            smsAisle.setOptionValue(optionJeson);

            if (id != null && !id.toString().isEmpty()) {
                smsAisle.setId(Integer.parseInt(id.toString()));
            }
            if (name != null && !name.toString().isEmpty()) {
                smsAisle.setName(name.toString());
            }
            if (state != null && !state.toString().isEmpty()) {
                smsAisle.setState(Integer.parseInt(state.toString()));
            }
            if (aisleType != null && !aisleType.toString().isEmpty()) {
                smsAisle.setSmsAisleTypeId(Integer.parseInt(aisleType.toString()));
            }
            if (signatureState != null && !signatureState.toString().isEmpty()) {
                smsAisle.setSignatureState(Integer.parseInt(signatureState.toString()));
            }
            if (maxNum != null && !maxNum.toString().isEmpty()) {
                smsAisle.setMaxNum(Integer.parseInt(maxNum.toString()));
            }
            if (maxNum != null && !maxNum.toString().isEmpty()) {
                smsAisle.setMaxNum(Integer.parseInt(maxNum.toString()));
            }
            if (singleNum != null && !singleNum.toString().isEmpty()) {
                smsAisle.setSingleNum(Integer.parseInt(singleNum.toString()));
            }
            if (shieldingField != null && !shieldingField.toString().isEmpty()) {
                smsAisle.setShieldingFieldId(Integer.parseInt(shieldingField.toString()));
            }
            if (smsAisleKey != null && !smsAisleKey.toString().isEmpty()) {
                smsAisle.setClassName(smsAisleKey.toString());
            }
            if (comment != null && !comment.toString().isEmpty()) {
                smsAisle.setComment(comment.toString());
            }
            if (smsRegion != null && !smsRegion.toString().isEmpty()) {
                smsAisle.setSmsRegionId(Integer.parseInt(smsRegion.toString()));
            }
            if (mobileSate != null && !mobileSate.toString().isEmpty()) {
                smsAisle.setMobileSate(Integer.parseInt(mobileSate.toString()));
            } else {
                smsAisle.setMobileSate(0);
            }
            if (unicomSate != null && !unicomSate.toString().isEmpty()) {
                smsAisle.setUnicomSate(Integer.parseInt(unicomSate.toString()));
            } else {
                smsAisle.setUnicomSate(0);
            }
            if (telecomState != null && !telecomState.toString().isEmpty()) {
                smsAisle.setTelecomState(Integer.parseInt(telecomState.toString()));
            } else {
                smsAisle.setTelecomState(0);
            }


            if (succeedBilling != null && !succeedBilling.toString().isEmpty()) {
                smsAisle.setSucceedBilling(Integer.parseInt(succeedBilling.toString()));
            } else {
                smsAisle.setSucceedBilling(0);
            }

            if (failureBilling != null && !failureBilling.toString().isEmpty()) {
                smsAisle.setFailureBilling(Integer.parseInt(failureBilling.toString()));
            } else {
                smsAisle.setFailureBilling(0);
            }

            if (unknownBilling != null && !unknownBilling.toString().isEmpty()) {
                smsAisle.setUnknownBilling(Integer.parseInt(unknownBilling.toString()));
            } else {
                smsAisle.setUnknownBilling(0);
            }

            if (extra != null && !extra.toString().isEmpty()) {
                String extraMd5 = MethodUtil.MD5(extra.toString());
                smsAisle.setExtra(extraMd5);
            }

            if (startCount != null && !startCount.toString().isEmpty()) {
                smsAisle.setStartCount(Integer.parseInt(startCount.toString()));
            }

            if (money != null && !money.toString().isEmpty()) {
                smsAisle.setMoney(Float.parseFloat(money.toString()));
            }

            if (smsAisle.getId() == null) {
                log.info("-----------------》新增通道");
                channelService.saveSmsAisle(smsAisle);
                instructSend(InstructState.ADDSMSAISLE_SUCESS, smsAisle.getId()); // 发送动作指令到redis
            } else {
                channelService.updateSmsAisle(smsAisle);
                instructSend(InstructState.UPDATESMSAISLE_SUCESS, smsAisle.getId()); // 发送动作指令到redis
            }
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return "redirect:/channel/listview.do?id=" + menuId;
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
            SmsAisle smsAisle = (SmsAisle) channelService.queryById(Integer.parseInt(id.toString()));
            String jsonStriong = smsAisle.getOptionValue();
            Map jsonMap = new HashMap<Object, Object>();
            if (jsonStriong != null && !"".equals(jsonStriong)) {
                JSONObject obj = jsonObject(jsonStriong);
                jsonMap = obj;
            }
            JspResponseBean jspResponseBean = new JspResponseBean();
            jspResponseBean.setData(smsAisle);
            jspResponseBean.setDataList(jsonMap);
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
     * @Description:删除通道信息
     * @author:liuyc
     * @time:2017年1月4日 下午4:39:53
     */
    @MethodDescription("删除通道信息")
    @RequestMapping(value = "/from/delete", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public DzdResponse delete(HttpServletRequest request, Model model) {
        DzdResponse response = new DzdResponse();
        try {
            String id = request.getParameter("id");
            log.info("--------->根据ID" + id + "删除通道信息");
            channelService.deleteAisleHasGroupByaid(Integer.parseInt(id));
            channelService.delete(Integer.parseInt(id));

            instructSend(InstructState.DELETESMSAISLE_SUCESS, Integer.parseInt(id)); // 发送动作指令到redis
            response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            log.info("--------->删除通道信息失败");
            response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * @Description:根据通道源标识查询通道源的json数据
     * @author:oygy
     * @time:2017年1月3日 上午11:55:53
     */
    @RequestMapping(value = "/queryPluginConfig")
    @ResponseBody
    public DzdResponse queryPluginConfig(HttpServletRequest request) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            Object className = request.getParameter("className");
            if (className == null) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdPageResponse;
            }
            String config = channelService.queryPluginConfig(className.toString());
            // JSONArray data = JSONArray.fromObject(config);
            JSONObject obj = jsonObject(config);
            Map map = obj;
            JspResponseBean jspResponseBean = new JspResponseBean();
            jspResponseBean.setData(map);
            dzdPageResponse.setData(jspResponseBean);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            log.error(null, e);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }













	/*------------------------------------------------通道组功能---------------------------------------------------------*/

    /**
     * @Description:访问通道组页面
     * @author:oygy
     * @time:2017年1月4日 下午4:43:58
     */
    @RequestMapping("/grouplistview")
    public String groupList(HttpServletRequest request, Model model) throws Exception {
        Object menuId = request.getParameter("id");
        model.addAttribute("menuId", menuId);
        List<SmsAisleGroupType> list = channelService.querySmsAisleGroupType();
        List<District> smsRegionList = channelService.queryDistrict();

        model.addAttribute("typeList", list);
        model.addAttribute("smsRegionList", smsRegionList);
        return "channel/channelGrouplist";
    }

    /**
     * @Description:查询通道组信息列表
     * @author:oygy
     * @time:2016年12月31日 下午2:01:34
     */
    @RequestMapping(value = "/channelGroupList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse channelGroupList(HttpServletRequest request, HttpServletResponse response,
                                        @RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = request.getParameter("menuId");
            Object state = data.get("state");
            Object email = data.get("name");

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
                sortMap.put("state", state.toString());
            }

            if (email != null && !StringUtil.isEmpty(email.toString())) {
                sortMap.put("name", email.toString());
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
            SmsAisle smsAisle = new SmsAisle();

            Page<SmsAisleGroup> dataList = channelService.querySmsAisleGroupList(dzdPageParam);
            dzdPageResponse.setData(sysMenuBtns);
            if (!CollectionUtils.isEmpty(dataList)) {
                for (SmsAisleGroup smsAisles : dataList.getResult()) {
                    smsAisles.setSysMenuBtns(sysMenuBtns);
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
     * @Description:根据通道类型查询通道信息
     * @author:oygy
     * @time:2017年1月3日 上午11:55:53
     */
    @RequestMapping(value = "/formAisleByaisle")
    @ResponseBody
    public DzdResponse formAisleByaisle(HttpServletRequest request) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            Object id = request.getParameter("id");
            if (id == null) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdPageResponse;
            }
            List<SmsAisle> smsAisle = channelService.querySmsAisleListById(Integer.parseInt(id.toString()));

            JspResponseBean jspResponseBean = new JspResponseBean();
            jspResponseBean.setData(smsAisle);
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
     * 通道组新增或修改
     *
     * @param request
     * @return
     */
    @MethodDescription("新增通道组或修改通道组")
    @RequestMapping(value = "/from/groupmerge", method = RequestMethod.POST)
    public String groupmerge(HttpServletRequest request, HttpServletResponse response, Model model) {
        Object menuId = request.getParameter("menuId");
        try {
            SmsAisleGroup smsAisleGroup = new SmsAisleGroup(); // 通道组

            Object id = request.getParameter("id");
            Object name = request.getParameter("name"); // 通道组姓名
            Object state = request.getParameter("state"); // 状态
            Object aisleType = request.getParameter("aisleType"); // 通道组类型

            Object statetype = request.getParameterValues("statetype"); // 选择的通道(数组)
            Object operatorId = request.getParameterValues("operatorId"); // 最小发送量

            Object comment = request.getParameter("awardMoney"); // 描述
            Object succeedBilling = request.getParameter("succeedBilling"); // 成功计费（计费模式）0：不支持，1：支持
            Object failureBilling = request.getParameter("failureBilling"); // 失败计费（计费模式）0：不支持，1：支持
            Object unknownBilling = request.getParameter("unknownBilling"); // 无知计费（计费模式）0：不支持，1：支持

            Object oneIntervalPrice = request.getParameter("oneIntervalPrice"); // 第一区间价格
            Object oneIntervalStart = request.getParameter("oneIntervalStart"); // 第一区间开始数量
            Object oneIntervalEnd = request.getParameter("oneIntervalEnd"); // 第一区间结束数量

            Object twoIntervalPrice = request.getParameter("twoIntervalPrice"); // 第二区间价格
            Object twoIntervalStart = request.getParameter("twoIntervalStart"); // 第二区间开始数量
            Object twoIntervalEnd = request.getParameter("twoIntervalEnd"); // 第二区间结束数量

            Object threeIntervalPrice = request.getParameter("threeIntervalPrice"); // 第三区间价格
            Object threeIntervalStart = request.getParameter("threeIntervalStart"); // 第三区间开始数量
            Object threeIntervalEnd = request.getParameter("threeIntervalEnd"); // 第三区间结束数量


            Object smsLength = request.getParameter("smsLength");// 长短信字数
            Object dredgeAM = request.getParameter("dredgeAM");// 开通时段 上午
            Object dredgePM = request.getParameter("dredgePM");// 开通时段 下午
            Object unregTypeId = request.getParameter("unregTypeId");// 开启、关闭退订格式
            Object shieldingFieldId = request.getParameter("shieldingFieldId");// 屏蔽词外键
            Object notice = request.getParameter("notice");// 公告
            Object hint = request.getParameter("hint");// 提示
            Object signature = request.getParameter("signature");// 签名

            if (dredgeAM != null && !dredgeAM.toString().isEmpty()) {
                smsAisleGroup.setDredgeAM(dredgeAM.toString());
                smsAisleGroup.setDredgePM(dredgePM.toString());
            }

            if (smsLength != null && !smsLength.toString().isEmpty()) {
                smsAisleGroup.setSmsLength(Integer.parseInt(smsLength.toString()));
            }
            if (unregTypeId != null && !unregTypeId.toString().isEmpty()) {
                smsAisleGroup.setUnregTypeId(Integer.parseInt(unregTypeId.toString()));
            }
            if (shieldingFieldId != null && !shieldingFieldId.toString().isEmpty()) {
                smsAisleGroup.setShieldingFieldId(shieldingFieldId.toString());
            }
            if (notice != null && !notice.toString().isEmpty()) {
                smsAisleGroup.setNotice(notice.toString());
            }
            if (hint != null && !hint.toString().isEmpty()) {
                smsAisleGroup.setHint(hint.toString());
            }
            if (signature != null && !signature.toString().isEmpty()) {
                smsAisleGroup.setSignature(signature.toString());
            }


            if (id != null && !id.toString().isEmpty()) {
                smsAisleGroup.setId(Integer.parseInt(id.toString()));
            }

            if (name != null && !name.toString().isEmpty()) {
                smsAisleGroup.setName(name.toString());
            }
            if (state != null && !state.toString().isEmpty()) {
                smsAisleGroup.setState(Integer.parseInt(state.toString()));
            }
            if (aisleType != null && !aisleType.toString().isEmpty()) {
                smsAisleGroup.setTid(Integer.parseInt(aisleType.toString()));
            }
            if (comment != null && !comment.toString().isEmpty()) {
                smsAisleGroup.setDescribes(comment.toString());
            }
            if (oneIntervalPrice != null && !oneIntervalPrice.toString().isEmpty()) {
                smsAisleGroup.setOneIntervalPrice(Float.parseFloat(oneIntervalPrice.toString()));
            }
            if (oneIntervalStart != null && !oneIntervalStart.toString().isEmpty()) {
                smsAisleGroup.setOneIntervalStart(Integer.parseInt(oneIntervalStart.toString()));
            }
            if (oneIntervalEnd != null && !oneIntervalEnd.toString().isEmpty()) {
                smsAisleGroup.setOneIntervalEnd(Integer.parseInt(oneIntervalEnd.toString()));
            }

            if (twoIntervalPrice != null && !twoIntervalPrice.toString().isEmpty()) {
                smsAisleGroup.setTwoIntervalPrice(Float.parseFloat(twoIntervalPrice.toString()));
            }
            if (twoIntervalStart != null && !twoIntervalStart.toString().isEmpty()) {
                smsAisleGroup.setTwoIntervalStart(Integer.parseInt(twoIntervalStart.toString()));
            }
            if (twoIntervalEnd != null && !twoIntervalEnd.toString().isEmpty()) {
                smsAisleGroup.setTwoIntervalEnd(Integer.parseInt(twoIntervalEnd.toString()));
            }

            if (threeIntervalPrice != null && !threeIntervalPrice.toString().isEmpty()) {
                smsAisleGroup.setThreeIntervalPrice(Float.parseFloat(threeIntervalPrice.toString()));
            }
            if (threeIntervalStart != null && !threeIntervalStart.toString().isEmpty()) {
                smsAisleGroup.setThreeIntervalStart(Integer.parseInt(threeIntervalStart.toString()));
            }
            if (threeIntervalEnd != null && !threeIntervalEnd.toString().isEmpty()) {
                smsAisleGroup.setThreeIntervalEnd(Integer.parseInt(threeIntervalEnd.toString()));
            }

            if (succeedBilling != null && !succeedBilling.toString().isEmpty()) {
                smsAisleGroup.setSucceedBilling(Integer.parseInt(succeedBilling.toString()));
            } else {
                smsAisleGroup.setSucceedBilling(0);
            }

            if (failureBilling != null && !failureBilling.toString().isEmpty()) {
                smsAisleGroup.setFailureBilling(Integer.parseInt(failureBilling.toString()));
            } else {
                smsAisleGroup.setFailureBilling(0);
            }

            if (unknownBilling != null && !unknownBilling.toString().isEmpty()) {
                smsAisleGroup.setUnknownBilling(Integer.parseInt(unknownBilling.toString()));
            } else {
                smsAisleGroup.setUnknownBilling(0);
            }
            smsAisleGroup.setType(0);


            // 保存多选通道信息
            List<SmsAisleGroupHasSmsAisle> aislehasGrouplist = new ArrayList<SmsAisleGroupHasSmsAisle>();
            if (operatorId != null && !operatorId.toString().isEmpty()) {
                String[] operatorIdArr = (String[]) operatorId; // 运营商id
                String[] asileArr = getAisleIdStr(statetype); // 通道ID

                for (int i = 0; i < operatorIdArr.length; i++) {
                    if (operatorIdArr[i] != null && !"".equals(operatorIdArr[i])) {
                        SmsAisleGroupHasSmsAisle sagha = new SmsAisleGroupHasSmsAisle();
                        sagha.setOperatorId(Integer.parseInt(operatorIdArr[i]));

                        if (asileArr[i] != null && !"".equals(asileArr[i])) {
                            sagha.setSmsAisleId(Integer.parseInt(asileArr[i]));
                        }
                        aislehasGrouplist.add(sagha);
                    }
                }
            }


            if (smsAisleGroup.getId() == null) {
                log.info("-----------------》新增通道组");
                channelService.saveSmsAislegroup(smsAisleGroup);
                // 为通道组添加多个通道
                for (SmsAisleGroupHasSmsAisle smsAisleGroupHasSmsAisle : aislehasGrouplist) {
                    smsAisleGroupHasSmsAisle.setSmsAisleGroupId(smsAisleGroup.getId());
                    channelService.saveSmsAislegroupHasAisle(smsAisleGroupHasSmsAisle);
                }
                instructSendGroup(InstructState.ADDSMSAISLEGROUP_SUCESS, smsAisleGroup.getId(), null); // 发送动作指令到redis
            } else {
                log.info("-----------------》修改通道组");
                channelService.updateSmsAislegroup(smsAisleGroup); // 修改
                channelService.deleteSmsAislegroupHasAisle(smsAisleGroup.getId());
                for (SmsAisleGroupHasSmsAisle smsAisleGroupHasSmsAisle : aislehasGrouplist) {
                    smsAisleGroupHasSmsAisle.setSmsAisleGroupId(smsAisleGroup.getId());
                    channelService.saveSmsAislegroupHasAisle(smsAisleGroupHasSmsAisle);
                }
                instructSendGroup(InstructState.UPDATESMSAISLEGROUP_SUCESS, smsAisleGroup.getId(), null); // 发送动作指令到redis
            }
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        return "redirect:/channel/grouplistview.do?id=" + menuId;
    }

    /**
     * @Description:根据ID查询通道组信息
     * @author:oygy
     * @time:2017年1月3日 上午11:55:53
     */
    @RequestMapping(value = "/formGroupEdit")
    @ResponseBody
    public DzdResponse groupedit(HttpServletRequest request) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            Object id = request.getParameter("id");
            if (id == null) {
                dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdPageResponse;
            }
            SmsAisleGroup smsAisle = (SmsAisleGroup) channelService
                    .querySmsAisleGroupById(Integer.parseInt(id.toString()));
            List<SmsAisleGroupHasSmsAisle> aisleHasGrouplist = channelService
                    .queryAisleHasGroupListById(Integer.parseInt(id.toString()));
            smsAisle.setAslist(aisleHasGrouplist);
            JspResponseBean jspResponseBean = new JspResponseBean();
            jspResponseBean.setData(smsAisle);
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
     * @Description:删除通道组信息
     * @author:liuyc
     * @time:2017年1月4日 下午4:39:53
     */
    @MethodDescription("删除通道组")
    @RequestMapping(value = "/from/deleteGroup", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public DzdResponse deleteGroup(HttpServletRequest request, Model model) {
        DzdResponse response = new DzdResponse();
        try {
            String id = request.getParameter("id");
            log.info("--------->根据ID" + id + "删除通道信息");
            String idString = channelService.querySmsAisleGrouphasUserById(Integer.parseInt(id));
            if (idString == null || "".equals(idString)) {
                idString = ",";
            }

            channelService.deleteSmsAislegroupHasAisle(Integer.parseInt(id));
            channelService.deleteUserAisleGroup(Integer.parseInt(id));
            channelService.deleteAisleGroup(Integer.parseInt(id));

            instructSendGroup(InstructState.DELETESMSAISLEGROUP_SUCESS, Integer.parseInt(id), idString); // 发送动作指令到redis
            response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            log.info("--------->删除通道信息失败");
            response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * @Description:根据通道组类型查询默认通道组
     * @author:liuyc
     * @time:2017年1月4日 下午4:39:53
     */
    @RequestMapping(value = "/queryAisleGroupDft", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DzdResponse queryAisleGroupDft(HttpServletRequest request, @RequestBody Map<String, Object> data) {
        DzdResponse response = new DzdResponse();
        try {
            String aisleType = request.getParameter("id");
            Object aisleId = data.get("aisleId");
            if (aisleType == null || "".equals(aisleType)) {
                response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return response;
            }
            Integer num = 0;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("aisleType", aisleType);
            if (aisleId != null && !aisleId.equals("")) {
                map.put("aisleId", Integer.parseInt(aisleId.toString()));

            } else {
                map.put("aisleId", -1);
            }
            num = channelService.queryAisleGroupDft(map);
            if (num > 0) {
                response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            } else {
                response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            }
        } catch (Exception e) {
            response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * @Description:通道数据处理动作发送
     * @author:oygy
     * @time:2017年1月11日 下午2:45:22
     */

    private void instructSend(String keys, Integer smsAisleId) {
        Instruct instruct = new Instruct();
        instruct.setKey(keys);
        instruct.setSmsAisleId(smsAisleId + "");
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
     * @Description:通道组数据处理动作发送
     * @author:oygy
     * @time:2017年1月11日 下午2:45:22
     */
    private void instructSendGroup(String keys, Integer smsAisleGroupId, String idString) {
        Instruct instruct = new Instruct();
        instruct.setKey(keys);
        instruct.setSmsAisleGroupId(smsAisleGroupId + "");
        instruct.setUserIdString(idString);
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
     * json转换成Object
     *
     * @Description:
     * @author:oygy
     * @time:2016年9月2日 下午3:34:50
     */
    private JSONObject jsonObject(String json) {
        String jsonMessage = json;
        JSONObject myJsonObject = new JSONObject();
        try {
            // 将字符串转换成jsonObject对象
            myJsonObject = JSONObject.fromObject(jsonMessage);
        } catch (JSONException e) {
            e.getMessage();
        }
        return myJsonObject;
    }


    private String[] getAisleIdStr(Object statetype) {
        String[] asileArr = (String[]) statetype; // 通道ID
        String st = "";
        List<String> strlist = Lists.newArrayList();
        for (String strings : asileArr) {
            strlist.add(strings);
        }
        for (String strs : strlist) {
            if (!strs.equals("")) {
                st += strs + ",";
            }
        }
        String[] aisleStrArr = st.split(",");
        return aisleStrArr;
    }
}