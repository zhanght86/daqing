package com.midas.uitls.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 读取Excel文件工具类
 * 
 * @author arron
 *
 */
public class ReadExcel2003Utils extends ReadExcelUtils {
    
    public ReadExcel2003Utils() {
    }
    
    /**
     * 
     * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
     * 
     * @param file
     *            读取数据的源Excel
     * 
     * @param ignoreRows
     *            读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
     * 
     * @return 读出的Excel中数据的内容
     * 
     * @throws FileNotFoundException
     * 
     * @throws IOException
     * 
     */
    @Override
    public String[][] getData(File file, int ignoreRows, int sheetIndex) throws FileNotFoundException, IOException {

        List<String[]> result = new ArrayList<String[]>();

        int rowSize = 0;

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        POIFSFileSystem fs = new POIFSFileSystem(in);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFCell cell = null;
        HSSFSheet st = wb.getSheetAt(sheetIndex);
        
        for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
            HSSFRow row = st.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            int tempRowSize = row.getLastCellNum() + 1;
            if (tempRowSize > rowSize) {
                rowSize = tempRowSize;
            }
            String[] values = new String[rowSize];
            Arrays.fill(values, "");
            boolean hasValue = false;
            for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
                String value = "";
                cell = row.getCell(columnIndex);
                if (cell != null) {
                    switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue().trim();
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            if (date != null) {
                                value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                            } else {
                                value = "";
                            }
                        } else {
                            value = (""+cell.getNumericCellValue()).trim();
                            if(value.endsWith(".0")) {
                                value = value.replace(".0", "");
                            }
                        }
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA:
                        // 导入时如果为公式生成的数据则无值
                        try {
//                            DecimalFormat df = new DecimalFormat("0");
//                            value = df.format(cell.getNumericCellValue());
                            value = cell.getStringCellValue().trim();
                        } catch (Exception e) {
                            value = cell.getStringCellValue().trim();
                        }
                        if(value.endsWith(".0") && value.length() >= 3) {
                            value = value.replace(".0", "");
                        }
                        break;
                    case HSSFCell.CELL_TYPE_BLANK:
                        break;
                    case HSSFCell.CELL_TYPE_ERROR:
                        value = "";
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        value = (cell.getBooleanCellValue() == true ? "Y" : "N");
                        break;
                    default:
                        value = "";
                    }
                }
                
                if (columnIndex == 0 && value.trim().equals("")) {
                    break;
                }
                values[columnIndex] = rightTrim(value);
                hasValue = true;
            }
            if (hasValue) {
                result.add(values);
            }
        }
        in.close();
        String[][] returnArray = new String[result.size()][rowSize];
        for (int i = 0; i < returnArray.length; i++) {
            returnArray[i] = (String[]) result.get(i);
        }
        return returnArray;
    }

    /**
     * 
     * 去掉字符串右边的空格
     * 
     * @param str
     *            要处理的字符串
     * 
     * @return 处理后的字符串
     * 
     */

    private static String rightTrim(String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        for (int i = length - 1; i >= 0; i--) {
            if (str.charAt(i) != 0x20) {
                break;
            }
            length--;
        }
        return str.substring(0, length);
    }

}
