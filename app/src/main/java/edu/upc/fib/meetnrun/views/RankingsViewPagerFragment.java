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
import edu.upc.fib.meetnrun.views.fragments.RankingsUserFragment;
import edu.upc.fib.meetnrun.views.fragments.RankingsZipFragment;
import edu.upc.fib.meetnrun.views.fragments.StatisticsProfileFragment;

/**
 * Created by Javier on 30/12/2017.
 */

public class RankingsViewPagerFragment extends AppCompatActivity {

    private PagerAdapterFragment adapterViewPager;
/*
FragmentPagerAdapter adapterViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
	}
 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ranking_users);

        Toolbar toolbar = findViewById(R.id.activity_toolbar_rankings);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        ViewPager pager = findViewById(R.id.viewpager_rankings);
        TabLayout tabs = findViewById(R.id.pager_tabs_rankings);

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

        private static final int NUM_ITEMS = 2;

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
                    return RankingsUserFragment.newInstance(0, "User");
                case 1:
                    return RankingsZipFragment.newInstance(1, "Zip");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public String getPageTitle(int position) {

            if(position == 0) return getString(R.string.rankings_users);
            else if(position == 1) return getString(R.string.rankings_zip);
            return null;
        }
    }

}


