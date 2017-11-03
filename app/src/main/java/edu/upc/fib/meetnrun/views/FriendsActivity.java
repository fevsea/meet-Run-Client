package edu.upc.fib.meetnrun.views;


import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.FriendsFragment;

public class FriendsActivity extends BaseDrawerActivity {

    @Override
    protected Fragment createFragment() {
        return new FriendsFragment();
    }

    @Override
    protected boolean finishOnChangeView() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}
