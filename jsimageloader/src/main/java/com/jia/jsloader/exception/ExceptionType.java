package com.jia.jsloader.exception;

/**
 * Created by hm on 2016/1/12.
 *
 *   作为一个常量类
 *   自定义异常类的辅助类，用来表示框架中可能存在的几种异常情况，
 *   主要包含的异常存在主要的几种情况：
 *   1、Json解析异常（IOException、ClientProtocolException）
 *   2、Internet访问超时异常
 *   3、服务器出现位置错误导致的异常返回
 *   4、返回数据出现差错（如：null）
 *   5、服务器302返回码（访问的资源已经不存在）
 */
public class ExceptionType {

    //Json解析中出现的异常
    public static final int JSON_EXCEPTION=1;
    //网络访问超时的异常
    public static final int NET_EXCEPTION=2;
    //数据为空的异常
    public static final int NULL_EXCEPTION=3;
    //服务器出现未知错误的异常（如高印所列举的情况，其他情况下服务器返回的包括状态码：。。。）
    public static final int SERVICE_EXCEPTION=4;


}
