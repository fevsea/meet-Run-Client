package edu.upc.fib.meetnrun.views;

import android.support.v4.app.Fragment;
import android.view.MenuItem;

import edu.upc.fib.meetnrun.views.fragments.ChatFragment;


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
