package com.dzd.phonebook.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.OperatorSectionNo;
import com.dzd.phonebook.entity.SmsFileConfig;
import com.dzd.phonebook.entity.SmsFilterNumberRecord;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.page.MobileCheckUtil;
import com.dzd.phonebook.service.SmsFileConfigService;
import com.dzd.phonebook.service.SmsFilterNumberRecordService;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.FileUploadUtil;
import com.dzd.phonebook.util.SmsSendUtil;
import com.dzd.phonebook.util.WebRequestParameters;
import com.dzd.sms.api.service.SmsFilterOrderBusiness;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsUser;
import com.dzd.sms.util.DistinguishOperator;
import com.github.pagehelper.Page;

/**
 * Created by dzd-technology01 on 2017/5/17.
 */
@Controller
@RequestMapping("/smsFilter")
public class SmsFilterNumberController extends WebBaseController{

    public static final Logger log = LoggerFactory.getLogger(SmsFilterNumberController.class);
    
    private static final int PAGECALC = 20;

    @SuppressWarnings("rawtypes")
	@Autowired
    private SmsFilterNumberRecordService smsFilterNumberRecordService;
    
    @Autowired
    private SmsFileConfigService<SmsFileConfig> smsFileConfigService;
    
    Map<String,Object> dateMap = new HashMap<String, Object>();

    public Map<String, Object> getDateMap()
	{
		return dateMap;
	}

	public void setDateMap(Map<String, Object> dateMap)
	{
		this.dateMap = dateMap;
	}

	@RequestMapping("/listView")
    public String pageView(HttpServletRequest request,Model model){
        Object id = request.getParameter("id");
        model.addAttribute("id", id);
        return "app/userSendMassSms/filter";
    }
    
	@RequestMapping("/distinguishoperator")
	public String distinguishOperator(HttpServletRequest request, Model model)
	{
		Object id = request.getParameter("id");
		model.addAttribute("id", id);
		return "systemUser/distinguishOperator";
	}
	
	@RequestMapping("/operatorsectionno")
	public String operatorSectionNo(HttpServletRequest request, Model model)
	{
		Object id = request.getParameter("id");
		model.addAttribute("id", id);
		return "systemUser/operatorSectionNo";
	}
	
	@MethodDescription("运营商号区分")
	@RequestMapping(value = "/filterDistinguishOperator")
	@ResponseBody
	public DzdResponse filterDistinguishOperator(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		DzdResponse dzdResponse = new DzdResponse();
		Map<String, Object> operatorMap = new HashMap<String, Object>();
		
		String uuid = request.getParameter(Define.REQUESTPARAMETER.FORWARDUUID);
		
		int mobileCurrentPage = Integer.valueOf(request.getParameter(Define.DISTINGUISHOPERATOR.MOBILECURRENTPAGE));
		int unicomCurrentPage = Integer.valueOf(request.getParameter(Define.DISTINGUISHOPERATOR.UNICOMCURRENTPAGE));
		int telecomCurrentPage = Integer.valueOf(request.getParameter(Define.DISTINGUISHOPERATOR.TELECOMCURRENTPAGE));
		int invalidCurrentPage = Integer.valueOf(request.getParameter(Define.DISTINGUISHOPERATOR.INVALIDCURRENTPAGE));
		
		
		List<SmsFileConfig> configList = smsFileConfigService.querySmsFileConfigList(uuid, null);
		
		if (CollectionUtils.isEmpty(configList))
		{
			return dzdResponse;
		}
		
		List<String> mobiles = SmsSendUtil.getPhoneListByConfig(configList);
		
		StringBuffer fileName = new StringBuffer();
		for ( SmsFileConfig smsFileConfig : configList )
		{
			fileName.append(smsFileConfig.getFileName() + "、");
		}
		operatorMap.put(Define.STATICAL.FILENAME, fileName.substring(0, fileName.length()-1));

		Map<String, List<String>> mobileAssort = MobileCheckUtil.mobileAssort(mobiles);

		List<String> invalidList = mobileAssort.get(Define.PHONEKEY.INVALID);
		List<String> validList = mobileAssort.get(Define.PHONEKEY.VALID);
		List<String> duplicateList = mobileAssort.get(Define.PHONEKEY.DUPLICATE);

		// 构造出参数据
		operatorMap = DistinguishOperator.construcFilterRecordMap(invalidList, validList, operatorMap);

		// 总页数传递前台
		setTotalPage(request, operatorMap);
				
		// 过滤记录入库
		insertFilterRecord(request, duplicateList, operatorMap);
		
		construcPhonesByPage(operatorMap, mobileCurrentPage, unicomCurrentPage, telecomCurrentPage,
		        invalidCurrentPage);

		dzdResponse.setData(operatorMap);
		dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
		return dzdResponse;
	}

