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
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
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
    private FloatingActionButton fab;
    private List<Chat> l;
    private ChatAdapter chatAdapter;

    private static List<Chat> list = new ArrayList<Chat>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        this.view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        l = new ArrayList<Chat>();

        setupRecyclerView();

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setImageResource(R.drawable.chat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChat();
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
        chatAdapter.updateChatList(list);
    }

    private void addChat() {
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
                    String friendName = chat.getFriendUsername().toLowerCase();
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
            String chatUserName = chat.getUserName();
            if (chatUserName.equals(user) || chatUserName.equals(friend)) {
                String chatFriendUserName = chat.getFriendUsername();
                if (chatFriendUserName.equals(user) || chatFriendUserName.equals(friend)) {
                    return chat;
                }
            }
        }
        return null;
    }

    public static boolean deleteChat(String name) {
        for (Chat chat : list) {
            if (chat.getUserName().equals(name) || chat.getFriendUsername().equals(name)) {
                list.remove(chat);
                return true;
            }
        }
        return false;
    }
}
