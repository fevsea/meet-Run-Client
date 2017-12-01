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
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.LoginActivity;
import edu.upc.fib.meetnrun.views.UserProfileActivity;

/**
 * Created by eric on 2/11/17.
 */

public class UsersListFragment extends FriendUserListFragmentTemplate {

    private IUserAdapter usersDBAdapter;


    @Override
    protected void initList() {
        getMethod();
    }

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
        Intent friendProfileIntent;
        if (friend.isFriend()) friendProfileIntent = new Intent(getActivity(),FriendProfileActivity.class);
        else friendProfileIntent = new Intent(getActivity(),UserProfileActivity.class);
        CurrentSession.getInstance().setFriend(friend);
        startActivity(friendProfileIntent);
    }

    @Override
    protected void getMethod() {
        new getUsers().execute();
    }


    private class getUsers extends AsyncTask<String,String,String> {

        List<User> friends = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            if (!swipeRefreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
            isLoading = true;
        }

        @Override
        protected String doInBackground(String... strings) {
            l = usersDBAdapter.getAllUsers(pageNumber);

            List<User> aux = new ArrayList<>();
            try {
                aux = friendsDBAdapter.getUserFriends(0);
            } catch (AutorizationException e) {
                e.printStackTrace();
            }

            int count = 1;
            while (aux.size() != 0) {
                friends.addAll(aux);
                try {
                    aux = friendsDBAdapter.getUserFriends(count);
                } catch (AutorizationException e) {
                    e.printStackTrace();
                }
                count++;
            }

            for (User user: l) {
                boolean equal = false;
                for (User friend: friends) {
                    if (user.getUsername().equals(friend.getUsername())) {
                        equal = true;
                        friends.remove(friend);
                        break;
                    }
                }
                if (user.getUsername().equals(CurrentSession.getInstance().getCurrentUser().getUsername())) l.remove(user);

                if (equal) user.setFriend(true);
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

}
