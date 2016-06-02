package com.midas.uitls.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ReadExcelUtils {

    protected static Logger logger = LoggerFactory.getLogger(ReadExcelUtils.class);

    public static void main(String[] args) throws Exception {

        File f2003 = new File("C:/Users/Administrator/Desktop/大庆项目/原始数据清单.xls");
        File f2007 = new File("C:/Users/Administrator/Desktop/大庆项目/ababab.xlsx");
        File f2007_2 = new File("C:/Users/Administrator/Desktop/大庆项目/mm.xlsx");

        String[][] str = readExcel(f2007, 1, 0);

        if (str == null) {
            System.out.println("无内容");
            System.exit(0);
        }

        // String [][] results = getData(f2003, 0, 0);

        for (String[] sr : str) {
            for (String s : sr) {
                System.out.print(s + "\t");
            }
            System.out.println();
        }
    }

    /**
     * 读取Excel
     * 
     * @param f
     *            读的文件
     * @param ignoreRows
     *            开始读取的行
     * @param sheetIndex
     *            需要读的标签页
     * @return
     */
    public static String[][] readExcel(File f, int ignoreRows, int sheetIndex) {
        ReadExcelUtils utils = null;
        if (isExcel2007(f.getAbsolutePath())) {
            utils = new ReadExcel2007Utils();
        } else if (isExcel2003(f.getAbsolutePath())) {
            utils = new ReadExcel2003Utils();
        } else {
            logger.error("文件不是Excel格式： {}", f.getAbsolutePath());
            return null;
        }
        try {
            return utils.getData(f, ignoreRows, sheetIndex);
        } catch (Exception e) {
            logger.error("读取失败", e);
        }
        return null;
    }

    public static String[][] readExcel(String filepath, int ignoreRows, int sheetIndex) {
        return readExcel(new File(filepath), ignoreRows, sheetIndex);
    }

    public abstract String[][] getData(File file, int ignoreRows, int sheetIndex)
            throws FileNotFoundException, IOException;

    private static boolean isExcel2003(String filepath) {
        return filepath.matches("^.+\\.(xls)$");
    }

    private static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }
}
