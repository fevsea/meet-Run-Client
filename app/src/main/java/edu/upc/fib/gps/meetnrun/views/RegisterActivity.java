package edu.upc.fib.gps.meetnrun.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.upc.fib.gps.meetnrun.R;
import edu.upc.fib.gps.meetnrun.exceptions.ParamsException;
import edu.upc.fib.gps.meetnrun.persistence.UserPersistenceController;

public class RegisterActivity extends AppCompatActivity{

    EditText editName, editSurname, editUsername, editEmail, editPc, editPassword1, editPassword2;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editName = (EditText) findViewById(R.id.editName);
        editSurname = (EditText) findViewById(R.id.editSurname);
        editUsername = (EditText) findViewById(R.id.editUsername);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPc = (EditText) findViewById(R.id.editPostalCode);
        editPassword1 = (EditText) findViewById(R.id.editPassword1);
        editPassword2 = (EditText) findViewById(R.id.editPassword2);

        text = (TextView) findViewById(R.id.text);

    }

    public void register(View v) {
        String name = editName.getText().toString();
        String surname = editSurname.getText().toString();
        String username = editUsername.getText().toString();
        String email = editEmail.getText().toString();
        String pc = editPc.getText().toString();
        String password1 = editPassword1.getText().toString();
        String password2 = editPassword2.getText().toString();

        boolean arrova = false;
        for (int i = 0; i < email.length(); i++) {
            char c = email.charAt(i);
            if (c == '@') arrova = true;
        }

        boolean pcNum = false;
        for (int i = 0; i < pc.length(); i++) {
            char c = pc.charAt(i);
            if (c != '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6' && c != '7' && c != '8' && c != '9') pcNum = true;
        }

        if (name.equals("")) {
            Toast.makeText(getApplicationContext(), "Name field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (surname.equals("")) {
            Toast.makeText(getApplicationContext(), "Surname field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (username.equals("")) {
            Toast.makeText(getApplicationContext(), "Username field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (email.equals("")) {
            Toast.makeText(getApplicationContext(), "E-mail field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (pc.equals("")) {
            Toast.makeText(getApplicationContext(), "Postal code field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (password1.equals("")) {
            Toast.makeText(getApplicationContext(), "Password field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (password2.equals("")) {
            Toast.makeText(getApplicationContext(), "Repeat password field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (!arrova) {
            Toast.makeText(getApplicationContext(), "E-mail field is wrong", Toast.LENGTH_SHORT).show();
        }
        else if (!pcNum) {
            Toast.makeText(getApplicationContext(), "Postal code field is wrong", Toast.LENGTH_SHORT).show();
        }
        else if (!password1.equals(password2)) {
            Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
        }
        else {
            int pcInt = Integer.parseInt(pc);
            text.setText("correct");

            UserPersistenceController upc = new UserPersistenceController();
            try {
                upc.registerUser(username, name, surname, email, pcInt, password1);
            } catch (ParamsException e) {
                e.printStackTrace();
            }
        }

    }
}
