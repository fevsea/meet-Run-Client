package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.CreateChat;
import edu.upc.fib.meetnrun.asynctasks.GetChat;
import edu.upc.fib.meetnrun.asynctasks.GetFriends;
import edu.upc.fib.meetnrun.asynctasks.GetPrivateChat;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * Created by eric on 22/11/17.
 */

public class ChatFriendsFragment extends FriendListFragmentTemplate {

    private Chat chat;
    private String friendUserName;
    private List<Integer> userList;
    private Date dateWithoutTime;
    private User friend;

    @Override
    protected void initList() {
        getPaginationMethod();
    }

    @Override
    protected void floatingbutton() {
        fab.setVisibility(View.GONE);
    }

    @Override
    protected void adapter() {
    }

    @Override
    protected void refreshList() {
        getPaginationMethod();
    }

    @Override
    protected void getIntent(User friend) {

        this.friend = friend;
        callGetPrivateChat(friend.getId());

    }

    @Override
    protected void getPaginationMethod() {
        callGetFriends();
    }

    @Override
    protected RecyclerViewOnClickListener getRecyclerViewListener() {
        return new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {

                User friend = friendsAdapter.getFriendAtPosition(position).getFriend();
                if (currentUser.getUsername().equals(friend.getUsername())) friend = friendsAdapter.getFriendAtPosition(position).getUser();
                getIntent(friend);

            }
        };
    }

    private void callGetFriends() {
        if (!swipeRefreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
        isLoading = true;
        GetFriends getFriends = new GetFriends(pageNumber) {

            @Override
            public void onResponseReceived(List<Friend> friends) {
                l = friends;
                if (l != null) {
                    if (pageNumber == 0) friendsAdapter.updateFriendsList(l);
                    else friendsAdapter.addFriends(l);

                    if (l.size() == 0) {
                        isLastPage = true;
                    }
                    else pageNumber++;
                }
                swipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
        try {
            getFriends.execute();
        }
        catch (AuthorizationException e) {
            Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
            dismissProgressBarsOnError();
        }
        catch (NotFoundException e) {
            Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
            dismissProgressBarsOnError();
        }
    }

    private void callCreateChat() {
        CreateChat createChat = new CreateChat(friendUserName,userList,0,null,"",0,dateWithoutTime) {
            @Override
            public void onResponseReceived(Chat chat) {
                if (chat != null) {
                    Intent i = new Intent();
                    CurrentSession.getInstance().setChat(chat);
                    BaseActivity.startWithFragment(getActivity(), new ChatFragment(), i);
                    getActivity().finish();
                }
            }
        };
        try {
            createChat.execute();
        }
        catch (AuthorizationException e) {
            Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
            dismissProgressBarsOnError();
        }
        catch (ParamsException e) {
            Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
            dismissProgressBarsOnError();
        }
    }

    private void updateData(Chat chat) {
        if (chat == null) {
            Calendar rightNow = Calendar.getInstance();

            dateWithoutTime = rightNow.getTime();

            userList = new ArrayList<>();
            userList.add(CurrentSession.getInstance().getCurrentUser().getId());
            userList.add(friend.getId());

            callCreateChat();
        }
        else {
            Intent i = new Intent();
            CurrentSession.getInstance().setChat(chat);
            BaseActivity.startWithFragment(getActivity(), new ChatFragment(), i);
            getActivity().finish();
        }
    }

    private void callGetPrivateChat(int chatId) {
        GetPrivateChat getPrivateChat = new GetPrivateChat() {
            @Override
            public void onResponseReceived(Chat responseChat) {
                updateData(chat);
            }
        };
        try {
            getPrivateChat.execute(chatId);
        }
        catch (AuthorizationException e) {
            Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
            dismissProgressBarsOnError();
        }
        catch (NotFoundException e) {
            Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
            dismissProgressBarsOnError();
        }
    }

    public int getTitle() {
        return R.string.friends_label;
    }


}
