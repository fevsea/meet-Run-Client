package edu.upc.fib.meetnrun.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;


/**
 * Created by Awais Iqbal on 02/11/2017.
 */

public class JsonUtils {
    public static final int PARAMEXCEPTION = 0;
    public static final int NOTFOUNDEXCEPTION = 1;
    public static final int AUTORIZATIONEXCEPTION = 2;

    public enum ExceptionTypes {
        ParamsException, NotFoundException, AutorizationException
    }

    public static final String TAG = "JSONUTILS";

    public static Map<String, String> ParseJsonToGetProblems(String json) {
        Map<String, String> m = new HashMap<>();
        JsonObject jObj = new Gson().fromJson(json, JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : jObj.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            m.put(key, value);
            Log.d(TAG, "Key: " + key + "\tValue: " + value);
        }
        return m;
    }


    public static ParamsException CreateParamExceptionFromJson(String jSON) {
        ParamsException pe = new ParamsException(JsonUtils.ParseJsonToGetProblems(jSON));
        return pe;
    }

    public static AutorizationException CreateAutorizationExceptionFromJson(String jSON) {
        AutorizationException pe = new AutorizationException(JsonUtils.ParseJsonToGetProblems(jSON));
        return pe;
    }

    public static NotFoundException CreateNotFoundExceptionFromJson(String jSON) {
        NotFoundException pe = new NotFoundException(JsonUtils.ParseJsonToGetProblems(jSON));
        return pe;
    }

    public static ForbiddenException CreateForbiddenExceptionFromJson(String jSON) {
        ForbiddenException pe = new ForbiddenException(JsonUtils.ParseJsonToGetProblems(jSON));
        return pe;
    }
}
