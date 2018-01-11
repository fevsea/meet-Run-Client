package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.AcceptOrRejectFriend;
import edu.upc.fib.meetnrun.asynctasks.GetFriends;
import edu.upc.fib.meetnrun.asynctasks.GetPendingFriends;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.ProfileViewPagerFragment;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.PendingFriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.TwoButtonsRecyclerViewOnClickListener;


public class FriendsFragment extends FriendListFragmentTemplate {

    protected List<Friend> pendingFriends;

    protected int pendingPageNumber;
    protected boolean pendingIsLoading;
    protected boolean pendingIsLastPage;

    private RecyclerView pendingList;
    private PendingFriendsAdapter pendingFriendsAdapter;

    @Override
    protected void initList() {
        pendingFriends = new ArrayList<>();
    }

    @Override
    protected void floatingbutton() {
        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.add_user_512);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewFriend();
            }
        });
    }

    @Override
    protected void adapter() {}

    @Override
    protected void getIntent(User friend) {
        CurrentSession.getInstance().setFriend(friend);
        Intent intent = new Intent(getActivity(), ProfileViewPagerFragment.class);
        intent.putExtra("userId",friend.getId());
        intent.putExtra("isFriend",true);
        startActivity(intent);
    }

    @Override
    protected void getPaginationMethod() {
        callGetFriends();
    }

    protected void refreshList() {
        getPaginationMethod();
        callGetPendingFriends();
    }

    private void addNewFriend() {
        BaseActivity.startWithFragment(getActivity(), new UsersListFragment());
    }

    @Override
    protected void setupRecyclerView() {
        super.setupRecyclerView();

        pendingList = view.findViewById(R.id.fragment_friends_request_container);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        pendingList.setLayoutManager(linearLayoutManager);

        pendingFriends = new ArrayList<>();

        pendingFriendsAdapter = new PendingFriendsAdapter(pendingFriends, getTwoButtonsRecyclerViewListener(), getContext());

        pendingList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!pendingIsLoading && !pendingIsLastPage &&
                        (visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                         firstVisibleItemPosition >= 0) {
                    callGetPendingFriends();
                }

            }
        });

        pendingList.setAdapter(pendingFriendsAdapter);
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

    private void setLoading() {
        if (!swipeRefreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
        isLoading = true;
    }

    protected TwoButtonsRecyclerViewOnClickListener getTwoButtonsRecyclerViewListener() {
        return new TwoButtonsRecyclerViewOnClickListener() {

            @Override
            public void onButtonAcceptClicked(int position) {
                Friend friend = pendingFriendsAdapter.getFriendAtPosition(position);
                friend.setAccepted(true);
                Log.d("PreAcceptFriend", friend.getUser().getUsername());
                callAcceptOrRejectFriend(friend);
            }

            @Override
            public void onButtonRejectClicked(int position) {
                Friend friend = pendingFriendsAdapter.getFriendAtPosition(position);
                friend.setAccepted(false);
                Log.d("PreRejectFriend", friend.getUser().getUsername());
                callAcceptOrRejectFriend(friend);
            }

            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {
                Friend friendship = pendingFriendsAdapter.getFriendAtPosition(position);
                User friend = friendship.getFriend();
                if (currentUser.getUsername().equals(friend.getUsername())) friend = friendship.getUser();
                CurrentSession.getInstance().setFriend(friend);
                Intent intent = new Intent(getActivity(), ProfileViewPagerFragment.class);
                intent.putExtra("userId",friend.getId());
                intent.putExtra("isFriend",true);
                startActivity(intent);
            }
        };
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

    private void updatePendingFriendsData() {
        if (l != null) {
            pendingFriends = new ArrayList<>();
            for (int i = 0; i < l.size(); ++i) {
                if (!l.get(i).getUser().getId().equals(currentUser.getId())) {
                    pendingFriends.add(l.get(i));
                }
            }
            if (pendingPageNumber == 0) {
                pendingFriendsAdapter.updateFriendsList(pendingFriends);
            }
            else {
                pendingFriendsAdapter.addFriends(pendingFriends);
            }
            if (pendingFriends.size() == 0) {
                pendingIsLastPage = true;
            }
            else {
                pageNumber++;
            }
            swipeRefreshLayout.setRefreshing(false);
            pendingIsLoading = false;
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void callGetPendingFriends() {
        if (!swipeRefreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
        pendingIsLoading = true;
        new GetPendingFriends(pendingPageNumber) {
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
                updatePendingFriendsData();
            }
        }.execute();
    }

    private void callAcceptOrRejectFriend(Friend friend) {
        new AcceptOrRejectFriend() {
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
            public void onResponseReceived(boolean b) {
                initializePagination();
                refreshList();
            }
        }.execute(friend);
    }

    @Override
    protected void initializePagination() {
        super.initializePagination();
        pendingPageNumber = 0;
        pendingIsLastPage = false;
        pendingIsLoading = false;

    }

    @Override
    public void onResume() {
        initializePagination();
        refreshList();
        super.onResume();
    }

    public int getTitle() {
        return R.string.friends_label;
    }
}
