package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;

/**
 * Created by eric on 2/11/17.
 */

public class UserProfileFragment extends Fragment {

    private View view;
    private IFriendsAdapter friendsDBAdapter;
    TextView postCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_users_profile, container, false);

        friendsDBAdapter = CurrentSession.getInstance().getFriendsAdapter();

        final Bundle profileInfo = getActivity().getIntent().getExtras();

        final TextView userName = view.findViewById(R.id.userName3);
        TextView name = view.findViewById(R.id.completeName3);
        postCode = view.findViewById(R.id.userPostCode3);
        ImageView img = view.findViewById(R.id.friend_request);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = getResources().getString(R.string.friend_request_dialog_title);
                String message = getResources().getString(R.string.friend_request_dialog_message)+" "+profileInfo.getString("userName")+"?";
                String ok = getResources().getString(R.string.accept_request);
                String cancel = getResources().getString(R.string.cancel);
                showDialog(title, message, ok, cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("request friend", userName.getText().toString());
                                new addFriend().execute(profileInfo.getString("id"));
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );

            }
        });
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        userName.setText(profileInfo.getString("userName"));
        name.setText(profileInfo.getString("name"));
        postCode.setText(profileInfo.getString("postCode"));

        getCityFromPostcode(profileInfo.getString("postCode"));

        return this.view;
    }

    private void showDialog(String title, String message, String okButtonText, String negativeButtonText, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okButtonText, ok);
        if (negativeButtonText != null && cancel != null)
            builder.setNegativeButton(negativeButtonText, cancel);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class addFriend extends AsyncTask<String,String,String> {

        boolean ok = false;

        @Override
        protected String doInBackground(String... s) {
            try {
                ok = friendsDBAdapter.addFriend(Integer.parseInt(s[0]));
            } catch (AutorizationException e) {
                e.printStackTrace();
            } catch (ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (ok) {
                Toast.makeText(getContext(), "Friend request sent", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            super.onPostExecute(s);
        }
    }

    private void getCityFromPostcode(String p) {
        new getCity().execute(p);
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
            postCode.setText(s);
        }
    }
}
