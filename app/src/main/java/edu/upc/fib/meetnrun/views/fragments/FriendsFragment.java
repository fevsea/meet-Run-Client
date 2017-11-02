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
import edu.upc.fib.meetnrun.views.ProfileActivity;
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

        final RecyclerView friendsList = view.findViewById(R.id.fragment_friends_container);
        friendsList.setLayoutManager(new LinearLayoutManager(getActivity()));

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

                Meeting friend = friendsAdapter.getMeetingAtPosition(position);
                Intent friendProfileIntent = new Intent(getActivity(),FriendProfileFragment.class);
                friendProfileIntent.putExtra("userName",friend.getTitle());
                friendProfileIntent.putExtra("name",friend.getDescription());
                friendProfileIntent.putExtra("postCode",friend.getDescription());
                startActivity(friendProfileIntent);

            }
        });
        friendsList.setAdapter(friendsAdapter);

    }

    private void getFriendsList() {
        new getFriends().execute();
    }

    private void addNewFriend() {
        Intent intent = new Intent(getActivity(),ProfileActivity.class);
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
