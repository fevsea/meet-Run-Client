package edu.upc.fib.meetnrun.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.DeleteAccount;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.views.LoginActivity;

/**
 * Created by eric on 9/11/17.
 */

public class SettingsFragment extends BaseFragment {

    private View view;
    private IUserAdapter controller;
    private CurrentSession cs;
    private static final String MY_PREFS_NAME = "TokenFile";
    Spinner language;
    Locale myLocale;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_settings, container, false);

        cs = CurrentSession.getInstance();
        controller = cs.getUserAdapter();
        final Configuration config;
        config=new Configuration();
        FloatingActionButton fab =
                getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        Button text = view.findViewById(R.id.delete_account);


        language = view.findViewById(R.id.spinnerLanguage);
        setLanguanges();

        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position==0){
                    setLocale("en");
                }
                else if (position==1){
                    setLocale("es");
                }
                else if (position==2){
                    setLocale("ca");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


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
                                callDeleteAccount();
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

    private void setLanguanges(){
        String[] languages={
                "English", "Spanish (Castillian)", "Catalan"
        };
        language.setAdapter(new ArrayAdapter<CharSequence>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, languages));

    }

    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }


    private void callDeleteAccount() {
        new DeleteAccount() {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof NotFoundException) {
                    Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof ParamsException) {
                    Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseReceived() {
                Toast.makeText(getContext(), "Account has been removed successfully", Toast.LENGTH_SHORT).show();
                deleteToken();
            }
        }.execute();
    }

    public int getTitle() {
        return R.string.action_settings;
    }

}
