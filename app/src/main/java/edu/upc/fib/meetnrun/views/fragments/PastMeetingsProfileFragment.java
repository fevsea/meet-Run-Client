package edu.upc.fib.meetnrun.views.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.TrackingData;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.MeetingInfoActivity;
import edu.upc.fib.meetnrun.views.PastMeetingInfoActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

public class PastMeetingsProfileFragment extends Fragment {

    private IUserAdapter userController;
    private IMeetingAdapter meetingController;
    private int userId;
    private int meetingId;
    private MeetingsAdapter meetingsAdapter;


    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Meeting> meetings;

    private TrackingData tracking;

    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static PastMeetingsProfileFragment newInstance(int page, String title) {
        PastMeetingsProfileFragment fragmentFirst = new PastMeetingsProfileFragment();
        Bundle args = new Bundle();
        args.putInt("1", page);
        args.putString("meetings", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("1", 1);
        title = getArguments().getString("meetings");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_list,container,false);
        this.view = view;

        User u = CurrentSession.getInstance().getCurrentUser();
        userId = u.getId();

        userController = CurrentSession.getInstance().getUserAdapter();
        meetingController = CurrentSession.getInstance().getMeetingAdapter();
        setupRecyclerView();

        swipeRefreshLayout =
                view.findViewById(R.id.fragment_meeting_swipe);
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

        meetings = new ArrayList<>();
        meetingsAdapter = new MeetingsAdapter(meetings, new RecyclerViewOnClickListener() {

            @Override
            public void onButtonClicked(int position) {
            }

            @Override
            public void onItemClicked(int position) {
                Toast.makeText(view.getContext(), "Showing selected meeting info", Toast.LENGTH_SHORT).show();
                Meeting meeting = meetingsAdapter.getMeetingAtPosition(position);
                meetingId = meeting.getId();
                getTrackingData();

                Intent pastMeetingInfoIntent = new Intent(getActivity(),PastMeetingInfoActivity.class);

                pastMeetingInfoIntent.putExtra("id",meeting.getId());
                pastMeetingInfoIntent.putExtra("title",meeting.getTitle());
                pastMeetingInfoIntent.putExtra("owner",meeting.getOwner().getUsername());
                pastMeetingInfoIntent.putExtra("ownerId",meeting.getOwner().getId());
                pastMeetingInfoIntent.putExtra("description",meeting.getDescription());
                String datetime = meeting.getDate();
                pastMeetingInfoIntent.putExtra("date",datetime.substring(0,datetime.indexOf('T')));
                pastMeetingInfoIntent.putExtra("time",datetime.substring(datetime.indexOf('T')+1,datetime.indexOf('Z')));
                pastMeetingInfoIntent.putExtra("level",String.valueOf(meeting.getLevel()));

                String distance, steps, totalTime, avSpeed, calories;
                List<LatLng> path = null;

                if(tracking == null) {
                    distance = "0";
                    steps = "0";
                    totalTime = "0";
                    avSpeed = "0";
                    calories = "0";
                } else {
                    distance = String.valueOf(tracking.getDistance());
                    steps = String.valueOf(tracking.getSteps());
                    totalTime = String.valueOf(tracking.getTotalTimeMillis());
                    avSpeed = String.valueOf(tracking.getAverageSpeed());
                    calories = String.valueOf(tracking.getCalories());
                    path = tracking.getRoutePoints();
                }
                pastMeetingInfoIntent.putExtra("distance", distance);
                pastMeetingInfoIntent.putExtra("steps", steps);
                pastMeetingInfoIntent.putExtra("totaltime", totalTime);
                pastMeetingInfoIntent.putExtra("avspeed", avSpeed);
                pastMeetingInfoIntent.putExtra("calories", calories);

                pastMeetingInfoIntent.putExtra("path", (Parcelable) path);

                startActivity(pastMeetingInfoIntent);

            }
        });
        updateMeetingList();
        meetingsList.setAdapter(meetingsAdapter);

    }

    private void getTrackingData() {
        new getPastMeetingsTracking().execute(userId, meetingId);
    }

    private void updateMeetingList() {
        new PastMeetingsProfileFragment.GetPastMeetings().execute(userId);
    }


    private class GetPastMeetings extends AsyncTask<Integer,Integer,String> {
        List<Meeting> l = new ArrayList<>();

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                l = userController.getUserPastMeetings(integers[0]);//TODO arreglar paginas
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
            meetingsAdapter.updateMeetingsList(l);
            super.onPostExecute(s);
        }
    }


    private class getPastMeetingsTracking extends AsyncTask<Integer, Integer, String> {
            @Override
            protected String doInBackground(Integer... integers) {
                try {
                    tracking = meetingController.getTracking(integers[0], integers[1]);
                } catch (AutorizationException e) {
                    e.printStackTrace();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                System.err.println("FINISHED");
                super.onPostExecute(s);
            }
        }


    }


