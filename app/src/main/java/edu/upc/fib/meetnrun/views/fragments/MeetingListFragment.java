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
import android.widget.Toast;

import java.util.List;


import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.views.CreateMeetingActivity;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.views.MeetingInfoActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;


public class MeetingListFragment extends Fragment {

    private MeetingsAdapter meetingsAdapter;
    private IMeetingAdapter meetingDBAdapter;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Meeting> meetings;

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
        setupRecyclerView();

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setImageResource(R.drawable.add_group_512);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewMeeting();
            }
        });
        swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.fragment_meeting_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateMeetingList();
            }
        });
        return view;
    }

    private void setupRecyclerView() {
        final RecyclerView meetingsList = view.findViewById(R.id.fragment_meeting_container);
        meetingsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        meetingsAdapter = new MeetingsAdapter(meetings, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {
                Meeting selectedMeeting = meetingsAdapter.getMeetingAtPosition(position);
                    joinMeeting(selectedMeeting);
            }

            @Override
            public void onMeetingClicked(int position) {
                Toast.makeText(view.getContext(), "Showing selected meeting info", Toast.LENGTH_SHORT).show();
                Meeting meeting = meetingsAdapter.getMeetingAtPosition(position);
                Intent meetingInfoIntent = new Intent(getActivity(),MeetingInfoActivity.class);
                meetingInfoIntent.putExtra("id",meeting.getId());
                meetingInfoIntent.putExtra("title",meeting.getTitle());
                meetingInfoIntent.putExtra("owner",meeting.getOwner().getUsername());
                meetingInfoIntent.putExtra("description",meeting.getDescription());
                String datetime = meeting.getDate();
                meetingInfoIntent.putExtra("date",datetime.substring(0,datetime.indexOf('T')));
                meetingInfoIntent.putExtra("time",datetime.substring(datetime.indexOf('T')+1,datetime.indexOf('Z')));
                meetingInfoIntent.putExtra("level",String.valueOf(meeting.getLevel()));
                meetingInfoIntent.putExtra("latitude",meeting.getLatitude());
                meetingInfoIntent.putExtra("longitude",meeting.getLongitude());
                startActivity(meetingInfoIntent);

            }
        });
        meetingsList.setAdapter(meetingsAdapter);

    }

    @Override
    public void onResume() {
        updateMeetingList();
        super.onResume();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.meeting_list_menu, menu);
        MenuItem item = menu.findItem(R.id.meeting_list_menu_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();
                new GetMeetingsFiltered().execute(query);
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
                updateMeetingList();
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void updateMeetingList() {
            new GetMeetings().execute();
    }

    private void createNewMeeting() {
        Intent intent = new Intent(getActivity(),CreateMeetingActivity.class);
        startActivity(intent);
    }

    private void joinMeeting(Meeting meeting) {
        new JoinMeeting().execute(meeting.getId());
    }

    private class GetMeetings extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.e("MAIN","DOINGGGG");
                meetings = meetingDBAdapter.getAllMeetings();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            System.err.println("FINISHED");
            meetingsAdapter.updateMeetingsList(meetings);
            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(s);
        }
    }


    private class GetMeetingsFiltered extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.e("MAIN","DOINGGGG");
           //TODO  meetingDBAdapter.(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            System.err.println("FINISHED");
            meetingsAdapter.updateMeetingsList(meetings);
            super.onPostExecute(s);
        }
    }

    private class JoinMeeting extends AsyncTask<Integer,String,String> {

        @Override
        protected String doInBackground(Integer... integers) {
            Log.e("MAIN","DOINGGGG");
            //TODO handle exceptions
            try {
                meetingDBAdapter.joinMeeting(integers[0]);
            } catch (AutorizationException e) {
                e.printStackTrace();
            } catch (ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            System.err.println("FINISHED");
            Toast.makeText(getActivity(),getString(R.string.joined_meeting),Toast.LENGTH_SHORT).show();
            updateMeetingList();
            super.onPostExecute(s);
        }
    }


}