	@SuppressWarnings("unchecked")
	private void construcPhonesByPage(Map<String, Object> operatorMap, int mobileCurrentPage,
	        int unicomCurrentPage, int telecomCurrentPage, int invalidCurrentPage)
	{
		List<String> beformobiles = (List<String>) operatorMap.get(Define.DISTINGUISHOPERATOR.MOBILEOPERATOR);
		List<String> befortelecoms = (List<String>) operatorMap.get(Define.DISTINGUISHOPERATOR.TELECOMOPERATOR);
		List<String> beforunicoms = (List<String>) operatorMap.get(Define.DISTINGUISHOPERATOR.UNICOMOPERATOR);
		List<String> beforinvalidList = (List<String>) operatorMap.get(Define.PHONEKEY.INVALID);
		
		int startLength;
		int endLength;
		if ( !CollectionUtils.isEmpty(beformobiles) )
		{
			startLength = (mobileCurrentPage - 1) * 20;
			endLength = getEndLength(beformobiles, startLength);
			operatorMap.put(Define.DISTINGUISHOPERATOR.MOBILEOPERATOR,
			        beformobiles.subList(startLength, endLength));
		}
		if ( !CollectionUtils.isEmpty(befortelecoms) )
		{
			startLength = (telecomCurrentPage - 1) * 20;
			endLength = getEndLength(befortelecoms, startLength);
			operatorMap.put(Define.DISTINGUISHOPERATOR.TELECOMOPERATOR,
			        befortelecoms.subList(startLength, endLength));
		}
		if ( !CollectionUtils.isEmpty(beforunicoms) )
		{
			startLength = (unicomCurrentPage - 1) * 20;
			endLength = getEndLength(beforunicoms, startLength);
			operatorMap.put(Define.DISTINGUISHOPERATOR.UNICOMOPERATOR,
			        beforunicoms.subList(startLength, endLength));
		}
		if ( !CollectionUtils.isEmpty(beforinvalidList) )
		{
			startLength = (invalidCurrentPage - 1) * 20;
			endLength = getEndLength(beforinvalidList, startLength);
			operatorMap.put(Define.PHONEKEY.INVALID,
			        beforinvalidList.subList(startLength, endLength));
		}
	}

	private int getEndLength(List<String> beformobiles, int startLength)
	{
		return ((startLength + 20 - 1) > (beformobiles.size() - 1)
		        || (startLength + 20 - 1) == (beformobiles.size() - 1)) ? beformobiles.size()
		                : startLength + 20;
	}
	
