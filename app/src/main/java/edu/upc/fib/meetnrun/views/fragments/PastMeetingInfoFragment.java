package edu.upc.fib.meetnrun.views.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.ProfileViewPagerFragment;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.UsersAdapter;

public class PastMeetingInfoFragment extends BaseFragment implements OnMapReadyCallback
        {
    private View view;
    private ArrayList<LatLng> path;
    private GoogleMap map;
    private UsersAdapter participantsAdapter;
    private IMeetingAdapter meetingController;
    private IFriendsAdapter friendsController;
    private List<Friend> friends;
    private int meetingId;
    private Marker marker;


            @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_meeting_info,container,false);
        this.view = view;

        FloatingActionButton fab =
                getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.INVISIBLE);

        meetingController = CurrentSession.getInstance().getMeetingAdapter();
        friendsController = CurrentSession.getInstance().getFriendsAdapter();
        Bundle pastMeetingInfo = getActivity().getIntent().getExtras();


        TextView title = view.findViewById(R.id.meeting_info_title);
        TextView level = view.findViewById(R.id.meeting_info_level);
        TextView description = view.findViewById(R.id.meeting_info_description);
        TextView date = view.findViewById(R.id.meeting_info_date);
        TextView time = view.findViewById(R.id.meeting_info_time);
        TextView owner = view.findViewById(R.id.meeting_info_creator);

        TextView distance = view.findViewById(R.id.meeting_info_distance);
        TextView steps = view.findViewById(R.id.meeting_info_steps);
        TextView totalTime = view.findViewById(R.id.meeting_info_totaltime);
        TextView avSpeed = view.findViewById(R.id.meeting_info_avspeed);
        TextView calories = view.findViewById(R.id.meeting_info_calories);


        meetingId = pastMeetingInfo.getInt("id");

        title.setText(pastMeetingInfo.getString("title"));
        owner.setText(pastMeetingInfo.getString("owner"));
        description.setText(pastMeetingInfo.getString("description"));
        String levelValue = pastMeetingInfo.getString("level");
        if(levelValue.equals("null")) levelValue = "0";
        level.setText(levelValue);
        date.setText(pastMeetingInfo.getString("date"));
        time.setText(pastMeetingInfo.getString("time"));

        String distanceValue = pastMeetingInfo.getString("distance") + " " + "m"; //TODO parse a km
        distance.setText(distanceValue);
        String stepsValue  = pastMeetingInfo.getString("steps");
        steps.setText(stepsValue);
        String timeValue = pastMeetingInfo.getString("totaltime") + " " + "ms";
        totalTime.setText(timeValue);
        String avSpeedValue = pastMeetingInfo.getString("avspeed") + " " + "m/s";
        avSpeed.setText(avSpeedValue);
        String caloriesValue = pastMeetingInfo.getString("calories") + " " + "kcal";
        calories.setText(caloriesValue);


        setupRecyclerView();

        path = (ArrayList<LatLng>) pastMeetingInfo.get("path");


        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.past_meeting_info_map, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        return view;
    }


    private void setupRecyclerView() {

        final RecyclerView friendsList = view.findViewById(R.id.fragment_friends_container);
        friendsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<User> users = new ArrayList<>();
        getParticipantsList();

        participantsAdapter = new UsersAdapter(users, new RecyclerViewOnClickListener() {
                    @Override
                    public void onButtonClicked(int position) {}

                    @Override
                    public void onItemClicked(int position) {
                        User participant = participantsAdapter.getFriendAtPosition(position);
                        Intent profileIntent;
                        if (participant.getId().equals(CurrentSession.getInstance().getCurrentUser().getId())) {
                            profileIntent = new Intent(getActivity(),ProfileViewPagerFragment.class);
                            startActivity(profileIntent);
                        }
                        else {
                            boolean isFriend = false;
                            Fragment frag;
                            for (Friend f : friends) {
                                User friend = f.getFriend();
                                if (CurrentSession.getInstance().getCurrentUser().getUsername().equals(friend.getUsername())) friend = f.getUser();
                                if (participant.getId().equals(friend.getId())) isFriend = true;
                            }
                            if (isFriend) {
                                profileIntent = new Intent();
                                frag = new FriendProfileFragment();
                            }
                            else {
                                profileIntent = new Intent();
                                frag = new UserProfileFragment();
                            }
                            profileIntent.putExtra("id",participant.getId().toString());
                            profileIntent.putExtra("userName", participant.getUsername());
                            String name = participant.getFirstName() + " " + participant.getLastName();
                            profileIntent.putExtra("name", name);
                            profileIntent.putExtra("postCode", participant.getPostalCode());
                            BaseActivity.startWithFragment(getActivity(), frag, profileIntent);
                        }

                    }
                }, getContext());
                friendsList.setAdapter(participantsAdapter);


            }

            @Override
            public void onResume() {
                getParticipantsList();
                super.onResume();
            }

            private void getParticipantsList() {
                new PastMeetingInfoFragment.getParticipants().execute(meetingId);
                new PastMeetingInfoFragment.getFriends().execute(CurrentSession.getInstance().getCurrentUser().getId().toString());
            }

            private class getParticipants extends AsyncTask<Integer,String,String> {

                private List<User> l = new ArrayList<>();

                @Override
                protected String doInBackground(Integer... integers) {
                    //TODO handle exceptions
                    try {
                        l = meetingController.getParticipantsFromMeeting(integers[0],0);//TODO arreglar paginas
                    } catch (AuthorizationException | ParamsException e) {
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

            private class getFriends extends AsyncTask<String,String,String> {

                @Override
                protected String doInBackground(String... strings) {

                    try {
                        friends = friendsController.listUserAcceptedFriends(CurrentSession.getInstance().getCurrentUser().getId(), 0);
                    } catch (AuthorizationException e) {
                        e.printStackTrace();
                    } catch (NotFoundException e) {
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

        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(path.get(0), 15);

        if(path.size() > 1) {
            camera = CameraUpdateFactory.newLatLngZoom(path.get((path.size()/2) + 1),13);
        }
        map.moveCamera(camera);

        marker = map.addMarker(new MarkerOptions().position(path.get(0)).title("Start"));
        if(path.size() > 1) marker = map.addMarker(new MarkerOptions().position(path.get(path.size() - 1)).title("End"));

        PolylineOptions options = new PolylineOptions();

        options.addAll(path);

        map.addPolyline(options);

    }

    public int getTitle() {
        return R.string.meeting;
    }
}
