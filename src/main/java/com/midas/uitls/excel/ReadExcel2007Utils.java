package com.midas.uitls.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel2007Utils extends ReadExcelUtils {

    public ReadExcel2007Utils() {
    }
    
    @Override
    public String[][] getData(File file, int ignoreRows, int sheetIndex) throws FileNotFoundException, IOException {
        // 构造 XSSFWorkbook 对象，strPath 传入文件路径
        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
        // 读取第一章表格内容
       
        if(sheetIndex > xwb.getActiveSheetIndex()) {
            logger.info("不能读取的标签页：{}", sheetIndex);
            return null;
        }
        
        XSSFSheet sheet = xwb.getSheetAt(sheetIndex);
        // 定义 row、cell
        XSSFRow row;
        String cell;
        // 循环输出表格中的内容
        int rows = sheet.getPhysicalNumberOfRows() - ignoreRows;
        int cols = 0;
        if(rows <= 0 || sheet.getRow(0) == null) {
            logger.info("excel文件为空");
            return null;
        }
        cols = sheet.getRow(0).getPhysicalNumberOfCells();
        
        String [][] data = new String[rows][cols];
        int startIndex = 0;
        for (int i = ignoreRows; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
                // 通过 row.getCell(j).toString() 获取单元格内容，
                XSSFCell xSScell = row.getCell(j);
                if(xSScell == null)  {
                    cell = "";
                } else {
                    cell = xSScell.toString().trim();
                }
                data[startIndex][j] = cell;
            }
            startIndex ++;
        }
        return data;
    }

}
