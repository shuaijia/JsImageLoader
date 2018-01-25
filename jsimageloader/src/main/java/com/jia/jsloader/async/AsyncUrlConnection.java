package com.jia.jsloader.async;

import android.graphics.Bitmap;
import android.os.Message;


import com.jia.jsloader.cache.ImageCacheUtil;
import com.jia.jsloader.cache.JsonCacheUtil;
import com.jia.jsloader.domain.RequestVo;
import com.jia.jsloader.thread.MyThreadFactory;

import java.io.IOException;

/**
 * Created by hm on 2016/3/24.
 */
public class AsyncUrlConnection {


    private static final int BITMAP_SUCCESS=1;
    private static final int BITMAP_ERROR=2;
    private static final int OBJECT_SUCCESS=3;
    private static final int OBJECT_ERROR=4;

    /**
     * 当前的方法完成的操作是根据网络的链接，获取对应的图片对象的异步操作方法
     *
     * @param saveTime
     * @param netUrl
     * @param asyncHandler
     */
    public static void getBitmap(final long saveTime, final String netUrl, final AsyncHandler asyncHandler){

//        new Thread(){
//            @Override
//            public void run() {
//
//                Bitmap bitmap= null;
//                try {
//                    bitmap = ImageCacheUtil.getBitmapByThreeCache(saveTime,netUrl);
//                    if(null!=bitmap){
//                        Message msg=Message.obtain();
//                        msg.what=BITMAP_SUCCESS;
//                        msg.obj=bitmap;
//                        asyncHandler.sendMessage(msg);
//                    }else{
//                        Message msg=Message.obtain();
//                        msg.what=BITMAP_ERROR;
//                        msg.obj="网络错误！";
//                        asyncHandler.sendMessage(msg);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Message msg=Message.obtain();
//                    msg.what=BITMAP_ERROR;
//                    msg.obj="网络错误！";
//                    asyncHandler.sendMessage(msg);
//                }
//
//
//            }
//        }.start();

        MyThreadFactory.getThreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap= null;
                try {
                    bitmap = ImageCacheUtil.getBitmapByThreeCache(saveTime,netUrl);
                    if(null!=bitmap){
                        Message msg= Message.obtain();
                        msg.what=BITMAP_SUCCESS;
                        msg.obj=bitmap;
                        asyncHandler.sendMessage(msg);
                    }else{
                     //   System.out.println("BITMAP_ERROR===Bitmap为空！！！");
                        Message msg= Message.obtain();
                        msg.what=BITMAP_ERROR;
                        msg.obj="网络错误！";
                        asyncHandler.sendMessage(msg);
                    }
                } catch (IOException e) {
                   // System.out.println("BITMAP_ERROR===Bitmap出现异常：IOException");
                    e.printStackTrace();
                    Message msg= Message.obtain();
                    msg.what=BITMAP_ERROR;
                    msg.obj="网络错误！";
                    asyncHandler.sendMessage(msg);
                }
            }
        });

    }

    public static void getObject(final RequestVo vo, final AsyncHandler asyncHandler){

    	 MyThreadFactory.getThreadPoolExecutor().execute(new Runnable() {
             @Override
             public void run() {
            	 Object object=null;
                 try {
                     object= JsonCacheUtil.getBeanByNetORCache(vo);
                     if(null!=object){
                         Message msg= Message.obtain();
                         msg.what=OBJECT_SUCCESS;
                         msg.obj=object;
                         asyncHandler.sendMessage(msg);
                     }else{
                         Message msg= Message.obtain();
                         msg.what=OBJECT_ERROR;
                         msg.obj= "网络错误！";
                         asyncHandler.sendMessage(msg);
                     }
                 } catch (IOException e) {
                     e.printStackTrace();
                     Message msg= Message.obtain();
                     msg.what=OBJECT_ERROR;
                     msg.obj="网络错误！";
                     asyncHandler.sendMessage(msg);
                 }

             }
         });
    }
}
