package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
        l.clear();
        new getUsers().execute();
    }


    private class getUsers extends AsyncTask<String,String,String> {

        List<User> friends = new ArrayList<User>();
        List<User> users = new ArrayList<User>();

        @Override
        protected String doInBackground(String... strings) {
            users = usersDBAdapter.getAllUsers();
            try {
                friends = friendsDBAdapter.getUserFriends();
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
            friendsAdapter.updateFriendsList(l);
            super.onPostExecute(s);
        }
    }

}
