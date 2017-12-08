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
import android.widget.ProgressBar;
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
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
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
    private boolean adduser;
    private User currentUser;

    private Chat chat;
    private String name;
    private Date dateWithoutTime;
    private List<Integer> selectedFriendsID;

    private boolean isLoading;
    private boolean isLastPage;
    private int pageNumber;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        this.view = inflater.inflate(R.layout.fragment_chat_groups, container, false);

        CurrentSession cs = CurrentSession.getInstance();
        chatDBAdapter = cs.getChatAdapter();
        friendsDBAdapter = cs.getFriendsAdapter();
        currentUser = cs.getCurrentUser();

        progressBar = view.findViewById(R.id.pb_loading_group_chat);
        initializePagination();

        String action = getActivity().getIntent().getExtras().getString("action");
        if (action != null && action.equals("adduser")) adduser = true;
        else adduser = false;

        groupName = view.findViewById(R.id.groupName);
        if (adduser) {
            getActivity().setTitle("Add Friend");
            Log.e("CGF","ADDUSER");
            groupName.setFocusable(false);
            groupName.setText(CurrentSession.getInstance().getChat().getChatName());
        }
        ok = view.findViewById(R.id.btnOk);
        numbFriends = view.findViewById(R.id.numb_friends);

        fab = getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        selectedFriends = new ArrayList<>();
        if (!adduser) selectedFriends.add(CurrentSession.getInstance().getCurrentUser());

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

                    for (User u : selectedFriends) {
                        u.setSelected(false);
                    }

                    if (adduser) {
                        chat = CurrentSession.getInstance().getChat();
                        List<User> groupUsers = chat.getListUsersChat();
                        groupUsers.addAll(selectedFriends);
                        chat.setListUsersChat(groupUsers);
                        new UpdateChat().execute(chat);
                        getActivity().finish();
                    }
                    else {
                        Calendar rightNow = Calendar.getInstance();
                        dateWithoutTime = rightNow.getTime();

                        selectedFriendsID = new ArrayList<>();

                        for (User user : selectedFriends) {
                            selectedFriendsID.add(user.getId());
                        }
                        new createChat().execute();
                    }
                }
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.fragment_friends_group_swipe);
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
        layoutManager = new LinearLayoutManager(getActivity());
        friendsList.setLayoutManager(layoutManager);

        l = new ArrayList<>();

        friendsAdapter = new FriendsAdapter(l, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {

                User friend = friendsAdapter.getFriendAtPosition(position).getFriend();
                if (currentUser.getUsername().equals(friend.getUsername())) friend = friendsAdapter.getFriendAtPosition(position).getUser();

                if (friend.isSelected()) {
                    selectedFriends.remove(friend);
                    friend.setSelected(false);
                }
                else {
                    selectedFriends.add(friend);
                    friend.setSelected(true);
                }
                int size = selectedFriends.size();
                if (!adduser) --size;
                numbFriends.setText(String.valueOf(size));
                friendsAdapter.notifyDataSetChanged();

            }
        }, getContext());

        friendsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        updateFriends();
                    }
                }
            }
        });

        friendsList.setAdapter(friendsAdapter);
    }

    private void setLoading() {
        if (!swipeRefreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
        isLoading = true;
    }

    private void updateData() {
        if (l != null) {
            int size = l.size();
            if (adduser) {
                List<User> usersInGroup = CurrentSession.getInstance().getChat().getListUsersChat();
                removeUsersInGroup(usersInGroup);
            }

            if (pageNumber == 0) friendsAdapter.updateFriendsList(l);
            else friendsAdapter.addFriends(l);

            if (size == 0) {
                isLastPage = true;
            }
            else pageNumber++;
        }
        swipeRefreshLayout.setRefreshing(false);
        isLoading = false;
        progressBar.setVisibility(View.INVISIBLE);
    }

    private class getFriends extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            setLoading();
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
            updateData();
            super.onPostExecute(s);
        }
    }

    private class createChat extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            setLoading();
        }


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

    private class UpdateChat extends AsyncTask<Chat,String,String> {

        @Override
        protected void onPreExecute() {
            setLoading();
        }


        @Override
        protected String doInBackground(Chat... chats) {
            try {
                chatDBAdapter.updateChat(chats[0]);
            } catch (AutorizationException e) {
                e.printStackTrace();
            } catch (ParamsException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void initializePagination() {
        pageNumber = 0;
        isLoading = false;
        isLastPage = false;
    }

    @Override
    public void onResume() {
        updateFriends();
        super.onResume();
    }

    private void removeUsersInGroup(List<User> users) {
        List<Friend> friendsInGroup = new ArrayList<>();
        for (User user : users) {
            for (Friend f : l) {
                User friend = f.getFriend();
                if (currentUser.getUsername().equals(friend.getUsername())) friend = f.getUser();
                if (user.getId().equals(friend.getId())) friendsInGroup.add(f);
            }
        }
        l.removeAll(friendsInGroup);
    }

}
