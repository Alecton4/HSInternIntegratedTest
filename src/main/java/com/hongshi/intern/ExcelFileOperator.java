package com.hongshi.intern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
        List<Map<String, String>> fileContent = null;
        String cellData;

        if (wb != null) {
            fileContent = new ArrayList<Map<String, String>>();
            //obtain num of rows
            sheet = wb.getSheetAt(0);
            int numRow = sheet.getPhysicalNumberOfRows();
            //obtain num of columns
            row = sheet.getRow(0);
            int numCol = row.getPhysicalNumberOfCells();
            //obtain column names
            String[] columnNames = new String[numCol];
            for (int i = 0; i < numCol; ++i) {
                columnNames[i] = cellValueToString(row.getCell(i));
            }

            for (int i = 1; i < numRow; i++) {
                Map<String, String> map = new LinkedHashMap<String, String>();
                row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < numCol; j++) {
                        cellData = cellValueToString(row.getCell(j));
                        map.put(columnNames[j], cellData);
                    }
                } else {
                    break;
                }
                fileContent.add(map);
            }
        }
        return fileContent;
    }

    public static void printFile(List<Map<String, String>> fileContent) {
        if (fileContent != null) {
            for (Map<String, String> map : fileContent) {
                for (Entry<String, String> entry : map.entrySet()) {
                    System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
                }
                System.out.println();
            }
            System.out.println("Number of rows: " + fileContent.size());
        } else {
            System.out.println("Invalid content!");
        }
    }

    private static Workbook readExcel(String filePath) {
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
                throw new IOException("Unsupported file type!");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String cellValueToString(Cell cell) {
        String cellValue = null;
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//日期格式yyyy-mm-dd
                    cellValue = sdf.format(cell.getDateCellValue()); //日期型
                } else {
                    DecimalFormat df = new DecimalFormat("0");//数字格式，防止长数字成为科学计数法形式，或者int变为double形式
                    cellValue = df.format(cell.getNumericCellValue()); //数字型
                }
                break;
            case STRING: //文本类型
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN: //布尔型
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK: //空白
                cellValue = cell.getStringCellValue();
                break;
            case ERROR: //错误
                cellValue = "Error";
                break;
            case FORMULA: //公式
                try {
                    cellValue = String.valueOf(cell.getStringCellValue());
                } catch (IllegalStateException e) {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            default:
                cellValue = cell.getRichStringCellValue() == null ? null : cell.getRichStringCellValue().toString();
        }
        return cellValue;
    }

    public static void exportExcel(List<Map<String, String>> tableContent, String filePath) {
        try {
            String extString = filePath.substring(filePath.lastIndexOf("."));
            Workbook wb = null;
            if (".xls".equals(extString)) {
                wb = new HSSFWorkbook();    //.xls
            } else if (".xlsx".equals(extString)) {
                wb = new XSSFWorkbook();    //.xlsx
            } else {
                throw new IOException("Unsupported file type!");
            }
            FileOutputStream fileOut = new FileOutputStream(filePath);

            int numRecords = tableContent.size();
            int numSheets = 1;
            System.out.println("Start writing to excel file，num of records：" + numRecords + ", num of sheets：" + numSheets);

            Sheet sheet = wb.createSheet();
            sheet.setDefaultColumnWidth(16);
            int currentRow = 0;
            for (Map<String, String> map : tableContent) {
                int currentCol = 0;
                Row row = sheet.createRow(currentRow);
                for (Entry<String, String> entry : map.entrySet()) {
                    if (currentRow == 0) {
                        row.createCell(currentCol).setCellValue(entry.getKey());
                    } else {
                        if (Helper.isInteger(entry.getValue())) {
                            row.createCell(currentCol).setCellValue(Integer.parseInt(entry.getValue()));
                        } else if (Helper.isDouble(entry.getValue())) {
                            row.createCell(currentCol).setCellValue(Double.parseDouble(entry.getValue()));
                        } else {
                            row.createCell(currentCol).setCellValue(entry.getValue());
                        }
                    }
                    ++currentCol;
                }
                ++currentRow;
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
