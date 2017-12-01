package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Message;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.ChatActivity;

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
        String currentUsername = user.getUsername();
        Chat chat = ChatListFragment.getChat(currentUsername, friendUserName);
        if (chat == null) {
            Calendar rightNow = Calendar.getInstance();

            Date dateWithoutTime = rightNow.getTime();

            Message m = new Message("", currentUsername, dateWithoutTime);

            List<User> userList = new ArrayList<>();
            userList.add(user);
            userList.add(friend);

            chat = new Chat(ChatListFragment.getCount(),friendUserName, userList, 0, m);
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
                l = friendsDBAdapter.getUserFriends(0);
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
