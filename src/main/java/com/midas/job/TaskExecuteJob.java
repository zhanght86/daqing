package com.midas.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.midas.service.BurnBusiness;
import com.midas.service.BurnService;

/**
 * 查找任务结果
 * 
 * @author sullivan
 * 
 * 搜索文件导出功能
 *
 */
@Component
public class TaskExecuteJob {

    private Logger logger = LoggerFactory.getLogger(TaskExecuteJob.class);

    @Autowired
    private BurnBusiness business;

    @Autowired
    private BurnService burnService;

    
    
  @Scheduled(fixedDelay = 30*1000) 
    public void execute() {

       // long st = System.currentTimeMillis();
       
        try {
        	logger.info("CheckTaskAndRun任务执行中");
           boolean rtnVal=burnService.CheckTaskAndRun("");
            
        } catch (Exception e) {
            logger.error("执行定时任务失败--fileExportTask", e);
        }

        //logger.info("start file export Task, times ： {} 毫秒", System.currentTimeMillis() - st);
    }

}
