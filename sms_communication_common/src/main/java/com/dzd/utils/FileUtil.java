package com.dzd.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import com.dzd.utils.sup.Encoding;
import com.dzd.utils.sup.HTMLDecoder;
import com.dzd.utils.sup.SinoDetect;


//import com.sun.media.jfxmedia.logging.Logger;

/**
 * 文件常用类的整理，包括几大常用功能:<br>
 * <ol>
 * 	<li>文件以及文件夹的操作（增、删、改、复制等）</li>
 * 	<li>文件读写部分</li>
 * 	<li>流，字节的读取</li>
 * 	<li>其他</li>
 * 	<li></li>
 * </ol>
 * 
 * @date 2011-4-20
 * @author MipatchTeam#hqj,MipatchTeam#yhh,MipatchTeam#chenc
 *
 */
public class FileUtil {

	// --------------------- 1. --------------------------------
	/**
	 * 新建目录
	 * 
	 * @param folderPath
	 *            String 如 c:/fqf
	 * @return boolean
	 */
	public static void newFolder(String folderPath) {
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.mkdir();
			}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 *            String
	 * @return boolean
	 */
	public static void delFile(String filePathAndName) {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myDelFile = new File(filePath);
			myDelFile.delete();
	}

	/**
	 * 删除文件夹
	 * 

	 *            String
	 * @return boolean
	 */
	public static void delFolder(String folderPath) {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 * @throws Exception 
	 */
	public static void copyFile(String oldPath, String newPath) throws Exception {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					// System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 * @throws Exception 
	 */
	public static void copyFolder(String oldPath, String newPath) throws Exception {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 * @throws Exception 
	 */
	public static void moveFile(String oldPath, String newPath) throws Exception {
		copyFile(oldPath, newPath);
		delFile(oldPath);

	}

	/**
	 * 移动文件夹到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 * @throws Exception 
	 */
	public static void moveFolder(String oldPath, String newPath) throws Exception {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);

	}
	// -----------------------------------------------------

	// --------------------- 2. --------------------------------
	/**
	 * 数据流直接写入文件
	 * 
	 * @param is
	 * @param filePath String 文件路径及名称 如c:/fqf.txt
	 * @throws Exception 
	 * @throws Exception 
	 */
	public static void writeFile(InputStream is, String filePath) throws Exception {
			FileOutputStream fs = new FileOutputStream(filePath);
			byte[] buf = new byte[1024];
			int len = is.read(buf);
			while (len != -1) {
				fs.write(buf, 0, len);
				len = is.read(buf);
			}
			fs.close();
	}

	/**
	 * 文字写入文件 以 <b>iso-8859-1</b> 为编码格式
	 * 
	 * @param src
	 * @param filePath String 文件路径及名称 如c:/fqf.txt
	 * @throws Exception 
	 */
	public static void writeFile(String src, String filePath) throws Exception {
			FileOutputStream fs = new FileOutputStream(filePath);
			fs.write(src.getBytes("iso-8859-1"));// 如果不指定编码，在中英文平台上运行时可能会出现意想不到的结果
			fs.close();
	}

	/**
	 * 文件写入文件并制定编码方式
	 * 
	 * @param src
	 * @param charset 指定编码格式
	 * @param filePath String 文件路径及名称 如c:/fqf.txt
	 */
	public static void writeFile(String src, String filePath, String charset) throws Exception {
			FileOutputStream fs = new FileOutputStream(filePath);
			fs.write(src.getBytes(charset));
			fs.close();
	}


	/**
	 * 文字附加在原内容后写入文件 以 <b>iso-8859-1</b> 为编码格式
	 * @param src
	 * @param filePath String 文件路径及名称 如c:/fqf.txt
	 */
	public static void writeFileAppend(String src, String filePath) throws Exception {
			FileOutputStream fs = new FileOutputStream(filePath,true);
			fs.write(src.getBytes("iso-8859-1"));//如果不指定编码，在中英文平台上运行时可能会出现意想不到的结果
			fs.close();
	}


	/**
	 * 文字附加在原内容后写入文件 以 charset 为编码格式
	 * @param src
	 * @param filePath String 文件路径及名称 如c:/fqf.txt
	 */
	public static void writeFileAppend(String src, String filePath, String charset) throws Exception {
			FileOutputStream fs = new FileOutputStream(filePath,true);
			fs.write(src.getBytes(charset));//如果不指定编码，在中英文平台上运行时可能会出现意想不到的结果
			fs.close();
	}
	
	/**
	 * 读取文件 以 <b>iso-8859-1</b> 为编码格式
	 * 
	 * @param filePath String 文件路径及名称 如c:/fqf.txt
	 * @return
	 */
	public static String readFile(String filePath) {
		FileInputStream fs;
		String content = "";
		try {
			fs = new FileInputStream(filePath);
			byte data[] = new byte[1024];
			int len = fs.read(data);
			while (len != -1) {
				content = content + new String(data, 0, len, "iso-8859-1");
				len = fs.read(data);
			}
			fs.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
		return content;
	}

	/**
	 * 以指定编码方式读取文件
	 * 
	 * @param filePath String 文件路径及名称 如c:/fqf.txt
	 * @param charset
	 * @return
	 */
	public static String readFile(String filePath, String charset) {
		FileInputStream fs;
		String content = "";
		try {
			fs = new FileInputStream(filePath);
			byte data[] = new byte[1024];
			int len = fs.read(data);
			while (len != -1) {
				content = content + new String(data, 0, len, charset);
				len = fs.read(data);
			}
			fs.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
		return content;
	}

	// -----------------------------------------------------

	// --------------------- 3. --------------------------------
	/**
	 * 读取流内容
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String readStream(InputStream is) {
		String cs = null;
		try {
			ByteArrayOutputStream buffer = null;
			BufferedInputStream in = new BufferedInputStream(is);
			buffer = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			int len = -1;
			// 从socket连接中获取输出流，主要为请求的响应报头和HTML编码

			while ((len = in.read(buff)) != -1) {
				buffer.write(buff, 0, len);
			}// 由于使用BufferOutputStream会出现一个连接被分割在两行的情况，因此只能利用字节流将所有源代码取得，而后换成String

			HTMLDecoder htmd = new HTMLDecoder();
			SinoDetect sd = new SinoDetect();

			if (buffer != null) {

				try {
					int i = sd.detectEncoding(buffer.toByteArray());
					//cs = buffer.toString(Encoding.htmlname[i]);
					//String encode = Encoding.htmlname[i];
					//System.out.println(Encoding.htmlname[i]);
					if( Encoding.htmlname[i].equals("GB2312") )
					{
						cs = buffer.toString("GBK");
					}else{
						cs = buffer.toString(Encoding.htmlname[i]);
					}
				} catch (RuntimeException e) {
					cs = buffer.toString("GBK");
				}
				// try{
				// cs=buffer.toString(Encoding.htmlname[i]);
				// }catch(Exception e){
				// cs=buffer.toString("GBK");
				// }
				cs = cs.replace("&nbsp;", "");
				cs = htmd.ASCIIToGB(cs);
			}

			is.close();
		} catch (IOException e) {
			
		}
		return cs;
	}

	/**
	 * 读取字节内容
	 * 

	 * @return
	 * @throws IOException
	 */
	public static String readBytes(byte[] buff) throws Exception {
		String cs = null;
		try {
			HTMLDecoder htmd = new HTMLDecoder();
			SinoDetect sd = new SinoDetect();

			if (buff != null) {
				if (sd.detectEncoding(buff) == 1) {
					cs = new String(buff, "GBK");
					cs = cs.replace("&nbsp;", " ");
					cs = htmd.ASCIIToGB(cs);
//				} else if (sd.detectEncoding(buff) == 2) {
//					cs = new String(buff, "HZ");
//					cs = cs.replace("&nbsp;", " ");
//					cs = htmd.ASCIIToGB(cs);
				} else if (sd.detectEncoding(buff) == 3) {
					cs = new String(buff, "BIG5");
					cs = cs.replace("&nbsp;", " ");
					cs = htmd.ASCIIToGB(cs);
				} else if (sd.detectEncoding(buff) == 4) {
					cs = new String(buff, "EUC_TW");
					cs = cs.replace("&nbsp;", " ");
					cs = htmd.ASCIIToGB(cs);
//				} else if (sd.detectEncoding(buff) == 5) {
//					cs = new String(buff, "ISO_2022_CN");
//					cs = cs.replace("&nbsp;", " ");
//					cs = htmd.ASCIIToGB(cs);
				} else if (sd.detectEncoding(buff) == 6) {
					cs = new String(buff, "UTF-8");
					cs = cs.replace("&nbsp;", " ");
					cs = htmd.ASCIIToGB(cs);
				} else if (sd.detectEncoding(buff) == 7) {
					cs = new String(buff, "Unicode");
					cs = cs.replace("&nbsp;", " ");
					cs = htmd.ASCIIToGB(cs);
				} else
					cs = new String(buff);
				// cs=new String(buff,"GBK");
			}

		} catch (IOException e) {
			throw e;
		}
		return cs;
	}

	/**
	 * 读取流内容，指定编码方式
	 * 
	 * @param is
	 * @param charset
	 * @return
	 */
	public static String readStream(InputStream is, String charset) {
		StringBuilder sb = new StringBuilder();
		// String content="";
		try {
			byte data[] = new byte[1024];
			for (int n; (n = is.read(data)) != -1;) {
				sb.append(new String(data, 0, n, charset));
			}

			is.close();
		} catch (IOException e) {

		}
		return sb.toString();
		// return content;
	}
	// -----------------------------------------------------

	// --------------------- 4. --------------------------------
	/**
	 *  获取文件大小
	 * @param fileName
	 * @return 单位为：kb
	 * @throws Exception
	 */
	public static double getFileSize(String fileName) {
		double size = 0;

		DecimalFormat df = new DecimalFormat("###.0");
		File file = new File(fileName);
		double length = file.length();
		if (file.exists()) {
			size = Double.valueOf(df.format(length /  1024));
		}

		return size;
	}
	// -----------------------------------------------------

}
