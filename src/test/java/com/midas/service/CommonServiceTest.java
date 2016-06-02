package com.midas.service;

import org.junit.Before;
import org.junit.Test;

import com.midas.BaseTest;
import com.midas.enums.DataType;
import com.midas.vo.BurnProgress;

public class CommonServiceTest extends BaseTest {

    private CommonService commonService;

    @Before
    public void setUp() {
        commonService = context.getBean(CommonService.class);
    }
    
    @Test
    public void getUniqueNextVal() {
        
        long st = System.currentTimeMillis();
        String cdate = "20151118";
        String ctype = "R";
        // String result = commonService.getUniqueNextVal(cdate, ctype);

        String result = commonService.seqUniqueNextVal(cdate, DataType.MIDDLE_DATA);
        
        System.out.println("返回字符串为：" + result);
        System.out.println(commonService.seqUniqueNextVal(cdate, DataType.RAW_DATA));
        System.out.println(commonService.seqUniqueNextVal(cdate, DataType.TWO_DATA));
        System.out.println(commonService.seqUniqueNextVal(cdate, DataType.THREE_DATA));
        System.out.println("返回字符串长度：" + result.length());
        System.out.println("耗时： " + (System.currentTimeMillis() - st) + "毫秒");
    }
    
//    @Test
    public void isBusy() {
        commonService = context.getBean(CommonService.class);
//        boolean bool = commonService.isBusy("SERVER1");
//        System.out.println("true忙， false：闲：, 机器结果为： " + bool);
//
//        int i = commonService.effectiveQuantity("SERVER1");
//        System.out.println("空白盘的数量： " + i);
        
        
        BurnProgress bp = commonService.getBurnStatus("SERVER1", "aa");
        if(bp.isSuccess()) {
            System.out.println("查询成功");
        } else {
            System.out.println("查询失败");
        }
        
    }

}
