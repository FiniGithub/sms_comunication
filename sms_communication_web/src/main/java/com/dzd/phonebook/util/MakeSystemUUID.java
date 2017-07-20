package com.dzd.phonebook.util;

import java.util.UUID;

/**
 * 生成系统UUID，唯一标识
 * 
 * @author Administrator
 *
 */
public class MakeSystemUUID {
	public static void main(String[] args) {
		String[] ss = getUUID(1);
		for (int i = 0; i < ss.length; i++) {
			System.out.println(ss[i]);
		}
	}

	/**
	 * 调用系统的唯一标识
	 * @return
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉"-"符号
		 return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
//		return s;
	}

	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}
}
