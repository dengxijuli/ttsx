package com.example.ttsxscheduling;

import java.util.Scanner;

/**
 * @program: -
 * @description: 面试题1 深圳开科唯识
 * @author: dx
 * @create: 2023/6/25 14:36
 */
public class test {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("请输入年份：");
        int year = input.nextInt();
        System.out.print("请输入月份：");
        int month = input.nextInt();
        System.out.print("请输入日期：");
        int day = input.nextInt();
        input.close();
        int dayOfYear = getDayOfYear(year, month, day);
        System.out.println(year + "年" + month + "月" + day + "日是" + year + "年的第" + dayOfYear + "天。");
    }

    private static int getDayOfYear(int year, int month, int day) {
        int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; // 每月的天数
        int dayOfYear = day;
        for (int i = 0; i < month - 1; i++) {
            dayOfYear += daysOfMonth[i];
        }
        if (month > 2 && isLeapYear(year)) { // 计算闰年2月份的天数
            dayOfYear += 1;
        }
        return dayOfYear;
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
}
