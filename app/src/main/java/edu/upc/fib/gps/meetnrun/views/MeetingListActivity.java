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
import edu.upc.fib.gps.meetnrun.views.fragments.MeetingListFragment;

public class MeetingListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.meeting_list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meeting_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
            case R.id.meeting_list_menu_search:
                //TODO search (query on recyclerview adapter)
                break;
        }
        return true;
    }
}
