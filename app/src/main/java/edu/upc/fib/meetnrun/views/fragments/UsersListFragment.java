package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
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
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.UserProfileActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * Created by eric on 2/11/17.
 */

public class UsersListFragment extends Fragment {

    private View view;
    private FriendsAdapter usersAdapter;
    private IUserAdapter userController;
    private IFriendsAdapter friendController;
    private List<User> l;
    private ProgressBar progress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        this.view = inflater.inflate(R.layout.fragment_users_list, container, false);
        userController = CurrentSession.getInstance().getUserAdapter();
        friendController = CurrentSession.getInstance().getFriendsAdapter();

        progress = (ProgressBar) view.findViewById(R.id.progressBarUsers);
        progress.setVisibility(View.INVISIBLE);

        l = new ArrayList<>();

        setupRecyclerView();

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.fragment_users_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUsersList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return this.view;
    }

    private void setupRecyclerView() {

        final RecyclerView friendsList = view.findViewById(R.id.fragment_users_container);
        friendsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        usersAdapter = new FriendsAdapter(l, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onMeetingClicked(int position) {

                User user = usersAdapter.getFriendAtPosition(position);
                Intent userProfileIntent = new Intent(getActivity(),UserProfileActivity.class);

                userProfileIntent.putExtra("id", String.valueOf(user.getId()));
                userProfileIntent.putExtra("userName", user.getUsername());
                String name = user.getFirstName()+" "+user.getLastName();
                userProfileIntent.putExtra("name", name);
                userProfileIntent.putExtra("postCode", user.getPostalCode());
                startActivity(userProfileIntent);

            }
        });
        friendsList.setAdapter(usersAdapter);

    }

    private void updateUsersList() {
        l.clear();
        new getUsers().execute();
    }

    private class getUsers extends AsyncTask<String,String,String> {

        List<User> friends = new ArrayList<User>();
        List<User> users = new ArrayList<User>();

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            users = userController.getAllUsers();
            try {
                friends = friendController.getUserFriends();
            } catch (AutorizationException e) {
                e.printStackTrace();
            }
            for (User user: users) {
                boolean equal = false;
                for (User friend: friends) {
                    if (user.getUsername().equals(friend.getUsername())) {
                        equal = true;
                        friends.remove(friend);
                        break;
                    }
                }
                if (user.getUsername().equals(CurrentSession.getInstance().getCurrentUser().getUsername())) {
                    equal = true;
                }
                if (!equal) {
                    l.add(user);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            usersAdapter.updateFriendsList(l);
            progress.setVisibility(View.INVISIBLE);
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

    @Override
    public void onResume() {
        updateUsersList();
        super.onResume();
    }
}
