package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.GetMeetings;
import edu.upc.fib.meetnrun.asynctasks.GetMeetingsFiltered;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

//TODO implementar crida a joinedMeetings per separat (ja no es dona aquesta info amb el meeting)
//TODO pero ara no funciona be la crida
public class MeetingListFragment extends BaseFragment {

    private MeetingsAdapter meetingsAdapter;
    private IMeetingAdapter meetingDBAdapter;
    private IChatAdapter chatAdapter;
    private IUserAdapter userAdapter;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private  FloatingActionButton fab;
    boolean filtered;
    String filteredQuery;
    private LinearLayoutManager layoutManager;
    int pageSize;
    private boolean refresh;
    private List<Meeting> myMeetings;

    //variables para paginacion
    private boolean isLoading;
    private boolean isLastPage;
    private int pageNumber;
    private ProgressBar progressBar;

    public MeetingListFragment() {
        meetingDBAdapter = CurrentSession.getInstance().getMeetingAdapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_list,container,false);
        this.view = view;

        meetingDBAdapter = CurrentSession.getInstance().getMeetingAdapter();
        chatAdapter = CurrentSession.getInstance().getChatAdapter();
        userAdapter = CurrentSession.getInstance().getUserAdapter();
        //iniciar paginacion y progressbar
        initializePagination();
        refresh = false;
        filtered = false;
        filteredQuery = "";
        progressBar = view.findViewById(R.id.pb_loading);
        setupRecyclerView();

