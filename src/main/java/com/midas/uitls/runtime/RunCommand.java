package com.midas.uitls.runtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midas.constant.ErrorConstant;
import com.midas.constant.SysConstant;
import com.midas.exception.ServiceException;
import com.midas.uitls.tools.CommonsUtils;

/**
 * 运行linux指令, 如果是win机器, 不运行
 * 
 * @author arron
 *
 */
public class RunCommand {

    private static Logger logger = LoggerFactory.getLogger(RunCommand.class);

    public static int execute(String... strings) {
        StringBuffer sb = new StringBuffer();
        String[] s = convert(strings);
        if (null == s) {
            logger.error("传递参数为空");
            return -1;
        }

        String os = System.getProperty("os.name");
        if (!os.toUpperCase().startsWith("LINUX")) {
            logger.error("使用的系统不正确，请使用Linux系统运行， 当前使用系统为： " + os);
            return -1;
        }

        InputStream is = null;
        BufferedReader br = null;
        Process pro = null;

        int waitFor = -1;

        try {
        	System.out.println("RunComand:"+s);
            pro = Runtime.getRuntime().exec(s);

            is = pro.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, SysConstant.ENCODING));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            waitFor = pro.waitFor();
            logger.info("执行命令： {}， 等待时间为： {}， 运行结果为： {}", s, waitFor, sb.toString());
        } catch (IOException e) {
            logger.error("执行失败IOException", e);
        } catch (InterruptedException e) {
            logger.error("执行失败InterruptedException", e);
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
            }
            if (null != pro) {
                pro.destroy();
            }
        }
        return waitFor;
    }

    public static void main(String[] args) {
//        execute("ipconfig");
//        execute("./fileExport.sh root 121.40.125.114 /tmp/a_split,/tmp/b_split,/tmp/c_split /tmp/download Emuzi666");
//        
        execute("e:\\123.bat","f:\\eclipse.ini","e:\\");
         
    }

    /**
     * // ./Merge.elf -i /jukebox/work/read -d /jukebox -n R00001 -a 8
     * 
     * @param volLabel
     *            卷标号
     * @param number
     *            刻盘数量
     * @return
     */
    public static boolean merge(String volLabel, String exportPath, int number) {
        boolean bool = false;
        try {
            String cm = CommonsUtils.getPropertiesValue(SysConstant.MERGE_ELF);
            String readpath = CommonsUtils.getPropertiesValue(SysConstant.READ_PATH);
            if (null == exportPath || "".equals(exportPath)) {
                exportPath = CommonsUtils.getPropertiesValue(SysConstant.EXPORT_PATH);
                ;
            }
            int result = execute(cm, "-i", readpath, "-d", exportPath, "-n", volLabel, "-a", "" + number);
            logger.info("执行合并指令， 输出目录为：{}, 卷标号为: {}, 盘的数量: {}, 执行的结果： 【{}】", exportPath, volLabel, number, result);
            if (0 == result) {
                bool = true;
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("合并文件失败, 未知异常", e);
        }
        return bool;
    }

    private static String[] convert(String... strings) {
        if (strings == null || strings.length == 0) {
            return null;
        }
        String[] s = new String[strings.length];

        for (int i = 0; i < strings.length; i++) {
            s[i] = strings[i];
        }
        return s;
    }
    //add by sullivan
    public static String executeResult(String... strings) {
        StringBuffer sb = new StringBuffer();
        String[] s = convert(strings);
        if (null == s) {
            logger.error("传递参数为空");
            return "-1";
        }

        String os = System.getProperty("os.name");
        if (!os.toUpperCase().startsWith("LINUX")) {
            logger.error("使用的系统不正确，请使用Linux系统运行， 当前使用系统为： " + os);
            return "-1";
        }

        InputStream is = null;
        BufferedReader br = null;
        Process pro = null;

        int waitFor = -1;

        try {
            pro = Runtime.getRuntime().exec(s);

            is = pro.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, SysConstant.ENCODING));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            waitFor = pro.waitFor();
            logger.info("执行命令： {}， 等待时间为： {}， 运行结果为： {}", s, waitFor, sb.toString());
        } catch (IOException e) {
            logger.error("执行失败IOException", e);
        } catch (InterruptedException e) {
            logger.error("执行失败InterruptedException", e);
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
            }
            if (null != pro) {
                pro.destroy();
            }
        }
        return sb.toString();
    }

}
