package com.hongshi.intern;

import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        byte choice = -1;
        while (choice != 0) {
            System.out.println("----------------------------------------");
            System.out.println("Welcome: ");
            System.out.println("1. Connect to MySql");
            System.out.println("2. Process Excel file");
            System.out.println("0. Quit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextByte();
            scanner.nextLine();

            switch (choice){
                case 1:
                    System.out.println("----------------------------------------");
                    MySqlConnector.connectMySql();
                    break;
                case 2:
                    System.out.println("----------------------------------------");
                    PoiExcelOperator.excelOperator();
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

        App.scanner.close();
    }
}
