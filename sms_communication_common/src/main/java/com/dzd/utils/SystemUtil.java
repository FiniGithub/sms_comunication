package com.dzd.utils;

import java.io.File;

import org.apache.commons.lang.SystemUtils;

/**
 * JAVA系统环境常用类的整理，包括几大常用功能:<br>
 * <ol>
 * 	<li>JAVA系统环境路径，版本等</li>
 * 	<li>其他</li>
 * </ol>
 * 
 * @date 2011-5-12
 * @author MipatchTeam#chenc
 *
 */
public class SystemUtil {
	

	/**
	 * 获取当前JAVA的版本属性
	 * @return
	 */
	public static float getJavaVersion(){
		return SystemUtils.JAVA_VERSION_FLOAT;
	}

	/**
	 * 获取 Java home 的文件夹.
	 * @return
	 */
	public static File getJavaHome(){
		return SystemUtils.getJavaHome();
	}
	
	/**
	 * 获取 Java Io 临时文件夹.
	 * @return
	 */
	public static File getJavaIoTmpDir(){
		return SystemUtils.getJavaIoTmpDir();
	}
}
