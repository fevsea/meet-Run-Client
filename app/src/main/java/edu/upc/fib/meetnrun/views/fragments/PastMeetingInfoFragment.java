package edu.upc.fib.meetnrun.views.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.GetAllFriends;
import edu.upc.fib.meetnrun.asynctasks.GetAllParticipants;
import edu.upc.fib.meetnrun.asynctasks.GetMeeting;
import edu.upc.fib.meetnrun.asynctasks.GetMyMeetings;
import edu.upc.fib.meetnrun.asynctasks.GetParticipants;
import edu.upc.fib.meetnrun.asynctasks.GetPastMeetingsTracking;
import edu.upc.fib.meetnrun.asynctasks.GetStaticMap;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.TrackingData;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.ProfileViewPagerFragment;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.UsersAdapter;

public class PastMeetingInfoFragment extends BaseFragment implements OnMapReadyCallback
        {
    private View view;
    private GoogleMap map;
    private UsersAdapter participantsAdapter;
    private LinearLayoutManager layoutManager;
            private List<User> meetingUsers;
            private List<Friend> friends;
    private Marker marker;
    private ImageButton shareTracking;
    private boolean staticMapAvailable;
    private Bitmap staticMap;
    private File cachedFile;
    private String distanceValue;
    private String timeValue;
    private String avSpeedValue;
    private int userId;
    private int meetingId;
    private TrackingData tracking;
    private ArrayList<LatLng> path;
    private ProgressBar progressBar;
            private Intent profileIntent;
            private Fragment frag;
            private FloatingActionButton fab;
            private boolean isLoading;
            private boolean isLastPage;
            private int pageNumber;




            @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_meeting_info,container,false);
        this.view = view;
        setHasOptionsMenu(true);

        friends = new ArrayList<>();
        callGetAllFriends();

        fab = getActivity().findViewById(R.id.activity_fab);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        FloatingActionButton fab =
                getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.INVISIBLE);
        progressBar = view.findViewById(R.id.pb_loading);

        Bundle pastMeetingInfo = getActivity().getIntent().getExtras();
        userId = pastMeetingInfo.getInt("userId");
        meetingId = pastMeetingInfo.getInt("meetingId");
        getTrackingData();
        TextView title = view.findViewById(R.id.meeting_info_title);
        TextView level = view.findViewById(R.id.meeting_info_level);
        TextView description = view.findViewById(R.id.meeting_info_description);
        TextView date = view.findViewById(R.id.meeting_info_date);
        TextView time = view.findViewById(R.id.meeting_info_time);
        TextView owner = view.findViewById(R.id.meeting_info_creator);

        shareTracking = view.findViewById(R.id.meeting_info_share);
        callGetMeeting(meetingId);
        callGetMyMeetings(CurrentSession.getInstance().getCurrentUser().getId());


        title.setText(pastMeetingInfo.getString("title"));
        owner.setText(pastMeetingInfo.getString("owner"));
        description.setText(pastMeetingInfo.getString("description"));
        String levelValue = pastMeetingInfo.getString("level");
        if(levelValue.equals("null")) levelValue = "0";
        level.setText(levelValue);
        date.setText(pastMeetingInfo.getString("date"));
        time.setText(pastMeetingInfo.getString("time"));


        shareTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTracking();
            }
        });

        initializePagination();
        setupRecyclerView();


        return view;
    }


    private void setupRecyclerView() {

        final RecyclerView friendsList = view.findViewById(R.id.fragment_friends_container);
        layoutManager = new LinearLayoutManager(getActivity());
        friendsList.setLayoutManager(layoutManager);

        meetingUsers = new ArrayList<>();

        participantsAdapter = new UsersAdapter(meetingUsers, new RecyclerViewOnClickListener() {
                    @Override
                    public void onButtonClicked(int position) {}

                    @Override
                    public void onItemClicked(int position) {
                        User participant = participantsAdapter.getFriendAtPosition(position);
                        Intent userProfileIntent = new Intent(getActivity(), ProfileViewPagerFragment.class);
                        if (participant.getId().equals(CurrentSession.getInstance().getCurrentUser().getId())) {
                            userProfileIntent.putExtra("userId",CurrentSession.getInstance().getCurrentUser().getId());
                            userProfileIntent.putExtra("isFriend",false);
                        }
                        else {
                            boolean isFriend = false;
                            for (Friend f : friends) {
                                User friend = f.getFriend();
                                if (CurrentSession.getInstance().getCurrentUser().getUsername().equals(friend.getUsername())) friend = f.getUser();
                                if (participant.getId().equals(friend.getId())) isFriend = true;
                            }
                            CurrentSession.getInstance().setFriend(participant);
                            userProfileIntent.putExtra("userId",participant.getId());
                            userProfileIntent.putExtra("isFriend",isFriend);
                            startActivity(userProfileIntent);
                        }

                    }
                }, getContext());

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

    /*@Override
    public void onResume() {
        getParticipantsList();
        super.onResume();
    }*/

    private void getParticipantsList() {
        callGetParticipants(meetingId);
    }

    private void setLoading() {
       progressBar.setVisibility(View.VISIBLE);
       isLoading = true;
    }

    private void updateData() {
      Log.e("MEETINGINFO","LIST = " + meetingUsers.toString());
      if (meetingUsers != null && meetingUsers.size() != 0) {
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

    private void dismissProgressBarsOnError() {
                progressBar.setVisibility(View.INVISIBLE);
            }

    private void callGetAllFriends() {

        new GetAllFriends() {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                    dismissProgressBarsOnError();
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

    private void callGetParticipants(int meetingId) {
        setLoading();
        new GetParticipants(pageNumber) {
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
                meetingUsers = users;
                updateData();
            }
        }.execute(meetingId);
    }

            private void callGetMeeting(int meetingId) {
                new GetMeeting() {
                    @Override
                    public void onExceptionReceived(GenericException e) {
                        if (e instanceof NotFoundException) {
                            Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onResponseReceived(Meeting meeting) {
                        List<User> owner = new ArrayList<>();
                        owner.add(meeting.getOwner());
                        participantsAdapter.addFriends(owner);
                    }
                }.execute(meetingId);
            }

            private void callGetMyMeetings(int userId) {
                new GetMyMeetings() {

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
                    public void onResponseReceived(List<Meeting> myMeetings) {
                        for (Meeting joinedMeeting: myMeetings) {
                            //if (joinedMeeting.getId().equals(meetingId)) chatButton.setVisibility(View.VISIBLE);
                        }
                    }
                }.execute(userId);
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
                if (staticMap != null) {
                    saveImageToCache();
                    staticMapAvailable = true;
                }
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

    private void getTrackingData() {
        progressBar.setVisibility(View.VISIBLE);
        callGetPastMeetingsTracking(userId,meetingId);
    }

    private void callGetPastMeetingsTracking(int userId, int meetingId) {
        new GetPastMeetingsTracking() {
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
            public void onResponseReceived(TrackingData trackingResponse) {
                tracking = trackingResponse;
                updateTrackingText();
                if (tracking.getRoutePoints() != null) callGetStaticMap();
            }
        }.execute(userId,meetingId);
    }

    private void updateTrackingText() {
        TextView distance = view.findViewById(R.id.meeting_info_distance);
        TextView steps = view.findViewById(R.id.meeting_info_steps);
        TextView totalTime = view.findViewById(R.id.meeting_info_totaltime);
        TextView avSpeed = view.findViewById(R.id.meeting_info_avspeed);
        TextView calories = view.findViewById(R.id.meeting_info_calories);
        distanceValue = String.valueOf(tracking.getDistance()); //TODO parse a km
        distance.setText(distanceValue);
        String stepsValue  = String.valueOf(tracking.getSteps());
        steps.setText(stepsValue);
        timeValue = getTimeInString(tracking.getTotalTimeMillis());
        totalTime.setText(timeValue);
        avSpeedValue = getSpeedInString(tracking.getAverageSpeed());
        avSpeed.setText(avSpeedValue);
        String caloriesValue = String.valueOf(tracking.getCalories());
        calories.setText(caloriesValue);

        path = (ArrayList<LatLng>) tracking.getRoutePoints();
        if (path != null) {
            staticMapAvailable = false;
            if (path.size() > 1) {
                Log.d("STATIC MAP", path.toString());
                callGetStaticMap();
            }
        }

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.past_meeting_info_map, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public String getTimeInString(float time) {
        float hours=time/3600000;
        float mins=(time%3600000)/60000;
        float secs=(time%60000)/1000;
        return String.format("%sh %sm %ss", (int) hours, (int) mins, (int) secs);
    }

    public String getSpeedInString(float speed){
        DecimalFormat df=new DecimalFormat("###.###");
        return String.valueOf(df.format(speed)) + " m/s";
    }

}
