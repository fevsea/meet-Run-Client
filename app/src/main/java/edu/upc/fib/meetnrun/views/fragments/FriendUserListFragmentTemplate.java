package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.UsersListActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * Created by eric on 16/11/17.
 */

public abstract class FriendUserListFragmentTemplate extends Fragment{

    private View view;
    private FriendsAdapter friendsAdapter;
    private IFriendsAdapter friendsDBAdapter;
    private List<User> l;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        
        init();
        
        friendsDBAdapter = CurrentSession.getInstance().getFriendsAdapter();

        progress = (ProgressBar) view.findViewById(R.id.progressBarFriends);
        progress.setVisibility(View.INVISIBLE);

        l = new ArrayList<User>();

        setupRecyclerView();

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        
        floatingbutton();
        
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.fragment_friends_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMethod();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return this.view;
    }

    protected abstract void floatingbutton();

    protected abstract void init();

    private void setupRecyclerView() {

        final RecyclerView friendsList = view.findViewById(R.id.fragment_friends_container);
        friendsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<User> users = new ArrayList<User>();

        friendsAdapter = new FriendsAdapter(users, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onMeetingClicked(int position) {

                User friend = friendsAdapter.getFriendAtPosition(position);
                Intent friendProfileIntent = selectIntent();

                friendProfileIntent.putExtra("id",String.valueOf(friend.getId()));
                friendProfileIntent.putExtra("userName",friend.getUsername());
                String name = friend.getFirstName()+" "+friend.getLastName();
                friendProfileIntent.putExtra("name",name);
                friendProfileIntent.putExtra("postCode",friend.getPostalCode());
                startActivity(friendProfileIntent);

            }
        });
        friendsList.setAdapter(friendsAdapter);

    }

    protected abstract Intent selectIntent();

    protected abstract void getMethod();

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
                for (User friend : l) {
                    String userName = friend.getUsername().toLowerCase();
                    String name = (friend.getFirstName()+" "+friend.getLastName()).toLowerCase();
                    String postCode = friend.getPostalCode();
                    if (userName != null && name != null && postCode != null) {
                        if (name.contains(newText) || userName.contains(newText) || postCode.contains(newText)) newList.add(friend);
                    }

                }
                friendsAdapter.updateFriendsList(newList);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        getMethod();
        super.onResume();
    }
}
