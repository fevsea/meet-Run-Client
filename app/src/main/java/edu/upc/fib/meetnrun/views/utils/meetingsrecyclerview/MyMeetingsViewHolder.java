package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.utils.UtilsGlobal;


public class MyMeetingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnMapReadyCallback, View.OnTouchListener{

    private final View view;
    private final WeakReference<MyMeetingsListener> listener;
    private ImageButton startMeetingButton;
    private ImageButton leaveMeetingButton;
    private LatLng location;
    private GoogleMap map;
    private Marker marker;

    private TextView userIcon;
    private TextView userIcon1;
    private TextView userIcon2;
    private TextView userIcon3;
    private TextView moreUsers;
    private TextView userName;
    private TextView meetingDate;
    private TextView meetingTime;
    private TextView startMeetingLabel;
    private MapView mapView;

    public MyMeetingsViewHolder(View itemView, MyMeetingsListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);

        userIcon = view.findViewById(R.id.mymeeting_item_user_icon);
        userIcon1 = view.findViewById(R.id.mymeeting_item_user_icon1);
        userIcon2 = view.findViewById(R.id.mymeeting_item_user_icon2);
        userIcon3 = view.findViewById(R.id.mymeeting_item_user_icon3);
        moreUsers = view.findViewById(R.id.mymeeting_item_more_users);
        userName = view.findViewById(R.id.mymeeting_item_title);
        meetingDate = view.findViewById(R.id.mymeeting_item_date);
        meetingTime = view.findViewById(R.id.mymeeting_item_time);
        startMeetingButton = view.findViewById(R.id.mymeeting_item_start);
        startMeetingLabel = view.findViewById(R.id.mymeeting_start_label);
        leaveMeetingButton = view.findViewById(R.id.mymeeting_item_leave);
        mapView = view.findViewById(R.id.mymeeting_info_map);

    }

    public void bindMeeting(Meeting meeting) {


        userIcon1.setVisibility(View.GONE);
        userIcon2.setVisibility(View.GONE);
        userIcon3.setVisibility(View.GONE);
        char letter = meeting.getOwner().getFirstName().charAt(0);
        String firstLetter = String.valueOf(letter);
        userIcon.setBackground(getColoredCircularShape((letter)));
        userIcon.setText(firstLetter);
        List<User> participants = meeting.getParticipants();
        int size = participants.size();
        if (size > 4) size = 4;
        for (int i = 0; i < size; ++i) {
            User participant = participants.get(i);
            letter = participant.getFirstName().charAt(0);
            firstLetter = String.valueOf(letter);
            if (i == 0) {
                userIcon1.setVisibility(View.VISIBLE);
                userIcon1.setBackground(getColoredSmallCircularShape((letter)));
                userIcon1.setText(firstLetter);
            }
            else if (i == 1) {
                userIcon2.setVisibility(View.VISIBLE);
                userIcon2.setBackground(getColoredSmallCircularShape((letter)));
                userIcon2.setText(firstLetter);
            }
            else if (i == 2) {
                userIcon3.setVisibility(View.VISIBLE);
                userIcon3.setBackground(getColoredSmallCircularShape((letter)));
                userIcon3.setText(firstLetter);
            }
            else {
                int moreParticipants = participants.size() - 3;
                moreUsers.setText("+" + moreParticipants);
            }
        }
        userName.setText(meeting.getTitle());

        String datetime = meeting.getDate();
        meetingDate.setText(datetime.substring(0,datetime.indexOf('T')));
        meetingTime.setText(datetime.substring(datetime.indexOf('T')+1,datetime.indexOf('T')+9));

        location = new LatLng(Double.parseDouble(meeting.getLatitude()),Double.parseDouble(meeting.getLongitude()));

        if (meeting.getOwner().getId().equals(CurrentSession.getInstance().getCurrentUser().getId())) {
            leaveMeetingButton.setVisibility(View.INVISIBLE);
        }
        else leaveMeetingButton.setOnClickListener(this);

        mapView.onCreate(new Bundle());
        mapView.setClickable(false);
        mapView.getMapAsync(this);
        mapView.onResume();

        if (isMeetingAvailable(meeting.getDate())) {
            startMeetingButton.setEnabled(true);
            startMeetingButton.setImageAlpha(255);
            startMeetingButton.setOnTouchListener(this);
            startMeetingLabel.setText(view.getResources().getString(R.string.start_meeting_label));
        }
        else {
            startMeetingButton.setEnabled(false);
            startMeetingButton.setImageAlpha(45);
            startMeetingLabel.setText(view.getResources().getString(R.string.not_start_meeting_label));
        }

        view.setOnClickListener(this);
    }

    private boolean isMeetingAvailable(String dateText) {
        Date date = null;
        date = UtilsGlobal.parseDate(dateText);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,-20);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        date = calendar.getTime();
        Date currentDate = Calendar.getInstance().getTime();
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        return currentDate.after(date) && (day == currentDay);
    }

    private GradientDrawable getColoredCircularShape(char letter) {
        int[] colors = view.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(view.getContext(),R.drawable.user_profile_circular_text_view);
        int position = letter%colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }

    private GradientDrawable getColoredSmallCircularShape(char letter) {
        int[] colors = view.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(view.getContext(),R.drawable.user_circular_text_view_small);
        int position = letter%colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == leaveMeetingButton.getId()) {
            listener.get().onLeaveClicked(getAdapterPosition());
        }
        else {
            listener.get().onMeetingClicked(getAdapterPosition());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.getUiSettings().setAllGesturesEnabled(false);
        marker = map.addMarker(new MarkerOptions().position(location).title("Meeting"));
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(location,13);
        map.moveCamera(camera);
        marker.remove();
        marker = map.addMarker(new MarkerOptions().position(location).title("Meeting"));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == startMeetingButton.getId() && event.getAction() == MotionEvent.ACTION_DOWN) {
            listener.get().onStartClicked(getAdapterPosition());
        }
        return true;
    }
}
