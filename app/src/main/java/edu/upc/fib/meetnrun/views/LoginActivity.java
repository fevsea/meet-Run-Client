package edu.upc.fib.meetnrun.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.persistence.IGenericController;
import edu.upc.fib.meetnrun.persistence.WebDBController;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private String username, password;
    public static final String MY_PREFS_NAME = "TokenFile";
    private IGenericController controller;
    private CurrentSession cs;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);

        cs = CurrentSession.getInstance();
        controller = cs.getController();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token",null);
        if (token != null) {
            new GetCurrentUser().execute(token);
        }
    }

    public void loginButton(View v) {

        username = editUsername.getText().toString();
        password = editPassword.getText().toString();

        if (username.equals("")) {
            Toast.makeText(getApplicationContext(), "Username field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (password.equals("")) {
            Toast.makeText(getApplicationContext(), "Password field is empty", Toast.LENGTH_SHORT).show();
        }
        else loginUser();

    }

    public void registerButton(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void loginUser() {
        new login().execute();
    }

    private void changeToMainActivity() {
        Intent intent = new Intent(this, MeetingListActivity.class);
        finish();
        startActivity(intent);
    }

    private void saveToken() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", cs.getToken());
        editor.commit();
    }

    private class login extends AsyncTask<String,String,String> {

        String token = null;
        User u = null;

        @Override
        protected String doInBackground(String... logUser) {
            try {
                token = controller.login(username, password);
                //TODO Pending to catch correctly
            } catch (AutorizationException e) {
                e.printStackTrace();
            }

            if(token != null && !token.equals("")){
                cs.setToken(token);
                saveToken();
                try {
                    u = controller.getCurrentUser();
                    //TODO Pending to catch correctly
                } catch (AutorizationException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (token == null || token.equals("")) {
                Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
            }
            else {
                cs.setCurrentUser(u);
                changeToMainActivity();
            }
            super.onPostExecute(s);
        }
    }

    private class GetCurrentUser extends AsyncTask<String,String,String> {

        User user = null;
        boolean ok = false;

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... s) {
            try {
                cs.setToken(s[0]);
                user = controller.getCurrentUser();
                if (user != null) ok = true;
            } catch (AutorizationException e) {
                e.printStackTrace();
                deleteToken();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progress.setVisibility(View.INVISIBLE);
            if (ok) {
                cs.setCurrentUser(user);
                changeToMainActivity();
            }

            super.onPostExecute(s);
        }
    }

    private void deleteToken() {
        cs.setToken(null);
        cs.setCurrentUser(null);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", CurrentSession.getInstance().getToken());
        editor.commit();
    }

}
