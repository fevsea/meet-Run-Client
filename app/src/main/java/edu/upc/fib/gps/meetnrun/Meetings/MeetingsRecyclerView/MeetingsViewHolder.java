package edu.upc.fib.gps.meetnrun.Meetings.MeetingsRecyclerView;


import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import edu.upc.fib.gps.meetnrun.R;
import edu.upc.fib.gps.meetnrun.models.Meeting;

public class MeetingsViewHolder extends RecyclerView.ViewHolder{


    public View view;

    public MeetingsViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    public void bindMeeting(Meeting meeting) {
        TextView userPhoto = view.findViewById(R.id.meeting_item_user_photo);
        userPhoto.setBackground(getColoredCircularShape());
        userPhoto.setText("A");
        TextView userName = view.findViewById(R.id.meeting_item_user_name);
        userName.setText("User name here");
        TextView meetingLocation = view.findViewById(R.id.meeting_item_location);
        meetingLocation.setText("Meeting location here");
        TextView meetingLevel = view.findViewById(R.id.meeting_item_level);
        meetingLevel.setText("10");
        TextView meetingDate = view.findViewById(R.id.meeting_item_date);
        meetingDate.setText("1/1/2020");
        TextView meetingTime = view.findViewById(R.id.meeting_item_time);
        meetingTime.setText("10:30");
        //TODO once the meeting model real (db connected), bind the views to its values
    }

    private GradientDrawable getColoredCircularShape() {
        int[] colors = view.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(view.getContext(),R.drawable.user_profile_circular_text_view);
        circularShape.setColor(colors[new Random().nextInt(colors.length)]);
        return circularShape;
    }
}
