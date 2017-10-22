package edu.upc.fib.gps.meetnrun.Meetings.MeetingsRecyclerView;


import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import edu.upc.fib.gps.meetnrun.R;
import edu.upc.fib.gps.meetnrun.models.Meeting;

public class MeetingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    private View view;
    private WeakReference<RecyclerViewOnClickListener> listener;
    private ImageButton addUserButton;

    public MeetingsViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
    }

    public void bindMeeting(Meeting meeting) {
        TextView userPhoto = view.findViewById(R.id.meeting_item_user_photo);
        char letter = meeting.getCreatorAuthor().charAt(0);
        String firstLetter = String.valueOf(letter);
        userPhoto.setBackground(getColoredCircularShape((letter)));
        userPhoto.setText(firstLetter);

        TextView userName = view.findViewById(R.id.meeting_item_user_name);
        userName.setText(meeting.getCreatorAuthor());

        TextView meetingLocation = view.findViewById(R.id.meeting_item_location);
        meetingLocation.setText(meeting.getDescription());

        TextView meetingLevel = view.findViewById(R.id.meeting_item_level);
        meetingLevel.setText(String.valueOf(meeting.getLevel()));

        TextView meetingDate = view.findViewById(R.id.meeting_item_date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        meetingDate.setText((simpleDateFormat.format(meeting.getDateTime())));

        TextView meetingTime = view.findViewById(R.id.meeting_item_time);
        simpleDateFormat = new SimpleDateFormat("h:mm a");
        meetingTime.setText(simpleDateFormat.format(meeting.getDateTime()));

        addUserButton = view.findViewById(R.id.meeting_item_meet);
        addUserButton.setOnClickListener(this);

        view.setOnClickListener(this);
    }

    private GradientDrawable getColoredCircularShape(char letter) {
        int[] colors = view.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(view.getContext(),R.drawable.user_profile_circular_text_view);
        int position = letter%colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == addUserButton.getId()) {
            listener.get().onButtonClicked(getAdapterPosition());
        }
        else {
            listener.get().onMeetingClicked(getAdapterPosition());
        }
    }
}
