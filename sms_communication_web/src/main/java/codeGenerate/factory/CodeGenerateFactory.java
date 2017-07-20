package codeGenerate.factory;

import codeGenerate.def.CodeResourceUtil;
import codeGenerate.util.CommonPageParser;
import codeGenerate.util.CreateBean;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

/**
 * 生成文件路径配置类
 * @author Administrator
 *
 */
public class CodeGenerateFactory {
	
	private static final Log log = LogFactory.getLog(CodeGenerateFactory.class);
	private static String url = CodeResourceUtil.URL;
	private static String username = CodeResourceUtil.USERNAME;
	private static String passWord = CodeResourceUtil.PASSWORD;
	private static String buss_package = CodeResourceUtil.bussiPackage;
	private static String projectPath = getProjectPath();
	
	/**
	 * 
	 * @param tableName
	 * @param codeName
	 * @param entityPackage
	 * @param keyType
	 * @param pageFolder  		jsp文件夹
	 * @param isFtl     		是否为jsp文件
	 */
	public static void codeGenerate(String tableName, String codeName , String keyType, String pageFolder,
			boolean isFtl) {
	
		if (null == pageFolder || "".equals(pageFolder)) {
			pageFolder = "view";
		}
		
		System.out.println("url:"+url);
		System.out.println("username:" + username );
		System.out.println( "password:" + passWord );
		
		CreateBean createBean = new CreateBean();
		createBean.setMysqlInfo(url, username, passWord);
		
		
		//类名， 首字母大写
		String className 	= createBean.getTablesNameToClassName( tableName );
		
		//首字母小写
		String lowerName 	= className.substring(0, 1).toLowerCase() + className.substring(1, className.length());
		
		//projectPath 项目根目录 
		String srcPath 		= projectPath + CodeResourceUtil.source_root_package + "/";
		
		//源代码包目录
		String pckPath 		= srcPath + "main/java/" + CodeResourceUtil.bussiPackageUrl + "/"; 
		
		//WEB目录
		String webPath 		= projectPath + CodeResourceUtil.web_root_package + "/" + pageFolder; // + "\\" + CodeResourceUtil.bussiPackageUrl + "\\";
		webPath = webPath + "/";
		
		String basePackage				= CodeResourceUtil.bussiPackageUrl.replaceAll("/", ".");
		String entityPackage 			= basePackage + ".entity";
		String pagePackage				= basePackage + ".page";
		String daoPackage 				= basePackage + ".dao"; 
		String servicePackage 			= basePackage + ".service";
		String controllerPackage		= basePackage + ".controller";
		String mapperPackage			= "mapper"; //该包名在资源目录下
		
		
		
		String entityPath 				= "entity/"  + className + ".java";
		String pagePath					= "page/" + className + "Page.java";
		String daoPath 					= "dao/" +  className + "Dao.java"; 
		String servicePath 				= "service/"  + className + "Service.java";
		String controllerPath			= "controller/"  + className + "Controller.java";
		String mapperPath				= "mapper/" + className + "Mapper.xml";
		
 
		
		
		String jspPath = lowerName + (isFtl ? ".ftl" : ".jsp");
		String jsPath = "page-" + lowerName + ".js";
		
		
		System.out.println("生成文件位置："+ pckPath );
		
		System.out.println("生成文件位置："+ entityPath );
		System.out.println("生成文件位置："+ pagePath );
		System.out.println("生成文件位置："+ daoPath );
		System.out.println("生成文件位置："+ mapperPath );
		System.out.println("生成文件位置："+ servicePath );
		System.out.println("controller 生成文件位置："+ controllerPath );
		System.out.println("mapper.xml 生成文件位置："+ mapperPath );
		System.out.println("jsp 生成文件位置："+ webPath + "\n\n" );
		System.out.println("jsPath 生成文件位置："+ jsPath + "\n\n" );
		
		
		VelocityContext context = new VelocityContext();
		context.put("className", className);
		context.put("lowerName", lowerName);
		context.put("codeName", codeName);
		context.put("tableName", tableName);
		
 
		context.put("entityPackage", 		entityPackage);
		context.put("pagePackage",			pagePackage);
		context.put("daoPackage",	 		daoPackage);
		context.put("servicePackage", 		servicePackage);
		context.put("controllerPackage", 	controllerPackage);
		context.put("mapperPackage", 		mapperPackage);
		
		
		
		context.put("keyType", keyType);
		try {
			context.put("feilds", createBean.getBeanFeilds(tableName));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Map sqlMap = createBean.getAutoCreateSql(tableName);
			context.put("columnDatas", createBean.getColumnDatas(tableName));
			context.put("SQL", sqlMap);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		CommonPageParser.WriterPage(context, "EntityTemplate.ftl", 		pckPath, 	entityPath);
		CommonPageParser.WriterPage(context, "PageTemplate.ftl", 		pckPath, 	pagePath);
		CommonPageParser.WriterPage(context, "DaoTemplate.ftl", 		pckPath, 	daoPath);
		CommonPageParser.WriterPage(context, "ServiceTemplate.ftl", 	pckPath, 	servicePath);
		CommonPageParser.WriterPage(context, "MapperTemplate.xml", 		srcPath+"main/resources/", mapperPath);
		CommonPageParser.WriterPage(context, "ControllerTemplate.ftl", 	pckPath, 	controllerPath);
		
		
		
		CommonPageParser.WriterPage(context, isFtl ? "ftlTemplate.ftl" : "jspTemplate.ftl", webPath, jspPath);
		CommonPageParser.WriterPage(context, "jsTemplate.ftl", webPath, jsPath);
		
		log.info("----------------------------代码生成完毕---------------------------");
	}
	
	public static String getProjectPath() {
	
		String path = System.getProperty("user.dir").replace("\\", "/") + "/";
		return path;
	}
}