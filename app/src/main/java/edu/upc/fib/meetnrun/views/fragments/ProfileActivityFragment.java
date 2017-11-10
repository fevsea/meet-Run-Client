package edu.upc.fib.meetnrun.views.fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.*;
import com.google.android.gms.location.places.Places;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.EditProfileActivity;

import com.google.android.gms.maps.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;


public class ProfileActivityFragment extends Fragment {

    User u;
    private View view;
    String city;
    TextView userPostCodeTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        u = CurrentSession.getInstance().getCurrentUser();

        String userName = u.getUsername();
        String name = u.getFirstName() + ' ' + u.getLastName();
        String postCode = u.getPostalCode();


        TextView userNameTextView = (TextView) view.findViewById(R.id.userName);
        TextView nameTextView = (TextView) view.findViewById(R.id.completeName);
        userPostCodeTextView = (TextView) view.findViewById(R.id.userPostCode);


        userNameTextView.setText(userName);
        nameTextView.setText(name);

        getCityFromPostcode(postCode);


        Button button = view.findViewById(R.id.editProfile_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        /*Button button2 = view.findViewById(R.id.changePass_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });*/

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setImageResource(android.R.drawable.ic_menu_edit);
        fab.setVisibility(View.GONE);


        return view;
    }

    /*private void getCityFromPostcode(String p) throws IOException, JSONException {
        // build a URL
        URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + p + "&region=es&key=AIzaSyDm6Bt_p5gn3F7DAJJLMYSEOR0kyqNL800");

        // read from the URL
        Scanner scan = new Scanner(url.openStream());
        String str = new String();
        while (scan.hasNext())
            str += scan.nextLine();
        scan.close();

        // build a JSON object
        JSONObject obj = new JSONObject(str);
        if (! obj.getString("status").equals("OK"))
            return;

        // get the first result
        JSONObject res = obj.getJSONArray("results").getJSONObject(0);

        Log.e("URL", res.getString("formatted_address"));
        //System.out.println(res.getString("formatted_address"));
        //JSONObject loc = res.getJSONObject("geometry").getJSONObject("location");
        //System.out.println("lat: " + loc.getDouble("lat") + ", lng: " + loc.getDouble("lng"));
    }*/


    private void getCityFromPostcode(String p) {
        Log.e("URL", "vamos a pasar el postcode!");
        new ProfileActivityFragment.getCity().execute(p);
    }


    private class getCity extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            URL url = null;
            String result = null;

            // build a URL
            try {
                url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + params[0] + "&region=es&key=AIzaSyDm6Bt_p5gn3F7DAJJLMYSEOR0kyqNL800");
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
            Log.e("URL", "change view");
            userPostCodeTextView.setText(s);
        }
    }



}
