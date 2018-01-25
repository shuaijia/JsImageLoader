package com.jia.jsloader.download;



import com.jia.jsloader.thread.MyThreadFactory;
import com.jia.jsloader.utils.EncryptUtil;
import com.jia.jsloader.utils.PackageUtil;
import com.jia.jsloader.utils.StorageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hm on 2016/4/4.
 *
 *   当前类完成的操作是完成项目中关于资源的多线程下载操作
 *   关于使用的技术点简单阐述：
 *   1、下载操作就是一个传统的Http--Get请求
 *   2、200返回码与206返回码的区别，
 *        a、200作为Http请求完整数据的操作的请求返回
 *        b、206作为Http请求部分数据的操作的请求返回
 *   3、关于存储位置
 *   4、多线程的开启，文件数据的划分
 *
 * =============================================================================
 *   逻辑描述：
 *   1、下面为什么将之前的Http请求的完整逻辑拆分开成为三个部分
 *        getUrlConnection()\doDownLoadByHttp()\doDownLoadByHttps()
 *      之所以按照这种方式进行拆分原因如下：
 *      a、进行下载操作过程中需要进行多次的UrlConnection对象的创建操作，并且由于具有完整Http请求和部分Http请求的不同
 *         主要是指设置Range的请求头，所以需要进行拆分
 *      b、
 *
 *
 */
public class DownLoadUtil {

    //获取当前设备CPU支持的核心数，用来定义开启的线程个数进行多线程的下载操作，默认设置为核心数的2倍
    private static int threadNum= Runtime.getRuntime().availableProcessors()*2;
    //定义每一个线程需要完成的下载任务的字节数
    private static int byteSize=0;
    //定义每次任务进行操作的时候的字节的开始位置和结束位置
    private static int startByte=0;
    private static int endByte=0;


    /**
     * 当前方法完成的操作是记性数据的下载操作，操作思路：
     * 根据现在的网络环境区分为Http与Https两种操作
     *
     * @param netUrl    需要下载的资源的网络连接
     */
    public static void downLoadFile(final String netUrl) throws IOException {

        /**
         * 关于下载操作的代码逻辑思路：
         * 1、首先通过网络连接的完整的Http请求获取到需要下载的资源的总得文件大小
         * 2、在本地创建一个文件并且创建一个与将要下载的资源的文件名称相同的文件名
         * 3、开启多个线程进行分段数据的下载
         * 4、下载过程中进行断点记录，并保存进度，使用File操作或者使用数据库SQLlite进行操作
         * 5、资源下载完成将保存进度的文件进行删除
         */
        //截取连接的前五个字符，进行比较是http、https
        String urlHead=netUrl.substring(0,5);
        switch(urlHead){
            case "https":
                doHttps(netUrl);
                break;
            case "http:":
                doHttp(netUrl);
                break;
        }
    }

