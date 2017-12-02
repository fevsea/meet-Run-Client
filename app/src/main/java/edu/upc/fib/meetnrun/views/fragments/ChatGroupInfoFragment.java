package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.ChatGroupsActivity;
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.ProfileViewPagerFragment;
import edu.upc.fib.meetnrun.views.UserProfileActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;


public class ChatGroupInfoFragment extends Fragment {

    protected View view;
    protected FriendsAdapter friendsAdapter;
    protected IFriendsAdapter friendsDBAdapter;
    protected List<User> groupUsers;
    protected FloatingActionButton fab;
    private TextView groupName;
    private RecyclerView recyclerView;
    private Chat chat;
    private List<User> friends;
    private ProgressBar progressBar;
    private boolean isLastPage;
    private boolean isLoading;
    private int pageNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        this.view = inflater.inflate(R.layout.fragment_chat_group_info, container, false);

        friendsDBAdapter = CurrentSession.getInstance().getFriendsAdapter();
        chat = CurrentSession.getInstance().getChat();

        String name = chat.getChatName();
        getActivity().setTitle(name);
        groupUsers = chat.getListUsersChat();
        String numberOfUsersText = getString(R.string.number_of_users) + ":  " + groupUsers.size();

        TextView groupImage = view.findViewById(R.id.profileImage);
        groupImage.setBackground(getColoredCircularShape(name.charAt(0)));
        groupImage.setText(String.valueOf(name.charAt(0)));
        groupName = view.findViewById(R.id.group_name);
        progressBar = view.findViewById(R.id.pb_loading_friends);
        groupName.setText(name);
        TextView groupNumberUsers = view.findViewById(R.id.group_num_users);
        groupNumberUsers.setText(numberOfUsersText);
        Button addUserButton = view.findViewById(R.id.group_adduser);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentSession.getInstance().setChat(chat);
                Intent addUserIntent = new Intent(getContext(), ChatGroupsActivity.class);
                addUserIntent.putExtra("action","adduser");
                startActivity(addUserIntent);
            }
        });
        setupRecyclerView();
        fab = getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.INVISIBLE);
        new GetAllFriends().execute();
        return this.view;
    }



    private void setupRecyclerView() {

        recyclerView = view.findViewById(R.id.group_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        friendsAdapter = new FriendsAdapter(groupUsers, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {

                User friend = friendsAdapter.getFriendAtPosition(position);
                getIntent(friend);

            }
        }, getContext(), false);

        recyclerView.setAdapter(friendsAdapter);

    }

    protected void getIntent(User friend) {
        if (isLoading) Toast.makeText(getContext(),R.string.loading_list,Toast.LENGTH_LONG).show();
        else {
            CurrentSession.getInstance().setFriend(friend);
            Intent userProfileIntent = null;
            if (CurrentSession.getInstance().getCurrentUser().getId().equals(friend.getId())) {
                userProfileIntent = new Intent(getActivity(), ProfileViewPagerFragment.class);
            }
            else if (isFriend(friend)) {
                userProfileIntent = new Intent(getActivity(), FriendProfileActivity.class);
            }
            else {
                userProfileIntent = new Intent(getActivity(), UserProfileActivity.class);
            }
            startActivity(userProfileIntent);
        }
    }

    private boolean isFriend(User friend) {
        for (User user : friends) {
            if (user.getId().equals(friend.getId())) return true;
        }
        return false;
    }

    private GradientDrawable getColoredCircularShape(char letter) {
        int[] colors = view.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(view.getContext(),R.drawable.user_profile_circular_text_view);
        int position = letter%colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }

    private class GetAllFriends extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            friends = new ArrayList<>();
            progressBar.setVisibility(View.VISIBLE);
            isLoading = true;
            pageNumber = 0;
            isLastPage = false;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<User> friendsPage = new ArrayList<>();
                while (!isLastPage) {
                    friendsPage = friendsDBAdapter.getUserFriends(pageNumber);
                    if (friendsPage.size() != 0) {
                        friends.addAll(friendsPage);
                        ++pageNumber;
                    }
                    else isLastPage = true;
                }
            } catch (AutorizationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            isLoading = false;
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(s);
        }

    }

    @Override
    public void onResume() {
        groupUsers = chat.getListUsersChat();
        friendsAdapter.updateFriendsList(groupUsers);
        super.onResume();
    }


}
