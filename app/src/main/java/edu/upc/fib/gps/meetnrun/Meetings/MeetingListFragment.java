package edu.upc.fib.gps.meetnrun.Meetings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.upc.fib.gps.meetnrun.Meetings.MeetingsRecyclerView.MeetingsAdapter;
import edu.upc.fib.gps.meetnrun.Meetings.MeetingsRecyclerView.RecyclerViewOnClickListener;
import edu.upc.fib.gps.meetnrun.R;
import edu.upc.fib.gps.meetnrun.models.Meeting;


public class MeetingListFragment extends Fragment {

    private MeetingsAdapter meetingsAdapter;
    private View view;

    public MeetingListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_list,container,false);
        this.view = view;
        setupRecyclerView();

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.meeting_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewMeeting();
            }
        });
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.fragment_meeting_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                meetingsAdapter.updateMeetingsList(getMeetingsList());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    private void setupRecyclerView() {
        final RecyclerView meetingsList = view.findViewById(R.id.fragment_meeting_container);
        meetingsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO get meetings from db
        List<Meeting> meetings = getMeetingsList();
        meetingsAdapter = new MeetingsAdapter(meetings, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {
                Toast.makeText(view.getContext(), "Added user to meeting!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMeetingClicked(int position) {
                Toast.makeText(view.getContext(), "Showing selected meeting info", Toast.LENGTH_SHORT).show();
                Meeting meeting = meetingsAdapter.getMeetingAtPosition(position);
                Intent meetingInfoIntent = new Intent(getActivity(),MeetingInfoActivity.class);
                meetingInfoIntent.putExtra("title",meeting.getTitle());
                meetingInfoIntent.putExtra("description",meeting.getDescription());
                meetingInfoIntent.putExtra("creatorAuthor",meeting.getCreatorAuthor());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                meetingInfoIntent.putExtra("date",simpleDateFormat.format(meeting.getDateTime()));
                simpleDateFormat = new SimpleDateFormat("h:mm a");
                meetingInfoIntent.putExtra("time",simpleDateFormat.format(meeting.getDateTime()));
                meetingInfoIntent.putExtra("level",String.valueOf(meeting.getLevel()));
                meetingInfoIntent.putExtra("latitude",String.valueOf(meeting.getLatitude()));
                meetingInfoIntent.putExtra("longitude",String.valueOf(meeting.getLongitude()));
                startActivity(meetingInfoIntent);

            }
        });
        meetingsList.setAdapter(meetingsAdapter);

    }

    private void createNewMeeting() {
        meetingsAdapter.addItem(this.getContext());
        /* TODO startactivity once createMeetingActivity is created
        Intent intent = new Intent(this,createMeetingActivity.java);
        startActivity(intent);*/
    }

    private ArrayList<Meeting> getMeetingsList() {
        //TODO get meetigns from DB
        return new ArrayList<>();
    }
}
