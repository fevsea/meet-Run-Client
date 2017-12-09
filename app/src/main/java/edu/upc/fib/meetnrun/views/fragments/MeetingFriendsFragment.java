package edu.upc.fib.meetnrun.views.fragments;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;


/**
 * Created by Javier on 08/11/2017.
 */

public class MeetingFriendsFragment extends FriendListFragmentTemplate {


    private List<User> selectedFriends = new ArrayList<>();

    @Override
    protected void initList() {}

    @Override
    protected void floatingbutton() {
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void adapter() {}


    @Override
    protected void getIntent(User friend) {
    }

    @Override
    protected void getMethod() {
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

    private class JoinMeeting extends AsyncTask<ArrayList<Integer>,String,String> {
        private int meetingId;

        @Override

        protected void onPreExecute() {
            meetingId = getActivity().getIntent().getExtras().getInt("meetingId");
        }
        @Override
        protected String doInBackground(ArrayList<Integer>... ids) {
            try {
                IMeetingAdapter meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
                for (int id : ids[0]) {
                    //TODO handle exceptions
                    try {
                        meetingAdapter.joinMeeting(meetingId,id);
                    } catch (ParamsException e) {
                        e.printStackTrace();
                    }
                }
            } catch (AutorizationException e) {
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
        getMethod();
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
            ArrayList<Integer> selectedFriendsID = new ArrayList<>();

            for (User user : selectedFriends) {
                selectedFriendsID.add(user.getId());
            }
            new JoinMeeting().execute(selectedFriendsID);
        }
        return super.onOptionsItemSelected(item);
    }
}
