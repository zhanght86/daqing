package com.midas.uitls.threadpool;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

public class ThreadPoolContainer {
	    	  
	private  static ConcurrentMap<String, ExecutorService> executorsVect=null;
	    
	 static {
		 executorsVect =new ConcurrentHashMap<>();
	}
	    private ThreadPoolContainer() {
	       // Exists only to defeat instantiation.
	    }
	 
	    public static ConcurrentMap<String, ExecutorService>   getInstance() {
	       
	       return executorsVect;
	    }
	    public  static void add(ExecutorService ex) {
	    	executorsVect.put(ex.hashCode()+"", ex);
		}
	    // Other methods...
	    
	public static int destrory(String instanceId) {
		ExecutorService exec = executorsVect.get(instanceId);
		try {

			if (null != exec) {
				exec.shutdownNow();
				executorsVect.remove(exec);
				return 0;
			} else {
//				Iterator iter = executorsVect.entrySet().iterator();
//				while (iter.hasNext()) {
//					Map.Entry entry = (Map.Entry) iter.next();
//					ExecutorService pool = (ExecutorService) entry.getValue();
//					pool.shutdownNow();
//				}
				return 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
	}
	
}
