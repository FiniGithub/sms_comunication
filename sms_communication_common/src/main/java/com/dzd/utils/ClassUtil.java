package com.dzd.utils;

import org.apache.commons.lang.ClassUtils;

import java.util.List;

/**
 * 类信息获取常用类的整理，包括几大常用功能:<br>
 * <ol>
 * <li>获取类的接口，父类，包名</li>
 * <li>类与类名的转换</li>
 * <li>其他</li>
 * </ol>
 * 
 * @date 2011-5-5
 * @author MipatchTeam#chenc
 * 
 */
public class ClassUtil {

	/**
	 * 获取给定类所有接口
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List getAllInterfaces(Class cls){
		return ClassUtils.getAllInterfaces(cls);
	}

	/**
	 * 获取给定类所有父类
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List getAllSuperclasses(Class cls){
		return ClassUtils.getAllSuperclasses(cls);
	}
	
	/**
	 * 获取给定类完整包名，如：Date --> java.util
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getPackageName(Class cls){
		return ClassUtils.getPackageName(cls);
	}

	/**
	 * 根据给出的字符串生成一个类
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static Class getClass(String className) throws ClassNotFoundException{
		return ClassUtils.getClass(className);
	}

	/**
	 * 将一组类直接转换为一组类名
	 * @param classes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List convertClassesToClassNames(List classes){
		return ClassUtils.convertClassesToClassNames(classes);
	}

	/**
	 * 将一组类名直接转换为一组类
	 * @param classNames
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List convertClassNamesToClasses(List classNames){
		return ClassUtils.convertClassNamesToClasses(classNames);
	}
	
	/**
	 * 两组类的位置对应，类型是否相同
	 * @param classArray
	 * @param toClassArray
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isAssignable(Class[] classArray,Class[] toClassArray){
		return ClassUtils.isAssignable(classArray, toClassArray);
	}
	
	/**
	 * 两个类的类型是否相同
	 * @param cls
	 * @param toClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isAssignable(Class cls,Class toClass){
		return ClassUtils.isAssignable(cls, toClass);
	}
}
