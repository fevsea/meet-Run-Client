package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.ChatActivity;
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.UsersListActivity;

/**
 * Created by eric on 22/11/17.
 */

public class ChatFriendsFragment extends FriendUserListFragmentTemplate {

    @Override
    protected void floatingbutton() {
        fab.setVisibility(View.GONE);
    }

    @Override
    protected void adapter() {}

    @Override
    protected void getIntent(User friend) {

        String friendUserName = friend.getUsername();
        String user = CurrentSession.getInstance().getCurrentUser().getUsername();
        Chat chat = ChatListFragment.getChat(user, friendUserName);
        if (chat == null) {
            String chatName = user+" - "+friendUserName;
            Calendar rightNow = Calendar.getInstance();
            StringBuilder sb = new StringBuilder();
            String hour = String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY));
            String minute = String.valueOf(rightNow.get(Calendar.MINUTE));
            sb.append(hour);
            sb.append(":");
            sb.append(minute);
            chat = new Chat(1,chatName, user, friendUserName, "", sb.toString());
            ChatListFragment.addChatFake(chat);
        }
        Intent i = new Intent(getContext(), ChatActivity.class);
        CurrentSession.getInstance().setChat(chat);
        getActivity().finish();
        startActivity(i);
    }

    @Override
    protected void getMethod() {
        new getFriends().execute();
    }

    private class getFriends extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                l = friendsDBAdapter.getUserFriends();
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
