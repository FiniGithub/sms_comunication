package com.dzd.utils;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.collections.iterators.UniqueFilterIterator;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;
import java.util.Map.Entry;

/**
 * 数组常用类的整理，包括几大常用功能:<br>
 * <ol>
 * <li>转换 比较等方法</li>
 * <li>其他</li>
 * </ol>
 * 
 * @date 2011-5-12
 * @author MipatchTeam#chenc
 * 
 */
public class ArrayUtil {

	/**
	 * 将对象数组转换为MAP
	 * 
	 * <pre>
	 * // Create a Map mapping colors.
	 * Map colorMap = MapUtils.toMap(new String[][] {{
	 *     {&quot;RED&quot;, &quot;#FF0000&quot;},
	 *     {&quot;GREEN&quot;, &quot;#00FF00&quot;},
	 *     {&quot;BLUE&quot;, &quot;#0000FF&quot;}});
	 * </pre>
	 * 
	 * @param array
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map toMap(Object[] array) {
		return ArrayUtils.toMap(array);
	}

	/**
	 * 获取数组的子集和
	 * 
	 * @param array
	 * @param startIndexInclusive
	 * @param endIndexExclusive
	 * @return
	 */
	public static Object[] subarray(Object[] array, int startIndexInclusive,
			int endIndexExclusive) {
		return ArrayUtils.subarray(array, startIndexInclusive,
				endIndexExclusive);
	}

	/**
	 * 数组长度是否相同
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static boolean isSameLength(Object[] array1, Object[] array2) {
		return ArrayUtils.isSameLength(array1, array2);
	}

	/**
	 * int  Array转为List
	 * 
	 * @param array1
	 * @return
	 */
	public static List<Integer> arrayToList(int[] array1) {
		List<Integer> l = new ArrayList<Integer>();

		for (int a : array1)
			l.add(a);

		return l;
	}
	/**
	 * Array转为 List<Object[]> 
	 * 
	 * @param array1
	 * @return
	 */
	
	public static List<Object[]> arrayToList(Object[] array1) {
		List<Object[]> l = new ArrayList<Object[]>(); 
		for (Object a : array1)
			l.add(new Object[]{ a });

		return l;
	}

	/**
	 * 数组排序，最终返回从小到大
	 * 
	 * @param list
	 * @return
	 */
	public static List<Integer> sortArray(List<Integer> list) {
		Collections.sort(list);
		return list;
	}

	/**
	 * 数组排序，最终返回从小到大
	 *
	 * @return
	 */
	public static int[] sortArray(int[] array1) {
		Arrays.sort(array1);
		return array1;
	}

	/**
	 * 过滤掉数组中的重复元素
	 * @param list
	 * @return 无重复的数组
	 */
	@SuppressWarnings("unchecked")
	public static List<String> uniqueFilterList(List<String> list) {
		List<String> listTmp = new ArrayList<String>();

		Iterator<String> iterator = new UniqueFilterIterator(list.iterator());
		while (iterator.hasNext()) {
			listTmp.add(iterator.next());
		}

		return listTmp;

	}
    //去除数组中重复的记录  
    public static String[] uniqueFilterArray(String[] a) { 
        List<String> list = new LinkedList<String>();  
        for(int i = 0; i < a.length; i++) {  
            if(!list.contains(a[i])) {  
                list.add(a[i]);  
            }  
        }  
        return (String[])list.toArray(new String[list.size()]);  
    }  
	/**
	 * 过滤掉数组中的指定条件元素
	 * @param list 传入数组
	 * @param keyNum 比对数字
	 * @param isBigger true表示大于比对数字的保留  false表示小于比对数字的保留
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Integer> predicateFilterList(List<Integer> list,final int keyNum,final boolean isBigger) {
		List<Integer> listTmp = new ArrayList<Integer>();

		// 过滤元素
		Predicate predicate = new Predicate() {
			public boolean evaluate(Object object) {
				int num = ((Integer) object).intValue();
				if(isBigger)
					return num > keyNum;
				else
					return num < keyNum;
			}
		};

		Iterator<Integer> iterator = new FilterIterator(list.iterator(),
				predicate);
		while (iterator.hasNext()) {
			listTmp.add(iterator.next());
		}

		return listTmp;
	}

	/**
	 * 求两个数组的交集   
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public static String[] intersect(String[] arr1, String[] arr2) {   
		Map<String, Boolean> map = new HashMap<String, Boolean>();   
		LinkedList<String> list = new LinkedList<String>();   
		for (String str : arr1) {   
			if (!map.containsKey(str)) {   
				map.put(str, Boolean.FALSE);   
			}   
		}   
		for (String str : arr2) {   
			if (map.containsKey(str)) {   
				map.put(str, Boolean.TRUE);   
			}   
		}   
	
		for (Entry<String, Boolean> e : map.entrySet()) {   
			if (e.getValue().equals(Boolean.TRUE)) {   
				list.add(e.getKey());   
			}   
		}   
		String[] result = {};   
        return list.toArray(result); 
	}
	
	/**
	 * 连接字符串数组
	 * @param o
	 * @param flag
	 * @return
	 */
	public static String join( Object[] o , String flag ){
	    StringBuffer str_buff = new StringBuffer(); 
	    for(int i=0 , len=o.length ; i<len ; i++){
	        str_buff.append( String.valueOf( o[i] ) );
	        if(i<len-1)str_buff.append( flag );
	    }
	 
	    return str_buff.toString();
	}
}
