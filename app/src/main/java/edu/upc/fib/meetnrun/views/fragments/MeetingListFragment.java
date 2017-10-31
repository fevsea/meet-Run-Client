package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.persistence.GenericController;
import edu.upc.fib.meetnrun.views.CreateMeetingActivity;
import edu.upc.fib.meetnrun.views.EditMeetingActivity;
import edu.upc.fib.meetnrun.views.MeetingInfoActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;
import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Meeting;


public class MeetingListFragment extends Fragment {

    private MeetingsAdapter meetingsAdapter;
    private View view;
    private String listType;

    public MeetingListFragment() {

    }

    public static MeetingListFragment newMeetingListInstance() {
        MeetingListFragment meetingListFragment = new MeetingListFragment();
        meetingListFragment.setType("MeetingList");
        return meetingListFragment;
    }

    public static MeetingListFragment newMyMeetingsInstance() {
        MeetingListFragment meetingListFragment = new MeetingListFragment();
        meetingListFragment.setType("MyMeetings");
        return meetingListFragment;
    }

    public void setType(String type) {
        listType = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_list,container,false);
        this.view = view;

        setupRecyclerView();

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setImageResource(R.drawable.add_group_512);
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
                updateMeetingList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    private void setupRecyclerView() {
        final RecyclerView meetingsList = view.findViewById(R.id.fragment_meeting_container);
        meetingsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Meeting> meetings = new ArrayList<>();
        updateMeetingList();
        meetingsAdapter = new MeetingsAdapter(meetings, listType, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {
                Meeting selectedMeeting = meetingsAdapter.getMeetingAtPosition(position);
                if (listType.equals("MyMeetings")) {
                    startMeeting(selectedMeeting);
                }
                else { //MeetingList
                    joinMeeting(selectedMeeting);
                }
            }

            @Override
            public void onMeetingClicked(int position) {
                Toast.makeText(view.getContext(), "Showing selected meeting info", Toast.LENGTH_SHORT).show();
                Meeting meeting = meetingsAdapter.getMeetingAtPosition(position);
                Intent meetingInfoIntent = new Intent(getActivity(),MeetingInfoActivity.class);
                meetingInfoIntent.putExtra("title",meeting.getTitle());
                meetingInfoIntent.putExtra("description",meeting.getDescription());
                String datetime = meeting.getDate();
                meetingInfoIntent.putExtra("date",datetime.substring(0,datetime.indexOf('T')));
                meetingInfoIntent.putExtra("time",datetime.substring(datetime.indexOf('T')+1,datetime.indexOf('Z')));
                meetingInfoIntent.putExtra("level",String.valueOf(meeting.getLevel()));
                meetingInfoIntent.putExtra("latitude",meeting.getLatitude());
                meetingInfoIntent.putExtra("longitude",meeting.getLongitude());
                startActivity(meetingInfoIntent);

            }
        });
        meetingsList.setAdapter(meetingsAdapter);

    }

    private void updateMeetingList() {
        if (listType.equals("MyMeetings")) {
            new GetMyMeetings().execute();
        }
        else { //MeetingList
            new GetMeetings().execute();
        }
    }

    private void createNewMeeting() {
        Intent intent = new Intent(getActivity(),CreateMeetingActivity.class);
        startActivity(intent);
    }

    private void startMeeting(Meeting meeting) {
        //TODO Start tracking
    }

    private void joinMeeting(Meeting meeting) {
        //TODO Join meeting
    }

    private class GetMeetings extends AsyncTask<String,String,String> {
        List<Meeting> l = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            Log.e("MAIN","DOINGGGG");
            l = GenericController.getInstance().getAllMeetings();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            System.err.println("FINISHED");
            meetingsAdapter.updateMeetingsList(l);
            super.onPostExecute(s);
        }
    }

    private class GetMyMeetings extends AsyncTask<String,String,String> {
        List<Meeting> l = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
           //TODO  l = GenericController.getInstance().getMyMeetings();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            System.err.println("FINISHED");
            meetingsAdapter.updateMeetingsList(l);
            super.onPostExecute(s);
        }
    }
}
