package edu.upc.fib.meetnrun.views;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.ChallengesListFragment;

public class ChallengesListActivity extends BaseDrawerActivity {

    @Override
    protected Fragment createFragment() {
        return new ChallengesListFragment();
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
