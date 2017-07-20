package com.dzd.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.apache.commons.lang.math.NumberUtils;

/**
 * 数学常用类的整理，包括几大常用功能:<br>
 * <ol>
 * 	<li>对于基础运算函数的加强定义</li>
 * 	<li>其他</li>
 * </ol>
 * <br>
 * 
 * <br>===================================================
 * <code>BigDecimal.ROUND_HALF_EVEN</code>   的相关介绍<br>
 * 
 * <br>
 *	<code>ROUND_UP</code>  <br>
 *	总是在非   0   舍弃小数(即截断)之前增加数字。     <br>
 *	<code>ROUND_DOWN  </code>  <br>
 *	从不在舍弃(即截断)的小数之前增加数字。  <br>
 *	<code>ROUND_HALF_UP  </code>  <br>
 *	若舍弃部分> =.5，则作   ROUND_UP   ；否则，作   ROUND_DOWN   。  <br>
 *	<code>ROUND_HALF_DOWN  </code>  <br>
 *	若舍弃部分>   .5，则作   ROUND_UP；否则，作   ROUND_DOWN   。  <br>
 *	<code>ROUND_HALF_EVEN  </code>  <br>
 *	如果舍弃部分左边的数字为奇数，则作   ROUND_HALF_UP   ；如果它为偶数，则作   ROUND_HALF_DOWN   。  <br>
 *	<code>ROUND_CEILING  </code>  <br>
 *	如果   BigDecimal   是正的，则做   ROUND_UP   操作；如果为负，则做   ROUND_DOWN   操作。<br>
 *	<code>ROUND_FLOOR  </code>  <br>
 *	如果   BigDecimal   为正，则作   ROUND_UP   ；如果为负，则作   ROUND_DOWN   。  <br>
 *	<code>ROUND_UNNECESSARY  </code>  <br>
 *	该“伪舍入模式”实际是指明所要求的操作必须是精确的，，因此不需要舍入操作。  <br>
 *===================================================<br>
 * 
 * @date 2011-4-21
 * @author MipatchTeam#chenc
 *
 */
public class MathUtil {

	// --------------------- 1. --------------------------------
	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_EVEN
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 *            表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double divide(double v1, double v2, int scale) {
		return divide(v1, v2, scale, BigDecimal.ROUND_HALF_EVEN);
	}
	
	/**
	 * 除法运算，默认精确至小数点后2位
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double divide(double v1, double v2) {
		return divide(v1, v2, 2, BigDecimal.ROUND_HALF_EVEN);
	}
	
	/**
	 * 内部基本除法
	 * @param v1
	 * @param v2
	 * @param scale
	 * @param round_mode
	 * @return
	 */
	private static double divide(double v1, double v2, int scale, int round_mode) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, round_mode).doubleValue();
	}
	
	/**
	 * 提供精确的小数位四舍五入处理
	 * @param v
	 * @return
	 */
	public static String round(String v){
		return round(v,2);
	}
	
	public static String round(String v, int scale){
		return round(v,scale,BigDecimal.ROUND_HALF_EVEN);
	}

	//保留小数点后两位小数
	public static  double   number(double   pDouble, int scale)
	{
		BigDecimal     bd=new  BigDecimal(pDouble);
		BigDecimal     bd1=new  BigDecimal(pDouble).setScale(scale,bd.ROUND_HALF_UP);
		pDouble=bd1.doubleValue();
		return   pDouble;
	}

	/**
	 * 提供精确的小数位四舍五入处理
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @param round_mode
	 *            指定的舍入模式
	 * @return 四舍五入后的结果，以字符串格式返回
	 */
	public static String round(String v, int scale, int round_mode) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(v);
		return b.setScale(scale, round_mode).toString();
	}

	
	/**
	 * 求若干数字中最大的一个
	 * 
	 * @param array 参数为double类型
	 * @return
	 */
	public static double max(double[] array) {
		return NumberUtils.max(array );
	}


	/**
	 * 求若干数字中最大的一个
	 * 
	 * @param array 参数为int类型
	 * @return
	 */
	public static int max(int[] array) {
		return NumberUtils.max(array );
	}

	
	/**
	 * 求若干数字中最小的一个
	 * 
	 * @param array 参数为double类型
	 * @return
	 */
	public static double min(double[] array) {
		return NumberUtils.min(array );
	}

	/**
	 * 求若干数字中最小的一个
	 * 
	 * @param array 参数为int类型
	 * @return
	 */
	public static int min(int[] array) {
		return NumberUtils.min(array );
	}

	/**
	 * 求组合C(n,r)
	 * 
	 * @param n
	 * @param r
	 * @return
	 */
	public static int getCombinationCount(int n, int r) {
		if (r > n)
			return 0;
		if (r < 0 || n < 0)
			return 0;
		return getFactorial(n).divide(getFactorial(r),
				BigDecimal.ROUND_HALF_DOWN).divide(getFactorial((n - r)),
				BigDecimal.ROUND_HALF_DOWN).intValue();
	}

	/**
	 * 求n的阶乘
	 * 
	 * @param num
	 * @return
	 */
	public static BigDecimal getFactorial(int num) {
		BigDecimal sum = new BigDecimal(1.0);
		for (int i = 1; i <= num; i++) {
			BigDecimal a = new BigDecimal(new BigInteger(i + ""));
			sum = sum.multiply(a);
		}
		return sum;
	}
	// ---------------------------------------------------------

	/**
	 * 对字符串进行排序，分隔符为 <b>,</b>
	 * 
	 * @param str
	 *            字符串
	 * @return str
	 */
	public static String Sorting(String str) {
		return Sorting(str, ",");
	}

	/**
	 * 对字符串进行排序，指定分隔符
	 * 
	 * @param str
	 *            字符串
	 * @param splitcode
	 *            分隔符
	 * @return str
	 */
	public static String Sorting(String str, String splitcode) {
		String[] str1 = str.split(splitcode);
		Arrays.sort(str1);
		if (str1 == null)
			return null;

		String tmp = "";
		for (int i = 0; i < str1.length; i++) {
			tmp += (0 == i) ? str1[i] : (splitcode + str1[i]);
		}
		return tmp;
	}
}
