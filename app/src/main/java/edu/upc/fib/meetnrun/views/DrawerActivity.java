package edu.upc.fib.meetnrun.views;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
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
import edu.upc.fib.meetnrun.views.fragments.FriendsFragment;
import edu.upc.fib.meetnrun.views.fragments.MeetingListFragment;
import edu.upc.fib.meetnrun.views.fragments.MyMeetingsFragment;
import edu.upc.fib.meetnrun.views.fragments.SettingsFragment;

public class DrawerActivity extends AppCompatActivity{

    protected DrawerLayout drawerLayout;
    public static final String MY_PREFS_NAME = "TokenFile";
    private CurrentSession cs;

    private void replaceFragment(Fragment fragment) {
        if (!isCurrentlyOpen(fragment.getClass().getName())) {
            String backStateName = fragment.getClass().getName();

            FragmentManager fragmentManager = getSupportFragmentManager();

            boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);

            if (!fragmentPopped) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_contentFrame, fragment)
                        .addToBackStack(backStateName)
                        .commit();
            }
        }
    }


    private boolean isCurrentlyOpen(String backStateName) {
        String currentBackStateName = getSupportFragmentManager().findFragmentById(R.id.activity_contentFrame).getClass().getName();
        return backStateName.equals(currentBackStateName);
    }

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
                            Fragment fragment;
                            switch (menuItem.getItemId()) {
                                case R.id.mymeetings:
                                    fragment = new MyMeetingsFragment();
                                    replaceFragment(fragment);
                                    break;
                                case R.id.logout:
                                    deleteToken();
                                    i = new Intent(getApplicationContext(),LoginActivity.class);
                                    startActivity(i);
                                    finishAffinity();
                                    break;

                                case R.id.meetings:
                                    fragment = new MeetingListFragment();
                                    replaceFragment(fragment);
                                    break;
                                case R.id.friends:
                                    fragment = new FriendsFragment();
                                    replaceFragment(fragment);
                                    break;
                                case R.id.settings:
                                    fragment = new SettingsFragment();
                                    replaceFragment(fragment);
                                    break;
                                default:
                                    break;
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
                    Intent i = new Intent(getApplicationContext(),ProfileViewPagerFragment.class);
                    startActivity(i);
                    drawerLayout.closeDrawers();
                }
            });


            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.activity_contentFrame);
            if (currentFragment == null) {

                currentFragment = new MeetingListFragment();
                String backStateName = currentFragment.getClass().getName();

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.activity_contentFrame,currentFragment)
                        .addToBackStack(backStateName)
                        .commit();
            }
        }



    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
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
