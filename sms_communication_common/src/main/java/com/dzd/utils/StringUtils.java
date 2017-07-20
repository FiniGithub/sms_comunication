package com.dzd.utils;

import java.io.UnsupportedEncodingException;

/**
 * @Author WHL
 * @Date 2017-3-23.
 */
public class StringUtils {

    public static boolean isNotBlank( String s){
        return s!=null && s.length()>0;
    }


    /**
     * 转换为字节数组
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes){
        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 转换为字节数组
     * @param str
     * @return
     */
    public static byte[] getBytes(String str){
        if (str != null){
            try {
                return str.getBytes("utf-8");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }else{
            return null;
        }
    }
}
