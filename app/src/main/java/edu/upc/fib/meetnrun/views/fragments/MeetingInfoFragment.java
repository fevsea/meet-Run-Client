package edu.upc.fib.meetnrun.views.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.EditMeetingActivity;
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.ProfileViewPagerFragment;
import edu.upc.fib.meetnrun.views.UserProfileActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

public class MeetingInfoFragment extends Fragment implements OnMapReadyCallback {

    private View view;
    private LatLng location;
    private GoogleMap map;
    private Marker marker;
    private FriendsAdapter participantsAdapter;
    private IMeetingAdapter meetingController;
    private IFriendsAdapter friendsController;
    private List<User> meetingUsers;
    private List<User> friends;
    private int meetingId;
    private boolean isLoading;
    private boolean isLastPage;
    private int pageNumber;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_info,container,false);
        this.view = view;

        meetingController = CurrentSession.getInstance().getMeetingAdapter();
        friendsController = CurrentSession.getInstance().getFriendsAdapter();
        Bundle meetingInfo = getActivity().getIntent().getExtras();

        TextView title = view.findViewById(R.id.meeting_info_title);
        TextView level = view.findViewById(R.id.meeting_info_level);
        TextView description = view.findViewById(R.id.meeting_info_description);
        TextView date = view.findViewById(R.id.meeting_info_date);
        TextView time = view.findViewById(R.id.meeting_info_time);
        TextView owner = view.findViewById(R.id.meeting_info_creator);

        meetingId = meetingInfo.getInt("id");
        title.setText(meetingInfo.getString("title"));
        owner.setText(meetingInfo.getString("owner"));
        description.setText(meetingInfo.getString("description"));
        String levelValue = meetingInfo.getString("level");
        if(levelValue.equals("null")) levelValue = "0";
        level.setText(levelValue);
        date.setText(meetingInfo.getString("date"));
        time.setText(meetingInfo.getString("time"));

        fab = getActivity().findViewById(R.id.activity_fab);
        if (CurrentSession.getInstance().getCurrentUser().getId() == meetingInfo.getInt("ownerId")) {

            fab.setImageResource(android.R.drawable.ic_menu_edit);
            fab.setBackgroundColor(0);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editMeetingIntent = new Intent(getActivity(),EditMeetingActivity.class);
                    editMeetingIntent.putExtra("id",meetingId);
                    getActivity().finish();
                    startActivity(editMeetingIntent);
                }
            });
        }
        else {
            fab.setVisibility(View.INVISIBLE);
        }

        progressBar = view.findViewById(R.id.pb_loading);
        initializePagination();

        setupRecyclerView();
        setupScrollView();
        location = new LatLng(Double.parseDouble(meetingInfo.getString("latitude")),Double.parseDouble(meetingInfo.getString("longitude")));

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.meeting_info_map, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        return view;
    }

    private void setupRecyclerView() {

        final RecyclerView friendsList = view.findViewById(R.id.fragment_friends_container);
        layoutManager = new LinearLayoutManager(getActivity());
        friendsList.setLayoutManager(layoutManager);

        meetingUsers = new ArrayList<>();
        participantsAdapter = new FriendsAdapter(meetingUsers, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {
                User participant = participantsAdapter.getFriendAtPosition(position);
                Intent profileIntent;
                if (participant.getId().equals(CurrentSession.getInstance().getCurrentUser().getId())) {
                    profileIntent = new Intent(getActivity(),ProfileViewPagerFragment.class);
                }
                else {
                    boolean isFriend = false;
                    for (User friend : friends) {
                        if (participant.getId().equals(friend.getId())) isFriend = true;
                    }
                    if (isFriend) {
                        profileIntent = new Intent(getActivity(), FriendProfileActivity.class);
                    }
                    else {
                        profileIntent = new Intent(getActivity(), UserProfileActivity.class);
                    }
                    profileIntent.putExtra("id",participant.getId().toString());
                    profileIntent.putExtra("userName", participant.getUsername());
                    String name = participant.getFirstName() + " " + participant.getLastName();
                    profileIntent.putExtra("name", name);
                    profileIntent.putExtra("postCode", participant.getPostalCode());
                }
                startActivity(profileIntent);

            }
        }, getContext(), false);

        friendsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        getParticipantsList();
                    }
                    else {
                        fab.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        getParticipantsList();

        friendsList.setAdapter(participantsAdapter);



    }


    private void initializePagination() {
        pageNumber = 0;
        isLoading = false;
        isLastPage = false;
    }

    private void setupScrollView() {
        final ScrollView scroll = view.findViewById(R.id.meeting_info_scroll);
        ImageView transparent = view.findViewById(R.id.meeting_info_imagetrans);
        transparent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        scroll.requestDisallowInterceptTouchEvent(true);
                        return false;

                    case MotionEvent.ACTION_UP:
                        scroll.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scroll.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    private void getParticipantsList() {
        new getParticipants().execute(meetingId);
        new getFriends().execute(CurrentSession.getInstance().getCurrentUser().getId().toString());
    }

    private void setLoading() {
        progressBar.setVisibility(View.VISIBLE);
        isLoading = true;
    }

    private void updateData() {
        Log.e("MEETINGINFO","LIST = " + meetingUsers.toString());
        if (meetingUsers != null) {
            if (pageNumber != 0) {

                participantsAdapter.updateFriendsList(meetingUsers);
            }
            else {
                participantsAdapter.addFriends(meetingUsers);
            }

            if (meetingUsers.size() == 0) {
                isLastPage = true;
            }
            else pageNumber++;
        }
        isLoading = false;
        progressBar.setVisibility(View.INVISIBLE);
    }


    private class getParticipants extends AsyncTask<Integer,String,String> {


        @Override
        protected void onPreExecute() {
            setLoading();
            Log.e("MEETINGINFO","ID = " + meetingId);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            //TODO handle exceptions
            try {
                meetingUsers = meetingController.getParticipantsFromMeeting(integers[0],pageNumber);//TODO arreglar paginas
            } catch (AutorizationException | ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            updateData();
            super.onPostExecute(s);
        }
    }

    private class getFriends extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                friends = friendsController.getUserFriends(0); //TODO arreglar paginas
            } catch (AutorizationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        marker = map.addMarker(new MarkerOptions().position(location).title("Meeting"));
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(location,15);
        map.moveCamera(camera);
        marker.remove();
        marker = map.addMarker(new MarkerOptions().position(location).title("Meeting"));
    }
}