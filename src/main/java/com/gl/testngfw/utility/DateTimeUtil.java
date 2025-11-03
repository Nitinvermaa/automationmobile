package com.gl.testngfw.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
    /**
     * To format date for reporting purpose
     *
     * @return :
     */
    public static String getDate() {
        String currentDate = "";
        int t = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        currentDate = currentDate.concat(String.valueOf(t));
        t = Calendar.getInstance().get(Calendar.MONTH);
        currentDate = currentDate.concat("_" + (t + 1));
        t = Calendar.getInstance().get(Calendar.YEAR);
        currentDate = currentDate.concat("_" + t);
        t = Calendar.getInstance().get(Calendar.HOUR);
        currentDate = currentDate.concat("-" + t);
        t = Calendar.getInstance().get(Calendar.MINUTE);
        currentDate = currentDate.concat("." + t);
        t = Calendar.getInstance().get(Calendar.AM_PM);

        if (t == 0) {
            currentDate = currentDate.concat("AM");
        } else {
            currentDate = currentDate.concat("PM");
        }
        return currentDate;
    }

    /**
     * Method to get Day
     *
     * @return : current day
     */
    public static String getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    }

    public static String getCurrentMonth() {
        String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec"};

        Calendar cal = Calendar.getInstance();
        return monthName[cal.get(Calendar.DAY_OF_MONTH)];
    }

    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}
