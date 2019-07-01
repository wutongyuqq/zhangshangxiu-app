package com.shoujia.zhangshangxiu.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 * AES加密以及解密
 * @author zhulin
 * @date 
 *
 */
public class Encryption {

	public static final String sKey = "youkang12android";
	public static final String vi = "1233211234567741";

	// 加密
	public static String Encrypt(byte[] b){
		try {
			byte[] raw = sKey.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
			IvParameterSpec iv = new IvParameterSpec(vi.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec,iv);
			byte[] encrypted = cipher.doFinal(b);
		    return byte2hex(encrypted).toLowerCase();   
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// 解密 
	public static byte[] Decrypt(String sSrc) {
		if(("").equals(sSrc)){
			return null;
		}
		try {
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(vi.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec,iv);
			byte[] encrypted1 = hex2byte(sSrc);
			byte[] original = cipher.doFinal(encrypted1);
			//String originalString = new String(original, "ISO-8859-1");
			return original;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static String byte2hex(byte[] b) {
    	String hs = "";
    	String stmp = "";
    	for (int n = 0; n < b.length; n++) {
    	stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
    	if (stmp.length() == 1) {
    	hs = hs + "0" + stmp;
    	} else {
    	hs = hs + stmp;
    	}
    	}
    	return hs.toUpperCase();
    }
    
    public static byte[] hex2byte(String strhex) {
    	if (strhex == null) {
    	return null;
    	}
    	int l = strhex.length();
    	if (l % 2 == 1) {
    	return null;
    	}
    	byte[] b = new byte[l / 2];
    	for (int i = 0; i != l / 2; i++) {
    		b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
    	}
    	return b;
    }

}
