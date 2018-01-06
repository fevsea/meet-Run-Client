package edu.upc.fib.meetnrun;


import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.upc.fib.meetnrun.models.CurrentSession;

class TestUtils {

    static final String TEST_USERNAME = "Batman";
    static final String TEST_COMPLETE_NAME = "Bruce Wayne";
    static final String TEST_PASSWORD = "soybatman";
    static final String TEST_MEETING = "N'em a corre per l'eixample";
    static final String TEST_POSTCODE = "08758 Cervelló, Barcelona, Spain";
    static final String TEST_FRIEND = "marcP";
    static final String TEST_NEW_MEETING = "Functional Test Meeting";
    static final String TEST_NEW_DESCRIPTION = "Functional Test Description";
    static final String TEST_NEW_LEVEL = "0";

    private static final String MY_PREFS_NAME = "TokenFile";

    static void deleteToken(Context context) {
        CurrentSession cs = CurrentSession.getInstance();
        cs.setToken(null);
        cs.setCurrentUser(null);
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", cs.getToken());
        editor.apply();
    }

    static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return Integer.valueOf(simpleDateFormat.format(date));
    }

    static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        return Integer.valueOf(simpleDateFormat.format(date));
    }

    static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        return Integer.valueOf(simpleDateFormat.format(date));
    }

    static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        return Integer.valueOf(simpleDateFormat.format(date));
    }

    static int getCurrentMinute() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm");
        return Integer.valueOf(simpleDateFormat.format(date));
    }
}
