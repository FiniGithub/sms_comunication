package com.dzd.utils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Enumeration;

/**
 * ZIP压缩常用类的整理，包括几大常用功能:<br>
 * <ol>
 * 	<li>文件压缩以及解压</li>
 * 	<li>其他</li>
 * 	<li></li>
 * </ol>
 * 
 * 代码示例：
 * ZipUtil.Zip("d:/DMZ/BBSICS/", "\\\\172.16.1.124\\d$/DMZ/200911/11/12/BBSICS.zip");
 * 
 * @date 2011-4-20
 * @author MipatchTeam#chenc
 *
 */
public class ZipUtil {

	public static void Zip(String sFile, String dFile) throws Exception {
		try {
			// 压缩后的输出路径及文件名
			File destFile = new File(dFile);
			if (!new File(destFile.getParent()).exists())
				new File(destFile.getParent()).mkdirs();

			File f = new File(sFile);
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(dFile));
			zip(out, f, "");
	        out.close();
		} catch (Exception e) {
//			System.out.println("进行压缩时错误，源文件：" + sFile + " 目标文件：" + dFile);
			throw new Exception("进行压缩时错误，源文件：" + sFile + " 目标文件：" + dFile,e);
		}
//		System.out.println("压缩 源文件：" + sFile + " 目标文件：" + dFile+" 完成");
	}

	private static void zip(ZipOutputStream out, File f, String base)
			throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
//			System.out.println(base + " zipped OK!");
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}

	@SuppressWarnings("unchecked")
	public static void UnZip(String sFile, String dFile) throws Exception {
		final int BUFFER = 2048;
		try {
			ZipFile zipFile = new ZipFile(sFile);
			Enumeration emu = zipFile.getEntries();
			while (emu.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) emu.nextElement();
				// 会把目录作为一个file读出一次，所以只建立目录就可以，之下的文件还会被迭代到。
				if (entry.isDirectory()) {
					new File(dFile + entry.getName()).mkdirs();
					continue;
				}
				BufferedInputStream bis = new BufferedInputStream(zipFile
						.getInputStream(entry));
				File file = new File(dFile + entry.getName());

				// 加入这个的原因是zipfile读取文件是随机读取的，这就造成可能先读取一个文件
				// 而这个文件所在的目录还没有出现过，所以要建出目录来。
				File parent = file.getParentFile();
				if (parent != null && (!parent.exists())) {
					parent.mkdirs();
				}

				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
				int count;
				byte data[] = new byte[BUFFER];
				while ((count = bis.read(data, 0, BUFFER)) != -1) {
					bos.write(data, 0, count);
				}
				bos.flush();
				bos.close();
				bis.close();
			}
			zipFile.close();
		} catch (Exception e) {
//			logger.error("进行解压时错误，源文件：" + sFile + " 目标文件：" + dFile, e);
			throw new Exception("进行压缩时错误，源文件：" + sFile + " 目标文件：" + dFile,e);
		}
	}
}