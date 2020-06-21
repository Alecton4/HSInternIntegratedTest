package com.hongshi.intern;

public class App {

    public static void main(String[] args) {
        MySqlConnector.connectMySql();
        Helper.scanner.close();
    }

}
