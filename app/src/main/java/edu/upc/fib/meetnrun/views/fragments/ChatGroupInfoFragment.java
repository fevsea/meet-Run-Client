package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
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
        groupName.setText(name);
        TextView groupNumberUsers = view.findViewById(R.id.group_num_users);
        groupNumberUsers.setText(numberOfUsersText);
        Button addUserButton = view.findViewById(R.id.group_adduser);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Intent para añadir usuario
            }
        });
        setupRecyclerView();
        fab = getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.INVISIBLE);
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
        Intent friendProfileIntent = new Intent(getActivity(),FriendProfileActivity.class);
        startActivity(friendProfileIntent);
        //TODO check if its friend or not to load different actitivy
    }

    private GradientDrawable getColoredCircularShape(char letter) {
        int[] colors = view.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(view.getContext(),R.drawable.user_profile_circular_text_view);
        int position = letter%colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }

}
