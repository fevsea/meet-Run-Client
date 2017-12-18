package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.GetFriends;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.UsersListActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;


public class FriendsFragment extends FriendListFragmentTemplate {

    @Override
    protected void initList() {}

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
    protected void getMethod() {
        callGetFriends();
    }

    private void addNewFriend() {
        Intent intent = new Intent(getActivity(),UsersListActivity.class);
        startActivity(intent);
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

    private void setLoading() {
        if (!swipeRefreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
        isLoading = true;
    }

    private void updateData() {
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
    }

    private void callGetFriends() {
        setLoading();
        new GetFriends(pageNumber) {

            @Override
            public void onResponseReceived(List<Friend> friends) {
                l = friends;
                updateData();
            }
        }.execute();
    }


    @Override
    public void onResume() {
        initializePagination();
        getMethod();
        super.onResume();
    }
}
