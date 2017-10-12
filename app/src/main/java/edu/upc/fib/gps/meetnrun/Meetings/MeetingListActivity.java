package edu.upc.fib.gps.meetnrun.Meetings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.upc.fib.gps.meetnrun.R;

public class MeetingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);

        //TODO setup toolbar and drawer

        MeetingListFragment meetingListFragment =
                (MeetingListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_meeting_list);
        if (meetingListFragment == null) {
            meetingListFragment = new MeetingListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_meeting_list,meetingListFragment)
                    .commit();
        }

    }
}
