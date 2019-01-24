
package com.springboot.test.util.common;

import java.util.Random;
import java.util.UUID;

public class IDUtil {
	
	private static final Random torrent = new Random();
	
	private static final String chars = "abcdefghijklmnopqrstuvwxyz";
	
	/**
	 * 返回一个16位的唯一ID串
	 * @return
	 */
	public static String RANDOM16(){
		return MD5Util.MD5_16(RANDOM32());
	}

	/**
	 * 返回一个32位的唯一ID串
	 * @return
	 */
	public static String RANDOM32(){
		String uid = UUID.randomUUID().toString();
		uid = uid.replaceAll("-", "").toLowerCase();
		if(Character.isDigit(uid.charAt(0))){
			uid = chars.charAt((int)(Math.random() * 26)) + uid.substring(1);
		}
		return uid;
	}
	
	/**
	 * 返回一个64位的唯一ID串
	 * @return
	 */
	public static String RANDOM64(){
		return RANDOM32() + RANDOM32();
	}
	
	/**
	 * 返回一个128位的唯一ID串
	 * @return
	 */
	public static String RANDOM128(){
		return RANDOM64() + RANDOM64();
	}
	
	public synchronized static Long RANDOM(){
		return torrent.nextLong();
	}
	
	public static void main(String a[]){
		for(int i=0;i<54;i++){
			System.out.println(IDUtil.RANDOM128());
		}
//		String s =MD5Util.MD5_16("d9e5717744d8484fa42dce216bde8312lbb0e185570e49d8bf73c624e11f4e8ao108be6d276a4cdcbeb48ed7c92eae36e1def4c87aa54fc1bf57d94303c792dc");
//		System.out.println(s);
	}
}
