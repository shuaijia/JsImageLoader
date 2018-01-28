package com.jia.jsloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.jia.jsloader.async.AsyncHandler;
import com.jia.jsloader.async.AsyncUrlConnection;

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

    private long cacheTime;

    private static JsLoader instance;

    private JsLoader() {

    }


    public static JsLoader with(@NonNull Context context) {
        if (null == context) {
            throw new NullPointerException();
        }

        instance = new JsLoader();
        instance.setContext(context);
        return instance;
    }

    public static JsLoader load(@NonNull String url) {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException();
        }
        instance.setUrl(url);
        return instance;
    }

    public static JsLoader defaultImg(int imgId) {
        instance.setDefaultImg(imgId);
        return instance;
    }

    public static JsLoader errorImg(int imgId) {
        instance.setErrorImg(imgId);
        return instance;
    }

    public static JsLoader cacheTime(long cacheTime) {
        instance.setCacheTime(cacheTime);
        return instance;
    }

    public static void into(@NonNull final ImageView imageView) {

        if (null == imageView) {
            throw new NullPointerException();
        }

        if (instance.getDefaultImg() != 0) {
            imageView.setImageResource(instance.getDefaultImg());
        }

        long cacheTime = 5000;
        if (instance.getCacheTime() != 0) {
            cacheTime = instance.getCacheTime();
        }

        AsyncUrlConnection.getBitmap(cacheTime, instance.getUrl(), new AsyncHandler() {
            @Override
            public void onSuccess(Object result) {
                imageView.setImageBitmap((Bitmap) result);
            }

            @Override
            public void onFail(String str) {
                if (instance.getErrorImg() != 0) {
                    imageView.setImageResource(instance.getErrorImg());
                }
            }
        });
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

    public int getDefaultImg() {
        return defaultImg;
    }

    public void setDefaultImg(int defaultImg) {
        this.defaultImg = defaultImg;
    }

    public int getErrorImg() {
        return errorImg;
    }

    public void setErrorImg(int errorImg) {
        this.errorImg = errorImg;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

}
