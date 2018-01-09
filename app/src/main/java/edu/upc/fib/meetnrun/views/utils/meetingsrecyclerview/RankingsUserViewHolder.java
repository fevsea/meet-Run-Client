package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.utils.UtilsGlobal;

/**
 * Created by Javier on 05/01/2018.
 */

public class RankingsUserViewHolder extends RecyclerView.ViewHolder {
    private final View view;
    private final WeakReference<RecyclerViewOnClickListener> listener;
    private TextView position;
    private TextView km;
    private TextView username;
    private TextView realname;
    private TextView zip;


    public RankingsUserViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
    }

    public void bindUserRanking(Meeting meeting, boolean joined) {
        position = view.findViewById(R.id.ranking_item_user_photo2);
        km = view.findViewById(R.id.ranking_item_km);
        username = view.findViewById(R.id.ranking_item_username);
        realname = view.findViewById(R.id.ranking_item_name);
        zip = view.findViewById(R.id.ranking_item_postcode);

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
            if (!joined) {
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
        Date date = null;
        date = UtilsGlobal.parseDate(dateText);

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
