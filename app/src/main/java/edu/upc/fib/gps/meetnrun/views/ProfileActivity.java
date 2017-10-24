package edu.upc.fib.gps.meetnrun.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import edu.upc.fib.gps.meetnrun.R;
import edu.upc.fib.gps.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.gps.meetnrun.models.User;
import edu.upc.fib.gps.meetnrun.persistence.MeetingsPersistenceController;
import edu.upc.fib.gps.meetnrun.persistence.UserPersistenceController;

public class ProfileActivity extends AppCompatActivity {

    TextView userNameTextView;
    TextView userEmailTextView;
    TextView userPostCodeTextView;
    User u;
    private UserPersistenceController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.userNameTextView =   (TextView) findViewById(R.id.userName);
        this.userEmailTextView =(TextView) findViewById(R.id.userEmail);
        this.userPostCodeTextView = (TextView) findViewById(R.id.userPostCode);
        this.controller = new UserPersistenceController();

        int id = getIntent().getIntExtra("id", -1);
        try {
            u = controller.get(id);
            if(u == null) return; // TODO created to avoid exception in tests, to do u how tests for the app u can create a stub with u = new User and test
        } catch (NotFoundException e) {
            e.printStackTrace();
        }


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
