package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by eric on 21/11/17.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private View view;
    private WeakReference<RecyclerViewOnClickListener> listener;

    public ChatViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
    }

    public void bindChat(Chat chat) {
        TextView userPhoto = view.findViewById(R.id.friend_photo);
        char letter = chat.getFriend().getUsername().charAt(0);
        String firstLetter = String.valueOf(letter);
        userPhoto.setBackground(getColoredCircularShape((letter)));
        userPhoto.setText(firstLetter);

        TextView userName = view.findViewById(R.id.username_friend);
        userName.setText(chat.getFriend().getUsername());

        TextView lastConverse = view.findViewById(R.id.chat_last_converse);
        lastConverse.setText(chat.getLast_converse());

        TextView lastHour = view.findViewById(R.id.chat_hour);
        lastHour.setText(chat.getLast_hour());

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
