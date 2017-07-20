package com.dzd.phonebook.page;

	import java.security.MessageDigest;
	import java.security.NoSuchAlgorithmException;
	/*
	 * MD5 算法
	*/
	public class MD5 {
	    
	    // 全局数组
	    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
	            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	    public MD5() {
	    }

	    // 返回形式为数字跟字符串
	    private static String byteToArrayString(byte bByte) {
	        int iRet = bByte;
	        // System.out.println("iRet="+iRet);
	        if (iRet < 0) {
	            iRet += 256;
	        }
	        int iD1 = iRet / 16;
	        int iD2 = iRet % 16;
	        return strDigits[iD1] + strDigits[iD2];
	    }

	    // 返回形式只为数字
	    private static String byteToNum(byte bByte) {
	        int iRet = bByte;
	        System.out.println("iRet1=" + iRet);
	        if (iRet < 0) {
	            iRet += 256;
	        }
	        return String.valueOf(iRet);
	    }

	    // 转换字节数组为16进制字串
	    private static String byteToString(byte[] bByte) {
	        StringBuffer sBuffer = new StringBuffer();
	        for (int i = 0; i < bByte.length; i++) {
	            sBuffer.append(byteToArrayString(bByte[i]));
	        }
	        return sBuffer.toString();
	    }

	    public static String GetMD5Code(String strObj) {
	        String resultString = null;
	        try {
	            resultString = new String(strObj);
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            // md.digest() 该函数返回值为存放哈希值结果的byte数组
	            resultString = byteToString(md.digest(strObj.getBytes()));
	        } catch (NoSuchAlgorithmException ex) {
	            ex.printStackTrace();
	        }
	        return resultString;
	    }

	    public static void main(String[] args) {
	        MD5 getMD5 = new MD5();
	        String Token = "e46987a3c0f4c237abcde6a0f463fdac6ba212add0100a9f76dd10b62a4aca141b06ccc3ef5af8ce07f3095b1fe7021b";
	        Long time = 1473389720735L; //System.currentTimeMillis();
	        System.out.println(time);
	        System.out.println(getMD5.GetMD5Code(Token+time));
	        //1473389720735
	        //c989f83a682fea842b41ce9cf3439487
	    }
	}
