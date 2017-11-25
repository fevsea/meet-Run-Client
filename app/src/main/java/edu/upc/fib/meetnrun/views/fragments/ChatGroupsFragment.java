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
import android.widget.ImageView;
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
    protected FriendsAdapter friendsAdapter;
    protected IFriendsAdapter friendsDBAdapter;
    private FloatingActionButton fab;
    private List<User> l;
    private EditText groupName;
    private Button ok;
    private TextView numbFriends;
    private List<User> selectedFriends;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        this.view = inflater.inflate(R.layout.fragment_chat_groups, container, false);

        friendsDBAdapter = CurrentSession.getInstance().getFriendsAdapter();

        groupName = (EditText) view.findViewById(R.id.groupName);
        ok = (Button) view.findViewById(R.id.btnOk);
        numbFriends = (TextView) view.findViewById(R.id.numb_friends);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        l = new ArrayList<>();
        selectedFriends = new ArrayList<>();

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
                    User friendUserName = null;
                    if (selectedFriends.size() > 0) {
                        friendUserName = selectedFriends.get(0);
                    }
                    User user = CurrentSession.getInstance().getCurrentUser();

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

                    Chat chat = new Chat(1,name, user, friendUserName.getUsername(), m);
                    ChatListFragment.addChatFake(chat);
                    
                    Intent i = new Intent(getContext(), ChatActivity.class);
                    CurrentSession.getInstance().setChat(chat);
                    getActivity().finish();
                    startActivity(i);

                }
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_friends_group_swipe);
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
            public void onMeetingClicked(int position) {

                User friend = friendsAdapter.getFriendAtPosition(position);
                selectedFriends.add(friend);
                numbFriends.setText(String.valueOf(selectedFriends.size()));
                //TODO cambiar color friend selected + tick

            }
        });
        friendsList.setAdapter(friendsAdapter);
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
    public void onResume() {
        updateFriends();
        super.onResume();
    }
}
