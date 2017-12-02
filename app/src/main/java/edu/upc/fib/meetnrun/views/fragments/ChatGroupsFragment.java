package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Message;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.ChatActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * Created by eric on 25/11/17.
 */

public class ChatGroupsFragment extends Fragment {

    private View view;
    private FriendsAdapter friendsAdapter;
    private IFriendsAdapter friendsDBAdapter;
    private FloatingActionButton fab;
    private List<User> l;
    private EditText groupName;
    private Button ok;
    private TextView numbFriends;
    private List<User> selectedFriends;
    private boolean adduser;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        this.view = inflater.inflate(R.layout.fragment_chat_groups, container, false);

        friendsDBAdapter = CurrentSession.getInstance().getFriendsAdapter();

        String action = getActivity().getIntent().getExtras().getString("action");
        if (action != null && action.equals("adduser")) adduser = true;
        else adduser = false;

        groupName = view.findViewById(R.id.groupName);
        if (adduser) {
            groupName.setFocusable(false);
            groupName.setText(CurrentSession.getInstance().getChat().getChatName());
        }
        ok = view.findViewById(R.id.btnOk);
        numbFriends = view.findViewById(R.id.numb_friends);

        fab = getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        l = new ArrayList<>();
        selectedFriends = new ArrayList<>();
        if (!adduser) selectedFriends.add(CurrentSession.getInstance().getCurrentUser());

        setupRecyclerView();

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = groupName.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(getContext(), "Name group field is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    groupName.setText("");

                    User user = selectedFriends.get(0);

                    Calendar rightNow = Calendar.getInstance();

                    Date dateWithoutTime = rightNow.getTime();

                    Message m = new Message("", user.getUsername(), dateWithoutTime);
                    Chat chat = null;
                    if (adduser) {
                        chat = CurrentSession.getInstance().getChat();
                        List<User> groupUsers = chat.getListUsersChat();
                        groupUsers.addAll(selectedFriends);
                        chat.setListUsersChat(groupUsers);
                    }
                    else {
                        chat = new Chat(ChatListFragment.getCount(), name, selectedFriends, 1, m);
                        ChatListFragment.addChatFake(chat);
                    }

                    for (User userSelected : selectedFriends) {
                        userSelected.setSelected(false);
                    }

                    if (!adduser) {
                        Intent i = new Intent(getContext(), ChatActivity.class);
                        CurrentSession.getInstance().setChat(chat);
                        startActivity(i);
                    }

                    getActivity().finish();

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

                User friend = friendsAdapter.getFriendAtPosition(position);

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
        }, getContext(), true);
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
            if (adduser) {
                Log.e("CGF","Entra");
                List<User> usersInGroup = CurrentSession.getInstance().getChat().getListUsersChat();
                Log.e("CGF",usersInGroup.toString());
                l.removeAll(usersInGroup);
                Log.e("CGF",l.toString());
            }
            friendsAdapter.updateFriendsList(l);
            super.onPostExecute(s);
        }
    }

    @Override
    public void onResume() {
        updateFriends();
        super.onResume();
    }


}
