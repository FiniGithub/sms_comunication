package com.dzd.utils.string;

import java.io.File;
import java.util.zip.CRC32;

import com.dzd.utils.encrypt.Base64Builder;
import com.dzd.utils.encrypt.MD5Builder;

public class CodecUtil {

	/**
	 * 加密字符串（以SHA1格式）
	 * 
	 * @param sPassword
	 */
	public static String encryptSHA(String sPassword) throws Exception {
		return new MD5Builder().getSHA1String(sPassword);
	}

	/**
	 * 生成文件特征码（以SHA1格式）
	 * 
	 * @param file
	 */
	public static String encryptSHA(File file) throws Exception {
		return new MD5Builder().getSHA1File(file);
	}

	/**
	 * 加密字符串（以MD5格式）
	 * 
	 * @param sPassword
	 */
	public static String encryptMD5(String sPassword) throws Exception {
		return new MD5Builder().getMD5String(sPassword);
	}

	/**
	 * 生成文件特征码（以MD5格式）
	 * 
	 * @param file
	 */
	public static String encryptMD5(File file) throws Exception {
		return new MD5Builder().getMD5File(file);
	}

	/**
	 * 加密字符串（以CRC32格式）
	 * 
	 * @param sPassword
	 */
	public static Long encryptCRC32(String sPassword) {
		CRC32 c = new CRC32();
		c.update(sPassword.getBytes());
		return c.getValue();
	}

	/**
	 * 加密字符串（以Base64格式）
	 * 
	 * @param sPassword
	 */
	public static String encryptBase64(String sPassword) {
		return Base64Builder.encode(sPassword);
	}

	/**
	 * 解密字符串（以Base64格式）
	 * 
	 * @param sPassword
	 * @throws Exception
	 */
	public static String unEncryptBase64(String sPassword) throws Exception {
		return Base64Builder.decode(sPassword);
	}
}
