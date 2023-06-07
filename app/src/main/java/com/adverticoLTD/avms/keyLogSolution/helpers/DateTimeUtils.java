package com.adverticoLTD.avms.keyLogSolution.helpers;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeUtils {

    public static String getCurrentDate(Context context) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String getCurrentTime(Context context) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
        String formattedDate = df.format(c.getTime());

        if (formattedDate.contains("p.m.")) {
            formattedDate = formattedDate.replace("p.m.", "PM");
        } else {
            formattedDate = formattedDate.replace("a.m.", "AM");
        }

        return formattedDate;
    }

    public static long getCurrentTimeInMilis(){
        return System.currentTimeMillis();
    }

}
