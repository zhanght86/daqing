package com.midas.job;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class TestRunTime {
 
    public static void main(String[] args) throws IOException, InterruptedException {
        String cmd = "";
        
//        if(args == null || args.length == 0){
//            System.out.println("请输入命令行参数");
//        }else{
//            
//            for(int i=0;i<args.length; i++){
//                cmd += args[i] + " ";
//            }
//        }
//        
// 
//        try {
//            Process process = Runtime.getRuntime().exec(cmd);
// 
//            InputStreamReader ir = new InputStreamReader(process.getInputStream());
//            LineNumberReader input = new LineNumberReader(ir);
// 
//            String line;
//            while ((line = input.readLine()) != null) {
//                System.out.println(line);
//            }
        
        
//        } catch (java.io.IOException e) {
//            System.err.println("IOException " + e.getMessage());
//        }
        String teststr1="测试中文";
        String teststr=new String(teststr1.getBytes("GBK"),"GBK");
        String filename=new String( teststr.getBytes( "utf8" ), "utf8" ) ;
        System.out.println(filename);
           String aa="server1,server2,,";
           if(aa.lastIndexOf(",")>1)
           System.out.println(aa.substring(0,aa.lastIndexOf(",")));
    }
}