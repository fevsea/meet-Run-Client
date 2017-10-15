package edu.upc.fib.gps.meetnrun.Meetings.MeetingsRecyclerView;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import edu.upc.fib.gps.meetnrun.R;

public class MeetingsViewHolder extends RecyclerView.ViewHolder{


    public View view;

    public MeetingsViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    public void bindMeeting(Meeting meeting) {
        ImageView userPhoto = view.findViewById(R.id.meeting_item_user_photo);
        userPhoto.setImageResource(R.drawable.ic_menu_camera);
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
        ImageButton addMeeting = view.findViewById(R.id.meeting_item_meet);
        addMeeting.setImageResource(R.drawable.ic_add);
        //TODO once the meeting model is created, bind the views to its values
    }
}
