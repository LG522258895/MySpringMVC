package com.cn.lg.mvcframework.utils;

public class StringUtils {
	public static String lowerFirst(String str) {
		char[] charArray = str.toCharArray();
		charArray[0] += 32;
		return String.valueOf(charArray);
	}
}
