package edu.upc.fib.gps.meetnrun.Meetings;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.gps.meetnrun.Meetings.MeetingsRecyclerView.MeetingsAdapter;
import edu.upc.fib.gps.meetnrun.R;


public class MeetingListFragment extends Fragment {

    private MeetingsAdapter meetingsAdapter;

    public MeetingListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_list,container,false);
        RecyclerView meetingsList = view.findViewById(R.id.fragment_meeting_container);
        meetingsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO get meetings from db
        List<Meeting> meetings = getMeetingsList();
        meetingsAdapter = new MeetingsAdapter(meetings);
        meetingsList.setAdapter(meetingsAdapter);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.meeting_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewMeeting();
            }
        });
        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.fragment_meeting_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                meetingsAdapter.updateMeetingsList(getMeetingsList());
            }
        });
    }

    private void createNewMeeting() {
        //TODO intent to the create meeting activity
    }

    private ArrayList<Meeting> getMeetingsList() {
        //TODO get meetigns from DB
    }
}
