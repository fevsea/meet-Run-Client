package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackString;

public abstract class GetCity extends AsyncTask<String, String, String> implements AsyncTaskCallbackString {

    @Override
    protected String doInBackground(String... params) {

        URL url = null;
        String result = null;

        // build a URL
        try {
            url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + params[0] + "&components=country:ES&region=es&key=AIzaSyDm6Bt_p5gn3F7DAJJLMYSEOR0kyqNL800");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // read from the URL
        Scanner scan = null;
        try {
            scan = new Scanner(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = new String();

        while (scan.hasNext()) str += scan.nextLine();
        scan.close();

        // build a JSON object
        JSONObject obj = null;
        try {
            obj = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //if (! obj.getString("status").equals("OK"))
        //return;

        // get the first result
        JSONObject res = null;
        try {
            res = obj.getJSONArray("results").getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            result = res.getString("formatted_address");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("URL", result);

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        onResponseReceived(s);
    }
}