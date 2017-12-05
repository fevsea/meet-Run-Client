package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;


import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;

public class MeetingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    private final View view;
    private final WeakReference<RecyclerViewOnClickListener> listener;
    private ImageButton addUserButton;
    private TextView userIcon;
    private TextView ownerName;
    private TextView meetingTitle;
    private TextView meetingLocation;
    private TextView meetingLevel;
    private TextView meetingDate;
    private TextView meetingTime;

    public MeetingsViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
        userIcon = view.findViewById(R.id.meeting_item_user_icon);
        ownerName = view.findViewById(R.id.meeting_item_owner);
        meetingTitle = view.findViewById(R.id.meeting_item_title);
        meetingLocation = view.findViewById(R.id.meeting_item_description);
        meetingLevel = view.findViewById(R.id.meeting_item_level);
        meetingDate = view.findViewById(R.id.meeting_item_date);
        meetingTime = view.findViewById(R.id.meeting_item_time);
        addUserButton = view.findViewById(R.id.meeting_item_meet);

    }

    public void bindMeeting(Meeting meeting) {
        char letter = meeting.getOwner().getFirstName().charAt(0);
        String firstLetter = String.valueOf(letter);
        userIcon.setBackground(getColoredCircularShape((letter)));
        userIcon.setText(firstLetter);

        ownerName.setText(meeting.getOwner().getUsername());

        meetingTitle.setText(meeting.getTitle());

        meetingLocation.setText(meeting.getDescription());

        String level = String.valueOf(meeting.getLevel());
        if (level.equals("null")) level = "0";
        meetingLevel.setText(String.valueOf(level));

        String datetime = meeting.getDate();
        meetingDate.setText(datetime.substring(0,datetime.indexOf('T')));
        meetingTime.setText(datetime.substring(datetime.indexOf('T')+1,datetime.indexOf('T')+9));
        if (isMeetingAvailable(meeting.getDate())) {
            addUserButton.setVisibility(View.VISIBLE);
            int userId = CurrentSession.getInstance().getCurrentUser().getId();
            if (notParticipating(meeting.getParticipants(), meeting.getOwner(), userId)) {
                addUserButton.setEnabled(true);
                addUserButton.setImageAlpha(255);
                addUserButton.setOnClickListener(this);
            } else {
                addUserButton.setEnabled(false);
                addUserButton.setImageAlpha(45);
            }
        }
        else {
            addUserButton.setVisibility(View.INVISIBLE);
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

    private boolean notParticipating(List<User> users, User owner, int id) {
        for (User user : users) {
            if (user.getId() == id) return false;
        }
        if (owner.getId() == id) return false;
        return true;
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
            listener.get().onItemClicked(getAdapterPosition());
        }
    }
}
