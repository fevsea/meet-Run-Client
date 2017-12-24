package edu.upc.fib.meetnrun.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.BaseFragment;


public class BaseActivity extends AppCompatActivity {

    private BaseFragment fragment;

    public static void startWithFragment(Context context, Fragment fragment, Intent intentExtras) {
        Intent i = new Intent(context, BaseActivity.class);
        i.putExtra("fragment", fragment.getClass());
        i.putExtras(intentExtras);
        Log.e("BaseActivity", "Starting.. " + fragment.getClass().getName());
        context.startActivity(i);
    }

    public static void startWithFragment(Context context, Fragment fragment) {
        Intent i = new Intent(context, BaseActivity.class);
        i.putExtra("fragment", fragment.getClass());
        Log.e("BaseActivity", "Starting.. " + fragment.getClass().getName());
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        Toolbar toolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        try {
            fragment = (BaseFragment) ((Class) (getIntent().getSerializableExtra("fragment"))).newInstance();
            setTitle(fragment.getTitle());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.activity_contentFrame, fragment).commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        fragment.onBackPressed();
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.empty_menu, menu);
        return true;
    }

}
