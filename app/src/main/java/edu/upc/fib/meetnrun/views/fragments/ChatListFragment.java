package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Message;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.ChatActivity;
import edu.upc.fib.meetnrun.views.ChatFriendsActivity;
import edu.upc.fib.meetnrun.views.FriendsActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.ChatAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * Created by eric on 21/11/17.
 */

public class ChatListFragment extends Fragment {

    private View view;
    private FloatingActionButton fab, fab2, fab3;
    Animation FabOpen, FabClose, FabRClockWise, FabRantiClockWise;
    private List<Chat> l;
    private ChatAdapter chatAdapter;
    private String friendUserName = null;
    private User currentUser;
    private boolean isOpen = false;

    private static List<Chat> list = new ArrayList<Chat>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        this.view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        currentUser = CurrentSession.getInstance().getCurrentUser();

        l = new ArrayList<Chat>();

        setupRecyclerView();

        fab = (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setImageResource(R.drawable.chat);

        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);

        FabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        FabRClockWise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_clockwise);
        FabRantiClockWise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anticlockwise);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    fab2.startAnimation(FabClose);
                    fab3.startAnimation(FabClose);
                    fab.startAnimation(FabRantiClockWise);
                    fab2.setClickable(false);
                    fab3.setClickable(false);
                    isOpen = false;
                }
                else {
                    fab2.startAnimation(FabOpen);
                    fab3.startAnimation(FabOpen);
                    fab.startAnimation(FabRClockWise);
                    fab2.setClickable(true);
                    fab3.setClickable(true);
                    isOpen = true;
                }
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_chat_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateChats();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return this.view;
    }

    private void updateChats() {
        //new getChats().execute();
        sortList();
        chatAdapter.updateChatList(list);
    }

    private void addChat() {
        Intent intent = new Intent(getActivity(), ChatFriendsActivity.class);
        startActivity(intent);
    }

    private void addGroup() {
        Intent intent = new Intent(getActivity(), ChatFriendsActivity.class);
        startActivity(intent);
    }

    private void setupRecyclerView() {

        final RecyclerView chatList = view.findViewById(R.id.fragment_chat_container);
        chatList.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Chat> chats = new ArrayList<Chat>();

        chatAdapter = new ChatAdapter(chats, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onMeetingClicked(int position) {

                Chat chat = chatAdapter.getChatAtPosition(position);
                Intent chatIntent = new Intent(getActivity(),ChatActivity.class);
                CurrentSession.getInstance().setChat(chat);
                startActivity(chatIntent);
            }
        });
        chatList.setAdapter(chatAdapter);
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
                ArrayList<Chat> newList = new ArrayList<Chat>();
                for (Chat chat : l) {

                    if (!currentUser.getUsername().equals(chat.getUserName1().getUsername())) friendUserName = chat.getUserName1().getUsername();
                    else friendUserName = chat.getUserName2();

                    String friendName = friendUserName.toLowerCase();
                    if (friendName != null) {
                        if (friendName.contains(newText)) newList.add(chat);
                    }

                }
                chatAdapter.updateChatList(newList);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private class getChats extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
                //l = chatDBAdapter.getUserChats();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //chatAdapter.updateChatList(l);
            super.onPostExecute(s);
        }
    }

    @Override
    public void onResume() {
        updateChats();
        super.onResume();
    }

    public static void addChatFake(Chat c) {
        list.add(c);
    }

    public static Chat getChat(String user, String friend) {

        for (Chat chat : list) {

            String chatUserName = chat.getUserName1().getUsername();
            if (chatUserName.equals(user) || chatUserName.equals(friend)) {
                String chatFriendUserName = chat.getUserName2();
                if (chatFriendUserName.equals(user) || chatFriendUserName.equals(friend)) {
                    return chat;
                }
            }
        }
        return null;
    }

    public static boolean deleteChat(String name) {
        for (Chat chat : list) {
            if (chat.getUserName1().getUsername().equals(name) || chat.getUserName2().equals(name)) {
                list.remove(chat);
                return true;
            }
        }
        return false;
    }

    public void sortList() {
       /* Collections.sort(list, new Comparator<Chat>() {
            @Override
            public int compare(Chat c2, Chat c1) {
                Message m1 = c1.getMessage();
                Message m2= c2.getMessage();
                if (m1.getDate().equals(m2.getDate())) {
                    if (m1.getHour().equals(m2.getHour())) {
                        return m1.getSeconds().compareTo(m2.getSeconds());
                    }
                    else return m1.getHour().compareTo(m2.getHour());
                }
                else return m1.getDate().compareTo(m2.getDate());
            }
        });*/
    }
}
