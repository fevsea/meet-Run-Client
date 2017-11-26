package edu.upc.fib.meetnrun.views;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;

import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.fragments.SettingsFragment;

public abstract class BaseDrawerActivity extends AppCompatActivity{

    protected DrawerLayout drawerLayout;

    public static final String MY_PREFS_NAME = "TokenFile";

    protected abstract Fragment createFragment();

    protected abstract boolean finishOnChangeView();

    private CurrentSession cs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        cs = CurrentSession.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_drawerlayout);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.activity_nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            Intent i = null;
                            menuItem.setCheckable(false);
                            switch (menuItem.getItemId()) {
                                case R.id.mymeetings:
                                    i = new Intent(getApplicationContext(),MyMeetingsActivity.class);
                                    break;
                                case R.id.logout:
                                    deleteToken();
                                    i = new Intent(getApplicationContext(),LoginActivity.class);
                                    finishAffinity();
                                    break;

                                case R.id.meetings:
                                    i = new Intent(getApplicationContext(),MeetingListActivity.class);
                                    break;
                                case R.id.friends:
                                    i = new Intent(getApplicationContext(),FriendsActivity.class);
                                    break;
                                case R.id.challenges:
                                    i = new Intent(getApplicationContext(), ChallengesListActivity.class);
                                    break;
                                case R.id.settings:
                                    i = new Intent(getApplicationContext(),SettingsActivity.class);
                                    break;
                                default:
                                    break;
                            }
                            if (i != null) {
                                if (finishOnChangeView()) finish();
                                startActivity(i);
                            }
                            drawerLayout.closeDrawers();
                            return true;
                        }
                    });

            TextView nav_user = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nameProfile);
            User user = cs.getCurrentUser();
            String name = user.getFirstName()+" "+user.getLastName();
            nav_user.setText(name);

            ImageButton profileButton = navigationView.getHeaderView(0).findViewById(R.id.imageView);
            profileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                    if (intent != null) {
                        if (finishOnChangeView()) finish();
                        startActivity(intent);
                    }
                }
            });
        }

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

    private void deleteToken() {
        cs.setToken(null);
        cs.setCurrentUser(null);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", cs.getToken());
        editor.commit();
    }

}
