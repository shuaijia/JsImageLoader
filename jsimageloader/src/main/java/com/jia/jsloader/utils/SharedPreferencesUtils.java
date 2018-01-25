package com.jia.jsloader.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 这是一个关于存储于获取SharedPreferences数据的工具类，其中封装了系统存储与获取的方法
 * 关于这个工具类的设计思想：java代码中关于方法的重载操作
 * @author hm
 *
 */
public class SharedPreferencesUtils {
	
	//定义关于存储的sp文件的名称
	public static String SNAME = "jsimageloader";
	//定义SharedPreferences的对象
	private static SharedPreferences sharedPreferences;

	/**
	 * 这个方法是关于存储字符串数据的工具方法
	 * @param context 需要的上下文
	 * @param key     存储数据的key
	 * @param value   存储数据的value
	 */
	public static void saveData(Context context, String key, String value) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SNAME,
					Context.MODE_PRIVATE);
		}

		sharedPreferences.edit().putString(key, value).commit();
	}

	/**
	 * 这个方法是关于存储布尔型数据的工具方法
	 * @param context 需要的上下文
	 * @param key     存储数据的key
	 * @param value   存储数据的value
	 */
	public static void saveData(Context context, String key, boolean value) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SNAME,
					Context.MODE_PRIVATE);
		}

		sharedPreferences.edit().putBoolean(key, value).commit();
	}

	/**
	 * 这个方法是关于存储float数据的工具方法
	 * @param context 需要的上下文
	 * @param key     存储数据的key
	 * @param value   存储数据的value
	 */
	public static void saveData(Context context, String key, float value) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SNAME,
					Context.MODE_PRIVATE);
		}

		sharedPreferences.edit().putFloat(key, value).commit();
	}

	/**
	 * 这个方法是关于存储int数据的工具方法
	 * @param context 需要的上下文
	 * @param key     存储数据的key
	 * @param value   存储数据的value
	 */
	public static void saveData(Context context, String key, int value) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SNAME,
					Context.MODE_PRIVATE);
		}

		sharedPreferences.edit().putInt(key, value).commit();
	}

	/**
	 * 这个方法是关于存储long数据的工具方法
	 * @param context 需要的上下文
	 * @param key     存储数据的key
	 * @param value   存储数据的value
	 */
	public static void saveData(Context context, String key, long value) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SNAME,
					Context.MODE_PRIVATE);
		}

		sharedPreferences.edit().putLong(key, value).commit();
	}

	//===================================================================
	
	/**
	 * 这里定义的是关于从sp中获取存储的数据的工具方法
	 * @param context  表示上下文
	 * @param key      关于存储数据的
	 * @param defValue 当想要获取的数据不存的时候，给定的默认值数据
	 * @return         方法的返回值
	 */
	public static String getData(Context context, String key, String defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SNAME,
					Context.MODE_PRIVATE);
		}
		return sharedPreferences.getString(key, defValue);
	}

	/**
	 * 这里定义的是关于从sp中获取存储的数据的工具方法
	 * @param context  表示上下文
	 * @param key      关于存储数据的
	 * @param defValue 当想要获取的数据不存的时候，给定的默认值数据
	 * @return         方法的返回值
	 */
	public static boolean getData(Context context, String key, boolean defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SNAME,
					Context.MODE_PRIVATE);
		}
		return sharedPreferences.getBoolean(key, defValue);
	}

	/**
	 * 这里定义的是关于从sp中获取存储的数据的工具方法
	 * @param context  表示上下文
	 * @param key      关于存储数据的
	 * @param defValue 当想要获取的数据不存的时候，给定的默认值数据
	 * @return         方法的返回值
	 */
	public static float getData(Context context, String key, float defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SNAME,
					Context.MODE_PRIVATE);
		}
		return sharedPreferences.getFloat(key, defValue);
	}

	/**
	 * 这里定义的是关于从sp中获取存储的数据的工具方法
	 * @param context  表示上下文
	 * @param key      关于存储数据的
	 * @param defValue 当想要获取的数据不存的时候，给定的默认值数据
	 * @return         方法的返回值
	 */
	public static int getData(Context context, String key, int defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SNAME,
					Context.MODE_PRIVATE);
		}
		return sharedPreferences.getInt(key, defValue);
	}

	/**
	 * 这里定义的是关于从sp中获取存储的数据的工具方法
	 * @param context  表示上下文
	 * @param key      关于存储数据的
	 * @param defValue 当想要获取的数据不存的时候，给定的默认值数据
	 * @return         方法的返回值
	 */
	public static long getData(Context context, String key, long defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SNAME,
					Context.MODE_PRIVATE);
		}
		return sharedPreferences.getLong(key, defValue);
	}

	
	//========================================================================
	public static boolean deltetData(Context context, String key) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SNAME,
					Context.MODE_PRIVATE);
		}
		return sharedPreferences.edit().remove(key).commit();
	}
	
}
