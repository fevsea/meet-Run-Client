package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.UserProfileActivity;
import edu.upc.fib.meetnrun.views.UsersListActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.PendingFriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.TwoButtonsRecyclerViewOnClickListener;


public class FriendsFragment extends FriendListFragmentTemplate {

    protected List<Friend> pendingFriends;

    protected int pendingPageNumber;
    protected boolean pendingIsLoading;
    protected boolean pendingIsLastPage;

    private RecyclerView pendingList;
    private PendingFriendsAdapter pendingFriendsAdapter;

    @Override
    protected void initList() {
        pendingFriends = new ArrayList<>();
    }

    @Override
    protected void floatingbutton() {
        fab.setImageResource(R.drawable.add_user_512);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewFriend();
            }
        });
    }

    @Override
    protected void adapter() {}

    @Override
    protected void getIntent(User friend) {
        Intent friendProfileIntent = new Intent(getActivity(),FriendProfileActivity.class);
        CurrentSession.getInstance().setFriend(friend);
        startActivity(friendProfileIntent);
    }

    @Override
    protected void getPaginationMethod() {
        new getFriends().execute();
    }

    protected void refreshList() {
        getPaginationMethod();
        new getPendingFriends().execute();
    }

    private void addNewFriend() {
        Intent intent = new Intent(getActivity(),UsersListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void setupRecyclerView() {
        super.setupRecyclerView();

        pendingList = view.findViewById(R.id.fragment_friends_request_container);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        pendingList.setLayoutManager(linearLayoutManager);

        pendingFriends = new ArrayList<>();

        pendingFriendsAdapter = new PendingFriendsAdapter(pendingFriends, getTwoButtonsRecyclerViewListener(), getContext());

        pendingList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!pendingIsLoading && !pendingIsLastPage &&
                        (visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                         firstVisibleItemPosition >= 0) {
                    new getPendingFriends().execute();
                }

            }
        });

        pendingList.setAdapter(pendingFriendsAdapter);
    }

    @Override
    protected RecyclerViewOnClickListener getRecyclerViewListener() {
        return new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {

                User friend = friendsAdapter.getFriendAtPosition(position).getFriend();
                if (currentUser.getUsername().equals(friend.getUsername())) friend = friendsAdapter.getFriendAtPosition(position).getUser();
                getIntent(friend);

            }
        };
    }

    protected TwoButtonsRecyclerViewOnClickListener getTwoButtonsRecyclerViewListener() {
        return new TwoButtonsRecyclerViewOnClickListener() {

            @Override
            public void onButtonAcceptClicked(int position) {
                Friend friend = pendingFriendsAdapter.getFriendAtPosition(position);
                friend.setAccepted(true);
                Log.d("PreAcceptFriend", friend.getUser().getUsername());
                new AcceptRejectFriend().execute(friend);
            }

            @Override
            public void onButtonRejectClicked(int position) {
                Friend friend = pendingFriendsAdapter.getFriendAtPosition(position);
                friend.setAccepted(false);
                Log.d("PreRejectFriend", friend.getUser().getUsername());
                new AcceptRejectFriend().execute(friend);
            }

            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {
                Friend friendship = pendingFriendsAdapter.getFriendAtPosition(position);
                User friend = friendship.getFriend();
                if (currentUser.getUsername().equals(friend.getUsername())) friend = friendship.getUser();
                Intent friendProfileIntent = new Intent(getActivity(),FriendProfileActivity.class);
                CurrentSession.getInstance().setFriend(friend);
                friendProfileIntent.putExtra("accepted", friendship.isAccepted());
                startActivity(friendProfileIntent);
            }
        };
    }

    private class getFriends extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            if (!swipeRefreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
            isLoading = true;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                l = friendsDBAdapter.listUserAcceptedFriends(currentUser.getId(), pageNumber);
            } catch (AuthorizationException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (l != null) {
                if (pageNumber == 0) friendsAdapter.updateFriendsList(l);
                else friendsAdapter.addFriends(l);

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

    private class getPendingFriends extends AsyncTask<String, String, List<Friend>> {

        @Override
        protected void onPreExecute() {
            if (!swipeRefreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
            pendingIsLoading = true;
        }

        @Override
        protected List<Friend> doInBackground(String... params) {
            try {
                return friendsDBAdapter.listUserPendingFriends(currentUser.getId(), pendingPageNumber);
            }
            catch (AuthorizationException | NotFoundException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Friend> l) {
            if (l != null) {
                pendingFriends = new ArrayList<>();
                for (int i = 0; i < l.size(); ++i) {
                    if (!l.get(i).getUser().getId().equals(currentUser.getId())) {
                        pendingFriends.add(l.get(i));
                    }
                }
                if (pendingPageNumber == 0) {
                    pendingFriendsAdapter.updateFriendsList(pendingFriends);
                }
                else {
                    pendingFriendsAdapter.addFriends(pendingFriends);
                }
                if (pendingFriends.size() == 0) {
                    pendingIsLastPage = true;
                }
                else {
                    pageNumber++;
                }
                swipeRefreshLayout.setRefreshing(false);
                pendingIsLoading = false;
                progressBar.setVisibility(View.INVISIBLE);
                super.onPostExecute(l);
            }
        }
    }

    private class AcceptRejectFriend extends AsyncTask<Friend, String, Boolean> {

        @Override
        protected Boolean doInBackground(Friend... params) {
            Friend friend = params[0];
            try {
                if (friend.isAccepted()) {
                    Log.d("AcceptFriend", friend.getUser().getUsername());
                    return friendsDBAdapter.addFriend(friend.getUser().getId());
                }
                else {
                    Log.d("RejectFriend", friend.getUser().getUsername());
                    return friendsDBAdapter.removeFriend(friend.getUser().getId());
                }
            }
            catch (AuthorizationException | NotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean s) {
            initializePagination();
            refreshList();
        }


    }

    @Override
    protected void initializePagination() {

        super.initializePagination();
        pendingPageNumber = 0;
        pendingIsLastPage = false;
        pendingIsLoading = false;

    }

    @Override
    public void onResume() {
        initializePagination();
        refreshList();
        super.onResume();
    }
}
