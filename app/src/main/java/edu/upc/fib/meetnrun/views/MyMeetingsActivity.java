package edu.upc.fib.meetnrun.views;

import android.support.v4.app.Fragment;

import edu.upc.fib.meetnrun.views.fragments.MyMeetingsFragment;

public class MyMeetingsActivity extends BaseDrawerActivity {
    @Override
    protected Fragment createFragment() {
        return new MyMeetingsFragment();
    }

    @Override
    protected boolean finishOnChangeView() {
        return true;
    }
}
