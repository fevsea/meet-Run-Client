package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    private final List<Message> messagesList = new ArrayList<>();
    private final Context c;
    private View v;
    private boolean sameHour = false;
    private boolean showDate = false;

    public MessageAdapter(Context c) {
        this.c = c;
    }

    public void addMessage(Message m) {
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
            sameHour = false;
        }
        Date date = m.getDateTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        String aux = String.valueOf(min);
        if (aux.length() == 1) {
            aux = "0"+aux;
        }

        int day = cal.get(Calendar.DAY_OF_MONTH);

        String messageHourMin = String.valueOf(hour)+":"+aux;

        String messageDay = String.valueOf(day)+"-"+String.valueOf(cal.get(Calendar.MONTH)+1)+"-"+String.valueOf(cal.get(Calendar.YEAR));

        if (previous != null) {

            Date previousDate = previous.getDateTime();
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(previousDate);

            int previousHour = cal2.get(Calendar.HOUR_OF_DAY);
            int previousMin = cal2.get(Calendar.MINUTE);

            if (m.getName().equals(previous.getName())) {
                if (hour == previousHour && min == previousMin) {
                    sameHour = true;
                }
            }

            int previousDay = cal2.get(Calendar.DAY_OF_MONTH);

            if(day != previousDay) {
                showDate = true;
            }

        }
        else showDate = true;

        String userName = m.getName();
        if (m.getName().equals(CurrentSession.getInstance().getCurrentUser().getUsername())) {
            holder.getName().setVisibility(View.GONE);
        }
        else {
            holder.getName().setText(userName);
            holder.getName().setTextColor(getColor(userName.charAt(0)));
        }

        holder.getMessage().setText(m.getMessage());
        if (sameHour) {
            holder.getHour().setVisibility(View.GONE);
            sameHour = false;
        }
        else {
            holder.getHour().setVisibility(View.VISIBLE);
            holder.getHour().setText(messageHourMin);
        }
        if (showDate) {
            holder.getDate().setVisibility(View.VISIBLE);
            holder.getDate().setText(messageDay);
            showDate = false;
        }
        else {
            holder.getDate().setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }


    private int getColor(char letter) {
        int[] colors = v.getResources().getIntArray(R.array.colors_chat);
        int position = letter%colors.length;
        return colors[position];
    }

}
