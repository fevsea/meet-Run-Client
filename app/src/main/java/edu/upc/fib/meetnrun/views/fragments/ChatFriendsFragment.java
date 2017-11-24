package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Message;
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
        User user = CurrentSession.getInstance().getCurrentUser();
        Chat chat = ChatListFragment.getChat(user.getUsername(), friendUserName);
        if (chat == null) {
            String chatName = user+" - "+friendUserName;
            Calendar rightNow = Calendar.getInstance();
            StringBuilder sb = new StringBuilder();
            String hour = null;
            String minute = null;
            String aux = String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY));
            if (aux.length() == 1) hour = "0"+aux;
            else hour = aux;
            aux = String.valueOf(rightNow.get(Calendar.MINUTE));
            if (aux.length() == 1) minute = "0"+aux;
            else minute = aux;
            sb.append(hour);
            sb.append(":");
            sb.append(minute);

            Date dateWithoutTime = rightNow.getTime();

            Message m = new Message("", user.getUsername(), sb.toString(), dateWithoutTime);

            chat = new Chat(1,chatName, user, friendUserName, m);
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
