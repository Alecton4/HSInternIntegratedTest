package com.hongshi.intern;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

  public static void printMapList(List<Map<String, String>> content){
        if (content != null) {
            for (Map<String, String> map : content) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
                }
                System.out.println();
            }
            System.out.println("Number of rows: " + content.size());
        } else {
            System.out.println("Invalid content!");
        }
    }

    public static void printListList(List<ArrayList<String>> content){
        if (content != null) {
            for (ArrayList<String> strList : content) {
                for (String str : strList) {
                    System.out.print(str + " ");
                }
                System.out.println();
            }
            System.out.println("Number of rows: " + content.size());
        } else {
            System.out.println("Invalid content!");
        }
    }
}
