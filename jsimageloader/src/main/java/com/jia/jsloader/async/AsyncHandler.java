package com.jia.jsloader.async;

import android.os.Handler;
import android.os.Message;

/**
 * Created by hm on 2016/3/24.
 */
public abstract class AsyncHandler<T> extends Handler {

    private static final int BITMAP_SUCCESS=1;
    private static final int BITMAP_ERROR=2;
    private static final int OBJECT_SUCCESS=3;
    private static final int OBJECT_ERROR=4;
    @Override
    public void handleMessage(Message msg) {

        T res=null;
        String str=null;
        switch (msg.what){

            case BITMAP_SUCCESS:
              //  System.out.println("Handler====BITMAP_SUCCESS消息发送成功！！");
                res= (T) msg.obj;
                onSuccess(res);
                break;
            case BITMAP_ERROR:
              //  System.out.println("Handler====BITMAP_ERROR消息发送失败！！");
                str= (String) msg.obj;
                onFail(str);
                break;
            case OBJECT_SUCCESS:
                res= (T) msg.obj;
                onSuccess(res);
                break;
            case OBJECT_ERROR:
                str= (String) msg.obj;
                onFail(str);
                break;

        }
    }
    public abstract void onSuccess(T result);
    public abstract void onFail(String str);
}
