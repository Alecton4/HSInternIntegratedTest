package com.hongshi.intern;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MySqlConnector {

    public static void connectMySql() {
        System.out.println("Start connecting MySQL...");
        System.out.print("Enter host: ");
        String host = Helper.scanner.nextLine();
        System.out.print("Enter port: ");
        String port = Helper.scanner.nextLine();
        System.out.print("Enter database name: ");
        String database = Helper.scanner.nextLine();
        System.out.print("Enter user name: ");
        String userName = Helper.scanner.nextLine();
        System.out.print("Enter password: ");
        String password = Helper.scanner.nextLine();

        //for test
        host = "localhost";
        port = "3306";
        database = "hs_test";
        userName = "root";
        password = "10030330";

        // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
//         final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//         final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB";

        // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
        final String DRIVER = "com.mysql.cj.jdbc.Driver";
        final String URL = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false";

        Connection conn = null;
        Statement stmtForRS = null;
        Statement stmtForRSMD = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        try {
            //a.导入驱动，加载具体的驱动类
            Class.forName(DRIVER);
            //b.与数据库建立连接
            conn = DriverManager.getConnection(URL, userName, password);
            //c.发送sql语句，执行sql语句
            stmtForRS = conn.createStatement();
            stmtForRSMD = conn.createStatement();
            System.out.println("Connection successful!");
            //variables needed
            byte choice = -1;
            String sql;
            String tableName;
            String columnNames;
            String values;
            String restriction;
            List<Map<String, String>> tableContent = null;
            String filePath;
            List<Map<String, String>> fileContent = null;

            while (choice != 0) {
                System.out.println("----------------------------------------");
                System.out.println("1: Enter a command in one line");
                System.out.println("2: View database table content");
                System.out.println("3: View Excel file content");
                System.out.println("4: Write database table to Excel file");
                System.out.println("5: Write Excel file to database table");
                System.out.println("0: Quit");
                System.out.print("Choose your operation: ");
                choice = Helper.scanner.nextByte();
                Helper.scanner.nextLine();

                switch (choice) {
                    case 1:
                        //finished
                        System.out.println("----------------------------------------");
                        System.out.println("enter your command(in one single line): ");
                        sql = Helper.scanner.nextLine();

                        try {
                            int count = stmtForRS.executeUpdate(sql);    //增删改时用executeUpdate
                            if (count > 0) {
                                System.out.println("Operation success");
                            }
                        } catch (SQLException throwable) {
                            throwable.printStackTrace();
                        }

                        break;

                    case 2:
                        //finished
                        System.out.println("----------------------------------------");
                        System.out.print("Enter table name: ");
                        tableName = Helper.scanner.nextLine();
                        System.out.print("Enter column names(separated by commas): ");
                        columnNames = Helper.scanner.nextLine();
                        System.out.print("Enter restrictions(in one single line, with \"WHERE\"): ");
                        restriction = Helper.scanner.nextLine();

                        //for test
                        tableName = "t_area";
                        columnNames = "*";
                        restriction = "";

                        sql = "SELECT " + columnNames + " FROM " + tableName + restriction;
                        try {
                            rs = stmtForRS.executeQuery(sql);    //查询的时候用executeQuery
                            rsmd = stmtForRSMD.executeQuery(sql).getMetaData();
                            printTable(rs, rsmd);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                        break;

                    case 3:
                        //finished but class related to Excel needs optimizing
                        System.out.println("----------------------------------------");
                        System.out.print("Enter Excel file path: ");
                        filePath = Helper.scanner.nextLine();

                        //for test
                        filePath = "D:\\yzhao\\Documents\\tmp\\t_area_part.xlsx";

                        ExcelFileOperator.printExcelFromListList(filePath);
                        break;

                    case 4:
                        //finished but class related to Excel needs optimizing
                        System.out.println("----------------------------------------");
                        System.out.print("Enter table name: ");
                        tableName = Helper.scanner.nextLine();
                        System.out.print("Enter column names(separated by commas): ");
                        columnNames = Helper.scanner.nextLine();
                        System.out.print("Enter restrictions(in one single line, with \"WHERE\"): ");
                        restriction = Helper.scanner.nextLine();
                        System.out.print("Enter Excel file path: ");
                        filePath = Helper.scanner.nextLine();

                        //for test
                        tableName = "t_area";
                        columnNames = "*";
                        restriction = "";
                        filePath = "D:\\yzhao\\Documents\\tmp\\output_test.xlsx";

                        sql = "SELECT " + columnNames + " FROM " + tableName + restriction;
                        try {
                            rs = stmtForRS.executeQuery(sql);    //查询的时候用executeQuery
                            rsmd = stmtForRSMD.executeQuery(sql).getMetaData();
                            tableContent = tableToList(rs, rsmd);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        ExcelFileOperator.exportExcelFromMapList(tableContent, filePath);

                        break;

                    case 5:
                        //TODO
                        System.out.println("----------------------------------------");
                        System.out.print("Enter Excel file path: ");
                        filePath = Helper.scanner.nextLine();
                        System.out.print("Enter table you want to write to: ");
                        tableName = Helper.scanner.nextLine();

                        //for test
                        filePath = "D:\\yzhao\\Documents\\tmp\\t_area_part.xlsx";
                        tableName = "t_area_test";

                        fileContent = ExcelFileOperator.excelToMapList(filePath);
                        if (fileContent != null) {
                            System.out.println("Writing to table...");

                            for (Map<String, String> map : fileContent) {
                                columnNames = "";
                                values = "";
                                for (Map.Entry<String, String> entry : map.entrySet()) {
                                        columnNames += "," + entry.getKey();
                                        values += ",\'" + entry.getValue() + "\'";
                                }
                                columnNames = columnNames.substring(1);
                                values = values.substring(1);
                                sql = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + values + ")";
                                stmtForRS.executeUpdate(sql);
                            }
                            System.out.println("Operation successful!");
                        } else {
                            System.out.println("Invalid content!");
                        }
                        break;

                    case 0:
                        System.out.println("----------------------------------------");
                        System.out.println("QUIT");
                        break;
                    default:
                        System.out.println("----------------------------------------");
                        System.out.println("Invalid choice!");
                }
            }
            if (rs != null) rs.close();
            if (stmtForRS != null) stmtForRS.close();
            if (stmtForRSMD != null) stmtForRSMD.close();
            if (conn != null) conn.close();

        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            //close resources
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            if (stmtForRS != null) {
                try {
                    stmtForRS.close();
                } catch (SQLException se2) {
                }//ignore
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }
        }
    }

    public static List<Map<String, String>> tableToList(ResultSet rs, ResultSetMetaData rsmd) {
        try {
            List<Map<String, String>> tableContent = new ArrayList<Map<String, String>>();
            int numCol = rsmd.getColumnCount();

            while (rs.next()) {
                Map<String, String> map = new LinkedHashMap<String, String>();
                for (int i = 0; i < numCol; i++) {
                    map.put(rsmd.getColumnName(i+1), rs.getString(i+1));
                }
                tableContent.add(map);
            }
            return tableContent;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void printTable(ResultSet rs, ResultSetMetaData rsmd){
        List<Map<String, String>> tableContent = tableToList(rs, rsmd);
        if (tableContent != null){
            for (Map<String, String> map : tableContent) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
                }
                System.out.println();
            }
            System.out.println("Number of rows: " + tableContent.size());
        }else {
            System.out.println("Invalid content!");
        }
    }
}
