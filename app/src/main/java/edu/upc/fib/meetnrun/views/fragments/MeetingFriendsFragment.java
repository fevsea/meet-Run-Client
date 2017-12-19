package edu.upc.fib.meetnrun.views.fragments;

import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;


/**
 * Created by Javier on 08/11/2017.
 */

public class MeetingFriendsFragment extends FriendListFragmentTemplate {


    private List<User> selectedFriends = new ArrayList<>();
    private IChatAdapter chatAdapter = CurrentSession.getInstance().getChatAdapter();

    @Override
    protected void initList() {}

    @Override
    protected void floatingbutton() {
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void adapter() {}

    @Override
    protected void refreshList() {
        getPaginationMethod();
    }


    @Override
    protected void getIntent(User friend) {
    }

    @Override
    protected void getPaginationMethod() {
        new getFriends().execute();
    }


    @Override
    protected RecyclerViewOnClickListener getRecyclerViewListener() {
        return new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {
                User friend = friendsAdapter.getFriendAtPosition(position).getFriend();
                if (currentUser.getId().equals(friend.getId())) friend = friendsAdapter.getFriendAtPosition(position).getUser();

                if (friend.isSelected()) {
                    selectedFriends.remove(friend);
                    friend.setSelected(false);
                }
                else {
                    selectedFriends.add(friend);
                    friend.setSelected(true);
                }
                friendsAdapter.notifyDataSetChanged();

            }
        };
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
            } catch (AuthorizationException e) {
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

    private class JoinMeeting extends AsyncTask<ArrayList<User>,String,String> {
        private int meetingId;

        @Override

        protected void onPreExecute() {
            meetingId = getActivity().getIntent().getExtras().getInt("meetingId");
        }
        @Override
        protected String doInBackground(ArrayList<User>... users) {
            try {
                IMeetingAdapter meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
                for (User user : users[0]) {
                    //TODO handle exceptions
                    try {
                        meetingAdapter.joinMeeting(meetingId,user.getId());
                    } catch (ParamsException e) {
                        e.printStackTrace();
                    }
                }
                Chat chat = CurrentSession.getInstance().getChat();
                List<User> chatUsers = chat.getListUsersChat();
                chatUsers.addAll(users[0]);
                chat.setListUsersChat(chatUsers);
                chatAdapter.updateChat(chat);

            } catch (AuthorizationException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            getActivity().finish();
            super.onPostExecute(s);
        }
    }


    @Override
    public void onResume() {
        initializePagination();
        getPaginationMethod();
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.edit_meeting_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done_button) {
            ArrayList<User> selectedFriendsID = new ArrayList<>();

            for (User user : selectedFriends) {
                selectedFriendsID.add(user);
            }
            new JoinMeeting().execute(selectedFriendsID);
        }
        return super.onOptionsItemSelected(item);
    }
}
