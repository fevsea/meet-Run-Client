package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Message;

/**
 * Created by eric on 21/11/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {


    private List<Message> messagesList = new ArrayList<>();
    private Context c;

    public MessageAdapter(Context c) {
        this.c = c;
    }

    public void addMensaje(Message m) {
        messagesList.add(m);
        notifyItemInserted(messagesList.size());
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_messages, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.getName().setText(messagesList.get(position).getName());
        holder.getMessage().setText(messagesList.get(position).getMessage());
        holder.getHour().setText(messagesList.get(position).getHour());
        holder.getPhoto().setText(messagesList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

}
