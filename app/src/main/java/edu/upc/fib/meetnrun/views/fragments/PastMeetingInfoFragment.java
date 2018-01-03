package edu.upc.fib.meetnrun.views.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.GetAllFriends;
import edu.upc.fib.meetnrun.asynctasks.GetAllParticipants;
import edu.upc.fib.meetnrun.asynctasks.GetStaticMap;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.Meeting;
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
    private ImageButton shareTracking;
    private boolean staticMapAvailable;
    private Bitmap staticMap;
    private File cachedFile;
    private String distanceValue;
    private String timeValue;
    private String avSpeedValue;



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

        shareTracking = view.findViewById(R.id.meeting_info_share);

        meetingId = pastMeetingInfo.getInt("id");

        title.setText(pastMeetingInfo.getString("title"));
        owner.setText(pastMeetingInfo.getString("owner"));
        description.setText(pastMeetingInfo.getString("description"));
        String levelValue = pastMeetingInfo.getString("level");
        if(levelValue.equals("null")) levelValue = "0";
        level.setText(levelValue);
        date.setText(pastMeetingInfo.getString("date"));
        time.setText(pastMeetingInfo.getString("time"));

        distanceValue = pastMeetingInfo.getString("distance") + " " + "m"; //TODO parse a km
        distance.setText(distanceValue);
        String stepsValue  = pastMeetingInfo.getString("steps");
        steps.setText(stepsValue);
        timeValue = pastMeetingInfo.getString("totaltime") + " " + "ms";
        totalTime.setText(timeValue);
        avSpeedValue = pastMeetingInfo.getString("avspeed") + " " + "m/s";
        avSpeed.setText(avSpeedValue);
        String caloriesValue = pastMeetingInfo.getString("calories") + " " + "kcal";
        calories.setText(caloriesValue);

        shareTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTracking();
            }
        });

        path = (ArrayList<LatLng>) pastMeetingInfo.get("path");
        if (path != null) {
            staticMapAvailable = false;
            if (path.size() > 1) {
                Log.d("STATIC MAP", path.toString());
                callGetStaticMap();
            }
        }

        setupRecyclerView();


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
        callGetAllParticipants(meetingId);
        callGetAllFriends();
    }

    private void callGetAllFriends() {
        new GetAllFriends() {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof NotFoundException) {
                    Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseReceived(List<Friend> allfriends) {
                friends = allfriends;
            }
        }.execute();
    }

    private void callGetAllParticipants(int meetingId) {
        new GetAllParticipants() {
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
            public void onResponseReceived(List<User> users) {
                participantsAdapter.updateFriendsList(users);
            }
        }.execute(meetingId);
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

    private void callGetStaticMap() {
        new GetStaticMap(path, getString(R.string.api_static_maps_key)) {
            @Override
            public void onResponseReceived(Bitmap bitmap) {
                staticMap = bitmap;
                saveImageToCache();
                staticMapAvailable = true;
            }
        }.execute();
    }

    private void saveImageToCache() {
        cachedFile = new File(getContext().getExternalCacheDir(), "trackingmap.png");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(cachedFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        staticMap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cachedFile.setReadable(true, false);
    }

    private void shareTracking() {
        String trackingStats = formatTrackingStats();

        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, trackingStats);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (staticMapAvailable) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cachedFile));
            intent.setType("image/png");
            Log.e("AAAAAAA",Uri.fromFile(cachedFile).toString());
        }
        else {
            intent.setType("text/plain");
            Log.e("AAAAAA","BB");
        }
        startActivityForResult(Intent.createChooser(intent, getString(R.string.share_via)),1);
    }

    private String formatTrackingStats() {
        return  getString(R.string.share_text) + "\n" +
                getString(R.string.distance)+ ": " + distanceValue + "\n" +
                getString(R.string.time) + ": " + timeValue + "\n" +
                getString(R.string.avg_speed) + ": " + avSpeedValue + "\n";
    }

}
