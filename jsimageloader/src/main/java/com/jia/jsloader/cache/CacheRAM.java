package com.jia.jsloader.cache;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hm on 2016/3/25.
 *
 *   当前类是整个框架关于数据缓存的类，其中可以在内存中缓存Bitmap和String（Json）
 *
 *   思路分析：：
 *   为了节省内存空间的占用和降低代码的重复度，现在当前类中创建几个相关的Map集合，并且在提供存储和获取的方法
 */
public class CacheRAM {

    private CacheRAM(){}

    //=============================================================================
    //关于图片的内存中的缓存保存的map集合
    //=======================================

    /**
     * 定义LinkedHashMap集合的大小限制，默认设置为30，表示常用的图片中有30个
     * 这里的LinkedHashMap作为第一级的缓存，保存强引用的文件
     */
    private static final int M_LINK_SIZE=30;

    /**
     * 定义的内存中的二级缓存，即保存若引用的位置的HashMap
     * ConcurrentHashMap：
     * 特点：高并发、安全的集合、存储量大
     */
    private static Map<String, SoftReference<Bitmap>> mWeakBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
            M_LINK_SIZE / 2);


    /**
     * 定义的内存中的一级缓存，即保存作为强引用的位置的HashMap
     *
     * LinkedHashMap：
     * 特点：
     *     可以限制大小、同时数据有序
     *
     *      数据结构中的链表结构理解
     *
     */
    private static final HashMap<String,Bitmap> mHardBitmapCache=new LinkedHashMap<String,Bitmap>(
            M_LINK_SIZE/2,0.75f,true){

        /**
         * 这个方法是是put或putAll时调用,默认返回false,表示添加数据时不移除最旧的数据.
         * @param eldest
         * @return
         */
        @Override
        protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
            if (size() > M_LINK_SIZE) {
                // 当map的size大于30时，把最近不常用的key放到mSoftBitmapCache中，从而保证mHardBitmapCache的效率
                Bitmap value = eldest.getValue();
                if (value != null) {
                    mWeakBitmapCache.put(eldest.getKey(),new SoftReference<Bitmap>(value));
                }
                return true;
            }
            return false;
        }
    };

    //=============================================================================
    //关于字符串String的内存中的缓存保存的map集合
    //=======================================

    /**
     * 定义的内存中的二级缓存，即保存若引用的位置的HashMap
     */
    private static Map<String, SoftReference<String>> mWeakStringCache = new ConcurrentHashMap<String, SoftReference<String>>(
            M_LINK_SIZE / 2);

    /**
     * 定义的内存中的一级缓存，即保存作为强引用的位置的HashMap
     */
    private static final HashMap<String,String> mHardStringCache=new LinkedHashMap<String,String>(
            M_LINK_SIZE/2,0.75f,true){

        /**
         * 这个方法是是put或putAll时调用,默认返回false,表示添加数据时不移除最旧的数据.
         * @param eldest
         * @return
         */
        @Override
        protected boolean removeEldestEntry(Entry<String, String> eldest) {
            if (size() > M_LINK_SIZE) {
                // 当map的size大于30时，把最近不常用的key放到mSoftBitmapCache中，从而保证mHardBitmapCache的效率
                String value = eldest.getValue();
                if (value != null) {
                    mWeakStringCache.put(eldest.getKey(),new SoftReference<String>(value));
                }
                return true;
            }
            return false;
        }
    };


    /**
     * 当前方法的作用是对于内存中缓存的Map集合进行清除，同时调用系统的垃圾回收机制
     */
    public static void clearAllMap() {
        mHardBitmapCache.clear();
        mWeakBitmapCache.clear();
        mHardStringCache.clear();
        mWeakStringCache.clear();
        //垃圾回收机制
        System.gc();
    }

    /**
     * 这里定义的操作方法完成的是从内存中的Map中获取图片的对象
     * 既然已经在内存中了，默认已经完成了压缩
     *
     * @param netUrlKey  作为图片在Map中唯一标志的网络图片URL
     * @return
     */
    public static Bitmap getBitmapFromRAM(String netUrlKey){

        if(mHardBitmapCache.containsKey(netUrlKey)){

            Bitmap usefulBitmap=mHardBitmapCache.get(netUrlKey);
            if(null!=usefulBitmap){
                //如果存在正在内存中的Bitmap图片，将图片的使用级别向前提，并返回Bitmap对象
                mHardBitmapCache.remove(netUrlKey);
                mHardBitmapCache.put(netUrlKey,usefulBitmap);
                return usefulBitmap;

            }else{
                //这里的情况是虽然在集合中包含对应的Key但是通过key得不到对应的Bitmap，此时将
                //key从Map中清楚，并返回null
                mHardBitmapCache.remove(netUrlKey);
                return null;
            }
        }else{
            //如果在强引用中不包含对应的key，那么在软引用中进行查找
            if(mWeakBitmapCache.containsKey(netUrlKey)){
                SoftReference<Bitmap> usefulSoftBitmap=mWeakBitmapCache.get(netUrlKey);
                if(null!=usefulSoftBitmap){
                    //从软应用中获取出对应的Bitmap对象
                    Bitmap usefulBitmap = usefulSoftBitmap.get();
                    if(null!=usefulBitmap){
                        //将软引用中的低级别图片转移到强引用中
                        mHardBitmapCache.put(netUrlKey,usefulBitmap);
                        return usefulBitmap;
                    }else{
                        //软引用中包含key但是获取不到图片
                        mWeakBitmapCache.remove(netUrlKey);
                        return null;
                    }

                }else{
                    //软引用中包含key但是获取不到图片
                    mWeakBitmapCache.remove(netUrlKey);
                    return null;
                }
            }else{
                //软引用中也不包括这个key，那么从判断SD卡中是否存在这个资源图片
                return null;
            }
        }
    }

    /**
     * 当前方法完成的操作是关于将图片的对象Bitmap保存到内存中，内存中使用的是Map集合
     *
     * @param netUrl      图片的网络连接作为图片在Map中的key
     * @param saveBitmap
     * @return
     */
    public static boolean putBitmapToRAM(String netUrl, Bitmap saveBitmap){
        if(null!=netUrl&&null!=saveBitmap){
            //将图片的对象保存到强引用对应的Map中
            mHardBitmapCache.put(netUrl,saveBitmap);
            //将图片对象保存到软引用对应的Map中
            mWeakBitmapCache.put(netUrl, new SoftReference<Bitmap>(saveBitmap));
            //成功保存后返回true
            return true;
        }else{
            return false;
        }

    }


    /**
     * 当前方法完成的操作是关于保存数据到内存中的Map中方法
     *
     * @param netUrlKey
     * @param saveData
     * @return
     */
    public static boolean putStringToRAM(String netUrlKey, String saveData){
        if(null!=netUrlKey&&null!=saveData){
            //将图片的对象保存到强引用对应的Map中
            mHardStringCache.put(netUrlKey,saveData);
            //将图片对象保存到软引用对应的Map中
            mWeakStringCache.put(netUrlKey, new SoftReference<String>(saveData));
            //成功保存后返回true
            return true;
        }else{
            return false;
        }

    }


    public static String getStringFromRAM(String netUrlKey){

        if(mHardStringCache.containsKey(netUrlKey)){

            String usefulString=mHardStringCache.get(netUrlKey);
            if(null!=usefulString){
                //如果存在正在内存中的Bitmap图片，将图片的使用级别向前提，并返回Bitmap对象
                mHardStringCache.remove(netUrlKey);
                mHardStringCache.put(netUrlKey,usefulString);
                return usefulString;

            }else{
                //这里的情况是虽然在集合中包含对应的Key但是通过key得不到对应的Bitmap，此时将
                //key从Map中清楚，并返回null
                mHardStringCache.remove(netUrlKey);
                return null;
            }
        }else{//如果在强引用中不包含对应的key，那么在软引用中进行查找
            if(mWeakStringCache.containsKey(netUrlKey)){
                SoftReference<String> usefulSoftString=mWeakStringCache.get(netUrlKey);
                if(null!=usefulSoftString){
                    //从软应用中获取出对应的Bitmap对象
                    String usefulString = usefulSoftString.get();
                    if(null!=usefulString){
                        //将软引用中的低级别图片转移到强引用中
                        mHardStringCache.put(netUrlKey,usefulString);
                        return usefulString;
                    }else{
                        //软引用中包含key但是获取不到图片
                        mWeakStringCache.remove(netUrlKey);
                        return null;
                    }

                }else{
                    //软引用中包含key但是获取不到图片
                    mWeakStringCache.remove(netUrlKey);
                    return null;
                }
            }else{
                //软引用中也不包括这个key，那么从判断SD卡中是否存在这个资源图片
                return null;
            }
        }
    }


}
