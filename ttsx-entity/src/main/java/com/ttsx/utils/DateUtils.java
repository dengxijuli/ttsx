package com.ttsx.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
        public static boolean isToday(Date date) {
            Calendar currentCalendar = Calendar.getInstance();
            Calendar targetCalendar = Calendar.getInstance();
            targetCalendar.setTime(date);
            return currentCalendar.get(Calendar.YEAR) == targetCalendar.get(Calendar.YEAR)
                    && currentCalendar.get(Calendar.MONTH) == targetCalendar.get(Calendar.MONTH)
                    && currentCalendar.get(Calendar.DAY_OF_MONTH) == targetCalendar.get(Calendar.DAY_OF_MONTH);
        }
    }