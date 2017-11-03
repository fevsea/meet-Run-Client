package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.MeetingInfoActivity;
import edu.upc.fib.meetnrun.views.ProfileActivity;
import edu.upc.fib.meetnrun.views.UsersListActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;


public class FriendsFragment extends Fragment {

    private View view;
    private MeetingsAdapter friendsAdapter;
    private IGenericController controller;
    private List<Meeting> l;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_friends, container, false);
        controller = WebDBController.getInstance();

        l = new ArrayList<>();

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

                Meeting friend = friendsAdapter.getMeetingAtPosition(position);
                Intent friendProfileIntent = new Intent(getActivity(),FriendProfileActivity.class);

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
        Intent intent = new Intent(getActivity(),UsersListActivity.class);
        startActivity(intent);
    }

    private class getFriends extends AsyncTask<String,String,String> {

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Meeting> newList = new ArrayList<>();
                for (Meeting meeting : l) {
                    String name = meeting.getTitle().toLowerCase();
                    if (name.contains(newText)) newList.add(meeting);
                }
                friendsAdapter.updateMeetingsList(newList);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
