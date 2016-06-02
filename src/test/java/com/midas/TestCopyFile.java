package com.midas;

import java.io.File;

import org.apache.commons.io.FileUtils;

/**
 * 测试copy文件
 * @author arron
 *
 */
public class TestCopyFile {

    public static void main(String[] args) throws Exception {
//        File f = new File("c:/ab/ab");
//        
//        boolean bool = f.renameTo(new File("c:/ab/cc"));
//        
//        System.out.println(bool);
//        
////        FileUtils.
//        
//        File fs = new File("\\\\192.168.0.222\\share");
//        for(String s : fs.list()) {
//            System.out.println("||||"+s);
//        }
//        
        File f = new File("c:/ab/ab.rar");
        FileUtils.copyFile(f, new File("c:/ab/ab/ab/aaaa.rar"));
//        FileUtils.copyFileToDirectory(f, new File("c:/ab/ab/ab/"));
//        FileUtils.copyFileToDirectory(srcFile, destDir, preserveFileDate);
    }
    
}
