package edu.upc.fib.meetnrun.views;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.ResetFirebaseToken;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.fragments.ChallengesListFragment;
import edu.upc.fib.meetnrun.views.fragments.ChatListFragment;
import edu.upc.fib.meetnrun.views.fragments.FeedFragment;
import edu.upc.fib.meetnrun.views.fragments.FriendsFragment;
import edu.upc.fib.meetnrun.views.fragments.MeetingListFragment;
import edu.upc.fib.meetnrun.views.fragments.MyMeetingsFragment;
import edu.upc.fib.meetnrun.views.fragments.SettingsFragment;

public class DrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private static final String MY_PREFS_NAME = "TokenFile";
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

        Toolbar toolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.activity_drawerlayout);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = findViewById(R.id.activity_nav_view);
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
                                    setTitle(R.string.mymeetings_label);
                                    replaceFragment(fragment);
                                    break;
                                case R.id.logout:
                                    resetFirebaseToken();
                                    //se ha movido el contenido de logout al recibir respuesta del server
                                    break;
                                case R.id.feed:
                                    fragment = new FeedFragment();
                                    setTitle(R.string.feed);
                                    replaceFragment(fragment);
                                    break;

                                case R.id.meetings:
                                    fragment = new MeetingListFragment();
                                    setTitle(R.string.meeting_list_label);
                                    replaceFragment(fragment);
                                    break;
                                case R.id.friends:
                                    fragment = new FriendsFragment();
                                    setTitle(R.string.friends_label);
                                    replaceFragment(fragment);
                                    break;
                                case R.id.challenges:
                                    fragment = new ChallengesListFragment();
                                    setTitle(R.string.challenges);
                                    replaceFragment(fragment);
                                    break;
                                case R.id.chat:
                                    fragment = new ChatListFragment();
                                    setTitle(R.string.chat_label);
                                    replaceFragment(fragment);
                                    break;
                                case R.id.rankings:
                                    i = new Intent(getApplicationContext(), RankingsViewPagerFragment.class);
                                    setTitle(R.string.rankings);
                                    startActivity(i);
                                    break;
                                case R.id.settings:
                                    fragment = new SettingsFragment();
                                    setTitle(R.string.settings);
                                    replaceFragment(fragment);
                                    break;
                                default:
                                    break;
                            }
                            drawerLayout.closeDrawers();
                            return true;
                        }
                    });

            TextView nav_user = navigationView.getHeaderView(0).findViewById(R.id.nameProfile);
            User user = cs.getCurrentUser();
            String name = user.getFirstName() + " " + user.getLastName();
            nav_user.setText(name);

            TextView profileButton = navigationView.getHeaderView(0).findViewById(R.id.imageView);
            char letter = user.getUsername().charAt(0);
            String firstLetter = String.valueOf(letter);
            profileButton.setBackground(getColoredCircularShape((letter)));
            profileButton.setText(firstLetter);
            profileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(),ProfileViewPagerFragment.class);
                    i.putExtra("userId",cs.getCurrentUser().getId());
                    i.putExtra("isFriend",false);
                    startActivity(i);
                    drawerLayout.closeDrawers();
                }
            });


            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.activity_contentFrame);
            if (currentFragment == null) {

                setTitle(R.string.meeting_list_label);
                currentFragment = new MeetingListFragment();
                String backStateName = currentFragment.getClass().getName();

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.activity_contentFrame, currentFragment)
                        .addToBackStack(backStateName)
                        .commit();
                navigationView.getMenu().getItem(0).setChecked(true);
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }

    private GradientDrawable getColoredCircularShape(char letter) {
        int[] colors = getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.user_profile_circular_text_view);
        int position = letter % colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }

    private void deleteToken() {
        cs.setToken(null);
        cs.setCurrentUser(null);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", cs.getToken());
        editor.commit();
    }

    private void resetFirebaseToken() {
        new ResetFirebaseToken() {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getApplicationContext(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseReceived() {
                deleteToken();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finishAffinity();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
