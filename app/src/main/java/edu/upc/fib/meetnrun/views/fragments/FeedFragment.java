package edu.upc.fib.meetnrun.views.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.JoinMeeting;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.FeedMeeting;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.TrackingData;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.utils.UtilsGlobal;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FeedMeetingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

public class FeedFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView itemListView;
    private FeedMeetingsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<FeedMeeting> itemList;
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feed, container, false);

        FloatingActionButton button = getActivity().findViewById(R.id.activity_fab);
        button.setVisibility(View.GONE);

        itemListView = view.findViewById(R.id.feed_recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.feed_swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(this);

        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        itemListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemList = new ArrayList<>();
        adapter = new FeedMeetingsAdapter(itemList, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {
                Meeting m = adapter.getItemAt(position).getMeeting();
                callJoinMeeting(m.getId(),m.getChatID());
            }

            @Override
            public void onItemClicked(int position) {
                FeedMeeting feedMeeting = adapter.getItemAt(position);
                Meeting meeting = feedMeeting.getMeeting();
                if (feedMeeting.getType() == FeedMeeting.PAST_FRIEND) {
                    TrackingData tracking = feedMeeting.getTracking();
                    List<LatLng> path = tracking.getRoutePoints();
                    int meetingId = meeting.getId();

                    Intent pastMeetingInfoIntent = new Intent();

                    pastMeetingInfoIntent.putExtra("id", meeting.getId());
                    pastMeetingInfoIntent.putExtra("title", meeting.getTitle());
                    pastMeetingInfoIntent.putExtra("owner", meeting.getOwner().getUsername());
                    pastMeetingInfoIntent.putExtra("ownerId", meeting.getOwner().getId());
                    pastMeetingInfoIntent.putExtra("description", meeting.getDescription());
                    String datetime = meeting.getDate();
                    pastMeetingInfoIntent.putExtra("date", datetime.substring(0, datetime.indexOf('T')));
                    pastMeetingInfoIntent.putExtra("time", datetime.substring(datetime.indexOf('T') + 1, datetime.length()));
                    pastMeetingInfoIntent.putExtra("level", String.valueOf(meeting.getLevel()));

                    String distance, steps, totalTime, avSpeed, calories;
                    path = new ArrayList<>();

                    if (tracking == null) {
                        distance = "0";
                        steps = "0";
                        totalTime = "0";
                        avSpeed = "0";
                        calories = "0";
                        path.add(new LatLng(Double.valueOf(meeting.getLatitude()), Double.valueOf(meeting.getLongitude())));
                    } else {
                        distance = String.valueOf(tracking.getDistance()); //m
                        steps = String.valueOf(tracking.getSteps());
                        totalTime = String.valueOf(tracking.getTotalTimeMillis()); //ms
                        avSpeed = String.valueOf(tracking.getAverageSpeed()); // m/s
                        calories = String.valueOf(tracking.getCalories()); // kcal
                        path = tracking.getRoutePoints();
                    }
                    pastMeetingInfoIntent.putExtra("distance", distance);
                    pastMeetingInfoIntent.putExtra("steps", steps);
                    pastMeetingInfoIntent.putExtra("totaltime", totalTime);
                    pastMeetingInfoIntent.putExtra("avspeed", avSpeed);
                    pastMeetingInfoIntent.putExtra("calories", calories);

                    pastMeetingInfoIntent.putExtra("path", (Serializable) path);

                    BaseActivity.startWithFragment(getActivity(), new PastMeetingInfoFragment(), pastMeetingInfoIntent);
                } else {
                    Intent meetingInfoIntent = new Intent();
                    meetingInfoIntent.putExtra("title", meeting.getTitle());
                    meetingInfoIntent.putExtra("chat", meeting.getChatID());
                    meetingInfoIntent.putExtra("owner", meeting.getOwner().getUsername());
                    meetingInfoIntent.putExtra("id", meeting.getId());
                    meetingInfoIntent.putExtra("description", meeting.getDescription());
                    String datetime = meeting.getDate();
                    meetingInfoIntent.putExtra("date", datetime.substring(0, datetime.indexOf('T')));
                    meetingInfoIntent.putExtra("time", datetime.substring(datetime.indexOf('T') + 1, datetime.indexOf('Z')));
                    meetingInfoIntent.putExtra("level", String.valueOf(meeting.getLevel()));
                    meetingInfoIntent.putExtra("latitude", meeting.getLatitude());
                    meetingInfoIntent.putExtra("longitude", meeting.getLongitude());
                    BaseActivity.startWithFragment(getActivity(), new MeetingInfoFragment(), meetingInfoIntent);
                }
            }
        });
        itemListView.setAdapter(adapter);
    }

    @Override
    public int getTitle() {
        return R.string.feed;
    }

    @Override
    public void onRefresh() {
        updateFeed();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFeed();
    }

    private void updateFeed() {
        new GetFeedMeetings().execute();
    }

    private void updateAdapter() {
        adapter.updateMeetingsList(itemList);
    }

    private class GetFeedMeetings extends AsyncTask<Void, String, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            int userID = CurrentSession.getInstance().getCurrentUser().getId();
            itemList = CurrentSession.getInstance().getUserAdapter().getUsersFeed(userID);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            updateAdapter();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void callJoinMeeting(int meetingId, int chatId) {
        List<User> current = new ArrayList<>();
        current.add(CurrentSession.getInstance().getCurrentUser());
        new edu.upc.fib.meetnrun.asynctasks.JoinMeeting(meetingId, chatId, current) {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                } else if (e instanceof ParamsException) {
                    Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
                } else if (e instanceof ForbiddenException) {
                    Toast.makeText(getActivity(), R.string.forbidden_banned, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseReceived() {
                Toast.makeText(getActivity(), getString(R.string.joined_meeting), Toast.LENGTH_SHORT).show();
                updateFeed();
            }
        }.execute();
    }
}
