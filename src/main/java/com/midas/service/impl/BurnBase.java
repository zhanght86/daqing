package com.midas.service.impl;

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

public class BurnBase {

    private static Logger logger = LoggerFactory.getLogger(BurnBusinessImpl.class);

    static int                        threads     = 4;           // 并发线程数
    private static Object             lock        = new Object();
    private static ThreadPoolExecutor threadPools = null;

    private static final int MAX_THREAD_NUM = 100;

    static {
        synchronized (lock) {
            threadPools = new ThreadPoolExecutor(threads, MAX_THREAD_NUM, 1L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardOldestPolicy());
            threadPools.allowCoreThreadTimeOut(true);
        }
    }

    protected Future<?> executeSubmit(Callable<?> call) {
        return threadPools.submit(call);
    }

    /**
     * 获取正在运行的线程数
     * 
     * @return
     */
    public static int getActiveCount() {
        return threadPools.getActiveCount();
    }
    
    /**
     * 执行
     * 
     * @return
     */    
    public static void execute(Runnable command) {
        threadPools.execute(command);
    }


    /**
     * Excel 文件内容转换为
     * 
     * @param indexFile
     *            索引文件
     * @param dataType
     *            数据类型 原始数据, 二维数据, 三维数据, 中间数据
     * @return
     */
    protected List<Map<String, Object>> FileToList(String indexFile, String dataType) {
        String[][] data = ReadExcelUtils.readExcel(indexFile, 1, 0);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (String[] rows : data) {
            Map<String, Object> map = BusinessTools.convertIni(rows, dataType);
            list.add(map);
        }
        return list;
    }

    /**
     * 获取数据量(GB)
     * 
     * @param list
     * @return
     */
    protected BigDecimal getDataQuantity(List<Map<String, Object>> list) {
        BigDecimal size = new BigDecimal(0);
        for (Map<String, Object> map : list) {
            if (map.containsKey("data_quantity")) {
                Object obj = map.get("data_quantity");
                if (null != obj && !"".equals(obj)) {
                    String val = ObjectUtils.toString(obj);
                    if(StringTools.isNotEmpty(val)) {
                        BigDecimal bd = new BigDecimal(val);
                        size = size.add(bd);
                    }
                }
            }
        }
        return size;
    }
    
    public static void main(String[] args) {
        BigDecimal bd = new BigDecimal("");
        System.out.println(bd);
    }

    /**
     * 检查盘的数量是否足够
     * 
     * @param sourceSize
     * @param discTypeSize
     * @param effQuan
     * @param moreQuan
     *            要比该类型的需要盘数多出张数
     * @return
     */
    protected static boolean checkDiscNumber(BigDecimal sourceSize, BigDecimal discTypeSize, BigDecimal effQuan,
            BigDecimal moreQuan) {
        if (sourceSize.doubleValue() <= 0) {
            logger.info("没有大小的情况下， 不对盘的数量进行验证");
            return true;
        }
        BigDecimal bdreslt = sourceSize.divide(discTypeSize, 0);

        int max = effQuan.subtract(bdreslt).intValue();
        logger.info("刻盘预计大小为: {}, 每张盘大小为: {}, 剩余空盘数量为： {}, 最起码需要的盘的数量为: {}, 多出盘数： {}", sourceSize, discTypeSize, effQuan,
                bdreslt, moreQuan);
        
        if (max >= moreQuan.intValue()) {
            return true;
        }
        return false;
    }

}
