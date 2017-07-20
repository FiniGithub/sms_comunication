package sms_agent_web;

import com.dzd.phonebook.entity.SmsFilterNumberRecord;
import com.dzd.phonebook.service.SmsFilterNumberRecordService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.List;

/** *
@author  作者 
E-mail: * 
@date 创建时间：2017年5月5日 上午10:50:33 * 
@version 1.0 * 
@parameter  * 
@since  * 
@return  */

public class Test
{
	public static void main(String[] args)
	{
		int a=5;
		for(int i=0;i<100;i++){
			a=++a;
		}
		System.out.println(a);
//		int indexOf = "aaaaa,bbbb,ccc".indexOf("cc");
//		System.out.println(indexOf);
		
	/*	boolean parseBoolean = Boolean.parseBoolean("true");
		System.out.println(parseBoolean);*/

	}

/*	@org.junit.Test
	public void getEmployeeList() {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:config/spring.xml"
				,"classpath:config/spring-mybatis.xml"});
		SmsFilterNumberRecordService test = (SmsFilterNumberRecordService) context.getBean("smsFilterNumberRecordService");
		SmsFilterNumberRecord s=new SmsFilterNumberRecord();
		s.setuId(1);
		s.setProject("AAAA");
		s.setName("AAAA");
		s.setValidNumber("111112222");
		s.setDuplicateNumber("33366666");
		s.setWrongNumber("569878899");
	        test.saveFilterNumberRecord(s);
	     }*/
}
