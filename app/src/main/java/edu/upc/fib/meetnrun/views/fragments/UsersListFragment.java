package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import edu.upc.fib.meetnrun.views.UserProfileActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * Created by eric on 2/11/17.
 */

public class UsersListFragment extends Fragment {

    private View view;
    private MeetingsAdapter usersAdapter;
    private IGenericController controller;
    private List<Meeting> l;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_users_list, container, false);
        controller = WebDBController.getInstance();

        l = new ArrayList<>();

        setupRecyclerView();

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        return this.view;
    }

    private void setupRecyclerView() {

        final RecyclerView friendsList = view.findViewById(R.id.fragment_users_container);
        friendsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Meeting> meetings = new ArrayList<>();
        getUsersList();

        usersAdapter = new MeetingsAdapter(meetings, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {

                Toast.makeText(view.getContext(), "Added user to meeting!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMeetingClicked(int position) {

                Meeting user = usersAdapter.getMeetingAtPosition(position);
                Intent userProfileIntent = new Intent(getActivity(),UserProfileActivity.class);
                userProfileIntent.putExtra("userName",user.getTitle());
                userProfileIntent.putExtra("name",user.getDescription());
                userProfileIntent.putExtra("postCode",user.getDescription());
                startActivity(userProfileIntent);

            }
        });
        friendsList.setAdapter(usersAdapter);

    }

    private void getUsersList() {
        new UsersListFragment.getUsers().execute();
    }

    private class getUsers extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            l = controller.getAllMeetings();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            usersAdapter.updateMeetingsList(l);
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
                usersAdapter.updateMeetingsList(newList);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