	@MethodDescription("运营商号区分--号码文件入库")
	@RequestMapping(value = "/saveuploadfile")
	@ResponseBody
	public DzdResponse saveUploadFileForSendMessage(@RequestParam final MultipartFile[] uploadFile, final HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		DzdResponse dzdResponse = new DzdResponse();

		String uuid = request.getParameter(Define.REQUESTPARAMETER.FORWARDUUID);

		Map<String, Object> operatorMap = new HashMap<String, Object>();
		// 校验文件格式
		uploadFileCheck(uploadFile, operatorMap, dzdResponse);

		if ( ErrorCodeTemplate.CODE_FAIL.equals(dzdResponse.getRetCode()) )
		{
			return dzdResponse;
		}
		List<String> mobileList = getImputMobiles(uploadFile);
		
		if (CollectionUtils.isEmpty(mobileList))
		{
			dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
			return dzdResponse;
		}
		
		StringBuffer strb = new StringBuffer();
		for(String phone : mobileList){
			strb.append(phone + "\n");
		}
		String phones = strb.toString().substring(0, strb.length() - 1);
		
		// 4. 上传文件到服务器
		String fileName = FileUploadUtil.saveFileInfo(new ByteArrayInputStream(phones.getBytes()),
		        new byte[phones.length()], operatorMap.get(Define.STATICAL.FILENAME).toString());

		SysUser user = SessionUtils.getUser(request);
		SmsUser smsUsers = SmsServerManager.I.getUserBySysId(Long.valueOf(user.getId()));
		Integer uid = user.getId();
		if ( smsUsers != null )
		{
			uid = smsUsers.getId().intValue();// 用户id
		}

		// 6. 保存文件信息到数据库
		SmsFileConfig config = new SmsFileConfig();
		config.setSms_uid(uid);
		config.setUuid(uuid);
		config.setFileName(fileName);
		config.setType(1);
		config.setPhone(
		        phones.indexOf("\n") == -1 ? phones : phones.substring(0, phones.indexOf("\n")));
		config.setPhoneSize(mobileList.size());
		try
		{
			smsFileConfigService.add(config);
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("====================》上传文件至服务器发生了异常，发送跳转失败：" + e.getMessage());
		}
		dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
		return dzdResponse;
	}
	
