package com.jia.jsloader.cache;

import android.graphics.Bitmap;


import com.jia.jsloader.utils.ImageUtil;
import com.jia.jsloader.utils.PackageUtil;
import com.jia.jsloader.utils.StorageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URLEncoder;

/**
 * Created by hm on 2016/3/25.
 *
 *   当前类完成的操作是将需要缓存的数据进行SD存储和获取的类
 */
public class CacheSD {

    /**
     * 这里完成的操作是判断传递进来的路径是否包括Bitmap对象，如果存在将Bitmap对象返回
     * 否则返回null
     *
     * @param netUrl   网络图片的网络路径作为文件名称
     * @return
     */
    public static Bitmap getBitmapFromSD(String netUrl){

        String imageSavePath=null;

        if(StorageUtil.isPhoneHaveSD()){
//            imageSavePath=StorageUtil.getPathBySD()+netUrl;
//            imageSavePath=new File(new File(StorageUtil.getPathBySD()), URLEncoder.encode(netUrl)).getAbsolutePath();

            //创建以SD卡根目录为路径的File对象
            File fileBySD=new File(StorageUtil.getPathBySD());
            //创建SD卡根目录下以当前应用包名为文件夹的文件对象，并验证是否存在当前目录
            File fileBySDSon=new File(fileBySD, PackageUtil.getAppPackageName());
//            File fileBySDSon=new File(fileBySD,"AA");
            if(fileBySDSon.exists()){
                //以包名为文件夹的对象存在的时候，通过将文件对象和图片的名称的拼接构建文件对象
                File imageFile=new File(fileBySDSon, URLEncoder.encode(netUrl));
                if(imageFile.exists()){
                    //图片文件对象存在的时候获取当前的图片对象对应的路径
                    imageSavePath=imageFile.getAbsolutePath();
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }else {

//            imageSavePath=StorageUtil.getPathBycache(context)+netUrl;
//            imageSavePath=new File(new File(StorageUtil.getPathBycache(context)), URLEncoder.encode(netUrl)).getAbsolutePath();

            //创建以Cache根目录为路径的File对象
            File fileByCache=new File(StorageUtil.getPathBycache());
            //创建SD卡根目录下以当前应用包名为文件夹的文件对象，并验证是否存在当前目录
            File fileByCacheSon=new File(fileByCache,PackageUtil.getAppPackageName());
//            File fileByCacheSon=new File(fileByCache,"AA");
            if(fileByCacheSon.exists()){
                //以包名为文件夹的对象存在的时候，通过将文件对象和图片的名称的拼接构建文件对象
                File imageFile=new File(fileByCacheSon, URLEncoder.encode(netUrl));
                if(imageFile.exists()){
                    //图片文件对象存在的时候获取当前的图片对象对应的路径
                    imageSavePath=imageFile.getAbsolutePath();
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }

        File imageFile = new File(imageSavePath);
        if(imageFile.exists()){
            /**
             * 这里的逻辑是当文件对象存在的时候将该文件对象获取出来，并生成Bitmap对象并返回
             */
//            Bitmap sdBitmap= BitmapFactory.decodeFile(imageSavePath);
            //从SD卡中获取图片的时候直接进行图片的压缩处理防止OOM

            System.out.println("保存的图片的链接：" + imageSavePath);
            Bitmap sdBitmap= ImageUtil.getCompressBitmapBYScreen(imageSavePath);
            return sdBitmap;
        }else{
            //文件不存在的时候直接返回null，通过判断继续从网络获取数据
            return null;
        }
    }

    /**
     * 当前方法完成的操作是将图片的原始版本保存到SD卡或者手机机身内存中
     *
     * 业务逻辑分析：
     * 1、从网络获取的图片首先保存到本地的SD卡或者机身内存中，这个时候传递进来的参数是
     *    a、netUrl  图片的网络路径，作为图片的名称
     *    b、bitmap  下载的图片的Bitmap对象
     * 2、在方法内部对当前手机的存储位置进行判断，分别包括
     *    a、手机机身内存
     *    b、外置SD卡内存
     * 3、将对应的图片保存到当前手机的符合要求的内存中
     *
     *
     * @param netUrl   图片文件的名称，使用图片的网络路径作为图片的名称
     * @param bitmap     要保存的图片文件
     * @return
     */
    public static boolean putBitmapToSD(String netUrl, Bitmap bitmap){

        FileOutputStream fos=null;
        String saveBitmapPath=null;
        if(null!=netUrl&&null!=bitmap){

            /**
             * 将网络获取到的图片保存到本机的缓存中（机身内存、SD卡）
             */
            if(StorageUtil.isPhoneHaveSD()&&StorageUtil.getUsefulSizeOfSD()> ImageUtil.getBitmapTotalByte(bitmap)*2){
//                saveBitmapPath=StorageUtil.getPathBySD()+netUrl;
//                saveBitmapPath=new File(new File(StorageUtil.getPathBySD()), URLEncoder.encode(netUrl)).getAbsolutePath();

                //以SD卡目录为路径创建File对象
                File fileBySD=new File(StorageUtil.getPathBySD());
                //在SD卡根目录中创建以当前应用包名为名称的文件夹
                File fileBySDSon=new File(fileBySD, PackageUtil.getAppPackageName());
//                File fileBySDSon=new File(fileBySD, "AA");
                if(!fileBySDSon.exists()){
                    fileBySDSon.mkdir();
                }
                //以图片的网络连接作为对应的文件名称，进行保存图片
                saveBitmapPath=new File(fileBySDSon, URLEncoder.encode(netUrl)).getAbsolutePath();

            }else {
//                saveBitmapPath=StorageUtil.getPathBycache(context)+netUrl;
//                saveBitmapPath=new File(new File(StorageUtil.getPathBycache(context)), URLEncoder.encode(netUrl)).getAbsolutePath();

                //以当前应用的缓存目录根目录创建File对象
                File fileByCache=new File(StorageUtil.getPathBycache());
                //在缓存目录下以当前应用的包名为名称创建一个文件夹
                File fileByCacheSon=new File(fileByCache,PackageUtil.getAppPackageName());
//                File fileByCacheSon=new File(fileByCache,"AA");
                if(!fileByCacheSon.exists()){
                    fileByCacheSon.mkdir();
                }
                //以图片的网络连接作为对应的文件名称，进行保存图片
                saveBitmapPath=new File(fileByCacheSon, URLEncoder.encode(netUrl)).getAbsolutePath();
            }

            try {
                //将数据写入到文件中
                fos=new FileOutputStream(saveBitmapPath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;

            }finally {
                try {
                    fos.flush();
                    fos.close();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

        }else{
            return false;
        }
    }
}
