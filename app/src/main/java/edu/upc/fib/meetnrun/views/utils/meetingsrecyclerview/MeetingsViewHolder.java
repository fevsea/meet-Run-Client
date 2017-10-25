package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;


import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Meeting;

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
        char letter = meeting.getTitle().charAt(0);
        String firstLetter = String.valueOf(letter);
        userPhoto.setBackground(getColoredCircularShape((letter)));
        userPhoto.setText(firstLetter);

        TextView userName = view.findViewById(R.id.meeting_item_title);
        userName.setText(meeting.getTitle());

        TextView meetingLocation = view.findViewById(R.id.meeting_item_description);
        meetingLocation.setText(meeting.getDescription());

        TextView meetingLevel = view.findViewById(R.id.meeting_item_level);
        String level = String.valueOf(meeting.getLevel());
        if (level.equals("null")) level = "0";
        meetingLevel.setText(String.valueOf(level));

        TextView meetingDate = view.findViewById(R.id.meeting_item_date);
        String datetime = meeting.getDate();
        meetingDate.setText(datetime.substring(0,datetime.indexOf('T')));

        TextView meetingTime = view.findViewById(R.id.meeting_item_time);
        meetingTime.setText(datetime.substring(datetime.indexOf('T')+1,datetime.indexOf('Z')));

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
