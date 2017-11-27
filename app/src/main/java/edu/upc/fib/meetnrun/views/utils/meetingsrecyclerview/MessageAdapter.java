package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Message;

/**
 * Created by eric on 21/11/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {


    private List<Message> messagesList = new ArrayList<>();
    private Context c;
    private View v;
    private boolean sameHour = false;
    private boolean showDate = false;

    public MessageAdapter(Context c) {
        this.c = c;
    }

    public void addMensaje(Message m) {
        messagesList.add(m);
        notifyItemInserted(messagesList.size());
    }

    public void deleteMessages() {
        messagesList.clear();
    }

    @Override
    public int getItemViewType(int position) {
        Message m = messagesList.get(position);
        if (m.isSender()) return 0;
        return 1;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        v = null;

        if (viewType == 0) {
            v = LayoutInflater.from(c).inflate(R.layout.card_view_message_send, parent, false);
        }
        else {
            v = LayoutInflater.from(c).inflate(R.layout.card_view_message_recieved, parent, false);
        }

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        Message m = messagesList.get(position);
        Message previous = null;
        if (position > 0) {
            previous = messagesList.get(position-1);
        }
        if (position == 0) {
            showDate = true;
        }
        if (previous != null) {
            if(m.getHour().equals(previous.getHour()) && m.getName().equals(previous.getName())) {
                sameHour = true;
            }
            Date date = m.getDateTime();
            Date previousDate = previous.getDateTime();

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            cal.setTime(previousDate);
            int previousDay = cal.get(Calendar.DAY_OF_MONTH);

            if(day != previousDay) {
                showDate = true;
            }

        }
        else showDate = true;

        String userName = m.getName();
        if (m.getName().equals(CurrentSession.getInstance().getCurrentUser().getUsername())) {
            holder.getName().setText("you");
        }
        else holder.getName().setText(userName);

        holder.getMessage().setText(m.getMessage());
        if (sameHour) {
            holder.getHour().setVisibility(View.GONE);
            sameHour = false;
        }
        else {
            holder.getHour().setVisibility(View.VISIBLE);
            holder.getHour().setText(m.getHour());
        }
        if (showDate) {
            holder.getDate().setVisibility(View.VISIBLE);
            holder.getDate().setText(m.getDateTime().toString());
            showDate = false;
        }
        else {
            holder.getDate().setVisibility(View.GONE);
        }

        char letter = userName.charAt(0);
        String firstLetter = String.valueOf(letter);
        holder.getPhoto().setBackground(getColoredCircularShape((letter)));
        holder.getPhoto().setText(firstLetter);
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    private GradientDrawable getColoredCircularShape(char letter) {
        int[] colors = v.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(v.getContext(),R.drawable.user_profile_circular_text_view);
        int position = letter%colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }

}
