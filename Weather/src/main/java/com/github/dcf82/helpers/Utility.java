package com.github.dcf82.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * @author David Castillo Fuentes
 */
public class Utility {

    /**
     * Utility function to print popup messages
     * @param msgId Message Id to be printed on the screen
     * @param context Current context in used
     */
    public static void showMessage(Context context, int msgId) {
        if (context == null || (context instanceof Activity && ((Activity)context).isFinishing())) return;
        Toast to = Toast.makeText(context, context.getResources().getString(msgId), Toast
                .LENGTH_LONG);
        to.setGravity(Gravity.CENTER, 0, 0);
        to.show();
    }

    /**
     * Get the corresponding day in string format of a given calendar day
     * @param dayNum Day
     */
    public static String getDay(int dayNum) {
        String day = "";
        switch(dayNum) {
            case Calendar.SUNDAY:
                day = "Sunday";
                break;
            case Calendar.MONDAY:
                day = "Monday";
                break;
            case Calendar.TUESDAY:
                day = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                day = "Wednesday";
                break;
            case Calendar.THURSDAY:
                day = "Thursday";
                break;
            case Calendar.FRIDAY:
                day = "Friday";
                break;
            case Calendar.SATURDAY:
                day = "Saturday";
                break;
        }
        return day;
    }

    /**
     * Utility method to print a non null nor empty string into a TextView
     */
    public static void setData(TextView tv,String prefix, String data) {
        if (data == null || data.equals("")) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(prefix + data);
        }
    }

    /**
     * Utility method to print a non null nor empty string into a TextView
     */
    public static void setData(TextView tv,String prefix, double data) {
        setData(tv, prefix, Double.toString(data));
    }

    /**
     * Utility method to capitalize an String
     * @param text: Input text
     * @param separator: Separator to use
     */
    public static String capitalize(String text, String separator) {
        String out = "";

        if (text == null || text.length() == 0 || text.trim().length() == 0) return out;

        text = text.trim();

        String[] buff = text.split(separator);

        for (int i = 0; i < buff.length; i++) {
            if (buff[i].trim().length() == 0) continue;
            out += (i > 0 ? separator : "") + Character.toString(buff[i].charAt(0)).toUpperCase();
            if (buff[i].length() > 1) {
                out += buff[i].substring(1).toLowerCase();
            }
        }

        return out;
    }
}
