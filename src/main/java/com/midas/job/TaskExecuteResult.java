package com.midas.job;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.midas.enums.BurnState;
import com.midas.enums.ExportState;
import com.midas.service.BurnBusiness;
import com.midas.service.BurnService;

/**
 * 查找任务结果
 * 
 * @author arron
 *
 */

@Component
public class TaskExecuteResult {

    private Logger logger = LoggerFactory.getLogger(TaskExecuteResult.class);

    @Autowired
    private BurnBusiness business;

    @Autowired
    private BurnService burnService;

    
    @Scheduled(cron="0/10 * *  * * ? ") 
    public void execute() {

        long st = System.currentTimeMillis();
        Map<String, Object>  taskMap=null;
        try {
          
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("burning_state", BurnState.BURNNING.getKey() + ", " + BurnState.BURN_WAIT.getKey());
			List<Map<String, Object>> list = burnService.list(paramMap);
			for (Map<String, Object> map : list) {
				business.masterNotify(map);
			}
		} catch (Exception e) {
			logger.error("执行定时任务失败--masterNotify", e);
		}

		try {
			List<Map<String, Object>> checklist = burnService.listExportRecordCheck(null, "0,1", null);// 查询状态为1的任务
			String runVoLabel = "";

			if (null != checklist && checklist.size() > 0)// 发现有状态为1的正在导出的任务则,退出
			{
				for (Map<String, Object> map : checklist) {
					String export_state = map.get("export_state") + "";
					if (ExportState.EXPORTTING.getKey().equals(export_state))
						return;
				}
				taskMap = checklist.get(0);
				taskMap.put("update_time", new Date());
				taskMap.put("export_state", ExportState.EXPORTTING.getKey());
				runVoLabel = taskMap.get("volume_label") + "";
				burnService.updateExportRecord(taskMap);// 更新任务状态
			} else {
				return;
			}

			List<Map<String, Object>> list = burnService.listExportRecord(runVoLabel, ExportState.EXPORTTING.getKey(),
					null);// 查询状态为0和1的任务

			Set<String> set = new HashSet<String>();
			for (Map<String, Object> map : list) {

				set.add(map.get("volume_label").toString());
			}
			for (String volLabel : set) {
				try {

					business.masterMerge(volLabel, ""); // 执行下载dump

					logger.info("检查合并的唯一号： {}", volLabel);
					business.masterMergeNotify(volLabel);// 检查下载完成后进行合并处理

				} catch (Exception e) {
					taskMap.put("update_time", new Date());
					taskMap.put("export_state", ExportState.EXPORT_FAILD.getKey());
					burnService.updateExportRecord(taskMap);// 更新任务失败
					logger.error("执行定时任务失败--masterMergeNotify", e);

				}
			}
		} catch (Exception e) {
			taskMap.put("update_time", new Date());
			taskMap.put("export_state", ExportState.EXPORT_FAILD.getKey());
			burnService.updateExportRecord(taskMap);// 更新任务失败
			logger.error("执行定时任务失败--masterMergeNotify", e);
		}

        logger.info("start business master notify, times ： {} 毫秒", System.currentTimeMillis() - st);
    }

}
