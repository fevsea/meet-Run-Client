package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.ChangePasswordActivity;
import edu.upc.fib.meetnrun.views.EditProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class ProfileActivityFragment extends Fragment {

    private User u;
    private View view;
    private TextView userPostCodeTextView;

    private String title;
    private int page;


    // newInstance constructor for creating fragment with arguments
    public static ProfileActivityFragment newInstance(int page, String title) {
        ProfileActivityFragment fragmentFirst = new ProfileActivityFragment();
        Bundle args = new Bundle();
        args.putInt("0", page);
        args.putString("Info", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("0", 0);
        title = getArguments().getString("Info");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        u = CurrentSession.getInstance().getCurrentUser();

        String userName = u.getUsername();
        String name = u.getFirstName() + ' ' + u.getLastName();
        String postCode = u.getPostalCode();


        TextView userNameTextView = view.findViewById(R.id.userName);
        TextView nameTextView = view.findViewById(R.id.completeName);
        userPostCodeTextView = view.findViewById(R.id.userPostCode);


        userNameTextView.setText(userName);
        nameTextView.setText(name);
        userPostCodeTextView.setText(postCode);
        getCityFromPostcode(postCode);


        Button button = view.findViewById(R.id.editProfile_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        Button button2 = view.findViewById(R.id.changePass_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        /*FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setImageResource(android.R.drawable.ic_menu_edit);
        fab.setVisibility(View.GONE);*/


        return view;
    }


    private void getCityFromPostcode(String p) {
        new ProfileActivityFragment.getCity().execute(p);
    }


    private class getCity extends AsyncTask<String, String, String> {

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
            userPostCodeTextView.setText(s);
        }
    }



}
