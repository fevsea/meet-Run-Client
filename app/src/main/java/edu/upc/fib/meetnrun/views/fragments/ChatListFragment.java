package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.GetChats;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.ChatAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * Created by eric on 21/11/17.
 */

public class ChatListFragment extends BaseFragment {

    private View view;
    private FloatingActionButton fab, fab2, fab3;
    private Animation FabOpen;
    private Animation FabClose;
    private Animation FabRClockWise;
    private Animation FabRantiClockWise;
    private List<Chat> charListArray;
    private ChatAdapter chatAdapter;
    private boolean isOpen = false;
    private IChatAdapter chatDBAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayoutManager layoutManager;
    private boolean isLoading;
    private boolean isLastPage;
    private int pageNumber;
    private ProgressBar progressBar;

    private boolean filtered;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        this.view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        filtered = false;
        chatDBAdapter = CurrentSession.getInstance().getChatAdapter();

        initializePagination();
        progressBar = view.findViewById(R.id.pb_loading_chat);
        setupRecyclerView();

        fab = getActivity().findViewById(R.id.activity_fab);
        fab.setImageResource(R.drawable.chat);
        fab.setVisibility(View.VISIBLE);

        fab2 = view.findViewById(R.id.fab2);
        fab3 = view.findViewById(R.id.fab3);

        FabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        FabRClockWise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_clockwise);
        FabRantiClockWise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anticlockwise);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animFab();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChat();
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGroup();
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.fragment_chat_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializePagination();
                updateChats();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setProgressViewOffset(true,200,400);

        return this.view;
    }

    private void animFab() {
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

    private void updateChats() {
        if (!filtered) callGetChats();
    }

    private void addChat() {
        Intent intent = new Intent();
        animFab();
        BaseActivity.startWithFragment(getActivity(), new ChatFriendsFragment(), intent);
    }

    private void addGroup() {
        Intent intent = new Intent();
        intent.putExtra("action","addgroup");
        animFab();
        BaseActivity.startWithFragment(getActivity(), new ChatGroupsFragment(), intent);
    }

    private void setupRecyclerView() {

        final RecyclerView chatList = view.findViewById(R.id.fragment_chat_container);
        layoutManager = new LinearLayoutManager(getActivity());
        chatList.setLayoutManager(layoutManager);

        charListArray = new ArrayList<>();

        chatAdapter = new ChatAdapter(charListArray, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {

                Chat chat = chatAdapter.getChatAtPosition(position);
                Intent chatIntent = new Intent();
                CurrentSession.getInstance().setChat(chat);
                BaseActivity.startWithFragment(getActivity(), new ChatFragment(), chatIntent);
            }
        });

        chatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                fab.setVisibility(View.VISIBLE);

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    if (!isLastPage) {
                        updateChats();
                    }
                    else {
                        fab.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        chatList.setAdapter(chatAdapter);
    }

    /*@Override
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
                filtered = true;
                newText = newText.toLowerCase();
                ArrayList<Chat> newList = new ArrayList<>();
                for (Chat chat : charListArray) {
                    String chatName = chat.getChatName().toLowerCase();
                    Log.e("name", chatName);
                    if (chatName != null) {
                        if (chatName.contains(newText)) newList.add(chat);
                    }
                }
                chatAdapter.updateChatList(newList);
                return true;
            }
        });
        searchView.setOnCloseListener(new android.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                filtered = false;
                initializePagination();
                updateChats();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    private void callGetChats() {
        setLoading();
        new GetChats(pageNumber) {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onResponseReceived(List<Chat> chats) {
                charListArray = chats;
                updateData();
            }
        }.execute();
    }

    private void updateData() {

        if (charListArray != null) {
            if (pageNumber == 0) chatAdapter.updateChatList(charListArray);
            else chatAdapter.addChats(charListArray);

            if (charListArray.size() == 0) {
                isLastPage = true;
            }
            else pageNumber++;
        }

        swipeRefreshLayout.setRefreshing(false);
        isLoading = false;
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setLoading() {
        if (!swipeRefreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
        isLoading = true;
    }

    private void initializePagination() {
        pageNumber = 0;
        isLoading = false;
        isLastPage = false;
    }

    @Override
    public void onResume() {
        initializePagination();
        updateChats();
        super.onResume();
    }

    public int getTitle() {
        return R.string.chat_label;
    }
}
