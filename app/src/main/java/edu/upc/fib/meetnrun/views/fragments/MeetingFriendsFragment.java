package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;


/**
 * Created by Javier on 08/11/2017.
 */

public class MeetingFriendsFragment extends Fragment {

    private View view;
    private int level;
    private int meetingId;

    private FriendsAdapter friendsAdapter;
    private IFriendsAdapter friendsDBAdapter;
    private List<User> l;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_friends, container, false);
        this.view = view;
        friendsDBAdapter = CurrentSession.getInstance().getFriendsAdapter();

        Bundle meetingLevel= getActivity().getIntent().getExtras();
        meetingId=meetingLevel.getInt("meetingId");
        level=meetingLevel.getInt("level");

        return view;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<User> newList = new ArrayList<User>();
                for (User friend : l) {
                    String userName = friend.getUsername().toLowerCase();
                    String name = (friend.getFirstName()+" "+friend.getLastName()).toLowerCase();
                    String postCode = friend.getPostalCode();
                    if (userName != null && name != null && postCode != null) {
                        if (name.contains(newText) || userName.contains(newText) || postCode.contains(newText)) newList.add(friend);
                    }

                }
                friendsAdapter.updateFriendsList(newList);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}


