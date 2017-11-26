package edu.upc.fib.meetnrun.views;

import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.ChatFragment;
import edu.upc.fib.meetnrun.views.fragments.ChatListFragment;

/**
 * Created by eric on 21/11/17.
 */

public class ChatListActivity extends BaseReturnActivity {

    @Override
    protected Fragment createFragment() {
        return new ChatListFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.empty_menu, menu);
        return true;
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
