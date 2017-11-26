package edu.upc.fib.meetnrun.views;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;

import edu.upc.fib.meetnrun.views.BaseDrawerActivity;
import edu.upc.fib.meetnrun.views.fragments.ChatFragment;
import edu.upc.fib.meetnrun.views.fragments.UsersListFragment;

/**
 * Created by eric on 21/11/17.
 */

public class ChatActivity extends BaseReturnActivity {

    @Override
    protected Fragment createFragment() {
        return new ChatFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
