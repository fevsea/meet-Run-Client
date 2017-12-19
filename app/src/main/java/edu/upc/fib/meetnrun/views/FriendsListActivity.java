package edu.upc.fib.meetnrun.views;

import android.support.v4.app.Fragment;

import edu.upc.fib.meetnrun.views.fragments.FriendsFragment;

public class FriendsListActivity extends BaseReturnActivity {
    @Override
    protected Fragment createFragment() {
        return new FriendsFragment();
    }
}
