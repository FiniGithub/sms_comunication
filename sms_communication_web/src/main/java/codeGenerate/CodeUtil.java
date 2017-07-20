package codeGenerate;

import java.util.Locale;
import java.util.ResourceBundle;

import codeGenerate.def.FtlDef;
import codeGenerate.factory.CodeGenerateFactory;

/**
 * 程序入口
 *
 */
public class CodeUtil {
	
	public static void main(String[] args) {
	
		/** 此处修改成你的 表名 和 中文注释 ***/
		String tableName = "sys_user"; //
		String codeName = "系统用户";// 中文注释 当然你用英文也是可以的 
		String keyType = FtlDef.KEY_TYPE_02;// 主键生成方式 01:UUID 02:自增
		
//		Locale loc = Locale.getDefault();
//		Locale local = Locale.getDefault();
		//ResourceBundle messages = ResourceBundle.getBundle("messages", local, new CodeUtil().getClass().getClassLoader());
		 
		CodeGenerateFactory.codeGenerate(tableName, codeName , keyType, "view", false);
	}
}