package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Meeting;


public class MyMeetingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private View view;
    private WeakReference<RecyclerViewOnClickListener> listener;
    private ImageButton startMeetingButton;

    public MyMeetingsViewHolder(View itemView, RecyclerViewOnClickListener listener) {
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
        char letter = meeting.getTitle().charAt(0);
        String firstLetter = String.valueOf(letter);
        userIcon.setBackground(getColoredCircularShape((letter)));
        userIcon.setText(firstLetter);
        userIcon1.setBackground(getColoredCircularShape((letter)));
        userIcon1.setText(firstLetter);
        userIcon2.setBackground(getColoredCircularShape((letter)));
        userIcon2.setText(firstLetter);
        userIcon3.setBackground(getColoredCircularShape((letter)));
        userIcon3.setText(firstLetter);

        //TODO if there are more than 4 users in the meeting, add smth like +3,+10... in the moreUsers TextView

        TextView userName = view.findViewById(R.id.mymeeting_item_title);
        userName.setText(meeting.getTitle());

        TextView meetingLevel = view.findViewById(R.id.mymeeting_item_level);
        String level = String.valueOf(meeting.getLevel());
        if (level.equals("null")) level = "0";
        meetingLevel.setText(String.valueOf(level));

        TextView meetingDate = view.findViewById(R.id.mymeeting_item_date);
        String datetime = meeting.getDate();
        meetingDate.setText(datetime.substring(0,datetime.indexOf('T')));
        TextView meetingTime = view.findViewById(R.id.mymeeting_item_time);
        meetingTime.setText(datetime.substring(datetime.indexOf('T')+1,datetime.indexOf('Z')));

        startMeetingButton = view.findViewById(R.id.meeting_item_meet);
        startMeetingButton.setOnClickListener(this);

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
        if (view.getId() == startMeetingButton.getId()) {
            listener.get().onButtonClicked(getAdapterPosition());
        }
        else {
            listener.get().onMeetingClicked(getAdapterPosition());
        }
    }
}
