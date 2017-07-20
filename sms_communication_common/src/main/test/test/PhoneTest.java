package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.dzd.db.mysql.MysqlOperator;

public class PhoneTest {

	@Test
	public void testT() {

	}

 


    public void testR2(){
    	
    	List<String> phoenList = readPhoneData();
    	long startTime = new Date().getTime();
        for(String phone :phoenList) {
        	updateTest(phone);
        }
        System.out.println( "RUN TIME:"+( new Date().getTime()-startTime) );
    }
    
    public void updateTest(String phone){
    	try {
            String sql = " UPDATE sms_send_task_phone SET state=100 WHERE sms_send_task_id IN (19970,19971,19972,19974,19975,19977,19979,19982,19984) AND phone=" + phone;
            MysqlOperator.I.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 读取文件夹中的号码
     * @return
     */
    public List<String> readPhoneData() {
    	File tempFile = new File("E:\\update-sql.txt");
		List<String> phoneList = new ArrayList<String>();// 号码集合
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(tempFile));
			BufferedReader br = new BufferedReader(reader);
			String s = br.readLine();
			while (s != null && !s.equals("")) {
				phoneList.add(s.trim());
				s = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("txt获取号码发生异常：" + e.getMessage());
		}
		return phoneList;
	}
     
}
