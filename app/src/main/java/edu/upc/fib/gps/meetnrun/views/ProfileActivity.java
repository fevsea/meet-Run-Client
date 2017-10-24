package edu.upc.fib.gps.meetnrun.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import edu.upc.fib.gps.meetnrun.R;

public class ProfileActivity extends AppCompatActivity {

    TextView userNameTextView =(TextView) findViewById(R.id.userName);
    TextView userEmailTextView =(TextView) findViewById(R.id.userEmail);
    TextView userPostCodeTextView =(TextView) findViewById(R.id.userPostCode);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*String userName = getUserName()+ " " + getFirstName() + " " + getLastName();
        String email = getEmail();
        int postCode = getPostCode();*/

        String name = "Monica Follana";
        String email = "monicfm44@gmail.com";
        String postCode = "08028";

        userNameTextView.setText(name);
        userEmailTextView.setText(email);
        userPostCodeTextView.setText(postCode);


        /*final TextView helloTextView = (TextView) findViewById(R.id.text_view_id);
        helloTextView.setText(R.string.user_greeting);*/
    }

}
