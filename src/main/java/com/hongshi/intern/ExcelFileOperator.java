package com.hongshi.intern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ExcelFileOperator {
    public static List<Map<String, String>> excelToList(String filePath) {
        Workbook wb = readExcel(filePath);
        Sheet sheet;
        Row row;
        List<Map<String, String>> list = null;
        String cellData;

        if (wb != null) {
            //用来存放表中数据
            list = new ArrayList<Map<String, String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int numRow = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int numCol = row.getPhysicalNumberOfCells();
            //obtain column names
            String[] columnNames = new String[numCol];
            for (int i = 0; i < numCol; ++i) {
                columnNames[i] = (String) getCellValue(row.getCell(i));
            }

            for (int i = 1; i < numRow; i++) {
                Map<String, String> map = new LinkedHashMap<String, String>();
                row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < numCol; j++) {
                        cellData = (String) getCellValue(row.getCell(j));
                        map.put(columnNames[j], cellData);
                    }
                } else {
                    break;
                }
                list.add(map);
            }
        }
        return list;
    }

    public static void printContent(List<Map<String, String>> list) {
        if (list != null) {
            //遍历解析出来的list
            for (Map<String, String> map : list) {
                for (Entry<String, String> entry : map.entrySet()) {
                    System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
                }
                System.out.println();
            }
            System.out.println("Number of rows: " + list.size());
        } else {
            System.out.println("Invalid content!");
        }
    }

    //读取excel
    public static Workbook readExcel(String filePath) {
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                return new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return new XSSFWorkbook(is);
            } else {
                return null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getCellValue(Cell cell) {
        Object cellValue;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
                case NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case FORMULA: {
                    //判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }

//    public static String convertCellValueToString(Cell cell) {
//        if (cell == null) {
//            return null;
//        }
//        String returnValue = null;
//        switch (cell.getCellType()) {
//            case NUMERIC:   //数字
//                Double doubleValue = cell.getNumericCellValue();
//
//                // 格式化科学计数法，取一位整数
//                DecimalFormat df = new DecimalFormat("0");
//                returnValue = df.format(doubleValue);
//                break;
//            case STRING:    //字符串
//                returnValue = cell.getStringCellValue();
//                break;
//            case BOOLEAN:   //布尔
//                Boolean booleanValue = cell.getBooleanCellValue();
//                returnValue = booleanValue.toString();
//                break;
//            case BLANK:     // 空值
//                break;
//            case FORMULA:   // 公式
//                returnValue = cell.getCellFormula();
//                break;
//            case ERROR:     // 故障
//                break;
//            default:
//                break;
//        }
//        return returnValue;
//    }

    public static void exportExcel(List<String> list, String filePath) {
        try {
            int numRecords = list.size();
            Workbook wb = new SXSSFWorkbook();    //.xlsx
//            Workbook wb = new HSSFWorkbook();    //.xls
            FileOutputStream fileOut = new FileOutputStream(filePath);

            int numSheets = 1;
            if (numRecords > 50000) {
                numSheets = (numRecords % 50000) == 0 ? (numRecords / 50000) : ((numRecords / 50000) + 1);
            }
            System.out.println("Start writing to excel file，num of records：" + numRecords + ", num of sheets：" + numSheets);
            int rowNums = 0;
            int listIndex = 0;
            for (int i = 1; i <= numSheets; i++) {
                Sheet sheet = wb.createSheet();
                sheet.setDefaultColumnWidth(25);
                if (i == numSheets) {
                    rowNums = numRecords - ((numSheets - 1) * 50000);
                    for (int j = 0; j < rowNums; j++) {
                        Row row = sheet.createRow(j);
                        row.createCell(0).setCellValue(list.get(j + listIndex));
                    }
                } else {
                    for (int j = 0; j < 50000; j++) {
                        Row row = sheet.createRow(j);
                        row.createCell(0).setCellValue(list.get(j + listIndex));
                    }
                }
                listIndex = listIndex + (i * 50000);
                System.out.println(i + "th sheet writing successfully...");
            }
            wb.write(fileOut);
            fileOut.close();
            wb.close();
            System.out.println("Writing successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
