package sms_agent_web;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.dzd.db.mysql.MysqlOperator;
import com.dzd.phonebook.entity.SmsSendTaskPhone;
import com.dzd.phonebook.service.SmsSendTaskService;
import com.dzd.utils.DateUtil;

public class Test2 {

	/*@Test
	public void test() {
		try {

			// 插入
			List<SmsSendTaskPhone> smsSendResultList = new ArrayList<SmsSendTaskPhone>();
			for (int i = 0; i < 1000; i++) {
				SmsSendTaskPhone sendTaskPhone = new SmsSendTaskPhone();
				Random random = new Random();
				int x = random.nextInt(9999 - 1000 + 1) + 1000;
				sendTaskPhone.setPhone("1861705" + x);
				sendTaskPhone.setState(1);
				sendTaskPhone.setSmsSendTaskId(0);
				smsSendResultList.add(sendTaskPhone);
			}

			if (smsSendResultList.size() > 0) {
				final List<SmsSendTaskPhone> tmpSmsSendResultList = smsSendResultList;
				String sql = "insert into sms_send_task_phone(phone,state,sms_send_task_id,create_time) "
						+ "values(?,?,?,now())";
				MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setString(1, tmpSmsSendResultList.get(i).getPhone());
						ps.setInt(2, tmpSmsSendResultList.get(i).getState());
						ps.setInt(3, tmpSmsSendResultList.get(i).getSmsSendTaskId());
					}

					public int getBatchSize() {
						return tmpSmsSendResultList.size();
					}
				});

				// 修改
//				updateTask(smsSendResultList);
				updateTask1(smsSendResultList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 发生异常需要把信息先缓存起来
		}

	}

	public void updateTask1(List<SmsSendTaskPhone> smsSendResultList) {
		try {
			StringBuffer sb = new StringBuffer();
			for(SmsSendTaskPhone sendPhone:smsSendResultList){
				String phone = sendPhone.getPhone();
				sb.append("'"+phone + "',");
			}
			String phoneStr = sb.toString();
			phoneStr = phoneStr.substring(0,phoneStr.length() - 1);
			
			Date day = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			String ksTate = df.format(day);
			String dateTime = DateUtil.formatDateTime();
			String sql = "UPDATE sms_send_task_phone AS p SET p.state =100, p.update_time='" + dateTime
					+ "' WHERE p.sms_send_task_id=0 AND p.phone IN ("+phoneStr+")";
			MysqlOperator.I.execute(sql);
			System.out.println("----------------开始时间" + ksTate + "----------------执行完毕！" + df.format(new Date()));
			System.out.println("!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateTask(List<SmsSendTaskPhone> smsSendResultList) {
		try {
			Date day = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			String ksTate = df.format(day);
			System.out.println("------------开始执行" + ksTate);

			if (smsSendResultList.size() > 0) {
				final List<SmsSendTaskPhone> tmpSmsSendResultList = smsSendResultList;
				// String sql = "update sms_send_task_phone set state=?,
				// update_time=? where sms_send_task_id=? and phone=?";

				String sql = "UPDATE sms_send_task_phone AS p SET p.state =?, p.update_time=? WHERE p.sms_send_task_id=? AND p.phone IN (?)";
				MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						String dateTime = DateUtil.formatDateTime();
						ps.setInt(1, 100);
						ps.setString(2, dateTime);
						ps.setString(3, tmpSmsSendResultList.get(i).getSmsSendTaskId().toString());
						ps.setString(4, tmpSmsSendResultList.get(i).getPhone());

						System.out.println(" taskid=" + tmpSmsSendResultList.get(i).getSmsSendTaskId() + ",mobile="
								+ tmpSmsSendResultList.get(i).getPhone() + ", updateTime=" + dateTime + ",state="
								+ tmpSmsSendResultList.get(i).getState());
					}

					public int getBatchSize() {
						return tmpSmsSendResultList.size();
					}
				});
			}
			System.out.println("----------------开始时间" + ksTate + "----------------执行完毕！" + df.format(new Date()));
			System.out.println("!!");
		} catch (Exception e) {
			e.printStackTrace();
			// 发生异常需要把信息先缓存起来
		}
	}*/

}
