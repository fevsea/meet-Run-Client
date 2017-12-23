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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.ProfileViewPagerFragment;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.UsersAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

public class MeetingInfoFragment extends BaseFragment implements OnMapReadyCallback {

    private View view;
    private LatLng location;
    private GoogleMap map;
    private Marker marker;
    private UsersAdapter participantsAdapter;
    private IMeetingAdapter meetingController;
    private IFriendsAdapter friendsController;
    private IUserAdapter userAdapter;
    private IChatAdapter chatAdapter;
    private List<User> meetingUsers;
    private List<Friend> friends;
    private int meetingId;
    private boolean isLoading;
    private boolean isLastPage;
    private int pageNumber;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton fab;
    private Meeting meeting;
    private int chatId;
    private Chat chat;
    private boolean isChatAvailable;
    private List<Meeting> myMeetings;
    private ImageButton chatButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_info,container,false);
        this.view = view;

        meetingController = CurrentSession.getInstance().getMeetingAdapter();
        friendsController = CurrentSession.getInstance().getFriendsAdapter();
        userAdapter = CurrentSession.getInstance().getUserAdapter();
        chatAdapter = CurrentSession.getInstance().getChatAdapter();
        Bundle meetingInfo = getActivity().getIntent().getExtras();

        TextView title = view.findViewById(R.id.meeting_info_title);
        TextView level = view.findViewById(R.id.meeting_info_level);
        TextView description = view.findViewById(R.id.meeting_info_description);
        TextView date = view.findViewById(R.id.meeting_info_date);
        TextView time = view.findViewById(R.id.meeting_info_time);
        TextView owner = view.findViewById(R.id.meeting_info_creator);
        chatButton = view.findViewById(R.id.meeting_info_chat);
        chatButton.setVisibility(View.INVISIBLE);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChatView();
            }
        });
        meetingId = meetingInfo.getInt("id");
        chatId = meetingInfo.getInt("chat");
        isChatAvailable = false;
        new getMeeting().execute();
        new getChat().execute();
        new GetMyMeetings().execute(CurrentSession.getInstance().getCurrentUser().getId());
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
                    Intent editMeetingIntent = new Intent();
                    editMeetingIntent.putExtra("id",meetingId);
                    BaseActivity.startWithFragment(getActivity(), new EditMeetingFragment(), editMeetingIntent);
                    getActivity().finish();
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

        participantsAdapter = new UsersAdapter(meetingUsers, new RecyclerViewOnClickListener() {
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
                    Fragment frag;
                    boolean isFriend = false;
                    for (Friend f : friends) {
                        User friend = f.getFriend();
                        if (CurrentSession.getInstance().getCurrentUser().getUsername().equals(friend.getUsername())) friend = f.getUser();
                        if (participant.getId().equals(friend.getId())) isFriend = true;
                    }
                    CurrentSession.getInstance().setFriend(participant);
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

    private void openChatView() {
        if (isChatAvailable){
            Intent chatIntent = new Intent();
            CurrentSession.getInstance().setChat(chat);
            BaseActivity.startWithFragment(getActivity(), new ChatFragment(), chatIntent);
        }
        else {
            Toast.makeText(getActivity(),R.string.chat_not_available,Toast.LENGTH_LONG).show();
        }
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
            } catch (AuthorizationException | ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("MEETINGUSERS",meetingUsers.toString());
            updateData();
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

    private class getMeeting extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                meeting = meetingController.getMeeting(meetingId);
                }
            catch (NotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
            List<User> owner = new ArrayList<>();
            owner.add(meeting.getOwner());
            participantsAdapter.addFriends(owner);
        }

    }

    private class getChat extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                chat = chatAdapter.getChat(chatId);
            }
            catch (NotFoundException e) {
                e.printStackTrace();
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
            isChatAvailable = true;
        }

    }

    private class GetMyMeetings extends AsyncTask<Integer,String,Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            //TODO handle exceptions
            try {
                myMeetings = userAdapter.getUsersFutureMeetings(integers[0]);
            } catch (AuthorizationException | ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            for (Meeting joinedMeeting: myMeetings) {
                if (joinedMeeting.getId().equals(meetingId)) chatButton.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("MeetingInfo", "onMapReady");
        this.map = googleMap;
        marker = map.addMarker(new MarkerOptions().position(location).title("Meeting"));
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(location,15);
        map.moveCamera(camera);
        marker.remove();
        marker = map.addMarker(new MarkerOptions().position(location).title("Meeting"));
    }

    public int getTitle() {
        return R.string.meeting;
    }

}
