package com.midas.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midas.constant.ErrorConstant;
import com.midas.constant.SysConstant;
import com.midas.enums.DataSource;
import com.midas.exception.ServiceException;
import com.midas.uitls.tools.StringTools;

public class DataBase {

    private static Logger logger = LoggerFactory.getLogger(DataBase.class);

    public static List<String> getAllFiles(String folder, String file, int number) {
        logger.info("获取文件的文件路径参数为： {}， {}, {}", folder, file, number);
        List<String> files = new ArrayList<String>();
        if (number <= 1) {
            files.add(folder + "/" + file);
        } else {
            String[] s = file.split("[^\\d]");

            if (s.length >= 3) {
                String type = file.substring(0, file.indexOf(s[s.length - 2]));
                int start = StringTools.paserInt(s[s.length - 2]);
                int end = StringTools.paserInt(s[s.length - 1]);

                int numberLen = s[s.length - 2].length();
                if (end - start > number) {
                    throw new ServiceException(ErrorConstant.CODE4000, "区间数据文件： 输入有错误： 【" + file + "】 请检查数据内容");
                }
                while (start <= end) {
                    files.add(folder + "/" + type + (String.format("%0" + numberLen + "d", start)));
                    start++;
                }
            } else {
                files.add(folder + "/" + file);
            }
        }
        logger.info("获取到的文件内容为：" + files);
        return files;
    }

    public static List<String> getAllFilesByFolder(String sourceWorkdir,String folder, int number) {
        logger.info("获取文件的文件路径参数为： {}， {},{}",sourceWorkdir, folder, number);
        
        List<String> filelist = new ArrayList<String>();
        List<String> files = new ArrayList<String>();
        File folder1=new File(sourceWorkdir+"/"+folder);
        if (number <= 1) {
           
//                File[] fs = folder1.listFiles();
//                for(int i=0; i<fs.length; i++){
////                 System.out.println(fs[i].getAbsolutePath());
//                if(!fs[i].isDirectory()){
////                  try{
////                 //  showAllFiles(fs[i]);
//         
//                      files.add(folder+"/"+fs[i].getName());
////                  
////                  }catch(Exception e){}
//                 } else{
//                     
            try {
                files=getFileList(sourceWorkdir+"/"+folder,folder,filelist);
                
                
                logger.info("获取文件的文件路径下文件个数为： {}",filelist.size());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//                }
              }
            
//    else {
//            String[] s = file.split("[^\\d]");
//
//            if (s.length >= 3) {
//                String type = file.substring(0, file.indexOf(s[s.length - 2]));
//                int start = StringTools.paserInt(s[s.length - 2]);
//                int end = StringTools.paserInt(s[s.length - 1]);
//
//                int numberLen = s[s.length - 2].length();
//                if (end - start > number) {
//                    throw new ServiceException(ErrorConstant.CODE4000, "区间数据文件： 输入有错误： 【" + file + "】 请检查数据内容");
//                }
//                while (start <= end) {
//                    files.add(folder + "/" + type + (String.format("%0" + numberLen + "d", start)));
//                    start++;
//                }
//            } else {
//                files.add(folder + "/" + file);
//            }
//        }
        
        return files;
    }
    
    public static List<String> getFileList(String strPath,String prifix,List<String>filelist) {
        
       ;
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath(),prifix,filelist); // 获取文件绝对路径
                } else {
                    String strFileName = files[i].getAbsolutePath();
                String  newString =strFileName.substring(strFileName.indexOf(prifix), strFileName.length());
                    filelist.add(newString);
                    logger.info("获取到的文件内容为：{},{}" ,files[i].getPath(),newString);
                  
                }
            }

        }
        return filelist;
    }
    
   
    
    public static void main(String[] args) {

        System.out.println(StringTools.paserInt(""));

        List<String> list = new ArrayList<>();
         list.addAll(getAllFiles("a", "Waaaaaa113468-113471", 4));
//        list.addAll(getAllFiles("a", "W78", 0));
        System.out.println();
        System.out.println();
        System.out.println(list);
        
        String s = "F:/temp/aaaaaaa";
        
        File f = new File(s);
        
        File f2 = new File(f.getParent());
        System.out.println(f2.list().length);
        for(String sb : f2.list()) {
            System.out.println(">>"+sb);
            System.out.println(">>"+f.getName());
            if(sb.startsWith(f.getName())) {
                System.out.println("----------");
            } else {
                System.out.println("bbbbbbbbbbbbbb");
            }
        }
        
        System.out.println(f.getParent());
        System.out.println(f.exists());

    }

}
