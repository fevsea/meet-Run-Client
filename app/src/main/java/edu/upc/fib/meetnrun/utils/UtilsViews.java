package edu.upc.fib.meetnrun.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.ProfileFragmentTemplate;

/**
 * Created by eric on 2/12/17.
 */

public class UtilsViews {

    public GradientDrawable getColoredCircularShape(char letter, View view) {

        int[] colors = view.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(view.getContext(),R.drawable.user_profile_circular_text_view);
        int position = letter%colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }

    public AlertDialog showDialog(String title, String message, String okButtonText, String negativeButtonText, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okButtonText, ok);
        if (negativeButtonText != null && cancel != null)
            builder.setNegativeButton(negativeButtonText, cancel);
        AlertDialog dialog = builder.create();
        return dialog;
        //dialog.show();
    }

    public static String getExpirationText(String deadline, String expirationTextDays, String expirationTextNoDays, String expirationPastTextDays, String expirationPastTextNoDays) throws ParseException {
        Date dateTime;
        String expirationText;
        dateTime = UtilsGlobal.parseDate(deadline);
        if (dateTime.getTime() > System.currentTimeMillis()) {
            final long millis = dateTime.getTime() - System.currentTimeMillis();
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(days);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.DAYS.toMinutes(days) - TimeUnit.HOURS.toMinutes(hours);
            if (days > 0) {
                expirationText = String.format(Locale.forLanguageTag("es"), expirationTextDays, days, hours, minutes);
            } else {
                expirationText = String.format(Locale.forLanguageTag("es"), expirationTextNoDays, hours, minutes);
            }
        }
        else {
            final long millis = System.currentTimeMillis() - dateTime.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(days);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.DAYS.toMinutes(days) - TimeUnit.HOURS.toMinutes(hours);
            if (days > 0) {
                expirationText = String.format(Locale.forLanguageTag("es"), expirationPastTextDays, days, hours, minutes);
            } else {
                expirationText = String.format(Locale.forLanguageTag("es"), expirationPastTextNoDays, hours, minutes);
            }
        }
        return expirationText;
    }
}
