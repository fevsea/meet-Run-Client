package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.UsersListActivity;


public class FriendsFragment extends FriendUserListFragmentTemplate {

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
        new getFriends().execute();
    }

    private void addNewFriend() {
        Intent intent = new Intent(getActivity(),UsersListActivity.class);
        startActivity(intent);
    }

    private class getFriends extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                l = friendsDBAdapter.getUserFriends(0);//TODO arreglar paginas
            } catch (AutorizationException e) {
                e.printStackTrace();
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
