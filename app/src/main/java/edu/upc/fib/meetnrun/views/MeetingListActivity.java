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
        String type = getIntent().getStringExtra("type");
        if (type.equals("MyMeetings"))
            return true;
        else if (type.equals("MeetingList")) return false;
        else throw new ActivityNotFoundException("LIST TYPE PARAMETER NEEDED");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meeting_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
            case R.id.meeting_list_menu_search:
                //TODO search (query on recyclerview adapter)
                break;
        }
        return true;
    }

}
