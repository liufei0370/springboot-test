package com.springboot.test.util.common;

import java.security.MessageDigest;

/**
 * MD5加密
 */
public class MD5Util {
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public final static String MD5_16(String s) {
		return MD5(s).substring(8, 24);
	}
	
	public static void main(String a[]){
		System.out.println(MD5Util.MD5_16("d9e5717744d8484fa42dce216bde8312lbb0e185570e49d8bf73c624e11f4e8ao108be6d276a4cdcbeb48ed7c92eae36e1def4c87aa54fc1bf57d94303c792dc").toLowerCase());
	}
}