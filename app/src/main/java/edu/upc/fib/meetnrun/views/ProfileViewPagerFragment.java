package edu.upc.fib.meetnrun.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.GetUser;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.fragments.FriendProfileFragment;
import edu.upc.fib.meetnrun.views.fragments.PastMeetingsProfileFragment;
import edu.upc.fib.meetnrun.views.fragments.ProfileActivityFragment;
import edu.upc.fib.meetnrun.views.fragments.StatisticsProfileFragment;
import edu.upc.fib.meetnrun.views.fragments.UserProfileFragment;
import edu.upc.fib.meetnrun.views.fragments.TrophiesProfileFragment;


public class ProfileViewPagerFragment extends AppCompatActivity {

    ViewPager pager = null;
    private int userId;
    private User profileUser;
    private ProgressBar progressBar;
    private boolean currentUser;
    private boolean isFriend;

    private PagerAdapterFragment adapterViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_viewpager);

        progressBar = findViewById(R.id.pb_loading);

        User currentSessionUser = CurrentSession.getInstance().getCurrentUser();

        userId = getIntent().getExtras().getInt("userId");
        isFriend = getIntent().getExtras().getBoolean("isFriend");
        if (userId == currentSessionUser.getId()) {
            profileUser = currentSessionUser;
            currentUser = true;
            loadProfileViews();
        }
        else {
            currentUser = false;
            callGetUser(userId);
        }

    }

    private void loadProfileViews() {
        Toolbar toolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ViewPager pager = findViewById(R.id.viewpager);
        TabLayout tabs = findViewById(R.id.pager_tabs);

        adapterViewPager = new PagerAdapterFragment(getSupportFragmentManager());
        pager.setAdapter(adapterViewPager);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public class PagerAdapterFragment extends FragmentPagerAdapter {

        private static final int NUM_ITEMS = 4;

        public PagerAdapterFragment(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return getProfileFragment();
                case 1:
                    return getPastMeetingsFragment();
                case 2:
                    return getStatisticsFragment();
                case 3:
                    return getTrohpiesFragment();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public String getPageTitle(int position) {

            if(position == 0) return getString(R.string.profile_fragment_title);
            else if(position == 1) return getString(R.string.past_meetings_fragment_title);
            else if(position == 2) return getString(R.string.statistics_fragment_title);
            else if(position == 3) return getString(R.string.trophies_fragment_title);
            return null;
        }
    }

    private void callGetUser(int userID) {
        progressBar.setVisibility(View.VISIBLE);
        new GetUser(userID) {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof NotFoundException) {
                    Toast.makeText(ProfileViewPagerFragment.this, getResources().getString(R.string.not_found_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onResponseReceied(User u) {
                profileUser = u;
                progressBar.setVisibility(View.INVISIBLE);
                loadProfileViews();
            }
        }.execute();
    }

    private Fragment getProfileFragment() {
        if (currentUser) return ProfileActivityFragment.newInstance(0, "profile");
        else {
            CurrentSession.getInstance().setFriend(profileUser);
            String name = profileUser.getFirstName() + " " + profileUser.getLastName();
            if (isFriend) {
                return FriendProfileFragment.newInstance(profileUser.getId().toString(),
                        profileUser.getUsername(),name,profileUser.getPostalCode());
            }
            else {
                return UserProfileFragment.newInstance(profileUser.getId().toString(),
                        profileUser.getUsername(),name,profileUser.getPostalCode());
            }
        }
    }

    private Fragment getPastMeetingsFragment() {
        return PastMeetingsProfileFragment.newInstance(1, "meetings",userId);
    }

    private Fragment getStatisticsFragment() {
        return StatisticsProfileFragment.newInstance(2, "statistics",userId);
    }

    private Fragment getTrohpiesFragment() {
        return TrophiesProfileFragment.newInstance(3, "trophies",userId);
    }
}
