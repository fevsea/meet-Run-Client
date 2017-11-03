package edu.upc.fib.meetnrun.views;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.UsersListFragment;

/**
 * Created by eric on 2/11/17.
 */

public class UsersListActivity extends BaseReturnActivity {

    @Override
    protected Fragment createFragment() {
        return new UsersListFragment();
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
