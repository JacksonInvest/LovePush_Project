package com.lovepushapp.helper;

import android.content.Context;
import android.util.Log;

import com.lovepushapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeAgo {

public String covertTimeToText(long dataDate, Context context) {

    String convTime = null;

    String prefix = "";
    String suffix = "ago";

    try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String msgData = dateFormat.format(new Date(dataDate * 1000));

        Date pasTime = dateFormat.parse(msgData);

        Date nowTime = new Date();

        long dateDiff = nowTime.getTime() - pasTime.getTime();

        long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
        long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
        long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
        long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

        if (second < 60) {
            if (second<1){
                convTime = "1 "+context.getString(R.string.secondAgo);
            }else {
                convTime = second+ " " +context.getString(R.string.secondAgo);
            }
        } else if (minute < 60) {
            convTime = minute+ " " +context.getString(R.string.minuteAgo);
        } else if (hour < 24) {
            convTime = hour+ " " +context.getString(R.string.hourAgo);
        } else if (day >= 7) {
            if (day > 30) {
                convTime = (day / 30)+ " " +context.getString(R.string.monthAgo);
            } else if (day > 360) {
                convTime = (day / 360)+ " " +context.getString(R.string.yearAgo);
            } else {
                convTime = (day / 7) + " " +context.getString(R.string.weekAgo);
            }
        } else if (day < 7) {
            convTime = day+ " " +context.getString(R.string.dayAgo);
        }

    } catch (ParseException e) {
        e.printStackTrace();
//        Log.e("ConvTimeE", e.getMessage());
    }

    return convTime;

}


    public String covertTimeStringToAgo(String dataDate, Context context) {

        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dateParser.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateParser.parse(dataDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("DateConverter", "convertDate: "+date);
        // Then convert the Date to a String, formatted as you dd/MM/yyyy
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        System.out.println(dateFormatter.format(date));


        String convTime = null;

        String prefix = "";
        String suffix = "ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date pasTime = dateFormat.parse(dateFormatter.format(date));

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                if (second<1){
                    convTime = "1 "+context.getString(R.string.secondAgo);
                }else {
                    convTime = second+ " " +context.getString(R.string.secondAgo);
                }
            } else if (minute < 60) {
                convTime = minute+ " " +context.getString(R.string.minuteAgo);
            } else if (hour < 24) {
                convTime = hour+ " " +context.getString(R.string.hourAgo);
            } else if (day >= 7) {
                if (day > 30) {
                    convTime = (day / 30)+ " " +context.getString(R.string.monthAgo);
                } else if (day > 360) {
                    convTime = (day / 360)+ " " +context.getString(R.string.yearAgo);
                } else {
                    convTime = (day / 7) + " " +context.getString(R.string.weekAgo);
                }
            } else if (day < 7) {
                convTime = day+ " " +context.getString(R.string.dayAgo);
            }


        } catch (ParseException e) {
            e.printStackTrace();
//            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;



    }

}