package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;

/**
 * Created by eric on 21/11/17.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final View view;
    private final WeakReference<RecyclerViewOnClickListener> listener;
    private final static int MAX_CHAT_LAST_MESSAGE = 35;
    private int userPosition;

    public ChatViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
    }

    public void bindChat(Chat chat) {

        for (int i = 0; i < chat.getListUsersChatSize(); i++) {
            if (CurrentSession.getInstance().getCurrentUser().getUsername().equals(chat.getUserAtPosition(i).getUsername())) {
                userPosition = i;
                break;
            }
        }

        if (chat.getType() == 0) {
            if (!CurrentSession.getInstance().getCurrentUser().getUsername().equals(chat.getUser1().getUsername())) chat.setChatName(chat.getUser1().getUsername());
            else chat.setChatName(chat.getUser2().getUsername());
        }

        TextView userPhoto = view.findViewById(R.id.friend_photo);
        char letter = chat.getChatName().charAt(0);
        String firstLetter = String.valueOf(letter);
        userPhoto.setBackground(getColoredCircularShape(letter));
        userPhoto.setText(firstLetter);

        TextView userName = view.findViewById(R.id.username_friend);
        userName.setText(chat.getChatName());

        String txt = chat.getMessage().getMessage();
        if (txt.length() > MAX_CHAT_LAST_MESSAGE) txt = txt.substring(0, MAX_CHAT_LAST_MESSAGE)+"...";

        TextView lastConverse = view.findViewById(R.id.chat_last_converse);
        lastConverse.setText(txt);

        TextView lastHour = view.findViewById(R.id.chat_hour);

        Date date = chat.getMessage().getDateTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        String aux = String.valueOf(min);
        if (aux.length() == 1) {
            aux = "0"+aux;
        }

        String messageHourMin = String.valueOf(hour)+":"+aux;

        lastHour.setText(messageHourMin);

        TextView numberChats = view.findViewById(R.id.chat_new_messages);

        if (!chat.getMessage().getName().equals(CurrentSession.getInstance().getCurrentUser().getUsername())) {

            if (chat.getNumbMessagesAtPosition(userPosition) != 0) {
                numberChats.setVisibility(View.VISIBLE);
                numberChats.setText(String.valueOf(chat.getNumbMessagesAtPosition(userPosition)));
            }
            else numberChats.setVisibility(View.GONE);
        }
        else numberChats.setVisibility(View.GONE);

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
        listener.get().onItemClicked(getAdapterPosition());
    }

}
