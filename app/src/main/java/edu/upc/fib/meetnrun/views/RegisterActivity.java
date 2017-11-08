package edu.upc.fib.meetnrun.views;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.adapters.IGenericController;
import edu.upc.fib.meetnrun.adapters.WebDBController;

public class RegisterActivity extends AppCompatActivity{

    private EditText editName, editSurname, editUsername, editPc, editPassword1, editPassword2, editAnswer;
    private Spinner spinnerQuestion;
    private String name, surname, username, password1, quest, answ,pcInt;
    private IGenericController controller;
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

        controller = WebDBController.getInstance();

        editName = (EditText) findViewById(R.id.editName);
        editSurname = (EditText) findViewById(R.id.editSurname);
        editUsername = (EditText) findViewById(R.id.editUsernameR);
        editPc = (EditText) findViewById(R.id.editPostalCode);
        editPassword1 = (EditText) findViewById(R.id.editPassword1);
        editPassword2 = (EditText) findViewById(R.id.editPassword2);
        spinnerQuestion = (Spinner) findViewById(R.id.spinnerQuestion);
        editAnswer = (EditText) findViewById(R.id.editAnswer);

        spinnerQuestion.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, questionsList));
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

        @Override
        protected String doInBackground(String... registerUser) {
            try {
                user = controller.registerUser(username, name, surname, pcInt, password1, quest, answ);
            } catch (ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (user == null) {
                Toast.makeText(getApplicationContext(), "Register ERROR", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Register complete!", Toast.LENGTH_SHORT).show();
                changeToLoginActivity();
            }
            super.onPostExecute(s);
        }

    }
}
