package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
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
        l.clear();
        new getUsers().execute();
    }


    private class getUsers extends AsyncTask<String,String,String> {

        List<User> friends = new ArrayList<User>();
        List<User> users = new ArrayList<User>();

        @Override
        protected String doInBackground(String... strings) {
            users = usersDBAdapter.getAllUsers(0);//TODO arreglar paginas
            try {
                friends = friendsDBAdapter.getUserFriends(0);//TODO arreglar paginas
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
