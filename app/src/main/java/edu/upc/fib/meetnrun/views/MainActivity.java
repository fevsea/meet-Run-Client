package edu.upc.fib.meetnrun.views;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.persistence.GenericController;
import edu.upc.fib.meetnrun.views.fragments.MeetingListFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActivityManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);

        if (CurrentSession.getInstance().getToken() == null) {
            Intent i = new Intent(this, LoginActivity.class);
            //finish();
            startActivity(i);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.meeting_list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_meeting_list_drawer);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        NavigationView navigationView = (NavigationView) findViewById(R.id.meeting_list_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);


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

    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i = null;
        Log.e("Meetings","Entrando en onNav");
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (id == R.id.crete_meeting) {
            if(!cn.getClassName().equals(CreateMeetingActivity.class.getName()))
            i = new Intent(this,CreateMeetingActivity.class);
        } else if (id == R.id.edit_meeting) {
            if(!cn.getClassName().equals(EditMeetingActivity.class.getName()))
            i = new Intent(this,EditMeetingActivity.class);
            i = i.putExtra("id", 3);
        } else if (id == R.id.user_profile) {
            if(!cn.getClassName().equals(ProfileActivity.class.getName()))
            i = new Intent(this,ProfileActivity.class);
        } else if (id == R.id.logout) {
            CurrentSession cs = CurrentSession.getInstance();
            cs.setToken(null);
            cs.setCurrentUser(null);
            i = new Intent(this,MainActivity.class);

        } else if (id == R.id.meetings) {
            if(!cn.getClassName().equals(MainActivity.class.getName()))
            i = new Intent(this,MainActivity.class);
        }
        if(i != null)
            startActivity(i);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, CreateMeetingActivity.class);
        startActivity(intent);
    }


}
