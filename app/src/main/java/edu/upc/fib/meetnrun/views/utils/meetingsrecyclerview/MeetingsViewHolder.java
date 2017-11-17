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


    private View view;
    private WeakReference<RecyclerViewOnClickListener> listener;
    private ImageButton addUserButton;

    public MeetingsViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
    }

    public void bindMeeting(Meeting meeting) {
        TextView userIcon = view.findViewById(R.id.meeting_item_user_icon);
        char letter = meeting.getOwner().getFirstName().charAt(0);
        String firstLetter = String.valueOf(letter);
        userIcon.setBackground(getColoredCircularShape((letter)));
        userIcon.setText(firstLetter);

        TextView ownerName = view.findViewById(R.id.meeting_item_owner);
        ownerName.setText(meeting.getOwner().getUsername());

        TextView meetingTitle = view.findViewById(R.id.meeting_item_title);
        meetingTitle.setText(meeting.getTitle());

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
        meetingTime.setText(datetime.substring(datetime.indexOf('T')+1,datetime.indexOf('T')+9));
        addUserButton = view.findViewById(R.id.meeting_item_meet);
        if (isMeetingAvailable(meeting.getDate())) {
            int userId = CurrentSession.getInstance().getCurrentUser().getId();
            if (notParticipating(meeting.getParticipants(), meeting.getOwner(), userId)) {
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
            listener.get().onMeetingClicked(getAdapterPosition());
        }
    }
}
