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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.GetAllFriends;
import edu.upc.fib.meetnrun.asynctasks.GetFriends;
import edu.upc.fib.meetnrun.asynctasks.GetUsers;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.UsersAdapter;

/**
 * Created by eric on 2/11/17.
 */

public class UsersListFragment extends BaseFragment {

    private View view;
    private UsersAdapter usersAdapter;
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
        Intent friendProfileIntent = new Intent();
        Fragment frag;
        if (friend.isFriend()) {
            frag = new FriendProfileFragment();
        }
        else {
            frag = new UserProfileFragment();
        }
        CurrentSession.getInstance().setFriend(friend);
        BaseActivity.startWithFragment(getActivity(), frag, friendProfileIntent);
    }

    private void getMethod() {
        callGetFriends();
    }

    private void updateData(List<User> users) {
        l = users;
        l.remove(CurrentSession.getInstance().getCurrentUser());
        for (User user: l) {
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
    }

    private void callGetFriends() {
        new GetAllFriends() {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof NotFoundException) {
                    Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseReceived(List<Friend> allfriends) {
                friends = allfriends;
                callGetUsers();
            }
        }.execute();
    }

    private void callGetUsers() {
        if (!swipeRefreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
        isLoading = true;
        new GetUsers(pageNumber) {
            @Override
            public void onResponseReceived(List<User> users) {
                updateData(users);
            }
        }.execute();
    }

    public int getTitle() {
        return R.string.users_label;
    }

}
