package com.dzd.utils;

import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


/**
 * 随机数常用类的整理，包括几大常用功能:<br>
 * <ol>
 * 	<li>产生随机数</li>
 * 	<li>其他</li>
 * </ol>
 * 
 * @date 2011-5-12
 * @author MipatchTeam#chenc
 *
 */
public class RandomUtil {

	/**
	 * 随机取指定 0-区间 的整数（无重复 全数字）
	 * 
	 * @param n
	 *            取的个数
	 * @param area
	 *            0 - 区间
	 * @return 以“,”分隔的数组
	 */
	public static String getRandomInArea(int area, int n) {
		return getRandomInArea(0, area, n, true);
	}

	/**
	 * 随机取指定 0-区间 的整数，无零的（无重复全数字）
	 * 
	 * @param n
	 *            取的个数
	 * @param area
	 *            0 - 区间
	 * @return 以“,”分隔的数组
	 */
	public static String getRandomInAreaNoZero(int area, int n) {
		return getRandomInArea(1, area, n, true);
	}

	/**
	 * 随机取指定 任意区间 的整数
	 * 
	 * @param farea
	 *            区间开始
	 * @param tarea
	 *            区间结束
	 * @param n
	 *            整数个数
	 * @param canRep
	 *            是否可以重复
	 * @return 以“,”分隔的数组
	 */
	public static String getRandomInArea(int farea, int tarea, int n,
			boolean canRep) {
		// 若起止区间写反 则调换
		if (farea > tarea) {
			int temp = farea;
			farea = tarea;
			tarea = temp;
		}
		if (n > (tarea - farea))
			return null;// 若需要个数大于区间，直接跳出

		List<Integer> list = new ArrayList<Integer>();
		int x = 1;
		while (true) {

			int num = new Random().nextInt(tarea);
			if (num < farea || num > tarea)
				continue;

			if (list.contains(num) && canRep)
				continue;// 不重复

			list.add(num);
			x++;
			if (list != null && list.size() >= n)
				break;
		}

		String str = "";// 返回拼的字符串
		Iterator<Integer> it = list.iterator();
		int i = 0;
		while (it.hasNext()) {
			i++;
			str += it.next() + ((i < n) ? "," : "");
		}
		return str;
	}
	
	/**
	 * 返回大小写字母与数字的混合随机字符串
	 * 
	 * @param count 字符串长度
	 * @return
	 */
	public static String randomAlphanumeric(int count) {
		return RandomStringUtils.randomAlphanumeric(count);
	}
	
	/**
	 * 返回大小写字母的混合随机字符串
	 * 
	 * @param count 字符串长度
	 * @return
	 */
	public static String randomAlphabetic(int count) {
		return RandomStringUtils.randomAlphabetic(count);
	}
	
	/**
	 * 返回大小写字母数字和符号的混合随机字符串
	 * 
	 * @param count 字符串长度
	 * @return
	 */
	public static String randomAscii(int count) {
		return RandomStringUtils.randomAscii(count);
	}
}
