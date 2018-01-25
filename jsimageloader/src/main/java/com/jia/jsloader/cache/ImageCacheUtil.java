package com.jia.jsloader.cache;

import android.graphics.Bitmap;


import com.jia.jsloader.net.Net;
import com.jia.jsloader.utils.FileUtil;
import com.jia.jsloader.utils.StreamTool;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hm on 2016/3/14.
 *
 *   当前类完成的操作是对于网络交互中的图片资源进行缓存操作，
 *   即：俗称的三级缓存图片，在图片出现错误的时候展示默认图片
 *      1、内存级别的缓存操作
 *      2、SD级别的缓存操作
 *
 *    a、对于内存中的缓存操作简单逻辑包括：
 *       LinkedHashMap ： 具有可排序性、具有可限制大小
 *       CurrentHashMap ： 高并发、高吞吐、线程安全
 *    b、对于SD卡中的缓存逻辑简单逻辑包括：SD卡的文件存储和获取
 *
 *  **遵从LRU算法   最近最少使用
 *  **借助
 *    强引用（StrongReference） 正常使用的类的引用即为强引用，只有在对其的所有引用计数归零的时候才被回收
 *    软引用（SoftReference）
 *    弱引用（WeakReference）
 *    虚引用（PhantomReference）
 */
public class ImageCacheUtil {


    /**
     * 将构造方法进行私有化，使不能创建当前类的实例对象，直接使用类调用工具方法
     */
    private ImageCacheUtil(){}


    /**
     * 这里定义的方法完成的操作是关于图片的三级缓存方案
     * 1、内存中（包括两级：强引用、软引用）
     * 2、手机的SD卡或者应用内部存储空间
     *
     * @param netUrl
     * @return
     */
    public static Bitmap getBitmapByThreeCache(long saveTime, final String netUrl) throws IOException {

       // System.out.println("代码运行在线程号：："+Thread.currentThread().getId());

        //定义变量用来保存三级缓存中的Bitmap对象
        Bitmap threeCacheBitmap=null;
        //定义变量用来保存SD卡或机身内存中图片的保存路径
        String threeBitmapPath=null;
        //1、先从内存中获取图片
        threeCacheBitmap=CacheRAM.getBitmapFromRAM(netUrl);
        if(null!=threeCacheBitmap){
            return threeCacheBitmap;
        }else{
            //2、内存中不包括图片的时候从机身内存或者SD卡中获取图片
            threeCacheBitmap= FileUtil.getBitmapFromSD(saveTime,netUrl);
            if(null!=threeCacheBitmap){

                //2.3、将获取的图片保存到Map中
                CacheRAM.putBitmapToRAM(netUrl, threeCacheBitmap);
            }else{
                //3、从网络获取图片
                threeCacheBitmap=getBitmapFromNetUrl(netUrl);


                if(null!=threeCacheBitmap){

                    final Bitmap finalThreeCacheBitmap = threeCacheBitmap;
                    new Thread(){
                        @Override
                        public void run() {
                            //3.1、从网络获取图片
                            //3.2、将图片压缩后的保存到SD卡或机身内存中
                            FileUtil.putBitmapToSD(netUrl, finalThreeCacheBitmap);
                            //3.4、将图片保存到Map中
                            CacheRAM.putBitmapToRAM(netUrl, finalThreeCacheBitmap);
                        }
                    }.start();

                }else{
                    //此时网络出现问题，整个框架获取不到图片
//                    throw new StructException("NetConnection has Error!");
                //	threeCacheBitmap = BitmapFactory.decodeResource(BaseApplication.getMainContext().getResources(),R.mipmap.video_location_bg);

//                    return null;
                }

            }
        }
        return threeCacheBitmap;

    }

    /**
     * 这里定义的方法完成的操作使用根据给定义的Url获取对应的Bitmap对象，并完成相应的数据的缓存操作
     * 注意：
     * 1、对于网络的链接中不区分网络的请求头类型：Http、Https
     * 2、默认进行数据缓存，不可更改
     *
     * 图片缓存的业务分析：
     * 1、工具类调用当前的方法，并传递图片的网络连接，将该网络连接作为对应图片的唯一标志
     * 2、先根据这个唯一标志的url，在内存中的Map集合中进行查找（集合中的逻辑定义一个单独的方法）
     * 3、根据Map中查找的对象的结果进行不容的业务操作，
     *    a、查找到对象--返回出想要的Bitmap
     *    b、进行网络访问的操作，并将网络中的数据缓存到Map和SD卡中
     *
     * @param netUrl    网络的图片资源链接
     * @return
     */
    private static Bitmap getBitmapFromNetUrl(final String netUrl) throws IOException {

        Bitmap returnBitmap =null;
        InputStream is=null;
        //截取连接的前五个字符，进行比较是http、https
        String urlHead=netUrl.substring(0,5);
        switch(urlHead){

            case "https":
                is= Net.getHttpsIOByGet(netUrl);
                break;
            case "http:":
                is=Net.getHttpIOByGet(netUrl);
                break;
        }

        returnBitmap= StreamTool.getBitmapByIO(is);
//        returnBitmap =ImageUtil.getCompressBitmap(null, returnBitmap,0,0,100);
        return returnBitmap;

    }


}
