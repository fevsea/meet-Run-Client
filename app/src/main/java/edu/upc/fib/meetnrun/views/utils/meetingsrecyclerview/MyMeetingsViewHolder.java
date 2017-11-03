package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Meeting;


public class MyMeetingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnMapReadyCallback{

    private View view;
    private WeakReference<MyMeetingsListener> listener;
    private ImageButton startMeetingButton;
    private ImageButton leaveMeetingButton;
    private LatLng location;
    private GoogleMap map;
    private Marker marker;

    public MyMeetingsViewHolder(View itemView, MyMeetingsListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
    }

    public void bindMeeting(Meeting meeting) {

        //TODO the Meetings model still needs the users that joines the meeting and the owned of the meeting
        TextView userIcon = view.findViewById(R.id.mymeeting_item_user_icon);
        TextView userIcon1 = view.findViewById(R.id.mymeeting_item_user_icon1);
        TextView userIcon2 = view.findViewById(R.id.mymeeting_item_user_icon2);
        TextView userIcon3 = view.findViewById(R.id.mymeeting_item_user_icon3);
        TextView moreUsers = view.findViewById(R.id.mymeeting_item_more_users);
        String title = meeting.getTitle();
        if (title.equals("null")) title = "";
        char letter = title.charAt(0);
        String firstLetter = String.valueOf(letter);
        userIcon.setBackground(getColoredCircularShape((letter)));
        userIcon.setText(firstLetter);
        //TODO if there are more users in the meeting, set the background, else hide this icons
        userIcon1.setBackground(getColoredSmallCircularShape((letter)));
        userIcon1.setText(firstLetter);
        userIcon2.setBackground(getColoredSmallCircularShape((letter)));
        userIcon2.setText(firstLetter);
        userIcon3.setBackground(getColoredSmallCircularShape((letter)));
        userIcon3.setText(firstLetter);

        //TODO if there are more than 4 users in the meeting, add smth like +3,+10... in the moreUsers TextView
        moreUsers.setText(view.getResources().getString(R.string.vertical_ellipsis));

        TextView userName = view.findViewById(R.id.mymeeting_item_title);
        userName.setText(meeting.getTitle());

        TextView meetingLevel = view.findViewById(R.id.mymeeting_item_level);
        String level = String.valueOf(meeting.getLevel());
        if (level.equals("null") || level.isEmpty()) level = "0";
        meetingLevel.setText(String.valueOf(level));

        TextView meetingDate = view.findViewById(R.id.mymeeting_item_date);
        String datetime = meeting.getDate();
        meetingDate.setText(datetime.substring(0,datetime.indexOf('T')));
        TextView meetingTime = view.findViewById(R.id.mymeeting_item_time);
        meetingTime.setText(datetime.substring(datetime.indexOf('T')+1,datetime.indexOf('Z')));

        location = new LatLng(Double.parseDouble(meeting.getLatitude()),Double.parseDouble(meeting.getLongitude()));

        startMeetingButton = view.findViewById(R.id.mymeeting_item_start);
        TextView startMeetingLabel = view.findViewById(R.id.mymeeting_start_label);

        leaveMeetingButton = view.findViewById(R.id.mymeeting_item_leave);
        leaveMeetingButton.setOnClickListener(this);

        MapView mapView = view.findViewById(R.id.mymeeting_info_map);
        mapView.onCreate(new Bundle());
        mapView.setClickable(false);
        mapView.getMapAsync(this);
        mapView.onResume();

        if (isMeetingAvailable(meeting.getDate())) {
            startMeetingButton.setOnClickListener(this);
            startMeetingLabel.setText(view.getResources().getString(R.string.start_meeting_label));
        }
        else {
            startMeetingButton.setEnabled(false);
            startMeetingLabel.setText(view.getResources().getString(R.string.not_start_meeting_label));
        }

        view.setOnClickListener(this);
    }

    private boolean isMeetingAvailable(String dateText) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = inputFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }

        Date currentDate = Calendar.getInstance().getTime();
        return currentDate.before(date);
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
        if (view.getId() == startMeetingButton.getId()) {
            listener.get().onStartClicked(getAdapterPosition());
        }
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

}