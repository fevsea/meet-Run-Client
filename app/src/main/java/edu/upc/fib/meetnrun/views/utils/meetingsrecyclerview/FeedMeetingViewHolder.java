package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.os.Bundle;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.FeedMeeting;
import edu.upc.fib.meetnrun.utils.UtilsViews;

public class FeedMeetingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnMapReadyCallback, View.OnTouchListener {

    private RecyclerViewOnClickListener listener;
    private View view;
    private GoogleMap map;
    private FeedMeeting meeting;

    public FeedMeetingViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        this.view = itemView;
        this.listener = listener;
    }


    public void bindMeeting(FeedMeeting meeting) {
        this.meeting = meeting;
        TextView userIcon = view.findViewById(R.id.meeting_user_icon);
        TextView userName = view.findViewById(R.id.action_username);
        TextView actionText = view.findViewById(R.id.action_text);

        TextView joinLabel = view.findViewById(R.id.meeting_join_label);
        ImageButton joinButton = view.findViewById(R.id.meeting_join);

        TextView titleView = view.findViewById(R.id.meeting_title);
        TextView dateView = view.findViewById(R.id.meeting_date);
        TextView timeView = view.findViewById(R.id.meeting_time);

        MapView mapView = view.findViewById(R.id.meeting_map);

        joinButton.setOnTouchListener(this);

        if (meeting.getFriend() != null) {
            char userNameFirstLetter = meeting.getFriend().getUsername().charAt(0);
            userIcon.setText(String.valueOf(userNameFirstLetter));
            userIcon.setBackground(UtilsViews.getColoredCircularShape(userNameFirstLetter, view));
            userName.setText(meeting.getFriend().getUsername());
        }

        switch (meeting.getType()) {
            case FeedMeeting.FUTURE_NEAR:
                userIcon.setVisibility(View.GONE);
                userName.setVisibility(View.GONE);
                actionText.setText(R.string.feed_meeting_near);
                break;
            case FeedMeeting.FUTURE_FRIEND_CREATED:
                actionText.setText(R.string.feed_meeting_created);
                break;
            case FeedMeeting.FUTURE_FRIEND_JOINED:
                actionText.setText(R.string.feed_meeting_joined);
                break;
            case FeedMeeting.PAST_FRIEND:
                actionText.setText(R.string.feed_meeting_past);
                joinButton.setOnTouchListener(null);
                joinButton.setVisibility(View.GONE);
                joinLabel.setVisibility(View.GONE);
                break;
        }

        String dateTime = meeting.getMeeting().getDate();
        dateView.setText(dateTime.substring(0,dateTime.indexOf('T')));
        timeView.setText(dateTime.substring(dateTime.indexOf('T')+1,dateTime.indexOf('T')+9));

        String title = meeting.getMeeting().getTitle();
        titleView.setText(title);

        mapView.onCreate(new Bundle());
        mapView.setClickable(false);
        mapView.getMapAsync(this);
        mapView.onResume();

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onItemClicked(getAdapterPosition());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        LatLng position = new LatLng(Double.valueOf(meeting.getMeeting().getLatitude()), Double.valueOf(meeting.getMeeting().getLongitude()));
        Marker marker = map.addMarker(new MarkerOptions().position(position).title("Meeting"));
        CameraUpdate camera;
        if (meeting.getType() == FeedMeeting.PAST_FRIEND) {
            map.getUiSettings().setAllGesturesEnabled(true);
            List<LatLng> path = meeting.getTracking().getRoutePoints();

            marker = map.addMarker(new MarkerOptions().position(path.get(0)).title("Start"));
            if(path.size() > 1) {
                marker = map.addMarker(new MarkerOptions().position(path.get(path.size() - 1)).title("End"));
            }

            PolylineOptions options = new PolylineOptions();
            options.addAll(path);
            map.addPolyline(options);

            if(path.size() > 2) {
                camera = CameraUpdateFactory.newLatLngZoom(path.get((path.size()/2) + 1),13);
            }
            else {
                camera = CameraUpdateFactory.newLatLngZoom(path.get(0), 13);
            }
        }
        else {
            map.getUiSettings().setAllGesturesEnabled(false);
            camera = CameraUpdateFactory.newLatLngZoom(position, 13);
        }
        map.moveCamera(camera);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.meeting_join && event.getAction() == MotionEvent.ACTION_DOWN) {
            listener.onButtonClicked(getAdapterPosition());
        }
        return true;
    }
}
