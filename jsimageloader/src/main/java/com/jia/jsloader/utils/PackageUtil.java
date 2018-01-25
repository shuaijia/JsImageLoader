package com.jia.jsloader.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.jia.jsloader.base.JsApplication;


/**
 * Created by hm on 2016/1/22.
 *
 *  这里是关于应用管理器的操作类
 *  1、
 *
 */
public class PackageUtil {

    /**
     * 将构造私有化
     */
    private PackageUtil(){}

    /**
     * 获取当前应用的名称
     *
     * @return
     */
    public static String getAppName(){
        String appName=null;
        PackageManager pm= JsApplication.getMainContext().getPackageManager();
        try {
            PackageInfo info=pm.getPackageInfo(JsApplication.getMainContext().getPackageName(),0);
            int labelRes = info.applicationInfo.labelRes;
            appName=JsApplication.getMainContext().getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return appName;
    }

    /**
     * 获取当前应用的包名
     *
     * @return
     */
    public static String getAppPackageName(){

        return JsApplication.getMainContext().getPackageName();
    }


    /**
     * 获取当前应用的版本号信息
     *
     * @return
     */
    public static String getAppVersionCode(){

        String appVersionCode=null;
        try {
            PackageManager packageManager = JsApplication.getMainContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(JsApplication.getMainContext().getPackageName(), 0);
            appVersionCode= packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return appVersionCode;

    }

}
