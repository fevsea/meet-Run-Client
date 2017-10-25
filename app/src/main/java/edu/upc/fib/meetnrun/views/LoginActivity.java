package edu.upc.fib.meetnrun.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
    }

    public void login(View v) {

        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();

        if (username.equals("")) {
            Toast.makeText(getApplicationContext(), "Username field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (password.equals("")) {
            Toast.makeText(getApplicationContext(), "Password field is empty", Toast.LENGTH_SHORT).show();
        }
        else {


            //CurrentSession.getInstance().setToken();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    public void register(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
