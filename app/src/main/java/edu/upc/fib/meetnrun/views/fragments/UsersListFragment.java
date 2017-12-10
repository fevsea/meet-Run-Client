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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.LoginActivity;
import edu.upc.fib.meetnrun.views.UserProfileActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.UsersAdapter;

/**
 * Created by eric on 2/11/17.
 */

public class UsersListFragment extends Fragment {

    private IUserAdapter usersDBAdapter;
    private View view;
    private UsersAdapter usersAdapter;
    private IFriendsAdapter friendsDBAdapter;
    private List<User> l;
    private List<Friend> friends;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefreshLayout;
    private User currentUser;

    private boolean isLoading;
    private boolean isLastPage;
    private int pageNumber;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        this.view = inflater.inflate(R.layout.fragment_friends, container, false);

        CurrentSession cs = CurrentSession.getInstance();
        usersDBAdapter = cs.getUserAdapter();
        friendsDBAdapter = cs.getFriendsAdapter();
        currentUser = cs.getCurrentUser();

        friends = new ArrayList<>();

        initializePagination();
        progressBar = view.findViewById(R.id.pb_loading_friends);

        setupRecyclerView();

        fab = getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        swipeRefreshLayout = view.findViewById(R.id.fragment_friends_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializePagination();
                getMethod();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setProgressViewOffset(true,200,400);

        initList();

        return this.view;
    }


    private void setupRecyclerView() {

        final RecyclerView usersList = view.findViewById(R.id.fragment_friends_container);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        usersList.setLayoutManager(layoutManager);

        l = new ArrayList<>();

        usersAdapter = new UsersAdapter(l, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {

                User user = usersAdapter.getFriendAtPosition(position);
                getIntent(user);

            }
        }, getContext());

        usersList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        getMethod();
                    }
                }
            }
        });

        usersList.setAdapter(usersAdapter);

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
                ArrayList<User> newList = new ArrayList<>();
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

    protected void initializePagination() {
        pageNumber = 0;
        isLoading = false;
        isLastPage = false;
    }


    private void initList() {
        getMethod();
    }

    private void getIntent(User friend) {
        Intent friendProfileIntent;
        if (friend.isFriend()) friendProfileIntent = new Intent(getActivity(),FriendProfileActivity.class);
        else friendProfileIntent = new Intent(getActivity(),UserProfileActivity.class);
        CurrentSession.getInstance().setFriend(friend);
        startActivity(friendProfileIntent);
    }

    private void getMethod() {
        new getFriends().execute();
    }


    private class getFriends extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            List<Friend> aux = new ArrayList<>();

            try {
                aux = friendsDBAdapter.listUserAcceptedFriends(currentUser.getId(), 0);
            } catch (AutorizationException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }

            int count = 1;
            while (aux.size() != 0) {
                friends.addAll(aux);
                try {
                    aux = friendsDBAdapter.listUserAcceptedFriends(currentUser.getId(), count);
                } catch (AutorizationException e) {
                    e.printStackTrace();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
                count++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new getUsers().execute();
            super.onPostExecute(s);
        }
    }

    private class getUsers extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            if (!swipeRefreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
            isLoading = true;
        }

        @Override
        protected String doInBackground(String... strings) {
            l = usersDBAdapter.getAllUsers(pageNumber);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            ArrayList<User> usersToRemove = new ArrayList<>();

            for (User user: l) {
                if (user.getUsername().equals(CurrentSession.getInstance().getCurrentUser().getUsername())) usersToRemove.add(user);
                boolean equal = false;
                for (Friend f: friends) {
                    User friend = f.getFriend();
                    if (currentUser.getUsername().equals(friend.getUsername())) friend = f.getUser();
                    if (user.getUsername().equals(friend.getUsername())) {
                        equal = true;
                        break;
                    }
                }
                if (equal) user.setFriend(true);
            }

            if (l != null) {
                if (pageNumber == 0) usersAdapter.updateFriendsList(l);
                else usersAdapter.addFriends(l);

                if (l.size() == 0) {
                    isLastPage = true;
                }
                else pageNumber++;
            }
            swipeRefreshLayout.setRefreshing(false);
            isLoading = false;
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(s);
        }
    }

}
