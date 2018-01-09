package edu.upc.fib.meetnrun.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public abstract class GetStaticMap extends AsyncTask<String, String, Bitmap> implements AsyncTaskCallbackBitmap {

    private ArrayList<LatLng> path;
    private String key;

    public GetStaticMap(ArrayList<LatLng> path, String key) {
        this.key = key;
        this.path = path;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = null;
            String urlpath = "";

            // build a URL
            try {
                for (LatLng coord : path) {
                    urlpath = urlpath + coord.latitude + "," + coord.longitude + "|";
                }
                urlpath = urlpath.substring(0, urlpath.length() - 1);

                url = new URL("http://maps.googleapis.com/maps/api/staticmap?size=400x400&path=" + urlpath + "&sensor=false&key=" + key);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            return image;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        onResponseReceived(bitmap);
        super.onPostExecute(bitmap);
    }
}