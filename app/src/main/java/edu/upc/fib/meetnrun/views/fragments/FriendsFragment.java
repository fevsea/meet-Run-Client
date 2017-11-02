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

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.persistence.IGenericController;
import edu.upc.fib.meetnrun.persistence.WebDBController;
import edu.upc.fib.meetnrun.views.CreateMeetingActivity;
import edu.upc.fib.meetnrun.views.MeetingInfoActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;


public class FriendsFragment extends Fragment {

    private View view;
    private MeetingsAdapter friendsAdapter;
    private IGenericController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_friends, container, false);
        controller = WebDBController.getInstance();

        setupRecyclerView();

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setImageResource(R.drawable.add_user_512);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewFriend();
            }
        });

        return this.view;
    }

    private void setupRecyclerView() {

        final RecyclerView meetingsList = view.findViewById(R.id.fragment_friends_container);
        meetingsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Meeting> meetings = new ArrayList<>();
        getFriendsList();

        friendsAdapter = new MeetingsAdapter(meetings, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {
                Toast.makeText(view.getContext(), "Added user to meeting!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMeetingClicked(int position) {
                Toast.makeText(view.getContext(), "Showing selected meeting info", Toast.LENGTH_SHORT).show();
                Meeting meeting = friendsAdapter.getMeetingAtPosition(position);
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
        meetingsList.setAdapter(friendsAdapter);

    }

    private void getFriendsList() {
        new getFriends().execute();
    }

    private void addNewFriend() {
        Intent intent = new Intent(getActivity(),CreateMeetingActivity.class);
        startActivity(intent);
    }

    private class getFriends extends AsyncTask<String,String,String> {
        List<Meeting> l = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            l = controller.getAllMeetings();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            friendsAdapter.updateMeetingsList(l);
            super.onPostExecute(s);
        }
    }
}
