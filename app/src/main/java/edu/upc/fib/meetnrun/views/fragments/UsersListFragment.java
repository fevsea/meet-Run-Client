package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.LoginActivity;
import edu.upc.fib.meetnrun.views.UserProfileActivity;

/**
 * Created by eric on 2/11/17.
 */

public class UsersListFragment extends FriendUserListFragmentTemplate {

    private IUserAdapter usersDBAdapter;


    @Override
    protected void floatingbutton() {
        fab.setVisibility(View.GONE);
    }

    @Override
    protected void adapter() {
        usersDBAdapter = CurrentSession.getInstance().getUserAdapter();
    }

    @Override
    protected void getIntent(User friend) {
        Intent friendProfileIntent = new Intent(getActivity(),UserProfileActivity.class);
        CurrentSession.getInstance().setFriend(friend);
        startActivity(friendProfileIntent);
    }

    @Override
    protected void getMethod() {
        new getUsers().execute();
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

}
