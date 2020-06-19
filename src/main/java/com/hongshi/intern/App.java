package com.hongshi.intern;

import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        MySqlConnector.connectMySql();
        App.scanner.close();
    }
}
