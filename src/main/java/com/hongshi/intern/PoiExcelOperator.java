package com.hongshi.intern;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PoiExcelOperator {

    public static void excelOperator() {

    }

    public List<ArrayList<String>> readXExcel(String filePath) throws FileNotFoundException, IOException, InvalidFormatException {
        List<ArrayList<String>> strLists = new ArrayList<ArrayList<String>>();  //存放Excel中的数据
        FileInputStream input = new FileInputStream(new File(filePath)); //读取的文件路径
        XSSFWorkbook wb = new XSSFWorkbook(new BufferedInputStream(input));
        XSSFSheet sheet = wb.getSheetAt(0); //获取第一张表

        int rowNum = sheet.getPhysicalNumberOfRows();//得到数据的行数
        System.out.println("行数：" + rowNum);
        strLists.clear();

        //traverse rows
        for (int i = 0; i < rowNum; i++) {
            List<String> strList = new ArrayList<String>();
            XSSFRow row = sheet.getRow(i);
            int colNum = row.getPhysicalNumberOfCells();//得到当前行中存在数据的列数
            //traverse columns
            for (int j = 0; j < colNum; j++) {
                XSSFCell cell = row.getCell(j);
                strList.add(getXCellVal(cell));
            }
            strLists.add(i, (ArrayList<String>) strList); //存储该行
        }

        //打印
        for (ArrayList<String> stringList : strLists) {
            for (String str : stringList) {
                System.out.print(str + "  ");
            }
            System.out.println();
        }
        wb.close();
        return strLists;
    }

    private String getXCellVal(XSSFCell cell) {
        String val = null;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); //日期格式yyyy-mm-dd
        DecimalFormat df = new DecimalFormat("0");             //数字格式，防止长数字成为科学计数法形式，或者int变为double形式
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    val = fmt.format(cell.getDateCellValue()); //日期型
                } else {
                    val = df.format(cell.getNumericCellValue()); //数字型
                }
                break;
            case STRING: //文本类型
                val = cell.getStringCellValue();
                break;
            case BOOLEAN: //布尔型
                val = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK: //空白
                val = cell.getStringCellValue();
                break;
            case ERROR: //错误
                val = "Error";
                break;
            case FORMULA: //公式
                try {
                    val = String.valueOf(cell.getStringCellValue());
                } catch (IllegalStateException e) {
                    val = String.valueOf(cell.getNumericCellValue());
                }
                break;
            default:
                val = cell.getRichStringCellValue() == null ? null : cell.getRichStringCellValue().toString();
        }
        return val;
    }

    public static void printExcel(String filePath) {
        //需要解析的Excel文件
        File file = new File(filePath);
        try {
            //创建Excel，读取文件内容
            HSSFWorkbook workbook = new HSSFWorkbook(FileUtils.openInputStream(file));
            //获取第一个工作表
            //方法一
            /*HSSFSheet sheet = workbook.getSheet("Sheet0");*/
            //方法二
            HSSFSheet sheet = workbook.getSheetAt(0);
            //读取表中的数据
            int firstRowNum = 0;
            //表中最后一行行号
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 0; i < lastRowNum; i++) {
                HSSFRow row = sheet.getRow(i);
                //获取当前行最后单元格列号
                int lastCellNum = row.getLastCellNum();
                for (int j = 0; j < lastCellNum; j++) {
                    HSSFCell cell = row.getCell(j);
                    String value = cell.getStringCellValue();
                    System.out.print(value + "    ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportExcel() {
        String[] title = {"id", "name", "sex"};
        //创建Excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个工作表sheet
        HSSFSheet sheet = workbook.createSheet();
        //创建第一行
        HSSFRow row = sheet.createRow(0);
        //插入第一行数据id,name,sex
        HSSFCell cell = null;
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        //追加数据
        for (int i = 1; i < 11; i++) {
            //创建后续行
            HSSFRow nextrow = sheet.createRow(i);
            //定义单元格
            HSSFCell cell2 = nextrow.createCell(0);
            //为第一个单元格赋值
            cell2.setCellValue("a" + i);
            //为第二个单元格赋值
            cell2 = nextrow.createCell(1);
            cell2.setCellValue("user" + i);
            cell2 = nextrow.createCell(2);
            cell2.setCellValue("男");
        }
        //创建一个文件
        File file = new File("d:/poi_test.xls");
        try {
            file.createNewFile();
            //将Excel内容存盘
            FileOutputStream stream = FileUtils.openOutputStream(file);
            workbook.write(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
