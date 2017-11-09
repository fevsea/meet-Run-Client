package edu.upc.fib.meetnrun.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.persistence.IGenericController;
import edu.upc.fib.meetnrun.views.LoginActivity;

/**
 * Created by eric on 9/11/17.
 */

public class SettingsFragment extends Fragment {

    private View view;
    private IGenericController controller;
    private CurrentSession cs;
    public static final String MY_PREFS_NAME = "TokenFile";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_settings, container, false);

        cs = CurrentSession.getInstance();
        controller = cs.getController();

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        TextView text = view.findViewById(R.id.delete_account);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = getResources().getString(R.string.delete_account_title);
                String message = getResources().getString(R.string.delete_account_message);
                String ok = getResources().getString(R.string.ok);
                String cancel = getResources().getString(R.string.cancel);
                showDialog(title, message, ok, cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new deleteAccount().execute();
                                Intent intent = new Intent(getContext(),LoginActivity.class);
                                getActivity().finishAffinity();
                                startActivity(intent);
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

        return this.view;
    }

    private void deleteToken() {
        cs.setToken(null);
        cs.setCurrentUser(null);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", cs.getToken());
        editor.commit();
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

    private class deleteAccount extends AsyncTask<String,String,String> {

        boolean ok = false;

        @Override
        protected String doInBackground(String... s) {
            try {
                controller.deleteUserByID(cs.getCurrentUser().getId().intValue());
                ok = true;
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (AutorizationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (ok) {
                Toast.makeText(getContext(), "Account has been removed successfully", Toast.LENGTH_SHORT).show();
                deleteToken();
            }
            else {
                Toast.makeText(getContext(), "Delete account ERROR", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }

}
