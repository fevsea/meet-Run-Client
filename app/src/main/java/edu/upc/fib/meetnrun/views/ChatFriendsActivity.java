package edu.upc.fib.meetnrun.views;

import android.support.v4.app.Fragment;
import android.view.MenuItem;

import edu.upc.fib.meetnrun.views.fragments.ChatFriendsFragment;
import edu.upc.fib.meetnrun.views.fragments.UsersListFragment;

/**
 * Created by eric on 22/11/17.
 */

public class ChatFriendsActivity extends BaseReturnActivity {

    @Override
    protected Fragment createFragment() {
        return new ChatFriendsFragment();
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
