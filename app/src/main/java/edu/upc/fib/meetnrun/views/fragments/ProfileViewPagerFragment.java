package edu.upc.fib.meetnrun.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.BaseReturnActivity;

public class ProfileViewPagerFragment extends FragmentActivity {

    ViewPager pager = null;

    PagerAdapterFragment adapterViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_viewpager);
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new PagerAdapterFragment(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
    }

}
