package edu.upc.fib.gps.meetnrun.views;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.upc.fib.gps.meetnrun.R;
import edu.upc.fib.gps.meetnrun.views.fragments.MeetingInfoFragment;

public class MeetingInfoActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.meeting_info_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_meeting_info_drawer);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.meeting_info_nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                //TODO cases on drawerlayout
                                default:
                                    break;
                            }
                            menuItem.setChecked(true);
                            drawerLayout.closeDrawers();
                            return true;
                        }
                    });
        }

        MeetingInfoFragment meetingInfoFragment =
                (MeetingInfoFragment) getSupportFragmentManager().findFragmentById(R.id.meeting_info_contentFrame);
        if (meetingInfoFragment == null) {
            meetingInfoFragment = new MeetingInfoFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.meeting_info_contentFrame,meetingInfoFragment)
                    .commit();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }
}

