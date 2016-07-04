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
//        String teststr1="测试中文";
//        String teststr=new String(teststr1.getBytes("GBK"),"GBK");
//        String filename=new String( teststr.getBytes( "utf8" ), "utf8" ) ;
//        System.out.println(filename);
//           String aa="server1,server2,,";
//           if(aa.lastIndexOf(",")>1)
//           System.out.println(aa.substring(0,aa.lastIndexOf(",")));
        
        String testvolue="/0254_W20160512000001(84-30)/芳38/W116324.001.SGY_midas_6_000.split,/0256_W20160512000001(84-31)/芳38/W116324.001.SGY_midas_6_001.split,/0257_W20160512000001(84-32)/芳38/W116324.001.SGY_midas_6_002.split,/0258_W20160512000001(84-33)/芳38/W116324.001.SGY_midas_6_003.split,/0259_W20160512000001(84-34)/芳38/W116324.001.SGY_midas_6_004.split,/0542_W20160512000001(84-75)/芳38/W116324.001.SGY_midas_6_005.split";
        //W20160512000001
        int beginNum=testvolue.indexOf("_");
        int endNum=testvolue.indexOf("(");
        String tmpStr="";
        if(beginNum>0&&endNum>0)
        {
         tmpStr=testvolue.substring(testvolue.indexOf("_")+1,testvolue.indexOf("("));
        }
        else
        {
        tmpStr=testvolue.substring(6,21);
        }
       
        System.out.println(tmpStr);
    }
}