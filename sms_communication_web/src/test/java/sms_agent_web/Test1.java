package sms_agent_web;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dzd.db.mysql.MysqlOperator;
import com.dzd.phonebook.entity.SmsSendTask;
import com.dzd.phonebook.service.SmsSendTaskService;
import com.dzd.sms.application.Define;
import com.dzd.sms.service.data.SmsSendResult;
import com.dzd.utils.DateUtil;

//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration(locations = { "classpath:config/spring.xml", "classpath:config/spring-mybatis.xml", "classpath:config/spring-mvc.xml" })

public class Test1 {
//
//  	@Autowired
//    private SmsSendTaskService smsSendTaskService;
//
//	@Test
//	public void test(){
//		Date day=new Date();
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String ksTate = df.format(day);
//		System.out.println("------------开始执行"+new Date());
//		SmsSendTask smsSendTask = new SmsSendTask();
//		smsSendTask.setSmsUserId(1);
//		smsSendTask.setState(1);
//		smsSendTask.setSendNum(2);
//		smsSendTask.setBillingNum(2);
//		smsSendTask.setErrorPhoneNum(1);
//		smsSendTask.setBlacklistPhoneNum(1);
//		smsSendTask.setSendType(1);
//		smsSendTask.setAuditState(1);
//		for (int i = 0; i < 1000; i++) {
//			smsSendTask.setContent("测试"+i);
//			smsSendTaskService.addTaskStat(smsSendTask);
//		}
//		System.out.println("----------------开始时间"+ksTate+"----------------执行完毕！"+df.format(new Date()));
//
//	}
//
//	@Test
//	public void test2(){
//
//	}
//	public void saveSmsSend(){
//        try {
//        	List<SmsSendTask>  smsSendResultList =new ArrayList<SmsSendTask>();
//    		for (int j = 0; j < 1000000; j++) {
//    			SmsSendTask smsSendTask = new SmsSendTask();
//    			smsSendTask.setSmsUserId(1);
//        		smsSendTask.setState(1);
//        		smsSendTask.setSendNum(2);
//        		smsSendTask.setBillingNum(2);
//        		smsSendTask.setErrorPhoneNum(1);
//        		smsSendTask.setBlacklistPhoneNum(1);
//        		smsSendTask.setSendType(1);
//        		smsSendTask.setAuditState(1);
//    			smsSendTask.setContent("测试"+j);
//    			smsSendResultList.add(smsSendTask);
//    		}
//
//    		Date day=new Date();
//    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//    		String ksTate = df.format(day);
//    		System.out.println("------------开始执行"+ksTate);
//
//            if( smsSendResultList.size()>0) {
//                final List<SmsSendTask> tmpSmsSendResultList = smsSendResultList;
//                String sql = "insert into sms_send_task(content,sms_user_id,state,create_time,send_num,billing_num,error_phone_num,blacklist_phone_num,send_type,audit_state)"
//                +"values(?,?,?,now(),?,?,?,?,?,?)";
//
//                MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter() {
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        ps.setString(1, tmpSmsSendResultList.get(i).getContent());
//                        ps.setInt(2, tmpSmsSendResultList.get(i).getSmsUserId());
//                        ps.setInt(3, tmpSmsSendResultList.get(i).getState());
//                        ps.setInt(4, tmpSmsSendResultList.get(i).getSendNum());
//                        ps.setInt(5, tmpSmsSendResultList.get(i).getBillingNum());
//                        ps.setInt(6, tmpSmsSendResultList.get(i).getErrorPhoneNum());
//                        ps.setInt(7, tmpSmsSendResultList.get(i).getBlacklistPhoneNum());
//                        ps.setInt(8, tmpSmsSendResultList.get(i).getSendType());
//                        ps.setInt(9, tmpSmsSendResultList.get(i).getAuditState());
//                    }
//                    public int getBatchSize() {
//                        return tmpSmsSendResultList.size();
//                    }
//                });
//            }
//            System.out.println("----------------开始时间"+ksTate+"----------------执行完毕！"+df.format(new Date()));
//        }catch (Exception e){
//            e.printStackTrace();
//            //发生异常需要把信息先缓存起来
//        }
//	}
//

}