package edu.upc.fib.meetnrun.views;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.persistence.GenericController;
import edu.upc.fib.meetnrun.utils.FormContainers;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
    }

    public void login(View v) {

        username = editUsername.getText().toString();
        password = editPassword.getText().toString();

        if (username.equals("")) {
            Toast.makeText(getApplicationContext(), "Username field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (password.equals("")) {
            Toast.makeText(getApplicationContext(), "Password field is empty", Toast.LENGTH_SHORT).show();
        }
        else {

            loginUser();

        }

    }

    public void register(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void loginUser() {
        FormContainers.LoginUser lc = new FormContainers.LoginUser(username, password);
        new loginUs().execute(lc);
    }

    private void changeToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    private class loginUs extends AsyncTask<FormContainers.LoginUser,String,String> {

        String token = null;

        @Override
        protected String doInBackground(FormContainers.LoginUser... logUser) {
            FormContainers.LoginUser lu = logUser[0];
            token = GenericController.getInstance().login(lu.getUsername(), lu.getPassword());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (token == null || token.equals("")) {
                Toast.makeText(getApplicationContext(), "Token ERROR", Toast.LENGTH_SHORT).show();
            }
            else {
                CurrentSession.getInstance().setToken(token);
                changeToMainActivity();
            }
            super.onPostExecute(s);
        }

    }
}
