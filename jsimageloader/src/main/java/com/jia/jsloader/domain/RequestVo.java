package com.jia.jsloader.domain;

import android.content.Context;


import com.jia.jsloader.parser.BaseParser;

import java.io.File;
import java.util.HashMap;


/**
 * Created by hm on 2016/1/12.
 *
*  这里是关于自定义网络框架的网络请求封装的请求的JavaBean
        *  主要用来封装关于网络请求过程主要的参数
        */
public class RequestVo {

    /**
     * 当前网络请求的类型
     * 1、HTTP_GET     http协议的Get请求
     * 2、HTTP_POST    http协议的Post请求
     * 3、HTTPS_GET    https协议的Get请求（客户端不包含证书访问）
     * 4、HTTPS_POST   https协议的Post请求（客户端不包含证书的访问）
     */
    private int requestType;
    /**
     * 关于网络访问的url
     * 1、使用Get请求的操作的时候，需要将参数直接拼接到baseUrl后
     * 2、使用Post请求的操作的时候，这里的baseUrl直接存储post请求的url参数
     *    至于Post请求的参数直接放到HashMap集合中
     */
    private String baseUrl;
    /**
     * 用来存储使用Post请求操作的时候，使用的参数
     */
    private HashMap<String,String> paramStringHashMap=new HashMap<String, String>();
    /**
     * 关于Post请求中文件的存储集合
     */
    private HashMap<String,File> paramFileHashMap=new HashMap<String,File>();
    /**
     * 由于每个接口的返回数据不同，要根据对应的JavaBean进行解析
     */
    private BaseParser<?> jsonParser;
    /**
     * 上下文环境
     */
    private Context mContext;
    /**
     * 关于是否提示用户当前的网络环境状态
     * 1、true：提示
     * 2、false：不提示（默认情况下不提示用户）
     */
    private boolean isShowWifi=false;
    /**
     * 关于有网络情况下是否进行数据缓存操作
     * 1、true：进行缓存操作（默认情况下进行数据缓存）
     * 2、false：不进行数据的缓存操作
     * 使用建议：
     * 1、具有资源性质的接口返回数据具有保存价值的可以选择true
     * 2、不具有长时间保存意义的接口返回数据建议设置为false
     */
    private boolean isSaveDataWithNet=true;
    /**
     * 这里是关于APP在没有网络连接的时候为了一段时间正常显示界面数据而设置的数据缓存时间
     * 默认缓存时间为10minute
     */
    private long dataSaveTimeNoNet;
    /**
     * 无参构造
     */
    public RequestVo() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * 有参构造
     * @param requestType         标识网络请求类型，参照RequestType中的常量
     * @param baseUrl             网络请求的URL
     * @param paramStringHashMap  Post请求的时候封装的请求参数的集合
     * @param paramFileHashMap    Post请求的时候封装的File类型参数
     * @param jsonParser          解析的支持类Parser
     * @param mContext            上下文环境
     * @param isShowWifi          是否提示用户处于的网络环境（true：表示提示，默认为false）
     * @param isSaveDataWithNet   网络正常的情况下是否选择缓存数据
     * @param dataSaveTimeNoNet   没有网络连接的时候缓存数据保存的时间
     */
    public RequestVo(int requestType, String baseUrl, HashMap<String, String> paramStringHashMap, HashMap<String, File> paramFileHashMap, BaseParser<?> jsonParser, Context mContext, boolean isShowWifi, boolean isSaveDataWithNet, long dataSaveTimeNoNet) {
        this.requestType = requestType;
        this.baseUrl = baseUrl;
        this.paramStringHashMap = paramStringHashMap;
        this.paramFileHashMap = paramFileHashMap;
        this.jsonParser = jsonParser;
        this.mContext = mContext;
        this.isShowWifi = isShowWifi;
        this.isSaveDataWithNet = isSaveDataWithNet;
        this.dataSaveTimeNoNet = dataSaveTimeNoNet;
    }



    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public HashMap<String, String> getParamStringHashMap() {
        return paramStringHashMap;
    }

    public void setParamStringHashMap(HashMap<String, String> paramStringHashMap) {
        this.paramStringHashMap = paramStringHashMap;
    }

    public HashMap<String, File> getParamFileHashMap() {
        return paramFileHashMap;
    }

    public void setParamFileHashMap(HashMap<String, File> paramFileHashMap) {
        this.paramFileHashMap = paramFileHashMap;
    }

    public BaseParser<?> getJsonParser() {
        return jsonParser;
    }

    public void setJsonParser(BaseParser<?> jsonParser) {
        this.jsonParser = jsonParser;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public boolean isShowWifi() {
        return isShowWifi;
    }

    public void setIsShowWifi(boolean isShowWifi) {
        this.isShowWifi = isShowWifi;
    }

    public boolean isSaveDataWithNet() {
        return isSaveDataWithNet;
    }

    public void setIsSaveDataWithNet(boolean isSaveDataWithNet) {
        this.isSaveDataWithNet = isSaveDataWithNet;
    }

    public long getDataSaveTimeNoNet() {
        return dataSaveTimeNoNet;
    }

    public void setDataSaveTimeNoNet(long dataSaveTimeNoNet) {
        this.dataSaveTimeNoNet = dataSaveTimeNoNet;
    }

    @Override
    public String toString() {
        return "RequestVo{" +
                "requestType=" + requestType +
                ", baseUrl='" + baseUrl + '\'' +
                ", paramStringHashMap=" + paramStringHashMap +
                ", paramFileHashMap=" + paramFileHashMap +
                ", jsonParser=" + jsonParser +
                ", mContext=" + mContext +
                ", isShowWifi=" + isShowWifi +
                ", isSaveDataWithNet=" + isSaveDataWithNet +
                ", dataSaveTimeNoNet=" + dataSaveTimeNoNet +
                '}';
    }
}
