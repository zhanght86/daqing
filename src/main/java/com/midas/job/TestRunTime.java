package com.midas.job;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.midas.uitls.tools.StringTools;
import com.sun.org.apache.bcel.internal.generic.NEW;


public class TestRunTime {
 
    public static void main(String[] args) throws IOException, InterruptedException {
        String cmd = "";
        TestRunTime tt=new TestRunTime();
        
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
        String testNum="W116287-W116323|W116287-W116322";
        String testdum="W114567.001.SGY_midas_3_002.split";
        String aa=testNum.substring(1);
        System.out.println(aa);
//        
//        String testNuma="287-323";
//        String splitAry[]=testNuma.split(",");
//        char[] minChar=splitAry[0].toCharArray();
//        char[] maxChar=splitAry[0].toCharArray();
//        char[] baseNum={0,1,2,3,4,5,6,7,8,9};
//        StringBuffer regxBuff=new StringBuffer();
//     for (int i = 0; i < minChar.length; i++) {
//		char min = minChar[i];
//		char max = maxChar[i];
//			if (i == 0) {
//				regxBuff.append("[").append(min).append("-").append(max).append("]");
//			} else {
//				for (char c : baseNum) {
//
//				}
//			}
//	
//		
//		
//	}
//        String regx="[1-4]\\d(?<!4[1-9])\\d\\d(?<!409[6-9])";
//        		String teststr="4095";
//System.out.println(teststr.matches(regx));
//	

        
       // tt.testThread();
        
        BigDecimal sumDumpSize=new BigDecimal(1);
        System.out.println(sumDumpSize.add(new BigDecimal(20)));
        
        

     
    }
    public void testThread()
    {
    	   ExecutorService executorService=Executors.newFixedThreadPool(2);
    	   ArrayList<Future> futures=new ArrayList<>();
    	   for (int i = 0; i < 10; i++) {
    		Future<Object> future=executorService.submit(new runCallalbe(i+"","2","3")); 
    		futures.add(future);

    	}
    	   System.out.println("11111111111");
    	   for (Future f : futures) {
			try {
				runCallalbe rs=(runCallalbe)f.get();
				if (rs.param1.equals("5")) {
					executorService.submit	(rs);
				}
				System.out.println(rs.param1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    		executorService.shutdown();
    }
    
    class runCallalbe implements Callable<Object>{
		String param1="";
		String param2="";
		String param3="";
		
    	public runCallalbe(String param1,String param2,String param3 ) {
			this.param1=param1;
			this.param2=param2;
			this.param3=param3;
		}
    	@Override
    	public Object call() throws Exception {
    		
    		Thread.sleep(10000);
			System.out.println("TestThread"+ Thread.currentThread().getId() +" no :" +param1);
		//	System.out.println(Thread.currentThread().getName()+" id "+ Thread.currentThread().getId());
			return this;
    	}
    }
    
}