package edu.upc.fib.meetnrun.views.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;


public class PagerAdapterFragment extends FragmentPagerAdapter {

    private static final int NUM_ITEMS = 3;

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
                    return ProfileActivityFragment.newInstance(0, "Your profile");
                case 1:
                    return PastMeetingsProfileFragment.newInstance(1, "Past Meetings");
                case 2:
                    return StatisticsProfileFragment.newInstance(2, "Statistics");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0) return "Your profile";
            else if(position == 1) return "Past Meetings";
            else if(position == 2) return "Your Statistics";
            return null;
        }
    }


