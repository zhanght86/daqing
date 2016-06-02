package com.midas.uitls.threadpool;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.midas.uitls.file.SmbUtils;

/**
 * 线程池管理
 * 
 * @author arron
 *
 */
public class ThreadPool {

    private ThreadPoolExecutor threadPools = null;

    private ThreadPool() {
    }

    public static ThreadPool getInstance(int threads, int maxThreadNum) {
        ThreadPool pool = new ThreadPool();
        pool.threadPools = new ThreadPoolExecutor(threads, maxThreadNum, 1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardOldestPolicy());
        pool.threadPools.allowCoreThreadTimeOut(true);
        return pool;
    }

    public void execute(Runnable command) {
        threadPools.execute(command);
    }

    public Future<?> submit(Runnable command) {
        return threadPools.submit(command);
    }

    public int getActiveCount() {
        return threadPools.getActiveCount();
    }

    public void destroy() {
        synchronized (threadPools) {
            if (!threadPools.isShutdown())
                threadPools.shutdown();
        }
    }

    public static void main(String[] args) throws Exception {
        ThreadPool tp = ThreadPool.getInstance(2, 20);

        String file1 = "/Tools/开发工具/dnq020enu1.ISO";
        String file2 = "/Tools/开发工具/dnq020enu2.ISO";
        String file3 = "/Tools/开发工具/dnq020enu3.ISO";
        String file4 = "/Tools/开发工具/msdn200110_1.iso";
        String file5 = "/Tools/开发工具/msdn200110_2.iso";
        String file6 = "/Tools/开发工具/msdn200110_3.iso";
        
        FutureTask<Boolean> task1 = new FutureTask<Boolean>(new UploadFile(file1));
        FutureTask<Boolean> task2 = new FutureTask<Boolean>(new UploadFile(file2));
        FutureTask<Boolean> task3 = new FutureTask<Boolean>(new UploadFile(file3));
        FutureTask<Boolean> task4 = new FutureTask<Boolean>(new UploadFile(file4));
        FutureTask<Boolean> task5 = new FutureTask<Boolean>(new UploadFile(file5));
        FutureTask<Boolean> task6 = new FutureTask<Boolean>(new UploadFile(file6));
        
        tp.submit(task1);
        tp.submit(task2);
        tp.submit(task3);
        tp.submit(task4);
        tp.submit(task5);
        tp.submit(task6);
        
        System.out.println(file1 + "." + task1.get());
        System.out.println(file2 + "." + task2.get());
        System.out.println(file3 + "." + task3.get());
        System.out.println(file4 + "." + task4.get());
        System.out.println(file5 + "." + task5.get());
        System.out.println(file6 + "." + task6.get());
    }

}

class UploadFile implements Callable<Boolean> {

    private String file;

    public UploadFile(String file) {
        this.file = file;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            SmbUtils su = new SmbUtils("192.168.0.19", "admin", "jvcnas");
            System.out.println("开始下载: " + file);
            su.copyFileSTL(file, "c:/test/" + file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
