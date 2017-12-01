package edu.upc.fib.meetnrun.views;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public class RegisterActivity extends AppCompatActivity{

    private EditText editName, editSurname, editUsername, editPc, editPassword1, editPassword2, editAnswer;
    private Spinner spinnerQuestion;
    private String name, surname, username, password1, quest, answ,pcInt;
    private IUserAdapter controller;
    private final static String[] questionsList = {"What is the first name of the person you first kissed?",
                                                                    "What was the name of your primary school?",
                                                                    "What time of the day were you born?",
                                                                    "What is your pet’s name?",
                                                                    "What is your favorite movie?"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.setTitle("Register");

        controller = CurrentSession.getInstance().getUserAdapter();

        Toolbar toolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editName = findViewById(R.id.editName);
        editSurname = findViewById(R.id.editSurname);
        editUsername = findViewById(R.id.editUsernameR);
        editPc = findViewById(R.id.editPostalCode);
        editPassword1 = findViewById(R.id.editPassword1);
        editPassword2 = findViewById(R.id.editPassword2);
        spinnerQuestion = findViewById(R.id.spinnerQuestion);
        editAnswer = findViewById(R.id.editAnswer);

        spinnerQuestion.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, questionsList));
    }

    public void register(View v) {
        name = editName.getText().toString();
        surname = editSurname.getText().toString();
        username = editUsername.getText().toString();
        pcInt = editPc.getText().toString();
        password1 = editPassword1.getText().toString();
        String password2 = editPassword2.getText().toString();
        quest = spinnerQuestion.getSelectedItem().toString();
        answ = editAnswer.getText().toString();

        if (name.equals("")) {
            Toast.makeText(getApplicationContext(), "Name field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (surname.equals("")) {
            Toast.makeText(getApplicationContext(), "Surname field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (username.equals("")) {
            Toast.makeText(getApplicationContext(), "Username field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (pcInt.equals("")) {
            Toast.makeText(getApplicationContext(), "Postal code field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (password1.equals("")) {
            Toast.makeText(getApplicationContext(), "Password field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (password2.equals("")) {
            Toast.makeText(getApplicationContext(), "Repeat password field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (pcInt.length() != 5) {
            Toast.makeText(getApplicationContext(), "Postal code field is wrong", Toast.LENGTH_SHORT).show();
        }
        else if (password1.length() < 5) {
            Toast.makeText(getApplicationContext(), "Password field is too short", Toast.LENGTH_SHORT).show();
        }
        else if (!password1.equals(password2)) {
            Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
        }
        else {
            registerUser();
        }

    }

    private void registerUser() {
        new register().execute();
    }

    private void changeToLoginActivity() {
        finish();
    }

    private class register extends AsyncTask<String,String,String> {

        User user = null;
        boolean uar = false;

        @Override
        protected String doInBackground(String... registerUser) {
            try {
                user = controller.registerUser(username, name, surname, pcInt, password1, quest, answ);
            } catch (ParamsException e) {
                e.printStackTrace();
                uar = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (uar) {
                Toast.makeText(getApplicationContext(), "User already registered", Toast.LENGTH_SHORT).show();
            }
            else if (user == null) {
                Toast.makeText(getApplicationContext(), "Register ERROR", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Register complete!", Toast.LENGTH_SHORT).show();
                changeToLoginActivity();
            }
            super.onPostExecute(s);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
