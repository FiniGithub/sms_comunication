package com.dzd.phonebook.timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dzd.phonebook.service.SmsUserService;
import com.dzd.phonebook.util.SmsUser;

/**
 * 
 * @Description:统计定时器
 * @author:oygy
 * @time:2016年12月21日 上午10:02:25
 */
public class FaCalculateService {
	
	 public static final Logger log = LoggerFactory.getLogger(FaCalculateService.class);
	
    //@Autowired
    //private InstructService instructService;
    
		@Autowired
		private SmsUserService smsUserService;
		
    /**
     * 
     * @Description: 激活统计（每天凌晨1点统计前一天的数据）
     * @author:oygy
     * @time:2016年12月21日 上午10:02:47
     */
	public void taCalculateTime(){
		System.out.println("----------------------开始设备激活定时器-------------------------");
		 log.info("-----------------The timer(start:taCalculateTime)--------------------");
		try{
			//Claim claim = new Claim();
			log.info("taCalculateTime---->>stSmsUserPuserBillList()");
		   
			  List<SmsUser> dataList = smsUserService.stSmsUserPuserBillList(); //统计出昨天的数据
			  List<SmsUser> dataList2 =smsUserService.querySmsUserListTj();//查询所有用户信息
					  
			  List<SmsUser> dataList3 = new ArrayList<SmsUser>();
			  //查询所有用户列表
			  Date dNow = new Date();   //当前时间
			  String[] datear =new String[]{getSpecifiedDayBefore(dNow,0),
					  						getSpecifiedDayBefore(dNow,-1),
					  						getSpecifiedDayBefore(dNow,-2),
					  						getSpecifiedDayBefore(dNow,-3)};
				
			  
			  for (SmsUser smsusers : dataList2) {
				for (int i = 0; i < datear.length; i++) {
					SmsUser smUsers = new SmsUser();
					smUsers.setId(smsusers.getId());
					smUsers.setName(smsusers.getName());
					smUsers.setEmail(smsusers.getEmail());
					smUsers.setAuditTime(datear[i]);
					smUsers.setSendNum(0);
					dataList3.add(smUsers);
				}
			  }
			  
			List<SmsUser> dataList5=new ArrayList<SmsUser>();
			for (SmsUser smsusers : dataList3) {
				for (SmsUser smsuserlist : dataList) {
					if(smsusers.getId() == smsuserlist.getId() && smsusers.getAuditTime().trim().equals(smsuserlist.getAuditTime().trim()))
					{
						dataList5.add(smsusers);
						
					}
				}
			}
			dataList3.removeAll(dataList5);
			dataList3.addAll(dataList);

		    log.info("taCalculateTime---->>saeveStSmsUserPuserBill()");
		   for (SmsUser smsUser : dataList3) {
			   smsUserService.saeveStSmsUserPuserBill(smsUser);
		   }
			//claimService.setTaStatistical(claim);
		}catch(Exception e){
			 log.error(null, e);
             e.printStackTrace();
		}
		log.info("-----------------The timer(end:taCalculateTime)--------------------");

	}
	
	public static String getSpecifiedDayBefore(Date dNow,Integer num){ 
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, num);  //设置为前一天
		dBefore = calendar.getTime();   //得到前一天的时间

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
		String defaultStartDate = sdf.format(dBefore);    //格式化前一天
		return defaultStartDate;
		} 
}
