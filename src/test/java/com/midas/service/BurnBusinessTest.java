//package com.midas.service;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.commons.io.FileUtils;
//
//import com.midas.BaseTest;
//import com.midas.enums.DataSource;
//import com.midas.enums.DataType;
//
//public class BurnBusinessTest extends BaseTest {
//
//    private BurnBusiness bus;
//
////    @Test
//    public void start() {
//        bus = context.getBean(BurnBusiness.class);
//       // bus.master("原始数据清单", "c:/原始数据清单Test.15112516320086129.xls", DataType.RAW_DATA.getType(), DataSource.NETWORK_SHARING.getKey(), "1","");
//    }
//    
////    @Test
//    public void down() {
//        bus = context.getBean(BurnBusiness.class);
//        bus.masterBurn("R20151127000002");
//    }
//    
////    @Test
//    public void masterNotify() {
//        bus = context.getBean(BurnBusiness.class);
//        Map<String, Object> map = new HashMap<String, Object>();
//        bus.masterNotify(map);
//    }
//    
////    @Test
//    public void masterMerge() {
//        bus = context.getBean(BurnBusiness.class);
//        bus.masterMerge("R20151204000003", "f:/tmp");
//    }
//    
////    @Test
//    public void masterMergeNotify() {
//        bus = context.getBean(BurnBusiness.class);
//        bus.masterMergeNotify(null);
//        try {
//            Thread.sleep(60 * 60 * 1000L);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    
//    public static void main(String[] args) throws IOException {
//        File srcDir = new File("F:/tmp/share/testABC");
//        File destDir = new File("F:/tmp/share/testMMMM");
////        FileUtils.copyDirectoryToDirectory(srcDir, destDir);
//        FileUtils.copyDirectory(srcDir, destDir);
//    }
//    
//}
