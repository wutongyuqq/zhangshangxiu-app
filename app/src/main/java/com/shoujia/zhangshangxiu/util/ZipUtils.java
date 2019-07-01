package com.shoujia.zhangshangxiu.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 数据压缩以及解压
 * 
 * @author zhulin
 * @date 2013.8.10
 */
public class ZipUtils {

	/**
	 * 字符串的压缩
	 * 
	 * @param str
	 *            待压缩的字符串
	 * @return 返回压缩后的字符串
	 * @throws IOException
	 */
	@SuppressWarnings("null")
	public static byte[] compress(String str) {
		byte[] result = new byte[1024];
		if (null == str || str.length() <= 0) {
			return result;
		}
		// 创建一个新的 byte 数组输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 使用默认缓冲区大小创建新的输出流
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			// 将 b.length 个字节写入此输出流
			gzip.write(str.getBytes());
			gzip.close();
			// 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
			result = out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 字符串的解压
	 * 
	 * @param str
	 *            对字符串解压
	 * @return 返回解压缩后的字符串
	 * @throws IOException
	 */
	public static String unCompress(byte[] str) {
		/*
		 * if(null == str ){ return null; } if(str.length <= 0){ return
		 * str.toString(); }
		 */
		if (null == str || str.length <= 0) {
			return str.toString();

		}
		// 创建一个新的 byte 数组输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
		ByteArrayInputStream in;
		String result = "";
		try {
			in = new ByteArrayInputStream(str);
			// 使用默认缓冲区大小创建新的输入流
			GZIPInputStream gzip = new GZIPInputStream(in);
			byte[] buffer = new byte[1024];
			int n = 0;
			while ((n = gzip.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
				// 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
				out.write(buffer, 0, n);
			}
			// 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
			// result = out.toString("UTF-8");
			   result = out.toString("UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}