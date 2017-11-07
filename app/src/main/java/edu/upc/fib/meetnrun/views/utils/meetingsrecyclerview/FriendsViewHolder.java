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
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by eric on 2/11/17.
 */

public class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private View view;
    private WeakReference<RecyclerViewOnClickListener> listener;

    public FriendsViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
    }

    public void bindMeeting(User user) {
        TextView userPhoto = view.findViewById(R.id.meeting_item_user_photo2);
        char letter = user.getUsername().charAt(0);
        String firstLetter = String.valueOf(letter);
        userPhoto.setBackground(getColoredCircularShape((letter)));
        userPhoto.setText(firstLetter);

        TextView userName = view.findViewById(R.id.meeting_item_username);
        userName.setText(user.getUsername());

        TextView name = view.findViewById(R.id.meeting_item_name);
        name.setText(user.getFirstName()+" "+user.getLastName());

        TextView postCode = view.findViewById(R.id.meeting_item_postcode);
        postCode.setText(user.getPostalCode());

        TextView meetingLevel = view.findViewById(R.id.meeting_item_level2);
        String level = String.valueOf(/*user.getLevel()*/0);
        if (level.equals("null")) level = "0";
        meetingLevel.setText(String.valueOf(level));

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

        listener.get().onMeetingClicked(getAdapterPosition());

    }

}
