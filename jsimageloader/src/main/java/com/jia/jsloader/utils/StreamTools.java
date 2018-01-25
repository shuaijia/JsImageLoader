package com.jia.jsloader.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class StreamTools {
	public static final int MSG_PROGRESS = 5;
	public static void saveData(InputStream in, File file) throws IOException {
		if(in !=null && file!=null){
			FileOutputStream out = new FileOutputStream(file);
			readData(in, out);
		}
	}
	
	public static byte[] read(InputStream in) throws IOException {
		byte[] bytes = null;
		if(in!=null){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			readData(in, out);
			bytes = out.toByteArray();
		}
		return bytes;
	}
	
	public static void readData(InputStream in, OutputStream out) throws IOException {
		if(in!=null && out!=null){
			BufferedInputStream bis = new BufferedInputStream(in);
			BufferedOutputStream bos = new BufferedOutputStream(out);
			int len = -1;
			byte[] bytes = new byte[1024];
			while((len = bis.read(bytes))!=-1){
				bos.write(bytes,0,len);
			}
			bos.close();
			out.close();
			bis.close();
			in.close();
		}
	}
	
	
	public static void save(File fromFile, File toFile) throws IOException {
		InputStream fis = new FileInputStream(fromFile);
		BufferedInputStream in = new BufferedInputStream(fis);
		OutputStream fos = new FileOutputStream(toFile);
		BufferedOutputStream out = new BufferedOutputStream(fos);
		
		int len = -1;
		byte[] bytes = new byte[1024];
		while((len = in.read(bytes))!=-1){
			out.write(bytes,0,len);
		}
		out.close();
		fos.close();
		in.close();
		fis.close();
	}
	
	public static ArrayList<String> readBook(InputStream in)throws IOException {
		 ArrayList<String> texts = new ArrayList<String>();
		 byte[] bytes = new byte[600];
		 int len = -1;
		 while((len = in.read(bytes))!=-1){
			 String text = new String(bytes,"GB2312");
			 texts.add(text);
		 }
		 in.close();
		return texts;
	}
	
//	public static String readStream(InputStream is) throws Exception{
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		byte[] buffer = new byte[1024];
//		int len = -1;
//		while((len = is.read(buffer))!=-1){
//			baos.write(buffer, 0, len);
//		}
//		is.close();
//		String temp =  baos.toString();
//		if(temp.contains("charset=utf-8")){
//			return temp;
//		}else if(temp.contains("gb2312")){
//			return baos.toString("gb2312");
//		}return null;
//	}
	
	/**
	 * 工具方法
	 * @param is 输入流
	 * @return 文本字符串
	 * @throws IOException
	 * @throws Exception
	 */
	public static String readStream(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while((len = is.read(buffer))!=-1){
			baos.write(buffer, 0, len);
		}
		is.close();
		String temp =  baos.toString();
		return temp;
	}
}
