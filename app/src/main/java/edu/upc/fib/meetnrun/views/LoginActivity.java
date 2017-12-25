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
import edu.upc.fib.meetnrun.asynctasks.GetCurrentUser;
import edu.upc.fib.meetnrun.asynctasks.Login;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
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
    private ProgressBar progressBar;

    private static final String TAG = LoginActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Actual token token: " + refreshedToken);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        progressBar = findViewById(R.id.pb_loading);

        cs = CurrentSession.getInstance();
        loginAdapter = cs.getLoginAdapter();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token",null);
        if (token != null) {
            callGetCurrentUser(token);
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
        callLogin();
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

    private void callLogin() {
        Login login = new Login(username,password) {
            @Override
            public void onResponseReceived(String token) {
                if(token != null && !token.equals("")){
                    cs.setToken(token);
                    saveToken();
                    updateFirebaseToken();
                    callGetCurrentUser(token);
                }
            }
        };
        try {
            login.execute();
        }
        catch (AuthorizationException e) {
            Toast.makeText(this, R.string.authorization_error, Toast.LENGTH_LONG).show();
        }
    }


    private void callGetCurrentUser(String token) {
        progressBar.setVisibility(View.VISIBLE);
        GetCurrentUser getCurrentUser = new GetCurrentUser() {
            @Override
            public void onResponseReceied(User u) {
                cs.setCurrentUser(u);
                progressBar.setVisibility(View.INVISIBLE);
                changeToMainActivity();
            }
        };
        try {
            getCurrentUser.execute(token);
        }
        catch (AuthorizationException e) {
            Toast.makeText(this, R.string.authorization_error, Toast.LENGTH_LONG).show();
            deleteToken();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void deleteToken() {
        CurrentSession.getInstance().setToken(null);
        CurrentSession.getInstance().setCurrentUser(null);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", CurrentSession.getInstance().getToken());
        editor.commit();
    }

}
