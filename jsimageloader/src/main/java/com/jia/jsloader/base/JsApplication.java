package com.jia.jsloader.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.jia.jsloader.utils.NetStateUtils;


/**
 * 这里是本应用的最底层的Application的封装类
 * 简单解释：BaseApplication在清单文件中已经配置了，应用运行的时候首先会运行这个Application，这个时候根据
 * 线程的管理，会生成一个主线程，Application运行在主线程中
 *
 * @author jia
 */
public class JsApplication extends Application {

    //应用的上下文
    private static Application mContext;
    //主线程
    private static Thread mMainThread;
    //主线程ID
    private static int mMainThreadID = -1;
    //主线程中的Looper
    private static Looper mMainThreadLooper;
    //主线程的Handler
    private static Handler mMainThreadHandler;

    // 保利文件下载路径
    private String downloadPath;
    private String aeskey = "VXtlHmwfS2oYm0CZ";
    private String iv = "2u9gDPKdX6GyQJKU";

    // 当前网络状态
    public static int mNetWorkState;

    // 是否使用流量上网
    private static boolean isLiuliang;

    @Override
    public void onCreate() {
        super.onCreate();

        this.mContext = this;
        this.mMainThread = Thread.currentThread();
        this.mMainThreadID = android.os.Process.myTid();
        this.mMainThreadLooper = getMainLooper();
        this.mMainThreadHandler = new Handler();


        initNetState();
    }

    private void initNetState() {
        mNetWorkState = NetStateUtils.getNetworkState(this);
    }


    /**
     * 下面的五个方法是关于定义的成员变量的获取的方法,为工具了进行封装提供方便
     */

    public static Application getMainContext() {
        return mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static int getMaiThredId() {
        return mMainThreadID;
    }

    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    public static Handler getMainHandler() {
        return mMainThreadHandler;
    }

    public static boolean isLiuliang() {
        return isLiuliang;
    }

    public static void setLiuliang(boolean isLiuliang) {
        JsApplication.isLiuliang = isLiuliang;
    }


}
