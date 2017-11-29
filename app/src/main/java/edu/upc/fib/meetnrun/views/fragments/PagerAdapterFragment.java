package edu.upc.fib.meetnrun.views.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;

import static edu.upc.fib.meetnrun.R.*;


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
                    return ProfileActivityFragment.newInstance(0, "profile");
                case 1:
                    return PastMeetingsProfileFragment.newInstance(1, "meetings");
                case 2:
                    return StatisticsProfileFragment.newInstance(2, "statistics");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0) return "Your Profile";//string.profile_fragment_title;
            else if(position == 1) return "Your Past Meetings";//string.past_meetings_fragment_title;
            else if(position == 2) return "Your Statistics";//string.statistics_fragment_title;
            return null;
        }
    }


