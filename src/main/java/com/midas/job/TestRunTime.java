package com.midas.job;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


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

        
      //  tt.testThread();
        
        BigDecimal sumDumpSize=new BigDecimal("9234567890123456789");
        
        System.out.println(sumDumpSize.toBigInteger());
        System.out.println(sumDumpSize.longValue());
        
        

     
    }

	public void testThread() {
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		CompletionService<Object> completionService=new ExecutorCompletionService(executorService);
		List<Future<?>> futures = new ArrayList<>();
		String[] fileList = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20".split(",");
		for (String file : fileList) {
			try {
				
				Future<Object> future = completionService.submit(new runCallalbe(file.replaceAll("//", "/"), "2", "3"));
				futures.add(future);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println("11111111111");

		int size = futures.size();
	for (Future<?> fa : futures) {
					try {
				Future<?> f = completionService.take();
				runCallalbe rs;

				rs = (runCallalbe) f.get();

				System.out.println("完成线程编号" + rs.param1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				
				
				executorService.shutdown();
			}
		}
	}
	
	
//			try {
//
//				Thread.sleep(5 * 1000L);
//				for (Future<?> f : futures) {
//
//					if (f.isDone()) {
//
//						runCallalbe rs = (runCallalbe) f.get();
//						if (rs.param1.equals("5")) {
//							executorService.submit(rs);
//						}
//						System.out.println("完成线程编号"+rs.param1);
//						size--;
//
//					}
//
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//			}

		

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
    		
    		//Thread.sleep((int)(Math.random()*10)*1000L);
    		Thread.sleep(1*1000L);
			System.out.println("TestThread"+ Thread.currentThread().getId() +" no :" +param1);
		//	System.out.println(Thread.currentThread().getName()+" id "+ Thread.currentThread().getId());
			return this;
    	}
    }
    }