	@MethodDescription("运营商号段更新")
	@RequestMapping(value = "/updateoperatorsectionno")
	@ResponseBody
	public void updateOperatorSectionNo(HttpServletRequest request, HttpServletResponse response)
	{
		String mobile = request.getParameter(Define.DISTINGUISHOPERATOR.MOBILEOPERATOR);
		String unicom = request.getParameter(Define.DISTINGUISHOPERATOR.UNICOMOPERATOR);
		String telecom = request.getParameter(Define.DISTINGUISHOPERATOR.TELECOMOPERATOR);

		try
		{
			if ( !StringUtils.isEmpty(mobile) )
			{
				String[] mobiles = mobile.split(":");
				smsFilterNumberRecordService.updateOperatorSectionNo(mobiles[0], mobiles[1]);
			}
			if ( !StringUtils.isEmpty(unicom) )
			{
				String[] unicoms = unicom.split(":");
				smsFilterNumberRecordService.updateOperatorSectionNo(unicoms[0], unicoms[1]);
			}
			if ( !StringUtils.isEmpty(telecom) )
			{
				String[] telecoms = telecom.split(":");
				smsFilterNumberRecordService.updateOperatorSectionNo(telecoms[0], telecoms[1]);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("====================》号段信息更新失败：" + e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryoperatorsectionno")
	@ResponseBody
	public List<OperatorSectionNo> queryOperatorSectionNo(HttpServletRequest request, HttpServletResponse response)
	{
		List<OperatorSectionNo> operatorSectionNoList =	null;
		try
		{
			operatorSectionNoList = smsFilterNumberRecordService.queryOperatorSectionNo();
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("====================》号段信息更新失败：" + e.getMessage());
		}
		return operatorSectionNoList;
	}
	
	@SuppressWarnings("unchecked")
	@MethodDescription("运营商号区分--导出")
	@RequestMapping(value = "/exportfilternumber")
	@ResponseBody
	public void exportFilterNumber(HttpServletRequest request, HttpServletResponse response)
	{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dateMap = new HashMap<String, Object>();
		
		// 入参数据
		String uuid = request.getParameter(Define.REQUESTPARAMETER.FORWARDUUID);
		String mobileOperator = request.getParameter(Define.DISTINGUISHOPERATOR.MOBILEOPERATOR);
		String unicomOperator = request.getParameter(Define.DISTINGUISHOPERATOR.UNICOMOPERATOR);
		String telecomOperator = request.getParameter(Define.DISTINGUISHOPERATOR.TELECOMOPERATOR);
		String invalidOperator = request.getParameter(Define.DISTINGUISHOPERATOR.INVALIDOPERATOR);
		
		// 获取服务器号码文件
		List<SmsFileConfig> configList = smsFileConfigService.querySmsFileConfigList(uuid, null);
		
		// 获取文件号码信息
		List<String> mobileList = SmsSendUtil.getPhoneListByConfig(configList);
		
		dateMap.put(Define.STATICAL.FILENAME, configList.get(0).getFileName());

		// 进行一次过滤，获取有效以及无效号码
		Map<String, List<String>> mobileAssort = MobileCheckUtil.mobileAssort(mobileList);

		List<String> invalidList = mobileAssort.get(Define.PHONEKEY.INVALID);
		List<String> validList = mobileAssort.get(Define.PHONEKEY.VALID);

		// 进行二次过滤，获取运营商号码信息
		dateMap = DistinguishOperator.construcFilterRecordMap(invalidList, validList, dateMap);
		
		// 前台选择导出号码信息
		List<String> mobiles = "1".equals(mobileOperator)?(List<String>) dateMap.get(Define.DISTINGUISHOPERATOR.MOBILEOPERATOR):new ArrayList<String>();
		List<String> telecoms = "1".equals(telecomOperator)?(List<String>) dateMap.get(Define.DISTINGUISHOPERATOR.TELECOMOPERATOR):new ArrayList<String>();
		List<String> unicoms = "1".equals(unicomOperator)?(List<String>) dateMap.get(Define.DISTINGUISHOPERATOR.UNICOMOPERATOR):new ArrayList<String>();
		List<String> invalids = "1".equals(invalidOperator)?invalidList:new ArrayList<String>();

        int[] numbers = {invalids.size(),mobiles.size(),telecoms.size(),unicoms.size()};
		bubbleSort(numbers);
		
		Map<String, Object> tmpMap = null;
		int number = 0;
		for ( int i = 0; i < numbers[3]; i++ )
		{
			number = number++ + 1;
			tmpMap = new HashMap<String, Object>();
			tmpMap.put(Define.STATICAL.NUMBER, number);
			tmpMap.put(Define.DISTINGUISHOPERATOR.MOBILEOPERATOR, mobiles.size()>i?mobiles.get(i):null);
			tmpMap.put(Define.DISTINGUISHOPERATOR.UNICOMOPERATOR, unicoms.size()>i?unicoms.get(i):null);
			tmpMap.put(Define.DISTINGUISHOPERATOR.TELECOMOPERATOR, telecoms.size()>i?telecoms.get(i):null);
			tmpMap.put(Define.DISTINGUISHOPERATOR.INVALIDOPERATOR, invalids.size()>i?invalids.get(i):null);

			resultList.add(tmpMap);
		}
		
		dateMap.put(Define.DISTINGUISHOPERATOR.MOBILELENGTH,
				mobiles.size());
		dateMap.put(Define.DISTINGUISHOPERATOR.UNICOMLENGTH,
				unicoms.size());
		dateMap.put(Define.DISTINGUISHOPERATOR.TELECOMLENGTH,
				telecoms.size());
		dateMap.put(Define.DISTINGUISHOPERATOR.INVALIDLENGTH,
				invalids.size());
		
		SmsFilterOrderBusiness smsFilterOrderBusiness = new SmsFilterOrderBusiness();
		smsFilterOrderBusiness.orderExport(request, response, dateMap, resultList);
	}

	private void setTotalPage(HttpServletRequest request, Map<String, Object> operatorMap)
	{
		
		int mobileLength = Integer.valueOf(operatorMap.get(Define.DISTINGUISHOPERATOR.MOBILELENGTH).toString());
		int telecomLength = Integer.valueOf(operatorMap.get(Define.DISTINGUISHOPERATOR.TELECOMLENGTH).toString());
		int unicomLength = Integer.valueOf(operatorMap.get(Define.DISTINGUISHOPERATOR.UNICOMLENGTH).toString());
		int invalidLength = Integer.valueOf(operatorMap.get(Define.DISTINGUISHOPERATOR.INVALIDLENGTH).toString());
		
		operatorMap.put(Define.DISTINGUISHOPERATOR.MOBILETOTALPAGE,
		        mobileLength < PAGECALC ? 1
		                : mobileLength % PAGECALC == 0 ? mobileLength / PAGECALC
		                        : mobileLength / PAGECALC + 1);
		operatorMap.put(Define.DISTINGUISHOPERATOR.UNICOMTOTALPAGE,
		        unicomLength < PAGECALC ? 1
		                : unicomLength % PAGECALC == 0 ? unicomLength / PAGECALC
		                        : unicomLength / PAGECALC + 1);
		operatorMap.put(Define.DISTINGUISHOPERATOR.TELECOMTOTALPAGE,
		        telecomLength < PAGECALC ? 1
		                : telecomLength % PAGECALC == 0 ? telecomLength / PAGECALC
		                        : telecomLength / PAGECALC + 1);
		operatorMap.put(Define.DISTINGUISHOPERATOR.INVALIDTOTALPAGE,
		        invalidLength < PAGECALC ? 1
		                : invalidLength % PAGECALC == 0 ? invalidLength / PAGECALC
		                        : invalidLength / PAGECALC + 1);
	}

	// 冒泡排序
	public void bubbleSort(int[] numbers)
	{
		int temp = 0;
		int size = numbers.length;
		for ( int i = 0; i < size - 1; i++ )
		{
			for ( int j = 0; j < size - 1 - i; j++ )
			{
				if ( numbers[j] > numbers[j + 1] ) // 交换两数位置
				{
					temp = numbers[j];
					numbers[j] = numbers[j + 1];
					numbers[j + 1] = temp;
				}
			}
		}
	}
	
	/**
	 * 
	 * @Title: uploadFileCheck
	 * @Description: 校验文件格式
	 * @author:    hz-liang
	 * @param uploadFile
	 * @param dzdResponse  
	 * @return: void   
	 * @throws
	 */
	private void uploadFileCheck(final MultipartFile[] uploadFile, Map<String, Object> operatorMap,
	        DzdResponse dzdResponse)
	{
		// 1. 获取文件
        String fName = uploadFile[0].getOriginalFilename();
        if (uploadFile[0].getSize() > Integer.MAX_VALUE || fName.equals("") || fName == null) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        }

        // 2. 获取文件后缀
        String suffix = fName.substring(fName.lastIndexOf("."));

        // 3. 判断文件格式
		if ( !(suffix.equals(".txt") || suffix.equals(".xlsx") || suffix.equals(".xls")) )
		{
			// 7. 文件格式不正确提示
			Map<String, String> map = new HashMap<String, String>();
			map.put("msg", "请选择txt文件或者excel文件");
			dzdResponse.setData(map);
			dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
		}
		operatorMap.put(Define.STATICAL.FILENAME, fName);
	}



	/**
	 * 
	 * @Title: insertFilterRecord
	 * @Description: 过滤记录入库
	 * @author:    hz-liang
	 * @param request
	 * @param duplicateList
	 * @param operatorMap  
	 * @return: void   
	 * @throws
	 */
	private void insertFilterRecord(final HttpServletRequest request, List<String> duplicateList,
	        Map<String, Object> operatorMap)
	{
		SysUser user = SessionUtils.getUser(request);
		SmsUser smsUsers = SmsServerManager.I.getUserBySysId(Long.valueOf(user.getId()));
		Integer uid = user.getId();
		if ( smsUsers != null )
		{
			uid = smsUsers.getId().intValue();// 用户id
		}

		SmsFilterNumberRecord filterNumberRecord = new SmsFilterNumberRecord();
		filterNumberRecord.setUid(uid);
		filterNumberRecord.setName(operatorMap.get(Define.STATICAL.FILENAME).toString());
		filterNumberRecord.setMobileNumber(Integer
		        .valueOf(operatorMap.get(Define.DISTINGUISHOPERATOR.MOBILELENGTH).toString()));
		filterNumberRecord.setUnicomNumber(Integer
		        .valueOf(operatorMap.get(Define.DISTINGUISHOPERATOR.UNICOMLENGTH).toString()));
		filterNumberRecord.setTelecomNumber(Integer
		        .valueOf(operatorMap.get(Define.DISTINGUISHOPERATOR.TELECOMLENGTH).toString()));
		filterNumberRecord.setDuplicateNumber(duplicateList.size());
		filterNumberRecord.setWrongNumber(Integer
		        .valueOf(operatorMap.get(Define.DISTINGUISHOPERATOR.INVALIDLENGTH).toString()));
		
		smsFilterNumberRecordService.saveFilterNumberRecord(filterNumberRecord);
	}

	private List<String> getImputMobiles(final MultipartFile[] uploadFile) throws Exception
	{
		List<String> mobileList = new ArrayList<String>();
		
		File tempFile = File.createTempFile("tempFile", null);
		uploadFile[0].transferTo(tempFile);
		String code = FileUploadUtil.resolveCode(tempFile);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(tempFile), code);
		BufferedReader br = new BufferedReader(reader);
		String s = br.readLine();
		while (s != null && !s.equals("")) {
			mobileList.add(s.trim());
			s = br.readLine();
		}
		br.close();
		
		// 判断UTF-8格式第一行会生成特殊符号并去除
        if (code.equals("UTF-8")) {
            String phone = mobileList.get(0);
            System.out.print(phone + ",length:" + phone.length());
            if (phone.length() > 11) {
                phone = phone.substring(1, phone.length());
                mobileList.remove(0);
                mobileList.add(0, phone);
            }
        }
		
		return mobileList;
	}
    
    /**
     *
     * @Description:过滤号码列表
     * @author:wangran
     * @time:2017年05月17日 下午2:01:34
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/filterList", method ={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public DzdResponse filterList(HttpServletRequest request,@RequestBody Map<String, Object> data) throws Exception {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            WebRequestParameters parameters = getRequestParameters(WebRequestParameters.class, data);
            if (parameters == null) {
                return dzdPageResponse;
            }
            Object menuId = data.get("menuId");
            Object email = data.get("email");

            if (menuId == null) {
                return dzdPageResponse;
            }

            DzdPageParam dzdPageParam = new DzdPageParam();
            Map<String, Object> sortMap = new HashMap<String, Object>();
            if (parameters.getPagenum() != 0 && parameters.getPagesize() != 0) {
                dzdPageParam.setStart(parameters.getPagenum());
                dzdPageParam.setLimit(parameters.getPagesize());
            }
            if (email!=null && !StringUtil.isEmpty(email.toString())) {
                sortMap.put("email", email.toString());
            }

            dzdPageParam.setCondition(sortMap);

            Page<SmsFilterNumberRecord> dataList = smsFilterNumberRecordService.querySmsFilterList(dzdPageParam);

            dzdPageResponse.setRows(dataList.getResult());
            dzdPageResponse.setTotal(dataList.getTotal());

        } catch (Exception e) {
			logger.error("====================》过滤号码列表查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return dzdPageResponse;
    }

    /**
     * @Description:设置用户状态
     * @author:wangran
     * @time:2017年05月17日 上午19:04:53
     */
    @MethodDescription("设置用户状态")
    @RequestMapping(value = "/setState")
    @ResponseBody
    public DzdResponse sheze(HttpServletRequest request){
        DzdResponse dzdResponse=new DzdResponse();

        return dzdResponse;
    }
}
