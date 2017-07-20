package com.dzd.phonebook.page;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dzd.base.util.StringUtil;
import com.dzd.sms.application.Define;

public class MobileCheckUtil {
	/**
	 * 对重复号码/无效号码/有效号码分类
	 * @param mobileList
	 * @return
	 */
	public static Map<String, List<String>> mobileAssort(List<String> mobileList) {
		if (null == mobileList || mobileList.size() < 1) {
			return null;
		}
		long begin = System.currentTimeMillis();
		List<String> vlist = new LinkedList<String>();
		List<String> ivlist = new LinkedList<String>();
		List<String> dplist = new LinkedList<String>();
		Map<String,String> vmap = new LinkedHashMap<String,String>();
		Map<String,String> ivmap = new LinkedHashMap<String,String>();
		Map<String,String> dpmap = new LinkedHashMap<String,String>();
		Map<String, List<String>> mobileInfoMap = new HashMap<String, List<String>>();
		mobileInfoMap.put(Define.PHONEKEY.INVALID, ivlist); // 无效号码
		mobileInfoMap.put(Define.PHONEKEY.VALID, vlist); // 有效号码
		mobileInfoMap.put(Define.PHONEKEY.DUPLICATE, dplist); // 重复号码

		String regEx="[^0-9]";
		Pattern p = Pattern.compile(regEx);
		for (String mobile : mobileList) {
			Matcher m = p.matcher(mobile);
			mobile = m.replaceAll("").trim();

			// 无效号码
			if (!NewRegexUtil.elevenNumber(mobile)) {
				if (!ivmap.containsKey(mobile)){
					ivmap.put(mobile, mobile);
				}else{
					ivlist.add(mobile);
				}
			}
			else if(!vmap.containsKey(mobile)) {
				vmap.put(mobile,mobile);
			} else {
				if (!dpmap.containsKey(mobile)){
					dpmap.put(mobile, mobile);
				}else{
					dplist.add(mobile);
				}
			}
		}
		for(Map.Entry<String, String> entry:ivmap.entrySet()){
			ivlist.add(entry.getKey());
		}
		for(Map.Entry<String, String> entry:dpmap.entrySet()){
			dplist.add(entry.getKey());
		}
		for(Map.Entry<String, String> entry:vmap.entrySet()){
			vlist.add(entry.getKey());
		}
		System.out.println("cost time:" + (System.currentTimeMillis()-begin));
		vmap.clear();
		ivmap.clear();
		dpmap.clear();
		return mobileInfoMap;
	}
	
	/**
	 * 将数组元素转换成List对象
	 * @param strAry
	 * @return
	 */
	public static List<String> getListForAry(String []strAry){
		List<String> lst = null;
		if(null!=strAry && strAry.length>0){
			lst = new ArrayList<String>();
			for(String str:strAry){
				lst.add(str);
			}
		}
		return lst;
	}
	
	public static List<String> map2List(Map<String,String> map){
		if (null==map || map.size()==0){
			return null;
		}
		List<String> result = new ArrayList<String>();
		for(Map.Entry<String, String> entry:map.entrySet()){
			result.add(entry.getKey());
		}
		return result;
	}
	
	
	public static void main(String []args) throws Exception{
		List<String> lst = new ArrayList<String>();
		File f = new File("d:\\111.txt");
		String temp = null;
		long begin = System.currentTimeMillis();
		BufferedReader br = new BufferedReader(new FileReader(f));
		while ((temp=br.readLine())!=null){
			if (StringUtil.isEmpty(temp)){
				continue;
			}
			lst.add(temp.trim());
		}
		System.out.println(lst.size());
		Map<String, List<String>> map = mobileAssort(lst);
		for(Entry<String, List<String>> entry:map.entrySet()){
			System.out.println(entry.getKey()+"\t" + entry.getValue().size());
		}
		System.out.println(System.currentTimeMillis()-begin);
	}
	
}