        fab = getActivity().findViewById(R.id.activity_fab);
        fab.setImageResource(R.drawable.add_group_512);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewMeeting();
            }
        });
        swipeRefreshLayout =
                view.findViewById(R.id.fragment_meeting_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializePagination();
                updateMeetingList();
            }
        });
        swipeRefreshLayout.setProgressViewOffset(true,200,400);
        updateMeetingList();
        return view;
    }

    private void setupRecyclerView() {
        final RecyclerView meetingsList = view.findViewById(R.id.fragment_meeting_container);
        layoutManager = new LinearLayoutManager(getActivity());
        meetingsList.setLayoutManager(layoutManager);
        List<Meeting> meetings = new ArrayList<>();
        meetingsAdapter = new MeetingsAdapter(meetings, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {
                Meeting selectedMeeting = meetingsAdapter.getMeetingAtPosition(position);
                joinMeeting(selectedMeeting);
            }

            @Override
            public void onItemClicked(int position) {
                Toast.makeText(view.getContext(), "Showing selected meeting info", Toast.LENGTH_SHORT).show();
                Meeting meeting = meetingsAdapter.getMeetingAtPosition(position);
                Intent meetingInfoIntent = new Intent();
                meetingInfoIntent.putExtra("id",meeting.getId());
                //TODO 'if' temporal, fins que es borri la BD
                if (meeting.getChatID() != null) meetingInfoIntent.putExtra("chat",meeting.getChatID());
                meetingInfoIntent.putExtra("title",meeting.getTitle());
                meetingInfoIntent.putExtra("owner",meeting.getOwner().getUsername());
                meetingInfoIntent.putExtra("ownerId",meeting.getOwner().getId());
                meetingInfoIntent.putExtra("description",meeting.getDescription());
                String datetime = meeting.getDate();
                meetingInfoIntent.putExtra("date",datetime.substring(0,datetime.indexOf('T')));
                meetingInfoIntent.putExtra("time",datetime.substring(datetime.indexOf('T')+1,datetime.length()));
                meetingInfoIntent.putExtra("level",String.valueOf(meeting.getLevel()));
                meetingInfoIntent.putExtra("latitude",meeting.getLatitude());
                meetingInfoIntent.putExtra("longitude",meeting.getLongitude());
                BaseActivity.startWithFragment(getActivity(), new MeetingInfoFragment(), meetingInfoIntent);

            }
        });

        //scrollListener para detectar que se llega al final de la lista para pedir la siguiente pagina
        meetingsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        updateMeetingList();
                    }
                    else {
                        if (!recyclerView.canScrollVertically(1)) fab.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        meetingsList.setAdapter(meetingsAdapter);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.meeting_list_menu, menu);
        MenuItem item = menu.findItem(R.id.meeting_list_menu_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtered = true;
                initializePagination();
                query = query.toLowerCase();
                filteredQuery = query;
                updateMeetingList();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                filtered = false;
                initializePagination();
                updateMeetingList();
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    //funcion inicializar paginacion
    private void initializePagination() {
        pageNumber = 0;
        isLoading = false;
        isLastPage = false;
    }

    private void updateMeetingList() {
        if (filtered) callGetMeetingsFiltered(filteredQuery);
        else callGetMeetings();
    }

    private void createNewMeeting() {
        refresh = true;
        Intent intent = new Intent();
        BaseActivity.startWithFragment(getActivity(), new CreateMeetingFragment(), intent);
    }

    private void getMyMeetings() {
        new GetMyMeetings().execute(CurrentSession.getInstance().getCurrentUser().getId());
    }

    private void joinMeeting(Meeting meeting) {
        new JoinMeeting().execute(meeting.getId(),meeting.getChatID());
    }

    private void setLoading() {
        if (!swipeRefreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
        isLoading = true;
    }

    private void updateData(List<Meeting> meetings) {
        if (meetings != null) {
            if (pageNumber == 0) {
                meetingsAdapter.updateMeetingsList(meetings);
                pageSize = meetings.size();
            }
            else {
                meetingsAdapter.addMeetings(meetings);
            }

            if (pageNumber != 0 && meetings.size() < pageSize) {
                isLastPage = true;
            }
            else pageNumber++;
        }
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    //nueva forma de usar asynctasks
    private void callGetMeetings() {
        setLoading();
        new GetMeetings(pageNumber) {
            @Override
            public void onResponseReceived(List<Meeting> meetings) {
                updateData(meetings);
            }
        }.execute();
    }

    private void callGetMeetingsFiltered(String query) {
        setLoading();
        new GetMeetingsFiltered(pageNumber) {

            @Override
            public void onResponseReceived(List<Meeting> meetings) {
                updateData(meetings);
            }
        }.execute(query);
    }


    /*
        new JoinMeeting.execute(meetingId,chatId)
     */
    private class JoinMeeting extends AsyncTask<Integer,String,String> {

        @Override
        protected String doInBackground(Integer... integers) {
            Log.e("MAIN","DOINGGGG");
            //TODO handle exceptions
            try {
                //TODO possible millora: crida al servidor joinChat
                meetingDBAdapter.joinMeeting(integers[0],CurrentSession.getInstance().getCurrentUser().getId());
                Chat chat = chatAdapter.getChat(integers[1]);
                List<User> chatUsers = chat.getListUsersChat();
                chatUsers.add(CurrentSession.getInstance().getCurrentUser());
                chat.setListUsersChat(chatUsers);
                chatAdapter.updateChat(chat);
            } catch (AuthorizationException | ParamsException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getActivity(),getString(R.string.joined_meeting),Toast.LENGTH_SHORT).show();
            getMyMeetings();
            super.onPostExecute(s);
        }
    }

    private class GetMyMeetings extends AsyncTask<Integer,String,String> {

        @Override
        protected String doInBackground(Integer... integers) {
            //TODO handle exceptions
            try {
                myMeetings = userAdapter.getUsersFutureMeetings(integers[0]);
            } catch (AuthorizationException | ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            meetingsAdapter.setMyMeetings(myMeetings);
            super.onPostExecute(s);
        }

    }

    @Override
    public void onResume() {
        if (refresh) {
            refresh = false;
            initializePagination();
            updateMeetingList();
        }
        getMyMeetings();
        super.onResume();
    }

    @Override
    public int getTitle() {
        return R.string.meetings;
    }

}
