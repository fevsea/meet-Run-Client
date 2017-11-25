package edu.upc.fib.meetnrun.views;

import android.content.ActivityNotFoundException;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.MeetingListFragment;

public class MeetingListActivity extends BaseDrawerActivity {


    @Override
    protected Fragment createFragment() {
        return new MeetingListFragment();
    }

    @Override
    protected boolean finishOnChangeView() {
        return false;
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
