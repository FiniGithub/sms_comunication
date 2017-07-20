package com.dzd.utils.sup;

import java.util.Comparator;

public class StringSort  implements Comparator<String> {

	public int compare(String o1, String o2) {
		// 若长度不同 则长度大的排在后面
		if(o1.trim().length()>o2.trim().length())
			return 1;
		else 	if(o1.trim().length()<o2.trim().length())
				return -1;
		
		// 若长度相同，则按每个字符的char值排序
		for (int i = 0; i < o1.length(); i++) {
			if(o1.charAt(i)>o2.charAt(i))
				return 1;
			else if(o1.charAt(i)<o2.charAt(i))
				return -1;
		}
		
		return 0;
	}

}
