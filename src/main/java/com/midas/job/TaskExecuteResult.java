package com.midas.job;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
public class TaskExecuteResult {

    private Logger logger = LoggerFactory.getLogger(TaskExecuteResult.class);

    @Autowired
    private BurnBusiness business;

    @Autowired
    private BurnService burnService;

    public void execute() {

        long st = System.currentTimeMillis();
        try {
            System.out.println("aaaaaa");
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
            List<Map<String, Object>> list = burnService.listExportRecord(null, ExportState.EXPORTTING.getKey(),null);
            Set<String> set = new HashSet<String>();
            for(Map<String, Object> map : list ) {
                set.add(map.get("volume_label").toString());
            }
            for (String volLabel : set) {
                try {
                    logger.info("检查合并的唯一号： {}", volLabel);
                    business.masterMergeNotify(volLabel);
                } catch (Exception e) {
                    logger.error("执行定时任务失败--masterMergeNotify", e);
                }
            }
        } catch (Exception e) {
            logger.error("执行定时任务失败--masterMergeNotify", e);
        }

        logger.info("start business master notify, times ： {} 毫秒", System.currentTimeMillis() - st);
    }

}
