package edu.upc.fib.meetnrun.views;

import android.app.ProgressDialog;
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

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.services.FirebaseInstanceService;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private String username, password;
    private static final String MY_PREFS_NAME = "TokenFile";
    private ILoginAdapter loginAdapter;
    private CurrentSession cs;
    private boolean see = false;

    private static final String TAG = LoginActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Actual token token: " + refreshedToken);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);

        cs = CurrentSession.getInstance();
        loginAdapter = cs.getLoginAdapter();

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
        Intent intent = new Intent(this, DrawerActivity.class);
        finish();
        startActivity(intent);
    }

    private void saveToken() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", cs.getToken());
        editor.commit();
    }

    private void updateFirebaseToken(){
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        new FirebaseInstanceService().onTokenRefresh();
    }

    private class login extends AsyncTask<String,String,String> {

        String token = null;
        User u = null;

        @Override
        protected String doInBackground(String... logUser) {
            try {
                token = loginAdapter.login(username, password);
                //TODO Pending to catch correctly
            } catch (AutorizationException e) {
                e.printStackTrace();
            }

            if(token != null && !token.equals("")){
                cs.setToken(token);
                saveToken();
                updateFirebaseToken();
                try {
                    u = loginAdapter.getCurrentUser();
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
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setTitle(R.string.login);
            mProgressDialog.setMessage(getResources().getString(R.string.getting_current_session));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... s) {
            try {
                cs.setToken(s[0]);
                user = loginAdapter.getCurrentUser();
                if (user != null) ok = true;
            } catch (AutorizationException e) {
                e.printStackTrace();
                deleteToken();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (ok) {
                cs.setCurrentUser(user);
                mProgressDialog.dismiss();
                changeToMainActivity();
            }
            mProgressDialog.dismiss();
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
