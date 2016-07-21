package com.midas.uitls.threadpool;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midas.uitls.excel.ReadExcelUtils;
import com.midas.uitls.tools.StringTools;

public class ThreadPoolS {

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolS.class);
    public static int                        count     = 0;           // 并发线程数
    static int                        threads     =1;           // 并发线程数
    private static Object             lock        = new Object();
    private static ThreadPoolExecutor qthreadPools = null;

    private static final int MAX_THREAD_NUM = 100;

    static {
        synchronized (lock) {
        	qthreadPools = new ThreadPoolExecutor(threads, MAX_THREAD_NUM, 1L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardOldestPolicy());
        	qthreadPools.allowCoreThreadTimeOut(true);
        }
    }

    public Future<?> executeSubmit(Callable<?> call) {
        return qthreadPools.submit(call);
    }
    
    public static Future<?> executeSubmitS(Callable<?> call) {
        return qthreadPools.submit(call);
    }


    public static int getActiveCount() {
        return qthreadPools.getActiveCount();
    }
    

    public static void execute(Runnable command) {
    	qthreadPools.execute(command);
    }


    

}
