package com.dzd.utils;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
	public static final Properties common = loadProperties("common.properties");
	public static String getproperties(String key,String defaultValue)
	{
		String ret = common.getProperty(key);
		return  StringUtils.isBlank(ret) ? defaultValue :ret;
	}


	private static Properties loadProperties(String resources) {

		// 使用InputStream得到一个资源文件

		InputStream inputstream = PropertiesUtils.class.getClassLoader().getResourceAsStream(resources);

		// new 一个Properties

		Properties properties = new Properties();

		try {

			// 加载配置文件

			properties.load(inputstream);

			return properties;

		} catch (IOException e) {

			throw new RuntimeException(e);

		} finally {

			try {

				inputstream.close();

			} catch (IOException e) {

				throw new RuntimeException(e);

			}

		}
	}

}
