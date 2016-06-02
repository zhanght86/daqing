package com.midas;

import java.io.File;
import java.io.FileFilter;


public class TestLogback {

//    private static Logger logger = LoggerFactory.getLogger(TestLogback.class);
    
    public static void main(String[] args) throws Exception {
    	
    	
    	File [] fs = File.listRoots();
    	for(File f : fs) {
    		for(File f1 : f.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					if(pathname.getName().startsWith(".")) {
						return false;
					}
					return true;
				}
			})) {
    			System.out.println(f1.getAbsolutePath());
    		}
    	}
    	
////        logger.error("ab");
//        String file = "\\\\192.168.0.19\\大法官安装程序\\新建文本文档.txt";
//        File f = new File(file);
//        
//        
//        BufferedReader br = new BufferedReader(new FileReader(f));
//        
//        String line = br.readLine();
//        
//        System.out.println(line);
//        
//        
//        File f140 = new File("\\\\192.168.0.140\\");
//        System.out.println(">>"+f140.isDirectory());
    }
    
}
