package edu.upc.fib.meetnrun.views;

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

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.PastMeetingsProfileFragment;
import edu.upc.fib.meetnrun.views.fragments.ProfileActivityFragment;
import edu.upc.fib.meetnrun.views.fragments.StatisticsProfileFragment;
import edu.upc.fib.meetnrun.views.fragments.TrophiesProfileFragment;

import static android.support.v4.content.res.TypedArrayUtils.getString;

public class ProfileViewPagerFragment extends AppCompatActivity {

    ViewPager pager = null;

    private PagerAdapterFragment adapterViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_viewpager);

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
                    return ProfileActivityFragment.newInstance(0, "profile");
                case 1:
                    return PastMeetingsProfileFragment.newInstance(1, "meetings");
                case 2:
                    return StatisticsProfileFragment.newInstance(2, "statistics");
                case 3:
                    return TrophiesProfileFragment.newInstance(3, "trophies");
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

}
