package org.builgforsdg.team127a.easymedic.utilities;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GeneralMethods {


    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(date.getTime());
    }

    /**
     * Parse date from an input string. Return current time if parsed failed
     * @param dateStr input date string
     * @return date from date string
     */
    public static Date parseDate(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(System.currentTimeMillis());
    }


    public static boolean checkTextLength(EditText myEditText, int minimumLength, Context context) {
        if (myEditText.getText().toString().trim().length() < minimumLength) {
            Toast.makeText(context,  "Search Term Length is invalid.", Toast.LENGTH_SHORT).show();
            return true;

        }
        return false;
    }

    public static boolean checkEmpty(EditText myEditText, Context context) {
        if (myEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Search Term is missing.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }






}
