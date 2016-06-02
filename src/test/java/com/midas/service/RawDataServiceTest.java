package com.midas.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.midas.BaseTest;
import com.midas.service.impl.RawDataServiceImpl;
import com.midas.uitls.excel.ReadExcelUtils;

public class RawDataServiceTest extends BaseTest {

    private DataService rawDataService;

    // @Test
    public void list() {
        rawDataService = context.getBean("rawDataService", RawDataServiceImpl.class);
        PageInfo<Map<String, Object>> pageInfo = rawDataService.list(null, new Page<>());
        System.out.println(pageInfo);
    }

     @Test
    public void insert() {
        rawDataService = context.getBean("rDataService", RawDataServiceImpl.class);

        Map<String, Object> map = new HashMap<String, Object>();

        /*
         * work_area, team_no, test_line_number, record_length, use_interval,
         * tape_number, data_quantity, construction_year, remarks,
         */
        map.put("work_area", "和10井");
        map.put("team_no", "团队");
        map.put("test_line_number", "1");
        map.put("record_length", "2");
        map.put("use_interval", "2");
        map.put("tape_number", "111");
        map.put("data_quantity", "10");
        map.put("construction_year", "10");
        map.put("remarks", "");

        int i = rawDataService.insert(map, "1");
        System.out.println(i);

    }
 
}
