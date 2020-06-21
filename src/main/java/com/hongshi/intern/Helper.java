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

//    public static boolean isNumeric(String value) {
//        Pattern pattern = Pattern.compile("[0-9]*");
//        if (value.indexOf(".") > 0) {//判断是否有小数点
//            if (value.indexOf(".") == value.lastIndexOf(".") && value.split("\\.").length == 2) { //判断是否只有一个小数点
//                return pattern.matcher(value.replace(".", "")).matches();
//            } else {
//                return false;
//            }
//        } else {
//            return pattern.matcher(value).matches();
//        }
//    }
//
//    public static boolean isNumeric_v2(String value) {
//        return value.matches("-?[0-9]+.*[0-9]*");
//    }
//
//    public static boolean isNumeric_v3(String str) {
//        //Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");//这个有问题，一位的整数不能通过
//        Pattern pattern = Pattern.compile("^([-+])?\\d+(\\.\\d+)?$");//这个是对的
//        Matcher isNum = pattern.matcher(str);
//        return isNum.matches();
//    }
//
//    public static boolean isInteger(String value){
//        Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");
//        return pattern.matcher(value).matches();
//    }
}