    private static void doHttp(final String netUrl) throws IOException {
        System.out.println("资源的下载连接："+netUrl);
        HttpURLConnection conn= (HttpURLConnection) getUrlConnection(netUrl);
        //获取网络中需要下载的资源的大小
        if(null==conn){

            System.out.println("HttpUrlConnection对象为Null");
        }
        System.out.println("AAAAA");
        int netFileLength= (int) doDownLoadByHttp(conn);
        if(netFileLength<=0){
            System.out.println("长度为0000000");
            return;
        }
        System.out.println("文件大小："+netFileLength);
        System.out.println("线程个数："+threadNum);
        //在本地创建一个同名称同大小的文件


        //以当前应用的缓存目录根目录创建File对象
        File fileByCache=new File(StorageUtil.getPathBycache());
        //在缓存目录下以当前应用的包名为名称创建一个文件夹
        File fileByCacheSon=new File(fileByCache, PackageUtil.getAppPackageName());
//                File fileByCacheSon=new File(fileByCache,"AA");
        if(!fileByCacheSon.exists()){
            fileByCacheSon.mkdir();
        }
        String md5Url = EncryptUtil.md5(netUrl);
        //以图片的网络连接作为对应的文件名称，进行保存图片
        final String saveHopePath=new File(fileByCacheSon, URLEncoder.encode(md5Url)).getAbsolutePath();
        System.out.println("文件存储的目录：："+saveHopePath);
        RandomAccessFile raf = new RandomAccessFile(saveHopePath, "rw");
        raf.setLength(netFileLength);
        byteSize=netFileLength/threadNum;
        System.out.println("文件大小：" + netFileLength);
        System.out.println("线程个数："+threadNum);
        System.out.println("区域大小："+byteSize);
        //4、创建一定数量的线程任务进行数据的下载操作
        for(int workId=0;workId<threadNum;workId++){

            //5、根据顺序规律进行不同线程负责需要下载的字节段的位置（起始位置、结束位置）
            //   这里的不同的任务是根据workId进行划分的，得到不同位置的起始byte位置与结束byte位置
            startByte=workId*byteSize;
            endByte=(workId+1)*byteSize-1;
            if(workId==(threadNum-1)){
                endByte=netFileLength-1;
            }
            System.out.println("==========================");
            System.out.println("这是第"+workId+"个线程操作");
            System.out.println("起始位置："+startByte+"-------"+"结束位置："+endByte);
            System.out.println("==========================");
            MyThreadFactory.getThreadPoolExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection conn = (HttpURLConnection) getUrlConnection(netUrl);
                        conn.setRequestProperty("Range", "bytes=" + startByte + "-" + endByte);
                        final InputStream is = (InputStream) doDownLoadByHttp(conn);
                        MyThreadFactory.getThreadPoolExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                RandomAccessFile raf = null;
                                try {
                                    raf = new RandomAccessFile(saveHopePath, "rw");
                                    raf.seek(startByte);
                                    int len = -1;
                                    byte[] buffer = new byte[1024];
                                    while ((len = is.read(buffer)) != -1) {
                                        raf.write(buffer, 0, len);
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        raf.close();
                                        is.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    /**
     * 对于Https连接形式下的数据下载操作
     *
     * @param netUrl
     * @throws IOException
     */
    private static void doHttps(final String netUrl) throws IOException {
        HttpsURLConnection conns= (HttpsURLConnection) getUrlConnection(netUrl);
        //1、获取网络中需要下载的资源的大小

        int netFileLengths= (int) doDownLoadByHttps(conns);
        if(netFileLengths<=0){
            return;
        }
        //2、在本地创建一个同名称同大小的文件
        RandomAccessFile rafs = new RandomAccessFile(StorageUtil.getPathBySD()+getNetFileName(netUrl), "rw");
        rafs.setLength(netFileLengths);
        //3、根据资源的总体大小以及将要划分的任务数目进行资源的划分
        //   按照从获取的CPU核心数的三倍进行任务的划分
        byteSize=netFileLengths/threadNum;
        //4、创建一定数量的线程任务进行数据的下载操作
        for(int workId=0;workId<threadNum;workId++){

            //5、根据顺序规律进行不同线程负责需要下载的字节段的位置（起始位置、结束位置）
            //   这里的不同的任务是根据workId进行划分的，得到不同位置的起始byte位置与结束byte位置
            startByte=workId*byteSize;
            endByte=(workId+1)*byteSize;
            if(workId==(threadNum-1)){
                endByte=netFileLengths-1;
            }
            final int finalWorkId = workId;
            MyThreadFactory.getThreadPoolExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpsURLConnection conns = (HttpsURLConnection) getUrlConnection(netUrl);
                        conns.setRequestProperty("Range", "bytes=" + startByte + "-" + endByte);
                        final InputStream is = (InputStream) doDownLoadByHttp(conns);
                        MyThreadFactory.getThreadPoolExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                RandomAccessFile raf = null;
                                try {
                                    System.out.println("进行第"+ finalWorkId +"个任务在下载！！");
                                    raf = new RandomAccessFile(StorageUtil.getPathBySD()+getNetFileName(netUrl), "rw");
                                    raf.seek(startByte);
                                    int len = -1;
                                    byte[] buffer = new byte[1024];
                                    while ((len = is.read(buffer)) != -1) {
                                        raf.write(buffer, 0, len);
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        raf.close();
                                        is.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        System.out.println("已经下载完成！！");
    }

    /**
     * 从网络连接中截取最后的字符串即为文件的名称
     *
     * @param netUrl
     * @return
     */
    private static String getNetFileName(String netUrl){
        int start = netUrl.lastIndexOf("/")+1;
        return netUrl.substring(start);
    }
    /**
     * 该方法完成的操作是对于已Http开头的网络连接进行不同请求格式的情况下的返回数据
     * 所谓请求格式：存在两种情况，分别是指设置Range请求头和不设置的时候的区别
     *
     * @param conn           HttpURLConnection的对象
     * @throws IOException
     */
    private static Object doDownLoadByHttp(HttpURLConnection conn) throws IOException {

        int code = conn.getResponseCode();
        if (code == 200) {
            //获取想要下载的资源的总得文件大小
            int length = conn.getContentLength();
            return length;
        }else if(code==206){
            InputStream is = conn.getInputStream();
            return is;
        }else{
            return null;
        }
    }

    /**
     * 该方法完成的操作是对于已Https开头的网络连接进行不同请求格式的情况下的返回数据
     * 所谓请求格式：存在两种情况，分别是指设置Range请求头和不设置的时候的区别
     *
     * @param conn          HttpsURLConnection的对象
     * @throws IOException
     */
    private static Object doDownLoadByHttps(HttpsURLConnection conn) throws IOException {

        int code = conn.getResponseCode();
        if (code == 200) {
            //获取想要下载的资源的总得文件大小
            int length = conn.getContentLength();
            return length;
        }else if(code==206){
            InputStream is = conn.getInputStream();
            return is;
        }else{
            return null;
        }
    }

    /**
     * 当前方法完成的操作是关于创建一个UrlConnection的对象并返回
     *
     * @param netUrl         可能是Http连接也可能是Https
     * @return                正常情况下返回UrlConnection的对象，否则返回null
     * @throws IOException
     */
    private static URLConnection getUrlConnection(String netUrl) throws IOException {

        if(null==netUrl){
            return null;
        }
        String urlHead=netUrl.substring(0,5);
        switch(urlHead){

            case "https":
                URL urls = new URL(netUrl);
                HttpsURLConnection conns = (HttpsURLConnection) urls.openConnection();
                conns.setRequestMethod("GET");
                conns.setConnectTimeout(5000);
                return conns;
            case "http:":
                URL url = new URL(netUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                return conn;
            default:
                return null;
        }

    }




}
