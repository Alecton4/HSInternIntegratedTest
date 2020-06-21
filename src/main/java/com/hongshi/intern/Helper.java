package com.hongshi.intern;

import java.util.Scanner;

public class Helper {

    public static Scanner scanner = new Scanner(System.in);

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            //ignore
        }
        return false;
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            //ignore
        }
        return false;
    }

}
