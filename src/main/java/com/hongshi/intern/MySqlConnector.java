package com.hongshi.intern;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MySqlConnector {
    public static void connectMySql() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Start connecting MySQL...");
        Connection conn;

        System.out.print("Enter host: ");
        String host = scanner.nextLine();
        System.out.print("Enter port: ");
        String port = scanner.nextLine();
        System.out.print("Enter database name: ");
        String database = scanner.nextLine();

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false";
        String driver = "com.mysql.cj.jdbc.Driver";

        System.out.print("Enter user name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        //for test
        host = "localhost";
        port = "3306";
        database = "hs_test";
        userName = "root";
        password = "10030330";
        url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false";

        Statement stmt = null;
        ResultSet rs = null;
        try {
            //a.导入驱动，加载具体的驱动类
            Class.forName(driver);
            //b.与数据库建立连接
            conn = DriverManager.getConnection(url, userName, password);
            //c.发送sql语句，执行sql语句
            stmt = conn.createStatement();
            System.out.println("Connection successful!");

            //variables needed
            byte choice = -1;
            String sql = "";
            String tableName = "";
            String columnNames = "";
            String values = "";
            String restriction = "";
            int numResultRow = 0;
            String filePath = "";
            List<Map<String, String>> fileContent;

            while (choice != 0) {
                System.out.println("----------------------------------------");
                System.out.println("1: Enter a command in one line");
                System.out.println("2: View database table content");
                System.out.println("3: View Excel file content");
                System.out.println("4: Write database table to Excel file");
                System.out.println("5: Write Excel file to database table");
                System.out.println("0: Quit");
                System.out.print("Choose your operation: ");
                choice = scanner.nextByte();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("----------------------------------------");
                        System.out.println("enter your command(in one single line): ");
                        sql = scanner.nextLine();

                        try {
                            //增删改时用executeUpdate
                            int count = stmt.executeUpdate(sql);
                            if (count > 0) {
                                System.out.println("Operation success");
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                        break;

                    case 2:
                        System.out.println("----------------------------------------");
                        System.out.print("Enter table name: ");
                        tableName = scanner.nextLine();
                        System.out.print("Enter column names(separated by commas): ");
                        columnNames = scanner.nextLine();
                        System.out.print("Enter restrictions(in one single line, with \"WHERE\"): ");
                        restriction = scanner.nextLine();
                        sql = "SELECT " + columnNames + " FROM " + tableName + restriction;

                        try {
                            //查询的时候用executeQuery
                            rs = stmt.executeQuery(sql);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        numResultRow = 0;
                        while (rs.next()) {
                            ++numResultRow;
                        }
                        printTable(rs);
                        System.out.println("Number of results: " + numResultRow);

                        break;

                    case 3:
                        System.out.println("----------------------------------------");
                        System.out.print("Enter Excel file path: ");
                        filePath = scanner.nextLine();
                        filePath = "D:\\yzhao\\Documents\\tmp\\t_area_part.xlsx";
                        fileContent = ExcelFileOperator.excelToList(filePath);
                        ExcelFileOperator.printContent(fileContent);
                        break;

                    case 4:
                        System.out.println("----------------------------------------");
                        System.out.print("Enter table name: ");
                        tableName = scanner.nextLine();
                        System.out.print("Enter column names(separated by commas): ");
                        columnNames = scanner.nextLine();
                        System.out.print("Enter restrictions(in one single line, with \"WHERE\"): ");
                        restriction = scanner.nextLine();
                        sql = "SELECT " + columnNames + " FROM " + tableName + restriction;

                        try {
                            //查询的时候用executeQuery
                            rs = stmt.executeQuery(sql);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        numResultRow = 0;
                        while (rs.next()) {
                            ++numResultRow;
                        }

                        break;

                    case 5:
                        System.out.println("----------------------------------------");
                        System.out.print("Enter Excel file path: ");
                        filePath = scanner.nextLine();
                        System.out.print("Enter table you want to write to: ");
                        tableName = scanner.nextLine();

                        //for test
                        filePath = "D:\\yzhao\\Documents\\tmp\\t_area_part.xlsx";
                        tableName = "t_area_test";

                        fileContent = ExcelFileOperator.excelToList(filePath);
                        if (fileContent != null) {
                            System.out.println("Writing to table...");
                            //遍历解析出来的list
                            for (Map<String, String> map : fileContent) {
                                columnNames = "";
                                values = "";
                                for (Map.Entry<String, String> entry : map.entrySet()) {
                                    if (!entry.getKey().equals("ctime")) {
                                        columnNames += "," + entry.getKey();
                                        values += ",\'" + entry.getValue() + "\'";
                                    }
                                }
                                columnNames = columnNames.substring(1, columnNames.length());
                                values = values.substring(1, values.length());
                                sql = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + values + ")";
                                stmt.executeUpdate(sql);
                            }
                            System.out.println("Operation successful!");
                        } else {
                            System.out.println("Invalid content!");
                        }
                        break;

                    case 0:
                        break;
                    default:
                        System.out.println("----------------------------------------");
                        System.out.println("Invalid choice, plz enter correct choice");
                }
            }
            scanner.close();
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }
        }
    }

    public static void printRow(int id, String area_code, String area_name, int level, String parent_code, String code2, String ctime) {
        System.out.println("id = " + id + ", 地区码 = " + area_code + ", 地区名称 = " + area_name + ", 级别 = " + level + ", 上级码 = " + parent_code + ", 城乡区别 = " + code2 + ", 创建时间 = " + ctime);
    }

    public static void printTable(ResultSet rs) {
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String area_code = rs.getString("area_code");
                String area_name = rs.getString("area_name");
                int level = rs.getInt("level");
                String parent_code = rs.getString("parent_code");
                String code2 = rs.getString("code2");
                String ctime = rs.getString("ctime");
                printRow(id, area_code, area_name, level, parent_code, code2, ctime);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
