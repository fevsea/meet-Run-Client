package edu.upc.fib.meetnrun.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;


/**
 * Created by Awais Iqbal on 02/11/2017.
 */

public class UtilsGlobal {
    public static final int PARAMEXCEPTION = 0;
    public static final int NOTFOUNDEXCEPTION = 1;
    public static final int AUTORIZATIONEXCEPTION = 2;

    private static final String TAG = "JSONUTILS";

    private static Map<String, String> ParseJsonToGetProblems(String json) {
        Map<String, String> m = new HashMap<>();
        JsonObject jObj = new Gson().fromJson(json, JsonObject.class);
        if (jObj != null) {
            for (Map.Entry<String, JsonElement> entry : jObj.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                m.put(key, value);
                Log.d(TAG, "Key: " + key + "\tValue: " + value);
            }
        }
        return m;
    }


    public static ParamsException CreateParamExceptionFromJson(String jSON) {
        ParamsException pe = new ParamsException(UtilsGlobal.ParseJsonToGetProblems(jSON));
        return pe;
    }

    public static AuthorizationException CreateAutorizationExceptionFromJson(String jSON) {
        AuthorizationException pe = new AuthorizationException(UtilsGlobal.ParseJsonToGetProblems(jSON));
        return pe;
    }

    public static NotFoundException CreateNotFoundExceptionFromJson(String jSON) {
        NotFoundException pe = new NotFoundException(UtilsGlobal.ParseJsonToGetProblems(jSON));
        return pe;
    }

    public static ForbiddenException CreateForbiddenExceptionFromJson() {
        ForbiddenException pe = new ForbiddenException();
        return pe;
    }

    /**
     * Method created to format a object @{@link Date} to a ISO 8601 format datetime String
     *
     * @param date Object date to format
     * @return String with date ISO 8601 format
     */
    public static String formatDate(Date date) {
        //TimeZone tz = TimeZone.getTimeZone("Europe/Madrid"); //for spanish hours
        TimeZone tz = TimeZone.getDefault();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZZ", Locale.US); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(date);
    }

    /**
     * Util method to parse a String in ISO 8601 datetime format to a object @{@link Date}
     *
     * @param lastMessageDateTime String in ISO 8601
     * @return @{@link Date} with date time especified on the String
     */
    public static Date parseDate(String lastMessageDateTime) {
        TimeZone tz = TimeZone.getDefault();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        Date d = null;
        try {
            d = df.parse(lastMessageDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
}
