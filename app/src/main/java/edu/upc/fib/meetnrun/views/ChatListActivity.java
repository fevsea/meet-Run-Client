package edu.upc.fib.meetnrun.views;

import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;

import edu.upc.fib.meetnrun.views.fragments.ChatFragment;
import edu.upc.fib.meetnrun.views.fragments.ChatListFragment;

/**
 * Created by eric on 21/11/17.
 */

public class ChatListActivity extends BaseDrawerActivity {

    @Override
    protected Fragment createFragment() {
        return new ChatListFragment();
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