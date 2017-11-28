package edu.upc.fib.meetnrun.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import edu.upc.fib.meetnrun.R;


public abstract class BaseReturnActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        Toolbar toolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Fragment activityFragment =
                getSupportFragmentManager().findFragmentById(R.id.activity_contentFrame);
        if (activityFragment == null) {
            activityFragment = createFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_contentFrame,activityFragment)
                    .commit();
        }

    }

}
