package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.ChatGroupsActivity;
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.MeetingInfoActivity;
import edu.upc.fib.meetnrun.views.ProfileViewPagerFragment;
import edu.upc.fib.meetnrun.views.UserProfileActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.UsersAdapter;


public class ChatGroupInfoFragment extends Fragment {

    protected View view;
    protected UsersAdapter usersAdapter;
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
    private TextView groupNumberUsers;
    private ImageButton meetingButton;

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

        meetingButton = view.findViewById(R.id.group_meeting);
        meetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMeetingView();
            }
        });
        TextView groupImage = view.findViewById(R.id.profileImage);
        groupImage.setBackground(getColoredCircularShape(name.charAt(0)));
        groupImage.setText(String.valueOf(name.charAt(0)));
        groupName = view.findViewById(R.id.group_name);
        progressBar = view.findViewById(R.id.pb_loading_friends);
        groupName.setText(name);
        groupNumberUsers = view.findViewById(R.id.group_num_users);
        updateNumberOfUsers();
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

    private void updateNumberOfUsers() {
        String numberOfUsersText = getString(R.string.number_of_users) + ":  " + groupUsers.size();
        groupNumberUsers.setText(numberOfUsersText);
    }

    private void setupRecyclerView() {

        recyclerView = view.findViewById(R.id.group_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        usersAdapter = new UsersAdapter(groupUsers, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {

                User friend = usersAdapter.getFriendAtPosition(position);
                getIntent(friend);

            }
        }, getContext());

        recyclerView.setAdapter(usersAdapter);

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
                List<Friend> friendsPage = new ArrayList<>();
                while (!isLastPage) {
                    friendsPage = friendsDBAdapter.listUserAcceptedFriends(CurrentSession.getInstance().getCurrentUser().getId(),pageNumber);
                    if (friendsPage.size() != 0) {
                        for (Friend f : friendsPage) {
                            User friend = f.getFriend();
                            if (CurrentSession.getInstance().getCurrentUser().getUsername().equals(friend.getUsername())) friend = f.getUser();
                            friends.add(friend);
                        }
                        ++pageNumber;
                    }
                    else isLastPage = true;
                }
            } catch (AutorizationException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            isLoading = false;
            progressBar.setVisibility(View.INVISIBLE);
            Log.e("Friends",friends.toString());
            super.onPostExecute(s);
        }

    }

    @Override
    public void onResume() {
        groupUsers = chat.getListUsersChat();
        usersAdapter.updateFriendsList(groupUsers);
        updateNumberOfUsers();
        super.onResume();
    }

    private void openMeetingView() {
        Meeting meeting = chat.getMeeting();
        Intent meetingInfoIntent = new Intent(getActivity(),MeetingInfoActivity.class);
        meetingInfoIntent.putExtra("id",meeting.getId());
        meetingInfoIntent.putExtra("chat",meeting.getChatID());
        meetingInfoIntent.putExtra("title",meeting.getTitle());
        meetingInfoIntent.putExtra("owner",meeting.getOwner().getUsername());
        meetingInfoIntent.putExtra("ownerId",meeting.getOwner().getId());
        meetingInfoIntent.putExtra("description",meeting.getDescription());
        String datetime = meeting.getDate();
        meetingInfoIntent.putExtra("date",datetime.substring(0,datetime.indexOf('T')));
        meetingInfoIntent.putExtra("time",datetime.substring(datetime.indexOf('T')+1,datetime.indexOf('Z')));
        meetingInfoIntent.putExtra("level",String.valueOf(meeting.getLevel()));
        meetingInfoIntent.putExtra("latitude",meeting.getLatitude());
        meetingInfoIntent.putExtra("longitude",meeting.getLongitude());
        startActivity(meetingInfoIntent);
    }

}
