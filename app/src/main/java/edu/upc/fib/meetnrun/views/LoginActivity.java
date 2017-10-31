package edu.upc.fib.meetnrun.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.persistence.GenericController;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private String username, password;
    public static final String MY_PREFS_NAME = "TokenFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        CurrentSession cs = CurrentSession.getInstance();
        cs.setToken(token);
        if (cs.getToken() != null) {
            changeToMainActivity();
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
        editor.putString("token", CurrentSession.getInstance().getToken());
        editor.commit();
    }

    private class login extends AsyncTask<String,String,String> {

        String token = null;
        GenericController gc = GenericController.getInstance();
        CurrentSession cs = CurrentSession.getInstance();
        User u = null;

        @Override
        protected String doInBackground(String... logUser) {
            token = gc.login(username, password);

            if(token != null && !token.equals("")){
                cs.setToken(token);
                saveToken();
                u = gc.getCurrentUser();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (token == null || token.equals("")) {
                Toast.makeText(getApplicationContext(), "Login ERROR", Toast.LENGTH_SHORT).show();
            }
            else {
                cs.setCurrentUser(u);
                changeToMainActivity();
            }
            super.onPostExecute(s);
        }

    }

}
