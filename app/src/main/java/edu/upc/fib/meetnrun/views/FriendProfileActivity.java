package edu.upc.fib.meetnrun.views;

import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.FriendProfileFragment;

public class FriendProfileActivity extends BaseReturnActivity {

    @Override
    protected Fragment createFragment() {
        return new FriendProfileFragment();
    }

}
