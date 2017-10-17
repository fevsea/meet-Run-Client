package edu.upc.fib.gps.meetnrun.Meetings;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import edu.upc.fib.gps.meetnrun.R;

public class MeetingListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);

        //TODO setup toolbar and drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_settings);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //TODO actionBar.setDisplayHomeAsUpEnabled(true);
        //TODO actionBar.setHomeAsUpIndicator(R.drawable.);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_meeting_list_drawer);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.meeting_list_nav_view);
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

        MeetingListFragment meetingListFragment =
                (MeetingListFragment) getSupportFragmentManager().findFragmentById(R.id.meeting_list_contentFrame);
        if (meetingListFragment == null) {
            meetingListFragment = new MeetingListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.meeting_list_contentFrame,meetingListFragment)
                    .commit();
        }



    }
}
