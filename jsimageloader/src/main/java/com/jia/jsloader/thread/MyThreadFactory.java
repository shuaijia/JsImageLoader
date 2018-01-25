package com.jia.jsloader.thread;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hm on 2016/4/3.
 *
 *   当前类作为一个线程池的管理类，进行实现
 *   思路介绍，由于当前的线程池，在整个项目中不需要创建多个对象，直接使用单例模式进行创建
 *
 *   ======================================================================
 *   Java中的线程池
 *   在Java中Executors是一个关于线程池的父亲接口该类中具有多个构造方法，用来完成不同类型的线程池的创建
 *   1、固定大小的线程池
 *      Executors threadPool= (Executors) Executors.newFixedThreadPool(3);
 *      固定大小的线程池当多个任务同时提交时，始终在线程池中具有一开始设置的线程数目，多余该数目的其他任务
 *      暂时阻塞在队列中等待线程池的操作
 *   2、具有缓冲的线程池（可变大小的线程池）
 *      Executors threadPool= (Executors) Executors.newCachedThreadPool();
 *      此类型线程池大小随着任务数量的多少，进行浮动的线程数创建，线程池中的线程数是浮动的
 *   3、单个线程的线程池
 *      Executors threadPool=Executors.newSingleThreadExecutor();
 *      此类线程池中的线程数始终只有一个，并且不会增多或者减少，
 *      比较：单个线程的线程池和单独一个线程
 *            单个线程的线程池，当该线程被关闭之后，线程池自动会生成一个新的线程来维持线程池中始终有一个线程
 *            在运行的职责，类似线程被重新启动了，但是线程不再是之前的那个线程，因为线程号已经变化（测试一下）
 *            单独一个线程，当该线程被关闭之后就关闭了，没有重新启动
 *   ======================================================================
 *   Android中的线程池
 *   ====总：
 *   在Android中使用线程池的类是：ThreadPoolExecutor
 *   ThreadPoolExecutor threadPoolExecutor=
 *   new ThreadPoolExecutor(int corePoolSize, int maxinumPoolSize, long keepAliveTime, TimeUnit unit,
 *                          BlockingDeque<Runnable> workQueue, ThreadFactory threadFactory);
 *       int corePoolSize      : 线程池中的核心线程数
 *       int maxinumPoolSize   ：线程池中允许的最大线程数目
 *       long keepAliveTime    ：非核心线程的超时时间，超出这个时间非核心线程会被回收
 *       TimeUnit unit         ：非核心线程的超时时间的时间单位
 *       BlockingDeque<Runnable> workQueue  ： 保存需要线程池执行的任务的列表
 *       ThreadFactory threadFactory        ： 线程工厂，只是一个接口，只有一个方法Thread newThread(Runnable r)
 *   ThreadPoolExecutor threadPoolExecutor=
 *   new ThreadPoolExecutor(num*2, num*4, 8, TimeUnit.SECONDS, workQueue, new ThreadPoolExecutor.CallerRunsPolicy());
 *   ====分：
 *   1、AsyncTask中关于线程池的配置完成不同的操作
 *   2、四类不同的线程池，这四类线程池都是通过直接或间接的配置ThreadPoolExecutor来创建线程池的
 *      a、FixedThreadPool类通过Executors的newFixedThreadPool();方法进行创建，线程固定的线程池
 *      b、CachedThreadPool类通过Executors的newCachedThreadPool();方法进行创建，线程数不固定的线程池
 *      c、ScheduedThreadPool类通过Executors的newScheduedThreadPool();非核心线程的数目不受限制，但核心线程数是固定的，
 *                                                                    并且非核心线程会在空闲时立即回收
 *      d、SingleThreadExecutor类通过Executors的newSingleThreadExecutor();方法进行创建，只有一个线程的线程池
 *                                                        优点：将所有的任务都统一到一个线程中，不需要处理线程安全问题
 *
 *
 */
public class MyThreadFactory {

    //Android的线程池类
    private static ThreadPoolExecutor threadPoolExecutor=null;
    //获取当前用户的手机的CPU的核心数
    private static int num= Runtime.getRuntime().availableProcessors();
    //用于存储提交任务的任务队列
    private static BlockingDeque<Runnable> workQueue=new LinkedBlockingDeque<>(num*50);
    private MyThreadFactory(){
    }
    public static ThreadPoolExecutor getThreadPoolExecutor(){
        if(null==threadPoolExecutor){
            threadPoolExecutor=new ThreadPoolExecutor(num*2, num*4, 8, TimeUnit.SECONDS, workQueue, new ThreadPoolExecutor.CallerRunsPolicy());
//            threadPoolExecutor=new ThreadPoolExecutor(1, 1, 8, TimeUnit.SECONDS, workQueue, new ThreadPoolExecutor.CallerRunsPolicy());
        }
        return threadPoolExecutor;
    }

}
