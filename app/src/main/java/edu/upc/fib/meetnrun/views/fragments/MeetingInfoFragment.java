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
import edu.upc.fib.meetnrun.asynctasks.GetAllFriends;
import edu.upc.fib.meetnrun.asynctasks.GetChat;
import edu.upc.fib.meetnrun.asynctasks.GetMeeting;
import edu.upc.fib.meetnrun.asynctasks.GetMeetings;
import edu.upc.fib.meetnrun.asynctasks.GetMyMeetings;
import edu.upc.fib.meetnrun.asynctasks.GetParticipants;
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

        friends = new ArrayList<>();
        callGetAllFriends();
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
        callGetMeeting(meetingId);
        callGetChat(chatId);
        callGetMyMeetings(CurrentSession.getInstance().getCurrentUser().getId());
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


    private void callGetParticipants(int meetingId) {
        setLoading();
        GetParticipants getParticipants = new GetParticipants(pageNumber) {
            @Override
            public void onResponseReceived(List<User> users) {
                meetingUsers = users;
                updateData();
            }
        };
        try {
            getParticipants.execute(meetingId);
        }
        catch (AuthorizationException e) {
            Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
            dismissProgressBarsOnError();
        }
        catch (ParamsException e) {
            Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
            dismissProgressBarsOnError();
        }
    }

    private void callGetAllFriends() {
        GetAllFriends getAllFriends = new GetAllFriends() {
            @Override
            public void onResponseReceived(List<Friend> allfriends) {
                friends = allfriends;
            }
        };
        try {
            getAllFriends.execute();
        }
        catch (AuthorizationException e) {
            Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
        }
        catch (NotFoundException e) {
            Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
        }
    }

    private void callGetMeeting(int meetingId) {
        GetMeeting getMeeting = new GetMeeting() {
            @Override
            public void onResponseReceived(Meeting meeting) {
                List<User> owner = new ArrayList<>();
                owner.add(meeting.getOwner());
                participantsAdapter.addFriends(owner);
            }
        };
        try {
            getMeeting.execute(meetingId);
        }
        catch (NotFoundException e) {
            Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
        }
    }

    private void callGetChat(int chatId) {
        GetChat getChat = new GetChat() {
            @Override
            public void onResponseReceived(Chat responseChat) {
                chat = responseChat;
                isChatAvailable = true;
            }
        };
        try {
            getChat.execute(chatId);
        }
        catch (AuthorizationException e) {
            Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
        }
        catch (NotFoundException e) {
            Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
        }
    }


    private void callGetMyMeetings(int userId) {
        GetMyMeetings getMyMeetings = new GetMyMeetings() {

            @Override
            public void onResponseReceived(List<Meeting> myMeetings) {
                for (Meeting joinedMeeting: myMeetings) {
                    if (joinedMeeting.getId().equals(meetingId)) chatButton.setVisibility(View.VISIBLE);
                }
            }
        };
        try {
            getMyMeetings.execute(userId);
        }
        catch (AuthorizationException e) {
            Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
        }
        catch (ParamsException e) {
            Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
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
