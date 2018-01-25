package com.jia.jsloader.domain;

/**
 * Created by hm on 2016/1/12.
 *
 *  这里是关于网络请求操作中关于网络访问的几种形式的定义的常量类
 *
 */
public class RequestType {

    /**
     * 当前网络请求的类型
     * 1、HTTP_GET     http协议的Get请求
     * 2、HTTP_POST    http协议的Post请求
     * 3、HTTPS_GET    https协议的Get请求（客户端不包含证书访问）
     * 4、HTTPS_POST   https协议的Post请求（客户端不包含证书的访问）
     */
    public static final int HTTP_GET=1;
    public static final int HTTP_POST=2;
    public static final int HTTPS_GET=3;
    public static final int HTTPS_POST=4;


}
