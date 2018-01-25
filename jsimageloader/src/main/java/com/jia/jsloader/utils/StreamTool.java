package com.jia.jsloader.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author hm
 * 当前类的功能是完成操作：
 * 1、关于IO的一些操作
 * 2、关于File的一些操作
 */
public class StreamTool {

	/**
	 * 该方法的功能是完成将流数据转换成为String的操作
	 * @param inputStream           读取的数据输入流
	 * @return
	 * @throws IOException IO操作中出现的IO异常
	 */
	public static String getStringByIO(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		//这个操作is.read(buffer)出现IOException
		//Log.e("Tag", inputStream.toString());
		while((len = inputStream.read(buffer))!=-1){
			baos.write(buffer, 0, len);
		}
		//这个操作is.close()出现IOException
		inputStream.close();
		String temp =  baos.toString();
		return temp;
	}

	/**
	 * 通过流获取流中的图片
	 *
	 * @param inputStream    输入的参数IO流
	 * @return
	 */
	public static Bitmap getBitmapByIO(InputStream inputStream){

		Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
		return bitmap;

	}



}
