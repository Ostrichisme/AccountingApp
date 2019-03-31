package com.matianyi.accountingapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    //unix time
    public static String getFormattedTime(long timeStamp){

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(new Date(timeStamp));
    }

    // get date
    public static String getFormattedDate(long timeStamp){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date(timeStamp));
    }

}
