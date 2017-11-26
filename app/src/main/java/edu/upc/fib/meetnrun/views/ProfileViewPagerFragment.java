package edu.upc.fib.meetnrun.views;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import edu.upc.fib.meetnrun.views.BaseReturnActivity;
import edu.upc.fib.meetnrun.views.fragments.PagerAdapterFragment;

public class ProfileViewPagerFragment extends AppCompatActivity {

    ViewPager pager = null;

    PagerAdapterFragment adapterViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_viewpager);

        Toolbar toolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ViewPager pager = findViewById(R.id.viewpager);
        TabLayout tabs = (TabLayout) findViewById(R.id.pager_tabs);

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

}
