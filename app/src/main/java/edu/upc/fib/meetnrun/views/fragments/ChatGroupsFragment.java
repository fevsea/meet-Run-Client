package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.ChatActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.UsersAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * Created by eric on 25/11/17.
 */

public class ChatGroupsFragment extends Fragment {

    private View view;
    private FriendsAdapter friendsAdapter;
    private IChatAdapter chatDBAdapter;
    private IFriendsAdapter friendsDBAdapter;
    private FloatingActionButton fab;
    private List<Friend> l;
    private EditText groupName;
    private Button ok;
    private TextView numbFriends;
    private List<User> selectedFriends;
    private User currentUser;

    private Chat chat;
    private String name;
    private Date dateWithoutTime;
    private List<Integer> selectedFriendsID;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        this.view = inflater.inflate(R.layout.fragment_chat_groups, container, false);

        CurrentSession cs = CurrentSession.getInstance();
        chatDBAdapter = cs.getChatAdapter();
        friendsDBAdapter = cs.getFriendsAdapter();
        currentUser = cs.getCurrentUser();


        groupName = view.findViewById(R.id.groupName);
        ok = view.findViewById(R.id.btnOk);
        numbFriends = view.findViewById(R.id.numb_friends);

        fab = getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        l = new ArrayList<>();
        selectedFriends = new ArrayList<>();
        selectedFriends.add(CurrentSession.getInstance().getCurrentUser());

        setupRecyclerView();

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                name = groupName.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(getContext(), "Name group field is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    groupName.setText("");

                    Calendar rightNow = Calendar.getInstance();
                    dateWithoutTime = rightNow.getTime();

                    selectedFriendsID = new ArrayList<>();
                    for (User user : selectedFriends) {
                        selectedFriendsID.add(user.getId());
                    }

                    chat = null;
                    new createChat().execute();

                    if (chat != null) {
                        Intent i = new Intent(getContext(), ChatActivity.class);
                        CurrentSession.getInstance().setChat(chat);
                        getActivity().finish();
                        startActivity(i);
                    }

                }
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.fragment_friends_group_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateFriends();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return this.view;
    }

    private void updateFriends() {
        new getFriends().execute();
    }

    private void setupRecyclerView() {

        final RecyclerView friendsList = view.findViewById(R.id.fragment_friends_group_container);
        friendsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        friendsAdapter = new FriendsAdapter(l, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {

                User friend = friendsAdapter.getFriendAtPosition(position).getFriend();
                if (currentUser.getUsername().equals(friend.getUsername())) friend = friendsAdapter.getFriendAtPosition(position).getUser();

                if (friend.isSelected()) {
                    selectedFriends.remove(friend);
                    numbFriends.setText(String.valueOf(selectedFriends.size()-1));
                    friend.setSelected(false);
                }
                else {
                    selectedFriends.add(friend);
                    numbFriends.setText(String.valueOf(selectedFriends.size()-1));
                    friend.setSelected(true);
                }
                friendsAdapter.updateFriendsList(l);
            }
        }, getContext());
        friendsList.setAdapter(friendsAdapter);
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

    private class createChat extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... s) {
            try {
                chat = chatDBAdapter.createChat(name, selectedFriendsID, 1, null, "", 0, dateWithoutTime);
            } catch (AutorizationException e) {
                e.printStackTrace();
            } catch (ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onResume() {
        updateFriends();
        super.onResume();
    }


}
