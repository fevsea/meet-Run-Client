package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.adapters.IGenericController;
import edu.upc.fib.meetnrun.adapters.WebDBController;
import edu.upc.fib.meetnrun.views.UserProfileActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * Created by eric on 2/11/17.
 */

public class UsersListFragment extends Fragment {

    private View view;
    private FriendsAdapter usersAdapter;
    private IGenericController controller;
    private List<User> l;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

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

        List<User> users = new ArrayList<User>();
        getUsersList();

        usersAdapter = new FriendsAdapter(users, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onMeetingClicked(int position) {

                User user = usersAdapter.getFriendAtPosition(position);
                Intent userProfileIntent = new Intent(getActivity(),UserProfileActivity.class);
                userProfileIntent.putExtra("userName",user.getUsername());
                String name = user.getFirstName()+" "+user.getLastName();
                userProfileIntent.putExtra("name",name);
                userProfileIntent.putExtra("postCode",user.getPostalCode());
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
            l = controller.getAllUsers();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            usersAdapter.updateFriendsList(l);
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
                ArrayList<User> newList = new ArrayList<User>();
                for (User user : l) {
                    String userName = user.getUsername().toLowerCase();
                    String name = (user.getFirstName()+" "+user.getLastName()).toLowerCase();
                    String postCode = user.getPostalCode();
                    if (userName != null && name != null && postCode != null) {
                        if (name.contains(newText) || userName.contains(newText) || postCode.contains(newText)) newList.add(user);
                    }
                }
                usersAdapter.updateFriendsList(newList);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
