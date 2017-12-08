package edu.upc.fib.meetnrun.views;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import edu.upc.fib.meetnrun.views.fragments.ChatFragment;
import edu.upc.fib.meetnrun.views.fragments.ChatGroupInfoFragment;


public class ChatGroupInfoActivity extends BaseReturnActivity {

    @Override
    protected Fragment createFragment() {
        return new ChatGroupInfoFragment();
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
