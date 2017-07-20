package com.dzd.phonebook.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class GsonUtil {
	private static Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHHmmss").create();

	public static String toStr(Object obj) {
		String jsonString = gson.toJson(obj);
		return jsonString;
	}

	/**
	 * getBean(这里用一句话描述这个方法的作用)
	 *
	 * @param @param
	 *            jsonString
	 * @param @param
	 *            cls
	 * @param @return
	 *            设定文件
	 * @return T 返回类型
	 * @throws @Title:
	 *             getBean
	 * @Description: TODO
	 */
	public static <T> T getBean(String jsonString, Class<T> cls) {
		T t = null;
		try {
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * getBeans(这里用一句话描述这个方法的作用)
	 *
	 * @param @param
	 *            jsonString
	 * @param @param
	 *            cls
	 * @param @return
	 *            设定文件
	 * @return List<T> 返回类型
	 * @throws @Title:
	 *             getBeans
	 * @Description: TODO
	 */

	public static <T> List<T> getListBean(String jsonString, Class<T> cls) {
		List<T> list = null;
		try {

			list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
			}.getType());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;

	}

	public static <T> List<Map<String, T>> getListMap(String gsonString) {
		List<Map<String, T>> list = null;
		try {
			list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
			}.getType());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 转成map的
	 *
	 * @param gsonString
	 * @return
	 */
	public static <T> Map<String, T> getMap(String gsonString) {
		Map<String, T> map = null;
		try {
			map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
			}.getType());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}

	public static <T> List<T> getListBean(List<String> list, Class<T> t) {
		List<T> tList = new ArrayList<T>();
		for (String obj : list) {
			T tBean = getBean(obj, t);
			tList.add(tBean);
		}
		return tList;
	}
}
