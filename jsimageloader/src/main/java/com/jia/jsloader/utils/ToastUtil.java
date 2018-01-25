package com.jia.jsloader.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.jia.jsloader.base.JsApplication;


/**
 * 这里定义一个关于Toast的单例类，并且添加上关于线程的操作，保证在任何线程中都可以进行Toast的输出操作
 *
 * @author hm
 *
 *  简单介绍关于上下文的不同操作
 *  1、使用BaseApplication获取的上下文环境是关于整个项目的上下文环境，基于的资源是关于工程的
 *     可以用来获取诸如res目录下的数据
 *  2、在Activity中获取的上下文主要用来操作以Activity为基础的上下文环境中的数据
 *
 *
 */
public class ToastUtil {

	private static Toast toast;

	/**
	 * 对Toast的简易封装。线程安全，可以在非UI线程调用。
	 * @param context  上下文
	 * @param str      显示内容
	 */
	public static void showToastSafe(final Context context, final String str) {
		if (isRunInMainThread()) {
			showToast(context,str);
		} else {
			post(new Runnable() {
				@Override
				public void run() {
					showToast(context,str);
				}
			});
		}
	}
	/**
	 * 完成的是关于Toast的单例输出
	 * @param context
	 * @param msg
	 */
	private static void showToast(Context context, String msg){
		if(toast == null){
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		}

		toast.setText(msg);
		toast.show();
	}
	/**
	 * 判断当前的线程是不是在主线程
	 */
	private static boolean isRunInMainThread() {
		return android.os.Process.myTid() == getMainThreadId();
	}
	/**
	 *  在主线程执行runnable
	 */
	private static boolean post(Runnable runnable) {
		return getMainHandler().post(runnable);
	}
	/**
	 * 获取当前应用的主线程的Handler
	 * @return
	 */
	private static Handler getMainHandler(){
		return JsApplication.getMainHandler();
	}
	/**
	 * 获取主线程的ID
	 * @return
	 */
	private static int getMainThreadId(){
		return JsApplication.getMaiThredId();
	}


}
