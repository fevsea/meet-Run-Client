package edu.upc.fib.meetnrun.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.persistence.MeetingsPersistenceController;
import edu.upc.fib.meetnrun.persistence.UserPersistenceController;

public class ProfileActivity extends AppCompatActivity {

    User u;
    private UserPersistenceController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


    }

}
