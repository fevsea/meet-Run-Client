package edu.upc.fib.meetnrun.views.fragments;

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

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.UsersAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * Created by eric on 16/11/17.
 */

public abstract class FriendListFragmentTemplate extends Fragment{

    protected View view;
    protected FriendsAdapter friendsAdapter;
    protected IFriendsAdapter friendsDBAdapter;
    protected List<Friend> l;
    protected FloatingActionButton fab;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected User currentUser;

    protected boolean isLoading;
    protected boolean isLastPage;
    protected int pageNumber;
    protected ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        this.view = inflater.inflate(R.layout.fragment_friends, container, false);
        adapter();

        CurrentSession cs = CurrentSession.getInstance();
        currentUser = cs.getCurrentUser();
        friendsDBAdapter = cs.getFriendsAdapter();

        initializePagination();
        progressBar = view.findViewById(R.id.pb_loading_friends);

        setupRecyclerView();

        fab = getActivity().findViewById(R.id.activity_fab);

        floatingbutton();

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

    protected abstract void initList();

    protected abstract void floatingbutton();

    protected abstract void adapter();

    private void setupRecyclerView() {

        final RecyclerView friendsList = view.findViewById(R.id.fragment_friends_container);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        friendsList.setLayoutManager(layoutManager);

        l = new ArrayList<>();

        friendsAdapter = new FriendsAdapter(l, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {

                User friend = friendsAdapter.getFriendAtPosition(position).getFriend();
                if (currentUser.getUsername().equals(friend.getUsername())) friend = friendsAdapter.getFriendAtPosition(position).getUser();
                getIntent(friend);

            }
        }, getContext());

        friendsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        friendsList.setAdapter(friendsAdapter);

    }

    protected abstract void getIntent(User friend);

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
                ArrayList<Friend> newList = new ArrayList<>();
                for (Friend f : l) {
                    User friend = f.getFriend();
                    if (currentUser.getUsername().equals(friend.getUsername())) friend = f.getUser();
                    String userName = friend.getUsername().toLowerCase();
                    String name = (friend.getFirstName()+" "+friend.getLastName()).toLowerCase();
                    String postCode = friend.getPostalCode();
                    if (userName != null && name != null && postCode != null) {
                        if (name.contains(newText) || userName.contains(newText) || postCode.contains(newText)) newList.add(f);
                    }

                }
                friendsAdapter.updateFriendsList(newList);
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
}
