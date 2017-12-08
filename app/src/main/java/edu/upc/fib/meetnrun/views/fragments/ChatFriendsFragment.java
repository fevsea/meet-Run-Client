package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.Message;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.ChatActivity;

/**
 * Created by eric on 22/11/17.
 */

public class ChatFriendsFragment extends FriendUserListFragmentTemplate {

    private IChatAdapter chatDBAdapter;
    private Chat chat;
    private String friendUserName;
    private List<Integer> userList;
    private Date dateWithoutTime;
    private User friend;

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
        chatDBAdapter = CurrentSession.getInstance().getChatAdapter();
    }

    @Override
    protected void getIntent(User friend) {

        this.friend = friend;
        new getChat().execute();

    }

    @Override
    protected void getMethod() {
        new getFriends().execute();
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
            } catch (AutorizationException e) {
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

    private class createChat extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... s) {
            try {
                chat = chatDBAdapter.createChat(friendUserName, userList, 0, null, "", 0, dateWithoutTime);
            } catch (AutorizationException e) {
                e.printStackTrace();
            } catch (ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (chat != null) {
                Intent i = new Intent(getContext(), ChatActivity.class);
                CurrentSession.getInstance().setChat(chat);
                getActivity().finish();
                startActivity(i);
            }
            super.onPostExecute(s);
        }
    }

    private class getChat extends AsyncTask<String,String,String> {

        User user;

        @Override
        protected void onPreExecute() {
            friendUserName = friend.getUsername();
            user = CurrentSession.getInstance().getCurrentUser();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... s) {
            try {
                chat = chatDBAdapter.getPrivateChat(friend.getId());
            } catch (AutorizationException e) {
                e.printStackTrace();
                chat = null;
            } catch (NotFoundException e) {
                e.printStackTrace();
                chat = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (chat == null) {
                Calendar rightNow = Calendar.getInstance();

                dateWithoutTime = rightNow.getTime();

                userList = new ArrayList<>();
                userList.add(user.getId());
                userList.add(friend.getId());

                new createChat().execute();
            }
            else {
                Intent i = new Intent(getContext(), ChatActivity.class);
                CurrentSession.getInstance().setChat(chat);
                getActivity().finish();
                startActivity(i);
            }
            super.onPostExecute(s);
        }
    }


}
