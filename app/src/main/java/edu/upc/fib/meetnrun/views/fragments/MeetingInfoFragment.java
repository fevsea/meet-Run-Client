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
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.EditMeetingActivity;
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

public class MeetingInfoFragment extends Fragment implements OnMapReadyCallback {

    private View view;
    private LatLng location;
    private GoogleMap map;
    private Marker marker;
    private FriendsAdapter participantsAdapter;
    private IMeetingAdapter controller;
    private int meetingId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_info,container,false);
        this.view = view;

        controller = CurrentSession.getInstance().getMeetingAdapter();
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

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
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

        setupRecyclerView();
        setupScrollView();
       // TODO location = new LatLng(Double.parseDouble(meetingInfo.getString("latitude")),Double.parseDouble(meetingInfo.getString("longitude")));

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
        friendsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<User> users = new ArrayList<User>();
        getParticipantsList();

        participantsAdapter = new FriendsAdapter(users, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onMeetingClicked(int position) {

                User particopant = participantsAdapter.getFriendAtPosition(position);
                Intent friendProfileIntent = new Intent(getActivity(),FriendProfileActivity.class);

                friendProfileIntent.putExtra("userName",particopant.getUsername());
                String name = particopant.getFirstName()+" "+particopant.getLastName();
                friendProfileIntent.putExtra("name",name);
                friendProfileIntent.putExtra("postCode",particopant.getPostalCode());
                startActivity(friendProfileIntent);

            }
        });
        friendsList.setAdapter(participantsAdapter);

    }

    private void setupScrollView() {
        final ScrollView scroll = (ScrollView) view.findViewById(R.id.meeting_info_scroll);
        ImageView transparent = (ImageView)view.findViewById(R.id.meeting_info_imagetrans);
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
    }

    private class getParticipants extends AsyncTask<Integer,String,String> {

        private List<User> l = new ArrayList<>();

        @Override
        protected String doInBackground(Integer... integers) {
            //TODO handle exceptions
            try {
                l = controller.getParticipantsFromMeeting(integers[0]);
            } catch (AutorizationException e) {
                e.printStackTrace();
            } catch (ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            participantsAdapter.updateFriendsList(l);
            super.onPostExecute(s);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        location = new LatLng(40,30);
        marker = map.addMarker(new MarkerOptions().position(location).title("Meeting"));
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(location,15);
        map.moveCamera(camera);
        marker.remove();
        marker = map.addMarker(new MarkerOptions().position(location).title("Meeting"));
    }
}