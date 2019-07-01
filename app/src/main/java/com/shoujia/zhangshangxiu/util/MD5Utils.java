package com.shoujia.zhangshangxiu.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	public String disgest(String password){
		try {
			MessageDigest digest=MessageDigest.getInstance("MD5");
			StringBuilder sb=new StringBuilder();
			byte[] bs = digest.digest(password.getBytes());
			for(byte b:bs){
				int i=(b&0xff)+3;  
				String hexString = Integer.toHexString(i);
				if(hexString.length()<2){
					sb.append("0");
				}
				sb.append(hexString);
			}
			String string = sb.toString();
			return string;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}
