package edu.upc.fib.meetnrun.views.fragments;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.GetFriends;
import edu.upc.fib.meetnrun.asynctasks.GetMeetingsFiltered;
import edu.upc.fib.meetnrun.asynctasks.JoinMeeting;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;


/**
 * Created by Javier on 08/11/2017.
 */

public class MeetingFriendsFragment extends FriendListFragmentTemplate {


    private List<User> selectedFriends = new ArrayList<>();
    private IChatAdapter chatAdapter = CurrentSession.getInstance().getChatAdapter();

    @Override
    protected void initList() {}

    @Override
    protected void floatingbutton() {
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void adapter() {}

    @Override
    protected void refreshList() {
        getPaginationMethod();
    }


    @Override
    protected void getIntent(User friend) {
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
                if (currentUser.getId().equals(friend.getId())) friend = friendsAdapter.getFriendAtPosition(position).getUser();

                if (friend.isSelected()) {
                    selectedFriends.remove(friend);
                    friend.setSelected(false);
                }
                else {
                    selectedFriends.add(friend);
                    friend.setSelected(true);
                }
                friendsAdapter.notifyDataSetChanged();

            }
        };
    }

    private void setLoading() {
        if (!swipeRefreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
        isLoading = true;
    }

    private void updateData() {
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



    private void callGetFriends() {
        setLoading();
        new GetFriends(pageNumber) {

            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                    dismissProgressBarsOnError();
                }
                else if (e instanceof NotFoundException) {
                    Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
                    dismissProgressBarsOnError();
                }
            }

            @Override
            public void onResponseReceived(List<Friend> friends) {
                l = friends;
                updateData();
            }
        }.execute();
    }

    private void callJoinMeeting(int meetingId, int chatId, List<User> users) {
            new JoinMeeting(meetingId,chatId,users) {

                @Override
                public void onExceptionReceived(GenericException e) {
                    if (e instanceof AuthorizationException) {
                        Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                    }
                    else if (e instanceof ParamsException) {
                        Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onResponseReceived() {
                    Log.e("MeetingFriendsFragment", "Users joined meeting succesfully");
                    getActivity().finish();
                }
            }.execute();
    }

    @Override
    public void onResume() {
        initializePagination();
        getPaginationMethod();
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.edit_meeting_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done_button) {
            ArrayList<User> selectedFriendsID = new ArrayList<>();

            for (User user : selectedFriends) {
                selectedFriendsID.add(user);
            }
            int meetingId = getActivity().getIntent().getExtras().getInt("meetingId");
            callJoinMeeting(meetingId,CurrentSession.getInstance().getChat().getId(),selectedFriendsID);
        }
        return super.onOptionsItemSelected(item);
    }
}
