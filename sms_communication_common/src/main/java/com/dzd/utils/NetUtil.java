package com.dzd.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.params.ClientPNames;

import com.dzd.utils.FileUtil;

/**
 * 网络请求常用类的整理，包括几大常用功能:<br>
 * <ol>
 * 	<li>发送网络请求</li>
 * 	<li>其他</li>
 * </ol>
 * 
 * @date 2011-4-21
 * @author MipatchTeam#chenc
 *
 */
public class NetUtil {

	//（2）、也重载PostMethod的getRequestCharSet()方法
	public static  class UTF8PostMethod extends PostMethod{
		public UTF8PostMethod(String url){
			super(url);
		}
		@Override
		public String getRequestCharSet() {
			return "UTF-8";
		}
	}

	public static  class GB2312PostMethod extends PostMethod{
		public GB2312PostMethod(String url){
			super(url);
		}
		@Override
		public String getRequestCharSet() {
			return "GB2312";
		}
	}

	// --------------------- 1. --------------------------------
	/**
	 * 根据URL发送请求，并获得内容
	 */
	public static String sendRequest(String url) throws Exception{
		url=url.replaceAll(" ", "%20");
		GetMethod method = new GetMethod(url);
		//method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
		HttpClient hc = new HttpClient();
		hc.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		//hc.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		hc.executeMethod(method);
		InputStream resStream = method.getResponseBodyAsStream();
		String result = FileUtil.readStream(resStream); 
		return result;
	}

	/**
	 * 根据URL发送请求，附加COOKIES，并获得内容
	 */
	public static String sendRequest(String url,String cookies) throws Exception{
		url=url.replaceAll(" ", "%20");
		GetMethod method = new GetMethod(url);
		method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		method.setRequestHeader("Cookie", cookies);
		
		HttpClient hc = new HttpClient();
		hc.executeMethod(method);
		InputStream resStream = method.getResponseBodyAsStream();
		String result = FileUtil.readStream(resStream);
		
		return result;
	}

	/**
	 * POST方法仅支持UTF-8编码
	 * @param url
	 * @param data
	 * @return
	 * @throws Exception
     */
	public static String post(String url, Map<String,String> data) throws Exception{
		return post( url, data, "UTF-8");
	}
	public static String post(String url, Map<String,String> data, String charsetCode) throws Exception{
		url=url.replaceAll(" ", "%20");
		StringBuffer buf = new StringBuffer();
		if( charsetCode.equals("UTF-8")){
			UTF8PostMethod method = new UTF8PostMethod(url);
			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charsetCode);
			NameValuePair[] pairs = new NameValuePair[data.size()];
			int i = 0;
			for (Map.Entry<String, String> entry : data.entrySet()) {
				pairs[i++] =  new NameValuePair( entry.getKey() , entry.getValue());
			}
			method.setRequestBody( pairs );

			HttpClient client = new HttpClient();
			client.executeMethod(method);
			//打印服务器返回的状态
			//System.out.println(new Date() + ":" + method.getStatusLine());
			InputStream stream = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(stream, charsetCode));

			String line;
			while (null != (line = br.readLine())) {
				buf.append(line).append("\n");
			}
			//System.out.println(new Date() + ":" + buf.toString());
			stream.close();
			//释放连接
			method.releaseConnection();
		}else{
			GB2312PostMethod method = new GB2312PostMethod(url);
			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charsetCode);
			NameValuePair[] pairs = new NameValuePair[data.size()];
			int i = 0;
			for (Map.Entry<String, String> entry : data.entrySet()) {
				pairs[i++] =  new NameValuePair( entry.getKey() , entry.getValue());
			}
			method.setRequestBody( pairs );
			HttpClient client = new HttpClient();
			client.executeMethod(method);
			//打印服务器返回的状态
			//System.out.println(new Date() + ":" + method.getStatusLine());
			InputStream stream = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(stream, charsetCode));

			String line;
			while (null != (line = br.readLine())) {
				buf.append(line).append("\n");
			}
			//System.out.println(new Date() + ":" + buf.toString());
			stream.close();
			//释放连接
			method.releaseConnection();
		}

		String result =buf.toString();
		return result;
	}
	// -----------------------------------------------------

	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url
	 *            发送请求的URL
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url,  Map<String,String> data) {
		String param="";
		for( String key:data.keySet()){
			param+="&"+key+"="+data.get(key);
		}
		param = param.substring(1);
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			//for (String key : map.keySet()) {
				//System.out.println(key + "--->" + map.get(key));
			//}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	public static String sendPost(String url, Map<String,String> data) {
		String param = "";
		for( String key:data.keySet()){
			param+="&"+key+"="+data.get(key);
		}
		param = param.substring(1);
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}
	public static String sendPostLine(String url, Map<String,String> data) {
		String param = "";
		for( String key:data.keySet()){
			param+="&"+key+"="+data.get(key);
		}
		param = param.substring(1);
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line+ "\r\n";
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 下载远程文件并保存到本地
	 * @param remoteFilePath 远程文件路径
	 * @param localFilePath 本地文件路径
	 */
	public static  void downloadFile(String remoteFilePath, String localFilePath)
	{
		URL urlfile = null;
		HttpURLConnection httpUrl = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		File f = new File(localFilePath);
		try
		{
			urlfile = new URL(remoteFilePath);
			httpUrl = (HttpURLConnection)urlfile.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			bos = new BufferedOutputStream(new FileOutputStream(f));
			int len = 2048;
			byte[] b = new byte[len];
			while ((len = bis.read(b)) != -1)
			{
				bos.write(b, 0, len);
			}
			bos.flush();
			bis.close();
			httpUrl.disconnect();
			System.out.println(" download success!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bis.close();
				bos.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}
