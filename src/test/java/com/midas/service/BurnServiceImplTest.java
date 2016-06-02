package com.midas.service;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.midas.BaseTest;

public class BurnServiceImplTest extends BaseTest {

    private BurnService burn;
    
    //@Test
    public void FreeBurn() {
        burn = context.getBean(BurnService.class);
        Map<String, Object> map = burn.getFreeBurn();
        System.out.println(map);
    }
    
    
//    @Test
    public void checkMerge() {
        burn = context.getBean(BurnService.class);
        
        boolean bool = burn.checkMerge("R20151204000003");
        System.out.println(bool);
    }
    
    @Test
    public void listMerge() {
        burn = context.getBean(BurnService.class);
        List<Map<String, Object>> list = burn.listExportRecord("", "");
        System.out.println(list);
    }
    
}
