package com.jia.jsloader;

import android.content.Context;
import android.widget.ImageView;

/**
 * Description:
 * Created by jia on 2018/1/25.
 * 人之所以能，是相信能
 */
public class JsLoader {

    private Context context;

    private ImageView targetView;

    private String url;

    private int defaultImg;

    private int errorImg;

    private int scaleType;

    private static JsLoader instance = new JsLoader();

    private JsLoader() {

    }

    public static JsLoader with(Context context) {
        instance.setContext(context);
        return instance;
    }

    public Context getContext() {
        return context;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public ImageView getTargetView() {
        return targetView;
    }

    public void setTargetView(ImageView targetView) {
        this.targetView = targetView;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDefauleImg() {
        return defaultImg;
    }

    public void setDefauleImg(int defaultImg) {
        this.defaultImg = defaultImg;
    }

    public int getErrorImg() {
        return errorImg;
    }

    public void setErrorImg(int errorImg) {
        this.errorImg = errorImg;
    }

    public int getScrolType() {
        return scaleType;
    }

    public void setScrolType(int scaleType) {
        this.scaleType = scaleType;
    }

}
