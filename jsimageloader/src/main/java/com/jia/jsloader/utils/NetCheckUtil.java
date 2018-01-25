package com.jia.jsloader.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jia.jsloader.base.JsApplication;
import com.jia.jsloader.domain.RequestVo;


/**
 * Created by hm on 2016/1/12.
 *
 *   这里是个关于网络连接状态的检测的操作的工具类
 *
 */
public class NetCheckUtil {

    //表达网络环境是Wifi
    private static final int NET_TYPE_WIFI=1;
    //表示网络环境是流量
    private static final int NET_TYPE_MOBILE=2;
    //表示网络不存在
    private static final int NET_TYPE_ERROR=3;

    /**
     * 用来完成网络相关的操作
     * 1、判断网络的连接状态
     * 2、在流量环境下提示用户
     * @param vo
     * @return
     */
    public static boolean hasConnectedNetwork(RequestVo vo) {
        // 关于网络连接的管理者
        ConnectivityManager connectivityManager = (ConnectivityManager) vo.getmContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfoActivity = connectivityManager.getActiveNetworkInfo();
        // 表示没有网络
        if (null == mobNetInfoActivity || !mobNetInfoActivity.isAvailable()) {
            return false;
        } else {
            // 为true的时候表示提示用户处于流量的环境中
            if (vo.isShowWifi()&&(NET_TYPE_MOBILE==checkNetTypeAvailable(vo.getmContext()))) {
                if(!JsApplication.isLiuliang()){
                	ToastUtil.showToastSafe(vo.getmContext(), "您正在使用流量上网！");
                }
                return true;
            } else {
                return true;
            }
        }
    }

    /**
     * 检查是否有网络可用
     *
     * @param context
     * @return false表示没有可用网络 true表示有可用网络
     *
     */
    public static boolean checkNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return true;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检查是否有网络可用
     *
     * @param context
     * @return false表示没有可用网络 true表示有可用网络
     *
     */
    private static int checkNetTypeAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return NET_TYPE_ERROR;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            //此时表示使用的WIFI上网
                            return NET_TYPE_WIFI;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            //此时为使用流量上网
                            return NET_TYPE_MOBILE;
                        }
                    }
                }
            }
        }
        return NET_TYPE_ERROR;
    }

}
