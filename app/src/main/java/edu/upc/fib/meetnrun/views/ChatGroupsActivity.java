package edu.upc.fib.meetnrun.views;

import android.support.v4.app.Fragment;
import android.view.MenuItem;

import edu.upc.fib.meetnrun.views.fragments.ChatGroupsFragment;

/**
 * Created by eric on 25/11/17.
 */

public class ChatGroupsActivity extends BaseReturnActivity {

    @Override
    protected Fragment createFragment() {
        return new ChatGroupsFragment();
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