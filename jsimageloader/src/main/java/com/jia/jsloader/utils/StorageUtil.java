package com.jia.jsloader.utils;

import android.os.Environment;
import android.text.format.Formatter;

import com.jia.jsloader.base.JsApplication;

import java.io.File;


/**
 * Created by hm on 2016/1/14.
 *
 *  这里的工具类完成的操作是关于手机的存储相关的控制方法
 *
 *  简介手机的存储体系
 *  1、手机的运行内存（即PC的内存条）
 *  2、手机的出厂内存（一般说的16G\32G\64G等，即PC的硬盘）
 *  3、手机的SD卡（即PC的移动存储器U盘等）
 *
 *  简述手机的资源获取路径：
 *  1、手机应用的本身的缓存目录
 *  2、手机的SD卡
 *  3、手机的出厂内存中获取资源即手机的根目录
 *
 *  ***********
 *  不同的手机由于系统版本、厂家的不同，存在多样问题：
 *  注意点：
 *  a、Android系统版本的区别
 *  b、手机厂家的区别
 *
 *  ==============================================================
 *  简单知识综述：
 *  1、应用内部的目录（不需要权限）
 *  context.getFileDir()  获取的是长期存储数据的目录的文件对象    类似于： data/data/packagename/files
 *                         在手机的设置中，点击清除数据，需要点击确认之后才可以删除
 *  context.getCacheDir()  获取的是手机中短期保存数据的目录的文件对象   类似于： data/data/packagename/cache
 *                           在手机的设置中，点击清除缓存，不需要再次确认即可删除数据，
 *                           手机存储空间不足的时候系统可以删除该区域文件
 *  2、应用外部的目录如SD卡（需要权限）
 *  Environment.getExternalStorageDirectory()
 *                        获取的是SD卡中存储根目录，   类似于： mnt/sdcard/info.txt
 *  权限：WRITE_EXTERNAL_STORAGE   READ_EXTERNAL_STORAGE
 *
 */
public class StorageUtil {

    /**
     * 这个方法完成的操作是判断当前的手机的SD卡是否存在，并且可读可写
     *
     *
     * @return   SD卡存在返回true  SD卡不存在返回false
     */
    public static boolean isPhoneHaveSD(){

//        return false;
        return !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡的整个大小，单位为byte
     *
     * @return
     */
    public static long getTotalSizeOfSD(){

        return Environment.getExternalStorageDirectory().getTotalSpace();
    }

    /**
     * 获取SD卡当前的可用存储空间大小，返回的值得单位是byte
     *
     * @return
     */
    public static long getUsefulSizeOfSD(){

        return Environment.getExternalStorageDirectory().getUsableSpace();
//        return Environment.getExternalStorageDirectory().getFreeSpace();

    }

    /**
     * 将传递进来的数据进行格式化操作，返回的字符串是包含具体单位的数据
     *
     * @return
     */
    public static String formatBytes(long byteSizes){

        return Formatter.formatFileSize(JsApplication.getMainContext(),byteSizes);
    }

    /**
     * 获取关于SD根目录的文件对象
     *
     * @return
     */
    public static File getFileBySD(){

        return Environment.getExternalStorageDirectory();
    }

    /**
     * 获取关于SD根目录的路径的字符串
     * 返回的数据类似于：mnt/sdcard/...
     * @return
     */
    public static String getPathBySD(){

//        return Environment.getExternalStorageDirectory().getPath();
//        Environment.getExternalStorageDirectory().
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
    }


    /**
     * 返回的数据是手机中可以长久保存数据的目录的文件对象
     *
     * @return
     */
    public static File getFileByLongStorge(){

        return JsApplication.getMainContext().getFilesDir();
    }

    /**
     * 返回的数据是手机中可以长久保存数据的目录的文件对象对应的路径字符串
     *
     * @return
     */
    public static String getPathByLongStorge(){


        return JsApplication.getMainContext().getFilesDir().getAbsolutePath()+"/";
    }

    /**
     * 返回的数据是手机中的缓存区域目录对应的文件对象
     *
     * @return
     */
    public static File getFileBycache(){

        return JsApplication.getMainContext().getCacheDir();
    }

    /**
     * 返回的数据是手机中的缓存区域目录对应的文件对象
     *
     * @return
     */
    public static String getPathBycache(){
        return JsApplication.getMainContext().getCacheDir().getPath()+"/";
    }



}
